package com.nlptech.function.gifsending

import com.nlptech.common.api.ResultData
import com.nlptech.function.gifsending.search.GifSearchResult
import com.nlptech.function.gifsending.send.gif.GifCategoryItems
import com.nlptech.function.gifsending.send.gif.GifItems

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GifSendingApi {

    @GET("gif/tags")
    fun fetchGifCategory(@Query("locale") locale: String): Call<com.nlptech.common.api.ResultData<GifCategoryItems>>

    @GET("gif/search/tag")
    fun fetchGifItemsByTag(@Query("locale") locale: String, @Query("tag") tag: String,
                           @Query("limit") limit: Int?,
                           @Query("offset") offset: Int?): Call<com.nlptech.common.api.ResultData<List<GifItems>>>

    @GET("gif/search/query")
    fun fetchGifItemsByKeyword(@Query("q") keyword: String,
                               @Query("limit") limit: Int, @Query("offset") offset: Int): Call<ResultData<GifSearchResult>>

}
