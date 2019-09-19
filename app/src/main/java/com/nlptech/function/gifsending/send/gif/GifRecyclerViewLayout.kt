package com.nlptech.function.gifsending.send.gif

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.GifSendingGifRecyclerviewLayoutBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.nlptech.common.utils.DensityUtil
import com.nlptech.common.utils.ViewUtils
import com.nlptech.function.gifsending.GifSendingManager
import com.nlptech.function.gifsending.dataclass.GifItem
import com.nlptech.function.gifsending.send.GifSendingViewModel
import com.nlptech.function.gifsending.send.GifRecyclerViewListener
import com.nlptech.function.gifsending.ui.GifSendingGridLayoutItemDecoration
import com.nlptech.function.gifsending.ui.GifSendingImageView
import com.nlptech.keybaordwidget.KeyboardWidgetManager

class GifRecyclerViewLayout(context: Context, attrs: AttributeSet? = null) : RelativeLayout(context, attrs) {

    lateinit var recyclerView: RecyclerView
    lateinit var lifecycleOwner: LifecycleOwner
    lateinit var viewModel: GifSendingViewModel

    private lateinit var mGifRecyclerViewAdapter: GifRecyclerViewAdapter
    private lateinit var mProgressBar: RelativeLayout
    private lateinit var mCategory: String

    private lateinit var mGifItemsObserver: GifItemsObserver
    private var viewList = ArrayList<View>()
    private var pagePos = -1;

    private val mAttachListener = object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
            viewList.add(view)
        }

        override fun onChildViewDetachedFromWindow(view: View) {
            Glide.with(context).clear(view)
            Glide.get(context).clearMemory()
            viewList.remove(view)
        }
    }

    fun setPageIndex(pageIndex:Int){
        pagePos = pageIndex
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        val binding = DataBindingUtil.bind<GifSendingGifRecyclerviewLayoutBinding>(this)
        recyclerView = binding!!.recyclerView
        mProgressBar = binding.progressLayout
        val keyboardHeight = KeyboardWidgetManager.getInstance().getTotalKeyboardHeight(context)
        val tabHeight = context.resources.getDimensionPixelSize(R.dimen.gif_send_tab_height)
        val progressLP = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, keyboardHeight - tabHeight * 2)
        progressLP.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
        mProgressBar.layoutParams = progressLP
    }

    fun setCategory(category: String) {
        mCategory = category

        mGifRecyclerViewAdapter = GifRecyclerViewAdapter(context, object: GifRecyclerViewListener {
            override fun onClick(view: View, gifItem: GifItem) {
                ViewUtils.disableViewTemp(view)
                GifSendingManager.instance.send(gifItem)
                viewModel.addRecentGifItem(context, gifItem)
            }

        })
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        val equalGapItemDecoration = GifSendingGridLayoutItemDecoration(2, DensityUtil.dp2px(context, 6f))
        recyclerView.addItemDecoration(equalGapItemDecoration)
        recyclerView.adapter = mGifRecyclerViewAdapter

        recyclerView.addOnChildAttachStateChangeListener(mAttachListener)

        mGifItemsObserver = GifItemsObserver(mProgressBar, mGifRecyclerViewAdapter)
        viewModel.getGifItems(mCategory).observe(lifecycleOwner, mGifItemsObserver)
    }

    class GifItemsObserver(private val progressBar: RelativeLayout, val adapter: GifRecyclerViewAdapter): androidx.lifecycle.Observer<PagedList<GifItem>> {
        override fun onChanged(it: PagedList<GifItem>?) {
            if (it == null) return
            progressBar.visibility = View.GONE
            adapter.submitList(it)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clear()
        clean()
    }

    fun clean(){
        viewList.forEach{
            Glide.with(it).clear(it)
        }
        viewList.clear()
        Glide.get(context).clearMemory()
    }

    fun notifyData(){
        mGifRecyclerViewAdapter.notifyDataSetChanged()
    }

    private fun clear() {
        viewModel.getGifItems(mCategory).removeObserver(mGifItemsObserver)
    }

    inner class GifRecyclerViewAdapter(val context: Context, val listener: GifRecyclerViewListener) : PagedListAdapter<GifItem, GifRecyclerViewViewHolder>(GifItem.GifItemDiffUtils) {

        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): GifRecyclerViewViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.gif_sending_gif_page_item, parent, false)
            return GifRecyclerViewViewHolder(context, view)
        }

        override fun onBindViewHolder(viewHolder: GifRecyclerViewViewHolder, index: Int) {
            if (getItem(index) != null && pagePos == GifPage.currentIndex) {
                viewHolder.bind(getItem(index)!!, listener)
            }
        }
    }

    inner class GifRecyclerViewViewHolder(val context: Context, val view: View) : RecyclerView.ViewHolder(view) {
        val iv = view.findViewById<GifSendingImageView>(R.id.image)

        fun bind(gifItem: GifItem, listener: GifRecyclerViewListener) {
            if (gifItem.media == null || gifItem.media!![0].dims == null) return
            val url = gifItem.media!![0].url
            val dims = gifItem.media!![0].dims!!
            val ratio = dims[0].toFloat() / dims[1].toFloat()
            Glide.with(iv)
                    .load(url)
                    .apply(RequestOptions()
                            .skipMemoryCache(true)
                            .format(DecodeFormat.PREFER_RGB_565)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .fitCenter()
                    )
                    .into(iv)
            iv.setAspectRatio(ratio)

            view.setOnClickListener { listener.onClick(view, gifItem) }
        }
    }
}