package com.nlptech.function.gifsending.send.gif

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.nlptech.function.gifsending.send.BasePage
import com.nlptech.function.gifsending.send.GifSendingViewModel
import com.nlptech.keybaordwidget.KeyboardWidgetManager
import android.widget.TextView
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.GifSendingGifPageBinding


class GifPage(context: Context, attrs: AttributeSet? = null) : BasePage(context, attrs)
        , ViewPager.OnPageChangeListener {

    interface GifPageCallback {
        fun onCategoryChanged()
    }

    private lateinit var mGifPageCallback: GifPageCallback
    private lateinit var mTabLayout: TabLayout
    private lateinit var mCategoryPageAdapter: CategoryPageAdapter
    private lateinit var mGifViewPager: ViewPager
    private lateinit var mProgressBar: RelativeLayout
    private lateinit var mOnGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener
    var lastViewLayout: GifRecyclerViewLayout? = null

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        notifyCategoryChanged(position)
    }

    companion object{
        var currentIndex = 0
    }

    private fun notifyCategoryChanged(position: Int) {
        currentIndex = position
        val gifRecyclerViewLayout =
                mGifViewPager.findViewWithTag<GifRecyclerViewLayout>("category_$position") ?: return
        gifRecyclerViewLayout.setPageIndex(position)
        recyclerView = gifRecyclerViewLayout.recyclerView

        lastViewLayout?.clean()
        lastViewLayout = gifRecyclerViewLayout
        gifRecyclerViewLayout.notifyData()
        mGifPageCallback.onCategoryChanged()
    }

    fun init(gifPageCallback: GifPageCallback, lifecycleOwner: LifecycleOwner, viewModel: GifSendingViewModel, moduleStateLowTranslationY: Float) {
        super.init(lifecycleOwner, viewModel)

        this.mGifPageCallback = gifPageCallback

        val binding = DataBindingUtil.bind<GifSendingGifPageBinding>(this)

        mProgressBar = binding!!.progressLayout
        val keyboardHeight = KeyboardWidgetManager.getInstance().getTotalKeyboardHeight(context)
        val tabHeight = context.resources.getDimensionPixelSize(R.dimen.gif_send_tab_height)
        val progressLP = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, keyboardHeight - tabHeight)
        progressLP.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
        mProgressBar.layoutParams = progressLP

        mTabLayout = binding.tabLayout

        mGifViewPager = binding.gifViewPager
        mCategoryPageAdapter = CategoryPageAdapter(lifecycleOwner, this.viewModel)
        mGifViewPager.adapter = mCategoryPageAdapter
        mGifViewPager.addOnPageChangeListener(this)
        mGifViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(mTabLayout))
        mTabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(mGifViewPager))
        mTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(selectedTab: TabLayout.Tab?) {
                val vg = mTabLayout.getChildAt(0) as ViewGroup
                val vgTab = vg.getChildAt(selectedTab!!.position) as ViewGroup
                val tabChildsCount = vgTab.childCount
                for (i in 0..tabChildsCount) {
                    val tabViewChild = vgTab.getChildAt(i)
                    if (tabViewChild is TextView) {
                        tabViewChild.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10.7f)
                        tabViewChild.setTextColor(context.resources.getColor(R.color.gif_send_category_unselect_color))
                    }
                }
            }

            override fun onTabSelected(selectedTab: TabLayout.Tab?) {
                val vg = mTabLayout.getChildAt(0) as ViewGroup
                val vgTab = vg.getChildAt(selectedTab!!.position) as ViewGroup
                val tabChildsCount = vgTab.childCount
                for (i in 0..tabChildsCount) {
                    val tabViewChild = vgTab.getChildAt(i)
                    if (tabViewChild is TextView) {
                        tabViewChild.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11.3f)
                        tabViewChild.setTextColor(context.resources.getColor(R.color.gif_send_category_select_color))
                    }
                }
            }

        })
        mTabLayout.visibility = View.INVISIBLE

        this.viewModel.getGifCategoryItem().observe(lifecycleOwner, Observer {
            updateCategoryItems(it)
            mTabLayout.visibility = View.VISIBLE
        })

        mOnGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            mGifViewPager.viewTreeObserver.removeOnGlobalLayoutListener(mOnGlobalLayoutListener)
            notifyCategoryChanged(0)
            updateGifViewPagerHeight(moduleStateLowTranslationY)
        }
        mGifViewPager.viewTreeObserver.addOnGlobalLayoutListener(mOnGlobalLayoutListener)

        viewModel.queryRecentGifItems(context)
    }

    fun updateGifViewPagerHeight(parentTranslationY: Float) {
        val lp = mGifViewPager.layoutParams
        lp.height= (this.height - parentTranslationY).toInt()
        mGifViewPager.layoutParams = lp
    }

    private fun updateCategoryItems(result: GifCategoryItems) {
        if (result.tags == null) return

        for (tagItem in result.tags!!) {
            createTab(tagItem.value!!)
        }

        mCategoryPageAdapter.setData(result.tags!!)
        notifyCategoryChanged(0)
        mProgressBar.visibility = View.GONE
    }

    private fun createTab(text: String) {
        val tab = mTabLayout.newTab()
        val tabTextView = TextView(context)
        tab.customView = tabTextView;
        tabTextView.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        tabTextView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        tabTextView.text = text.substring(0,1).toUpperCase() + text.substring(1)
        tabTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10.7f)
        tabTextView.setTextColor(context.resources.getColor(R.color.gif_send_category_unselect_color))
        mTabLayout.addTab(tab)
    }

    private class CategoryPageAdapter(val lifecycleOwner: LifecycleOwner, val viewModel: GifSendingViewModel) : PagerAdapter() {

        var gifCategoryItem: List<GifCategoryItems.GifCategoryItem>? = null

        fun setData(items: List<GifCategoryItems.GifCategoryItem>) {
            gifCategoryItem = items
            notifyDataSetChanged()
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val inflater = LayoutInflater.from(container.context)
            val view = inflater.inflate(R.layout.gif_sending_gif_recyclerview_layout, null) as GifRecyclerViewLayout

            view.lifecycleOwner = lifecycleOwner
            view.viewModel = viewModel
            view.setCategory(gifCategoryItem!![position].value!!)
            view.tag = "category_$position"
            container.addView(view)
            return view
        }

        override fun isViewFromObject(p0: View, p1: Any): Boolean {
            return p0 === p1
        }

        override fun getCount(): Int {
            return if (gifCategoryItem == null) 0 else gifCategoryItem!!.size
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            if (`object` is GifRecyclerViewLayout) {
                val recyclerView = `object`.recyclerView
                val count = recyclerView.childCount
                for (i in 0 until count) {
                    val view = recyclerView.getChildAt(i)
                    if (view != null && isValidContextForGlide(view.context)) {
                        Glide.with(view.context).clear(view)
                    }
                }
            }
            (container as ViewPager).removeView(`object` as View)
        }

        private fun isValidContextForGlide(context: Context?): Boolean {
            if (context == null) {
                return false
            }
            if (context is Activity) {
                val activity = context as Activity?
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (activity!!.isDestroyed) {
                        return false
                    }
                }

                if (activity!!.isFinishing) {
                    return false
                }
            }
            return true
        }
    }
}