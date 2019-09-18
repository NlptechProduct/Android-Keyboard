package com.nlptech.keybaordwidget.draggable;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nlptech.keybaordwidget.KeyboardWidget;
import com.nlptech.keybaordwidget.KeyboardWidgetManager;

import static com.nlptech.keybaordwidget.draggable.DraggableLayout.STATE_CLOSE;

public abstract class DraggableKeyboardWidget extends KeyboardWidget implements DraggableLayout.Callback {
    public static final String TAG = DraggableKeyboardWidget.class.getSimpleName();

    private DraggableLayout mDraggableLayout;

    @Override
    public DraggableLayout onCreateView(LayoutInflater inflater, ViewGroup container) {
        mDraggableLayout = new DraggableLayout(container.getContext());
        if (getCustomAppTheme() != -1) {
            mDraggableLayout.getContext().setTheme(getCustomAppTheme());
        }
        mDraggableLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mDraggableLayout.setChild(onCreateContentView(inflater));
        mDraggableLayout.setEnableHighMode(isEnableHeightMode(container.getContext()));
        mDraggableLayout.setCallback(this);
        return mDraggableLayout;
    }

    @NonNull
    protected abstract View onCreateContentView(LayoutInflater inflater);

    protected abstract boolean isEnableHeightMode(Context context);

    @Override
    public void onSwipeClose() {
        KeyboardWidgetManager.getInstance().close(getClass());
    }

    @Override
    public void onTouchTopOutSideClose() {
        close();
    }

    @Override
    public boolean scrollOnChild() {
        return false;
    }

    @Override
    public void onDragStart() {
    }

    @Override
    public void onTranslationYChanged(float y) {
    }

    @Override
    public void onFinishOpenAnimation() {
    }

    protected void close() {
        mDraggableLayout.animateTo(STATE_CLOSE);
    }

    @Override
    public Rect getViewTouchableRect() {
        return mDraggableLayout.getViewTouchableRect();
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        mDraggableLayout.setRecyclerView(recyclerView);
    }

    public void setStateHighTranslationY(int y) {
        mDraggableLayout.setStateHighTranslationY(y);
    }

    public void setEnableHighMode(boolean enableHighMode) {
        mDraggableLayout.setEnableHighMode(enableHighMode);
    }

    public float getStateLowTranslationY() {
        return mDraggableLayout.getStateLowTranslationY();
    }

    protected int getCustomAppTheme() {
        return -1;
    }
}
