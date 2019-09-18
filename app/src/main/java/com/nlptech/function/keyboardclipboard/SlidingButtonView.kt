package com.nlptech.function.keyboardclipboard

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ImageView
import com.android.inputmethod.latin.R

class SlidingButtonView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : HorizontalScrollView(context, attrs, defStyleAttr) {

    private var mDelete: ImageView? = null
    private var mDeleteBg: View? = null
    private var mScrollWidth: Int = 0
    private var isOpen: Boolean = false
    private var once: Boolean = false

    private var mOpenX: Int = 0
    private var mCloseX: Int = 0
    private var mOpenThresholdX: Int = 0

    private var mIonSlidingButtonListener: IonSlidingButtonListener? = null

    init {
        this.overScrollMode = View.OVER_SCROLL_NEVER
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!once) {
            mDelete = findViewById(R.id.delete_icon)
            once = true

            mDeleteBg = findViewById(R.id.delete_background)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (changed) {
            this.scrollTo(0, 0)
            //set delete btn's width as the scroll width
            mScrollWidth = mDelete?.width ?: 0
            // handle bg size
            mDeleteBg?.top = 0
            mDeleteBg?.bottom = height
        }

        mOpenX = mScrollWidth
        mCloseX = 0
        mOpenThresholdX = mScrollWidth / 2
        scrollX = mCloseX
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action
        when (action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> mIonSlidingButtonListener?.onDownOrMove(this)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                changeScrollx()
                return true
            }
            else -> {
            }
        }
        return super.onTouchEvent(ev)
    }

    private fun changeScrollx() {
        val shouldOpen = (scrollX >= mOpenThresholdX)
        if (shouldOpen) {
            this.smoothScrollTo(mOpenX, 0)
            isOpen = true
            mIonSlidingButtonListener?.onMenuIsOpen(this)
        } else {
            this.smoothScrollTo(mCloseX, 0)
            isOpen = false
        }
    }

    fun openMenu() {
        if (isOpen) {
            return
        }
        this.smoothScrollTo(mOpenX, 0)
        isOpen = true
        mIonSlidingButtonListener?.onMenuIsOpen(this)
    }

    fun closeMenu() {
        if (!isOpen) {
            return
        }
        this.smoothScrollTo(mCloseX, 0)
        isOpen = false
    }

    fun setSlidingButtonListener(listener: IonSlidingButtonListener) {
        this.mIonSlidingButtonListener = listener
    }

    interface IonSlidingButtonListener {
        fun onMenuIsOpen(view: View)

        fun onDownOrMove(slidingButtonView: SlidingButtonView)
    }
}