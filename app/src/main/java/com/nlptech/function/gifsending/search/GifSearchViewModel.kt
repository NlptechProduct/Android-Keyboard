package com.nlptech.function.gifsending.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.nlptech.function.gifsending.dataclass.GifItem

class GifSearchViewModel(application: Application) : AndroidViewModel(application), GifSearchItemsPagingDataSource.Listener {

    /**
     * List
     * **/
    private var totalGifItemsLiveData: LiveData<PagedList<GifItem>>? = null
    private val dataSourceFactory = GifSearchItemsPagingDataSource.Factory(this)

    /**
     * Status
     * **/
    private val statusLiveData = MutableLiveData<Int>()

    fun search(keyword: String) {
        setStatus(GifSearchRepository.STATUS_IN_SEARCHING)
        val lastKeyword = dataSourceFactory.keyword
        dataSourceFactory.keyword = keyword
        val ds = totalGifItemsLiveData!!.value!!.dataSource as GifSearchItemsPagingDataSource
        ds.invalidate()
    }

    override fun onNoNetwork() {
        setStatus(GifSearchRepository.STATUS_ON_NO_NETWORK)
    }

    override fun onLoadNoResult() {
        setStatus(GifSearchRepository.STATUS_ON_NO_RESULT)
    }

    fun onSearchKeywordChanged(text: CharSequence) {
        val status: Int = if (text.toString() == "")
            GifSearchRepository.STATUS_WAIT_TO_INPUT
        else
            GifSearchRepository.STATUS_IN_INPUTTING
        setStatus(status)
    }

    fun getGifItemsLiveData(): LiveData<PagedList<GifItem>> {
        if (totalGifItemsLiveData == null) {
            val pagedListConfig = PagedList.Config.Builder()
                    .setPrefetchDistance(10)
                    .build()
            totalGifItemsLiveData = LivePagedListBuilder(dataSourceFactory, pagedListConfig)
                    .build()
        }
        return totalGifItemsLiveData as LiveData<PagedList<GifItem>>
    }

    fun setStatus(status: Int) {
        if (statusLiveData.value == GifSearchRepository.STATUS_ON_NO_NETWORK &&
                status == GifSearchRepository.STATUS_ON_NO_RESULT) {
            return
        }
        statusLiveData.postValue(status)
    }

    fun getStatusLiveData(): MutableLiveData<Int> {
        return statusLiveData
    }

    fun getKeyword(): String {
        return dataSourceFactory.keyword
    }

}
