package com.nlptech.function.suggestionStrip;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.nlptech.keyboardview.keyboard.chinese.ChineseComposingTextView;

public class CustomizedChineseComposingTextView extends ChineseComposingTextView {

    private TextView mComposingTextView;

    public CustomizedChineseComposingTextView(Context context) {
        super(context);
    }

    public CustomizedChineseComposingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomizedChineseComposingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mComposingTextView = findViewById(com.nlptech.keyboardview.R.id.composing_tv);
    }


    @Override
    public void setComposingText(String s) {
        mComposingTextView.setText(s);
    }
}
