package com.nlptech.function.gifsending.search

import androidx.annotation.WorkerThread
import com.nlptech.common.api.RequestManager
import com.nlptech.function.gifsending.GifSendingApi

class GifSearchRepository {

    companion object {
        const val LIMIT_OF_PAGE = 50

        /**
         * In the EditText, text is empty.
         * **/
        const val STATUS_WAIT_TO_INPUT = 0

        /**
         * In the EditText, text is not empty.
         * **/
        const val STATUS_IN_INPUTTING = 1

        /**
         * Wait for search result
         * **/
        const val STATUS_IN_SEARCHING = 2

        const val STATUS_ON_SUCCESSFUL_RESULT = 3

        const val STATUS_ON_NO_RESULT = 4

        const val STATUS_ON_NO_NETWORK = 5

        val instance = GifSearchRepository()
    }

    @WorkerThread
    fun loadSearchResult(keyword: String, offset: Int): GifSearchResult? {
        return if (keyword == "") {
            null

        } else {
            val retrofit = RequestManager.getInstance().obtainRetrofit()
            val call = retrofit
                    .create(GifSendingApi::class.java)
                    .fetchGifItemsByKeyword(keyword, LIMIT_OF_PAGE, offset)
            try {
                val response = call.execute()
                if (response.isSuccessful) {
                    if (response.body()!!.data != null) {
                        response.body()!!.data.isNoResult = false
                        response.body()!!.data
                    } else {
                        val data = GifSearchResult()
                        data
                    }
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }


}