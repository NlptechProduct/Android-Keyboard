package com.nlptech.function.gifsending.search

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.android.inputmethod.TestApplication
import com.nlptech.common.utils.NetworkUtil
import com.nlptech.function.gifsending.dataclass.GifItem

class GifSearchItemsPagingDataSource(val keyword: String, val listener: Listener) : PageKeyedDataSource<Int, GifItem>() {

    interface Listener {
        fun onNoNetwork()
        fun onLoadNoResult()
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, GifItem>) {
        val hasNetwork = NetworkUtil.isAvailable(TestApplication.getInstance())
        if (!hasNetwork) {
            listener.onNoNetwork()
            return
        }
        val GifSearchResult = GifSearchRepository.instance.loadSearchResult(keyword, 0)
        if (GifSearchResult != null) {
            val gifItems = GifSearchResult.gif
            val nextOffset = GifSearchResult.next_offset
            if (!GifSearchResult.isNoResult &&
                    gifItems != null &&
                    gifItems.isNotEmpty()) {
                callback.onResult(gifItems, null, nextOffset)
                return
            }
        }
        listener.onLoadNoResult()
        return
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, GifItem>) {
        val offset = params.key
        val GifSearchResult = GifSearchRepository.instance.loadSearchResult(keyword, offset)
        if (GifSearchResult != null) {
            val gifItems = GifSearchResult.gif
            val nextOffset = GifSearchResult.next_offset
            if (gifItems != null) {
                callback.onResult(gifItems, nextOffset)
                return
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, GifItem>) {
    }

    class Factory(val listener: Listener) : DataSource.Factory<Int, GifItem>() {
        var keyword = ""
        override fun create(): DataSource<Int, GifItem> {
            return GifSearchItemsPagingDataSource(keyword, listener)
        }
    }
}