package com.nlptech.function.gifsending.send.recent

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.GifSendingRecentPageBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.nlptech.common.utils.DensityUtil
import com.nlptech.common.utils.ViewUtils
import com.nlptech.function.gifsending.GifSendingManager
import com.nlptech.function.gifsending.dataclass.GifItem
import com.nlptech.function.gifsending.send.BasePage
import com.nlptech.function.gifsending.send.GifSendingViewModel
import com.nlptech.function.gifsending.send.GifRecyclerViewListener
import com.nlptech.function.gifsending.ui.GifSendingGridLayoutItemDecoration
import com.nlptech.function.gifsending.ui.GifSendingImageView

class RecentPage(context: Context, attrs: AttributeSet? = null) : BasePage(context, attrs) {

    private lateinit var mRecentSendAdapter: RecentSendAdapter
    private lateinit var mGifItemsObserver: GifItemsObserver

    public override fun init(lifecycleOwner : LifecycleOwner, viewModel: GifSendingViewModel) {
        super.init(lifecycleOwner, viewModel)

        val binding = DataBindingUtil.bind<GifSendingRecentPageBinding>(this)
        mRecentSendAdapter = RecentSendAdapter(context, object : GifRecyclerViewListener {
            override fun onClick(view: View, gifItem: GifItem) {
                ViewUtils.disableViewTemp(view)
                GifSendingManager.instance.send(gifItem)
                viewModel.addRecentGifItem(context, gifItem)
            }

        })
        recyclerView = binding?.recyclerView
        recyclerView?.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        val equalGapItemDecoration = GifSendingGridLayoutItemDecoration(2, DensityUtil.dp2px(context, 6f))
        recyclerView?.addItemDecoration(equalGapItemDecoration)
        recyclerView?.adapter = mRecentSendAdapter

        mGifItemsObserver = GifItemsObserver(mRecentSendAdapter)
        viewModel.getRecentGifList(context).observe(lifecycleOwner, mGifItemsObserver)
    }

    private class GifItemsObserver(val adapter: RecentSendAdapter): androidx.lifecycle.Observer<ArrayList<GifItem>> {
        override fun onChanged(it: ArrayList<GifItem>?) {
            if (it == null) return
            adapter.setGifItems(it)
        }
    }

    private class RecentSendAdapter(val context: Context, val listener: GifRecyclerViewListener) : RecyclerView.Adapter<RecentSendViewHolder>() {

        private var gifItems: ArrayList<GifItem> = ArrayList()

        fun setGifItems(gifItems: ArrayList<GifItem>) {
            this.gifItems.clear()
            this.gifItems.addAll(gifItems)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecentSendViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.gif_sending_recent_page_item, parent, false)
            return RecentSendViewHolder(context, view)
        }

        override fun getItemCount(): Int {
            return gifItems.size
        }

        override fun onBindViewHolder(viewHolder: RecentSendViewHolder, position: Int) {
            viewHolder.bind(gifItems[position], listener)
        }
    }

    private class RecentSendViewHolder(val context: Context, val view: View) : RecyclerView.ViewHolder(view) {

        private var iv: GifSendingImageView = view.findViewById(R.id.image)

        fun bind(gifItem: GifItem, listener: GifRecyclerViewListener) {
            if (gifItem.media == null || gifItem.media!![0].dims == null) return

            val url = gifItem.media!![0].url
            val dims = gifItem.media!![0].dims!!
            val ratio = dims[0].toFloat() / dims[1].toFloat()
            Glide.with(context)
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