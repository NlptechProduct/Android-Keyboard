package com.nlptech.function.languagesetting.langswitch

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.View

import com.nlptech.common.utils.DisplayUtil

class MaxHeightRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : RecyclerView(context, attrs, defStyle) {
    private var mMaxHeight: Int = DisplayUtil.getScreenHeight(context) / 2

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val heightSpec = View.MeasureSpec.makeMeasureSpec(mMaxHeight, View.MeasureSpec.AT_MOST)
        super.onMeasure(widthSpec, heightSpec)
    }

    fun setMaxHeight(maxHeight : Int) {
        mMaxHeight = maxHeight
        requestLayout()
    }
}
