package com.nlptech.function.gifsending.send.gif

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.nlptech.function.gifsending.dataclass.GifItem
import com.nlptech.function.gifsending.send.GifSendingRepository

class GifItemsPagingDataSource(val tag: String, val listener: Listener): PageKeyedDataSource<Int, GifItem>() {

    companion object {
        const val LOAD_ITEMS_LIMIT = 30
    }

    interface Listener {
        fun onGifItemsPagingLoadInitialFailed()
        fun onGifItemsPagingLoadAfterFailed()
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, GifItem>) {
        val response = GifSendingRepository.instance.loadGifItems(tag, LOAD_ITEMS_LIMIT, 0)
        if (response.isSuccessful) {
            val gifItems = response.body()?.data
            if (gifItems != null && gifItems.isNotEmpty()) {
                val gifList = gifItems[0].content?.gif
                val nextOffset = gifItems[0].content?.next_offset
                if (gifList != null && nextOffset != null) {
                    callback.onResult(gifList, null, nextOffset)
                    return
                }
            }
        }
        listener.onGifItemsPagingLoadInitialFailed()
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, GifItem>) {
        val offset = params.key
        val response = GifSendingRepository.instance.loadGifItems(tag, LOAD_ITEMS_LIMIT, offset)
        if (response.isSuccessful) {
            val gifItems = response.body()?.data
            if (gifItems != null && gifItems.isNotEmpty()) {
                val gifList = gifItems[0].content?.gif
                val nextOffset = gifItems[0].content?.next_offset
                if (gifList != null && nextOffset != null) {
                    callback.onResult(gifList, nextOffset)
                    return
                }
            }
        }
        listener.onGifItemsPagingLoadAfterFailed()
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, GifItem>) {
    }

    class GifItemsPagingDataSourceFactory(val tag: String, val listener: Listener) : DataSource.Factory<Int, GifItem>() {
        override fun create(): DataSource<Int, GifItem> {
            return GifItemsPagingDataSource(tag, listener)
        }
    }
}