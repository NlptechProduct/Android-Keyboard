package com.nlptech.function.gifsending.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.android.inputmethod.latin.databinding.ViewholderGifSearchBinding
import com.bumptech.glide.Glide
import com.nlptech.function.gifsending.dataclass.GifItem
import kotlinx.android.synthetic.main.viewholder_gif_search.view.*

class GifSearchAdapter(val context: Context, val listener: Listener) : PagedListAdapter<GifItem, GifSearchItemViewHolder>(GifItem.GifItemDiffUtils) {

    interface Listener {
        fun onItemClick(view: View, gifItem: GifItem, position: Int)
    }

    companion object {
        const val SPAN_COUNT = 2
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): GifSearchItemViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val binding = ViewholderGifSearchBinding.inflate(layoutInflater, viewGroup, false)
        return GifSearchItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GifSearchItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!, listener, position)
    }

    override fun onViewRecycled(holder: GifSearchItemViewHolder) {
        Glide.with(holder.itemView.context).clear(holder.itemView.viewholder_gif_search_image_view)
        super.onViewRecycled(holder)
    }
}
