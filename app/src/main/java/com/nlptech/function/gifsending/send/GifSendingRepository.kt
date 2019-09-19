package com.nlptech.function.gifsending.send

import com.nlptech.common.api.RequestManager
import com.nlptech.common.api.ResultData
import com.nlptech.function.gifsending.GifSendingApi
import com.nlptech.function.gifsending.send.gif.GifCategoryItems
import com.nlptech.function.gifsending.send.gif.GifItems
import com.nlptech.language.VertexInputMethodManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GifSendingRepository {

    companion object {
        val instance = GifSendingRepository()
    }

    interface Listener {
        fun onGifCategoryItemsLoadedSuccessful(items: GifCategoryItems?)
        fun onGifCategoryItemsLoadedFailed()
    }

    private val retrofit = RequestManager.getInstance().obtainRetrofit()!!

    fun asyncLoadGifCategoryItems(listener: Listener) {
        val locale = VertexInputMethodManager.getInstance().currentLocaleString[0]
        retrofit.create(GifSendingApi::class.java).fetchGifCategory(locale)
                .enqueue(object : Callback<ResultData<GifCategoryItems>> {
                    override fun onFailure(call: Call<ResultData<GifCategoryItems>>, t: Throwable) {
                        listener.onGifCategoryItemsLoadedFailed()
                    }

                    override fun onResponse(call: Call<ResultData<GifCategoryItems>>, response: Response<ResultData<GifCategoryItems>>) {
                        if (response.body() != null) {
                            listener.onGifCategoryItemsLoadedSuccessful(response.body()?.data)
                        } else {
                            listener.onGifCategoryItemsLoadedFailed()
                        }
                    }

                })
    }

    fun loadGifItems(tag : String, limit: Int, offset: Int) : Response<ResultData<List<GifItems>>> {
        val locale = VertexInputMethodManager.getInstance().currentLocaleString[0]
        return retrofit.create(GifSendingApi::class.java)
                .fetchGifItemsByTag(locale, tag, limit, offset)
                .execute()
    }
}