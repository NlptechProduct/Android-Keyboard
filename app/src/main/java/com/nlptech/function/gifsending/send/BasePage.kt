package com.nlptech.function.gifsending.send

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.lifecycle.LifecycleOwner

open class BasePage(context: Context, attrs: AttributeSet? = null) : RelativeLayout(context, attrs) {

    var recyclerView: RecyclerView? = null
    lateinit var viewModel: GifSendingViewModel
    lateinit var lifecycleOwner: LifecycleOwner

    protected open fun init(lifecycleOwner : LifecycleOwner, viewModel: GifSendingViewModel) {
        this.lifecycleOwner = lifecycleOwner
        this.viewModel = viewModel
    }
}