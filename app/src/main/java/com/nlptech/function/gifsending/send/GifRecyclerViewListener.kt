package com.nlptech.function.gifsending.send

import android.view.View
import com.nlptech.function.gifsending.dataclass.GifItem

interface GifRecyclerViewListener {
    fun onClick(view: View, gifItem: GifItem)
}