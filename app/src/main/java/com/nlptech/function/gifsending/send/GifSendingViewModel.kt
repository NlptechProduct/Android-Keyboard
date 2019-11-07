package com.nlptech.function.gifsending.send

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.google.gson.Gson
import com.nlptech.common.utils.FileUtils
import com.nlptech.function.gifsending.dataclass.GifItem
import com.nlptech.function.gifsending.send.gif.GifCategoryItems
import com.nlptech.function.gifsending.send.gif.GifItemsPagingDataSource
import java.io.*
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.disposables.CompositeDisposable


class GifSendingViewModel(application: Application) : AndroidViewModel(application), GifSendingRepository.Listener,
        GifItemsPagingDataSource.Listener{

    companion object {
        private const val FILE_STORE_NAME = ".recent_gif_list"
    }

    private val recentGifList = ArrayList<GifItem>()
    private val recentGifListLiveData = MutableLiveData<ArrayList<GifItem>>()
    private val gifCategoryLiveData = MutableLiveData<GifCategoryItems>()
    private var allGifItems = HashMap<String, LiveData<PagedList<GifItem>>>()
    private val mObject = Any()
    private val mCompositeDisposable = CompositeDisposable()

    override fun onGifCategoryItemsLoadedSuccessful(items: GifCategoryItems?) {
        if (items?.tags != null) {
            gifCategoryLiveData.value = items
        }
    }

    override fun onGifCategoryItemsLoadedFailed() {
        // TODO
    }

    override fun onGifItemsPagingLoadInitialFailed() {
        // TODO
    }

    override fun onGifItemsPagingLoadAfterFailed() {
        // TODO
    }

    fun getGifCategoryItem(): MutableLiveData<GifCategoryItems> {
        GifSendingRepository.instance.asyncLoadGifCategoryItems(this)
        return gifCategoryLiveData
    }

    fun getGifItems(tag : String): LiveData<PagedList<GifItem>> {
        if (allGifItems[tag] == null) {
            val dataSource = GifItemsPagingDataSource.GifItemsPagingDataSourceFactory(tag, this)
            val pagedListConfig = PagedList.Config.Builder()
                    .setPrefetchDistance(10)
                    .build()
            allGifItems[tag] = LivePagedListBuilder(dataSource, pagedListConfig).build()
        }
        return allGifItems[tag]!!
    }

    fun getRecentGifList(context: Context): MutableLiveData<ArrayList<GifItem>> {
        if (recentGifList.size == 0) {
            queryRecentGifItems(context)
        }
        return recentGifListLiveData
    }

    fun addRecentGifItem(context: Context, gifItem: GifItem) {
        val repeatItems = ArrayList<GifItem>()
        for (tmp in recentGifList) {
            if (tmp.id.equals(gifItem.id)) {
                repeatItems.add(tmp)
            }
        }
        if (repeatItems.size > 0) {
            recentGifList.removeAll(repeatItems)
        }

        recentGifList.add(0, gifItem)
        recentGifListLiveData.value = recentGifList
        val disposable = Observable.create(ObservableOnSubscribe<Boolean> {
            val success = saveRecentGifListToFile(context, recentGifList)
            it.onNext(success) })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {}
        mCompositeDisposable.add(disposable)
    }

    fun queryRecentGifItems(context: Context) {
        val disposable = Observable.create(ObservableOnSubscribe<List<GifItem>> {
            val gifList = queryRecentGifListFromFile(context)
            it.onNext(gifList) })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    recentGifList.clear()
                    recentGifList.addAll(it)
                    recentGifListLiveData.value = recentGifList
                }
        mCompositeDisposable.add(disposable)
    }

    private fun saveRecentGifListToFile(context: Context, gifList: List<GifItem>) : Boolean {
        synchronized(mObject) {
            val file = FileUtils.getPrivateFile(context, FILE_STORE_NAME)
            FileUtils.delete(file)
            try {
                FileUtils.createFileIfNecessary(file)
            } catch (e: IOException) {
                return false
            }

            val fileWriter = FileWriter(file.path)
            try {
                Gson().toJson(gifList, fileWriter)
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                fileWriter.close()
            }
            return false
        }
    }

    private fun queryRecentGifListFromFile(context: Context) : List<GifItem> {
        synchronized(mObject) {
            val file = FileUtils.getPrivateFile(context, FILE_STORE_NAME)
            var data: List<GifItem>? = null
            if (FileUtils.isFileExist(file)) {
                val fileReader = FileReader(file.path)
                try {
                    val listType = object : TypeToken<List<GifItem>>() {}.type
                    data = Gson().fromJson(fileReader, listType)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    fileReader.close()
                }
            }
            if (data == null) {
                return ArrayList()
            }
            return data
        }
    }

    fun destroy() {
        mCompositeDisposable.dispose()
    }
}