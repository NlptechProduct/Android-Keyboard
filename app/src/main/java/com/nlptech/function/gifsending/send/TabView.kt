package com.nlptech.function.gifsending.send

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import com.nlptech.common.utils.DensityUtil
import android.view.Gravity
import com.android.inputmethod.latin.R

class TabView(context: Context, attrs: AttributeSet? = null): FrameLayout(context, attrs) {

    private val mImageView = ImageView(context)
    private val mSelectBackground = context.resources.getDrawable(R.drawable.gif_content_selection)
    private var mSelectImageRes: Int = 0
    private var mUnselectImageRes: Int = 0

    init {
        val picSize = DensityUtil.dp2px(context.applicationContext, 19.3f)
        val lp = FrameLayout.LayoutParams(picSize, picSize)
        lp.gravity = Gravity.CENTER
        mImageView.layoutParams = lp
        addView(mImageView)
    }

    fun setImageRes(selectResId: Int, unselectResId: Int) {
        mSelectImageRes = selectResId
        mUnselectImageRes = unselectResId
    }

    fun setSelect(select: Boolean) {
        background = if (select) mSelectBackground else null
        mImageView.setImageResource(if (select) mSelectImageRes else mUnselectImageRes)
    }
}