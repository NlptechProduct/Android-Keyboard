package com.nlptech.function.gifsending.send

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.android.inputmethod.TestApplication
import com.android.inputmethod.latin.R
import com.google.android.material.tabs.TabLayout
import com.nlptech.common.utils.NetworkUtil
import com.nlptech.common.utils.PrefUtil
import com.nlptech.function.gifsending.send.gif.GifPage
import com.nlptech.function.gifsending.send.recent.RecentPage
import com.nlptech.keybaordwidget.KeyboardWidgetManager
import com.nlptech.keybaordwidget.draggable.DraggableKeyboardWidget
import com.nlptech.keyboardview.theme.KeyboardThemeManager


class GifSendingWidget : DraggableKeyboardWidget(),
        ViewPager.OnPageChangeListener, GifPage.GifPageCallback {

    companion object {
        const val PREF_CURRENT_PAGE = "pref_gif_sending_current_page"
        const val DEFAULT_CURRENT_PAGE = 1

        const val PAGE_APAPTER_RECENT_PAGE_ID = R.layout.gif_sending_recent_page
        const val PAGE_APAPTER_GIF_PAGE_ID = R.layout.gif_sending_gif_page
        const val PAGE_APAPTER_TAB_COUNT = 2
    }

    private lateinit var mGifSendingPageAdapter: GifSendingPageAdapter
    private lateinit var mTabLayout: TabLayout
    private lateinit var mViewPager: ViewPager
    private lateinit var mNewworkUnavailable: RelativeLayout
    private var mCurrentPageIndex: Int = 0
    private var mViewModel: GifSendingViewModel? = null
    private val color = 0xFF000000.toInt();

    override fun isEnableHeightMode(context: Context?): Boolean {
        return true
    }

    override fun getCustomAppTheme(): Int {
        return R.style.AppTheme
    }

    override fun onPageScrollStateChanged(p0: Int) {
    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
    }

    override fun onPageSelected(position: Int) {
        mCurrentPageIndex = position
        PrefUtil.putInt(TestApplication.getInstance(), PREF_CURRENT_PAGE, position)
        setRecyclerViewWithCurrentPage()
        // update tab
        for (i in 0..mTabLayout.tabCount) {
            val customView = mTabLayout.getTabAt(i)?.customView
            if (customView is TabView) {
                customView.setSelect(i == position)
            }
        }
    }

    override fun onTranslationYChanged(y: Float) {
        super.onTranslationYChanged(y)

        // update GifPage content
        val count = mViewPager.childCount
        for (i in 0 until count) {
            val child = mViewPager.getChildAt(i)
            if (child is GifPage) {
                child.updateGifViewPagerHeight(if (y < 0) 0f else y)
            }
        }
    }

    override fun onCategoryChanged() {
        setRecyclerViewWithCurrentPage()
    }

    override fun onCreateContentView(inflater: LayoutInflater?): View {
        val view = inflater!!.inflate(R.layout.gif_sending_main, null)
        initLayout(view)
        return view
    }

    override fun onCreate(intent: Intent?) {
        super.onCreate(intent)
        mViewModel = GifSendingViewModel(TestApplication.getInstance())
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel?.destroy()
        mViewModel = null
    }

    private fun setRecyclerViewWithCurrentPage() {
        val tag = "index_$mCurrentPageIndex"
        val basePage = mViewPager.findViewWithTag<BasePage>(tag)
        if (basePage == null) {
            return
        }
        setRecyclerView(basePage.recyclerView)
    }

    override fun onFinishOpenAnimation() {
        mViewPager.adapter = mGifSendingPageAdapter
        mViewPager.setCurrentItem(mCurrentPageIndex, false)
        setRecyclerViewWithCurrentPage()
    }

    private fun initLayout(root: View) {
        // background
        KeyboardThemeManager.getInstance().setUiModuleBackground(root)

        // title
        KeyboardThemeManager.getInstance().colorUiModuleTitleText(root.findViewById(R.id.title))

        // close btn
        val close = root.findViewById<ImageView>(R.id.dismiss_btn)
        KeyboardThemeManager.getInstance().colorUiModuleIcon(close, color)
        close.setOnClickListener { close() }

        mCurrentPageIndex = PrefUtil.getInt(TestApplication.getInstance(), PREF_CURRENT_PAGE, DEFAULT_CURRENT_PAGE)

        // tab
        mTabLayout = root.findViewById<View>(R.id.tab_layout) as TabLayout
        createTabs()

        // viewpager
        mGifSendingPageAdapter = GifSendingPageAdapter(this, this, mViewModel!!)
        mViewPager = root.findViewById(R.id.gif_sending_main_view_pager)
        mViewPager.addOnPageChangeListener(this)
        mViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(mTabLayout))
        mTabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(mViewPager))

        // network unavailable layout
        mNewworkUnavailable = root.findViewById(R.id.network_unavailable)
        val networkUnavailableIcon = root.findViewById<ImageView>(R.id.network_unavailable_icon)
        KeyboardThemeManager.getInstance().colorUiModuleIcon(networkUnavailableIcon, color)
        KeyboardThemeManager.getInstance().setUiModuleBackground(mNewworkUnavailable)
        val keyboardHeight = KeyboardWidgetManager.getInstance().getTotalKeyboardHeight(root.context)
        val tabHeight = root.context.resources.getDimensionPixelSize(R.dimen.gif_send_tab_height)
        val networkUnavailableLP = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, keyboardHeight - tabHeight)
        networkUnavailableLP.addRule(RelativeLayout.BELOW, R.id.gif_sending_main_tab)
        mNewworkUnavailable.layoutParams = networkUnavailableLP
        mNewworkUnavailable.visibility = if (NetworkUtil.isNetworkConnected(root.context)) View.GONE else View.VISIBLE
    }

    private fun createTabs() {
        var tab = mTabLayout.newTab()
        tab.customView = createTabView(mTabLayout, R.drawable.ic_history_press, R.drawable.ic_history_normal)
        mTabLayout.addTab(tab)

        tab = mTabLayout.newTab()
        tab.customView = createTabView(mTabLayout, R.drawable.ic_gif_press, R.drawable.ic_gif_normal)
        mTabLayout.addTab(tab)

        for (i in 0..mTabLayout.tabCount) {
            val customView = mTabLayout.getTabAt(i)?.customView
            if (customView is TabView) {
                customView.setSelect(i == mCurrentPageIndex)
            }
        }
    }

    private fun createTabView(viewGroup: ViewGroup, selectResId: Int, unselectResId: Int): View {
        val tabView = TabView(viewGroup.context)
        tabView.setImageRes(selectResId, unselectResId)
        return tabView
    }

    override fun isExtendedInFloatingKeyboard(): Boolean {
        return false
    }

    inner class GifSendingPageAdapter(private val gifPageCallback: GifPage.GifPageCallback
                                      , private val lifecycleOwner: LifecycleOwner
                                      , val viewModel: GifSendingViewModel) : PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val inflater = LayoutInflater.from(container.context)
            val view: View
            if (position == 0) {
                view = inflater.inflate(PAGE_APAPTER_RECENT_PAGE_ID, null) as RecentPage
                view.init(lifecycleOwner, viewModel)
            } else {
                view = inflater.inflate(PAGE_APAPTER_GIF_PAGE_ID, null) as GifPage
                view.init(gifPageCallback, lifecycleOwner, viewModel, stateLowTranslationY)
            }

            view.tag = "index_$position"
            container.addView(view)
            return view
        }

        override fun isViewFromObject(p0: View, p1: Any): Boolean {
            return p0 === p1
        }

        override fun getCount(): Int {
            return PAGE_APAPTER_TAB_COUNT
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            (container as ViewPager).removeView(`object` as View)
        }
    }
}