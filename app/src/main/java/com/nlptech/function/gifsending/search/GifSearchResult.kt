package com.nlptech.function.gifsending.search

import com.nlptech.function.gifsending.dataclass.GifItem

class GifSearchResult {

    var isNoResult: Boolean = true

    /**
     * gif : []
     * next_offset : 1
     */
    var next_offset: Int = 0
    var gif: List<GifItem>? = null
    override fun toString(): String {
        return "GifSearchItems(next_offset=$next_offset, gif=$gif)"
    }


}
