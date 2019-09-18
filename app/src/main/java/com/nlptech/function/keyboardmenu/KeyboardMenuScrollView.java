package com.nlptech.function.keyboardmenu;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import android.util.AttributeSet;

public class KeyboardMenuScrollView extends NestedScrollView {
    private OnScrollChangeListener mListener;

    public KeyboardMenuScrollView(@NonNull Context context) {
        super(context);
    }

    public KeyboardMenuScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardMenuScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnScrollChangeListener(@Nullable OnScrollChangeListener l) {
        mListener = l;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mListener != null) {
            mListener.onScrollChange(this, l, t, oldl, oldt);
        }
    }
}
