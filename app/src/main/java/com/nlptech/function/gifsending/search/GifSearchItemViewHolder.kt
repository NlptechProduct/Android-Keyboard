package com.nlptech.function.gifsending.search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.android.inputmethod.latin.databinding.ViewholderGifSearchBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.nlptech.function.gifsending.dataclass.GifItem
import kotlinx.android.synthetic.main.loading_page.view.*
import kotlinx.android.synthetic.main.viewholder_gif_search.view.*

class GifSearchItemViewHolder(
        val binding: ViewholderGifSearchBinding
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.executePendingBindings()
        itemView.loading_waiting_text.visibility = View.GONE
    }

    fun bind(gifItem: GifItem, listener: GifSearchAdapter.Listener, position: Int) {
        binding.item = gifItem
        binding.listener = listener
        binding.position = position
        binding.executePendingBindings()
        val imageWidth = gifItem.media!![0].dims!![0].toFloat()
        val imageHeight = gifItem.media!![0].dims!![1].toFloat()
        itemView.viewholder_gif_search_image_view.setAspectRatio(imageWidth / imageHeight)
        itemView.viewholder_gif_search_progress_layout.visibility = View.VISIBLE
        Glide.with(itemView.context)
                .asGif()
                .load(gifItem.getUrl(GifItem.MediaBean.TYPE_NANO))
                .apply(RequestOptions()
                        .skipMemoryCache(true)
                        .format(DecodeFormat.PREFER_RGB_565)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .fitCenter()
                )
                .listener(object : RequestListener<GifDrawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: GifDrawable?, model: Any?, target: Target<GifDrawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        itemView.viewholder_gif_search_progress_layout.visibility = View.GONE
                        return false
                    }
                })
                .into(itemView.viewholder_gif_search_image_view)
    }

}
