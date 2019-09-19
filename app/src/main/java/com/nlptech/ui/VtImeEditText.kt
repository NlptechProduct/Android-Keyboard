package com.nlptech.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.EditText
import com.android.inputmethod.latin.LatinIME

class VtImeEditText(context: Context?, attrs: AttributeSet?) :
        EditText(context, attrs) {


    companion object {
        const val ENABLE_IME_EDIT_TEXT_DELAY_DURATION: Long = 200
    }

    private var mEI: EditorInfo? = null

    private fun createLocalEditInfo(): EditorInfo {
        val localEditorInfo = EditorInfo()
        localEditorInfo.imeOptions = EditorInfo.IME_ACTION_SEARCH
        localEditorInfo.actionId = EditorInfo.IME_ACTION_SEARCH
        localEditorInfo.fieldId = id
        localEditorInfo.inputType = inputType
        localEditorInfo.fieldName = context.packageName + ".GifSearchEditText"
        localEditorInfo.packageName = context.packageName
        localEditorInfo.hintText = hint
        localEditorInfo.initialSelEnd = selectionStart
        localEditorInfo.initialSelEnd = selectionEnd
        return localEditorInfo
    }

    fun getEditInfo(): EditorInfo {
        if (mEI == null) {
            mEI = createLocalEditInfo()
        }
        return mEI as EditorInfo
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateInputConnection(outAttrs: EditorInfo?): InputConnection {
        if (mEI == null)
            mEI = createLocalEditInfo()

        val gestureDetector = GestureDetector(object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                return super.onSingleTapConfirmed(e)
            }
        })

        this.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                return gestureDetector.onTouchEvent(event)
            }
        })

        return super.onCreateInputConnection(mEI)
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        // 重新整理 composing text 狀態
        LatinIME.getInstance().onUpdateSelection(
                LatinIME.getInstance().inputLogic.expectedSelectionStart,
                LatinIME.getInstance().inputLogic.expectedSelectionEnd,
                selStart,
                selEnd,
                LatinIME.getInstance().inputLogic.composingStart,
                LatinIME.getInstance().inputLogic.composingStart + LatinIME.getInstance().inputLogic.composingLength)

    }


    /**
     * 只給ImeEditText使用
     */
    fun enableImeEditText() {
        val editorInfo = getEditInfo()
        LatinIME.getInstance().onFinishInputView(true)
        LatinIME.getInstance().inputLogic.setImeEditTextICAndEI(onCreateInputConnection(null), editorInfo)
        postDelayed({
            requestFocus()
            callOnClick()
        }, ENABLE_IME_EDIT_TEXT_DELAY_DURATION)
    }

    /**
     * 只給ImeEditText使用
     */
    fun disableImeEditText() {
        LatinIME.getInstance().inputLogic.setImeEditTextICAndEI(null, null)
    }
}