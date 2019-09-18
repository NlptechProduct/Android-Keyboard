package com.nlptech.keybaordwidget;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

public abstract class KeyboardWidget implements LifecycleOwner {

    public static class Type {
        /**
         * 標準型Module，
         * 每一次openModule都會新建一個新的Module
         **/
        public static final int NORMAL = 0;

        /**
         * 單例型Module，
         * 只有在LatinIME.onDestroy時，
         * 或是LatinIMEonCreateInputView時，
         * 才會被destroy，
         * 否則一般情況下，被關閉後只會走到pause，並且放到cache中。
         **/
        public static final int SINGLETON = 1;
    }

    private LifecycleRegistry mLifecycleRegistry;

    private View mView;
    private Intent mIntent;

    public KeyboardWidget() {
    }

    /**
     * @return {@link Type#NORMAL} or {@link Type#NORMAL}, default is {@link Type#NORMAL}
     **/
    public int getType(){
        return Type.NORMAL;
    }

    public void onCreate(@Nullable Intent intent) {
        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
    }

    public View performCreateView(LayoutInflater inflater, ViewGroup container) {
        mView = onCreateView(inflater, container);
        return mView;
    }

    @NonNull
    public abstract View onCreateView(LayoutInflater inflater, ViewGroup container);

    public void onViewCreated(Intent intent) {
    }

    public void onResume() {
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
    }

    public void onPause() {
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
    }

    public void onDestroy() {
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    }

    public void onNewIntent(Intent intent) {
    }

    public View getView() {
        return mView;
    }

    public Context getContext() {
        if (mView != null) {
            return mView.getContext();
        } else {
            return null;
        }
    }

    public Resources getResources() {
        if (mView != null) {
            return mView.getContext().getResources();
        } else {
            return null;
        }
    }

    /**
     * 用來決定可觸控的範圍
     **/
    public Rect getViewTouchableRect() {
        Rect rect = new Rect();
        if (mView != null) {
            mView.getGlobalVisibleRect(rect);
        }
        return rect;
    }

    public int getHeight() {
        if (mView == null) {
            return 0;
        }
        return mView.getMeasuredHeight();
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }
}
