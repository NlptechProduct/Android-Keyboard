package com.nlptech.keybaordwidget.draggable;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;

import com.nlptech.common.utils.LogUtil;
import com.nlptech.keybaordwidget.KeyboardWidgetManager;

public class DraggableLayout extends FrameLayout {
    private static final String TAG = "DraggableLayout";

    private static final int ANIMATION_DURATION_100 = 100;
    private static final int ANIMATION_DURATION_150 = 150;
    private static final int ANIMATION_DURATION_250 = 250;

    private static final int MOVE_HIGH_TRANSLATION_Y_DIFFERENCE = 50;

    public static final int STATE_LOW = 0;
    public static final int STATE_HIGH = 1;
    public static final int STATE_MIDDLE = 2;
    public static final int STATE_CLOSE = 3;
    public static final int STATE_OPEN = 4;

    private View childView;
    private float minTransY = 0;
    private float maxTransY;
    private float lastY;
    private int state = STATE_LOW;
    private int stateLowTranslationY;
    private int stateHighTranslationY;
    private boolean scrollable = true;
    private Callback callback;
    private boolean enableHighMode = true;
    private RecyclerView recyclerView;

    private boolean mIsFirst = true;
    private boolean mDragStart = false;
    private boolean mIsFling = false;
    private boolean mTouchTopOutSide = false;

    public DraggableLayout(Context context) {
        this(context, null);
    }

    public DraggableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private GestureDetector mGestureDetector;
    private void init(@NonNull Context context) {
        setEnableHighMode(context.getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);

        FlingDetector flingDetector = new FlingDetector();
        // Pass the FlingDetector to mGestureDetector to receive the appropriate callbacks
        mGestureDetector = new GestureDetector(getContext(), flingDetector);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (childView == null) {
            childView = getChildAt(0);
            stateHighTranslationY = 0;
        }

        updateMinMaxTransY();
        stateLowTranslationY = getMeasuredHeight() - KeyboardWidgetManager.getInstance().getTotalKeyboardHeight(getContext());

        if (mIsFirst) {
            mIsFirst = false;
            childView.setTranslationY(getMeasuredHeight());
            animateTo(STATE_OPEN, new AnimCallback() {
                @Override
                public void onEnd() {
                    setState(STATE_LOW);
                    if (callback != null) {
                        callback.onFinishOpenAnimation();
                    }
                }

                @Override
                public void onStart() {

                }
            });
        }
    }

    public void setChild(View view) {
        childView = view;
        addView(view, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setStateHighTranslationY(int y) {
        stateHighTranslationY = y;
    }

    public float getStateLowTranslationY() {
        return stateLowTranslationY;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public boolean canChildScrollUp() {
        if (callback != null && callback.scrollOnChild()) {
            return true;
        }

        if (recyclerView == null) {
            return false;
        }

        return recyclerView.canScrollVertically(-1);
    }

    public boolean canChildScrollDown() {
        if (childView.getTranslationY() == 0) {
            return true;
        }

        if (recyclerView == null) {
            return false;
        }
        return recyclerView.canScrollVertically(1);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!scrollable) {
            return false;
        }
        float y = ev.getY();
        if (y < childView.getTranslationY() && ev.getAction() == MotionEvent.ACTION_DOWN) {
            mTouchTopOutSide = true;
            if (callback != null) {
                callback.onTouchTopOutSideClose();
            }
            return true;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                return false;
            case MotionEvent.ACTION_MOVE: {
                float dy = y - lastY;
                if (Math.abs(dy) >= ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    if (state == STATE_HIGH) {
                        if (dy > 0 && canChildScrollUp()) {
                            return false;
                        } else if (dy < 0 && canChildScrollDown()) {
                            return false;
                        }
                    } else if (!enableHighMode) {
                        if (state == STATE_LOW) {
                            if (dy < 0) {
                                return false;
                            } else if (dy > 0 && canChildScrollUp()) {
                                return false;
                            }
                        }
                    }
                    return true;
                }
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);

        if (!scrollable) {
            return false;
        }

        if (mTouchTopOutSide || mIsFling) {
            return true;
        }

        float currentY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = currentY;
                return true;
            case MotionEvent.ACTION_MOVE: {
                if (!mDragStart) {
                    mDragStart = true;
                    callback.onDragStart();
                }

                float dy = currentY - lastY;
                lastY = currentY;

                boolean doSwipeClose = false;
                // case 1 : 正常拖动數值
                float newTransY = childView.getTranslationY() + dy;
                // case 2 : 低于最小可移动高度Y
                if (newTransY < minTransY) {
                    newTransY = minTransY;
                }
                // case 3 : 达到上限位置以后可以继续向上运动，运动距离衰减为手指移动距离的1/2
                else if (newTransY < stateHighTranslationY) {
                    newTransY = childView.getTranslationY() + dy / 2;
                }
                // case 4 : 超过键盘底端 最多设置为底端
                else if (newTransY >= getMeasuredHeight()) {
                    newTransY = getMeasuredHeight();
                    doSwipeClose = true;
                }
                childView.setTranslationY(newTransY);
                callback.onTranslationYChanged(newTransY);

                // 超过键盘底端 关闭
                if (doSwipeClose && callback != null) {
                    callback.onSwipeClose();
                }
                return true;
            }
            case MotionEvent.ACTION_UP:
                mDragStart = false;
                lastY = currentY;

                // 在键盘工具列下方
                if (childView.getTranslationY() > stateLowTranslationY) {
                    int closeBoundary = stateLowTranslationY + (getMeasuredHeight() - stateLowTranslationY) / 2;
                    // 在键盘工具列下方空间一半以上 回到工具列高度 反之关闭
                    boolean needClose = childView.getTranslationY() > closeBoundary;
                    animateTo(needClose ? STATE_CLOSE : STATE_LOW, ANIMATION_DURATION_150, null);
                }
                // 在键盘工具列上方
                else {
                    // 需要缓冲值让他返回最高高度 (ex : Touch Event很难刚好把Y移动到0)
                    if (childView.getTranslationY() < stateHighTranslationY + MOVE_HIGH_TRANSLATION_Y_DIFFERENCE) {
                        animateTo(STATE_HIGH, ANIMATION_DURATION_150, null);
                    } else {
                        // 直接停放
                        setState(STATE_MIDDLE);
                    }
                }
                return true;
        }
        return true;
    }

    public void animateTo(final int state, final AnimCallback animCallback) {
        animateTo(state, -1, animCallback);
    }

    public void animateTo(final int state, long animDuration, final AnimCallback animCallback) {
        float value;
        long duration;
        TimeInterpolator interpolator;
        if (state == STATE_LOW) {
            value = stateLowTranslationY;
            interpolator = new LinearOutSlowInInterpolator();
            duration = ANIMATION_DURATION_150;
        } else if (state == STATE_HIGH) {
            value = stateHighTranslationY;
            interpolator = new LinearOutSlowInInterpolator();
            duration = ANIMATION_DURATION_150;
        } else if (state == STATE_CLOSE) {
            value = getMeasuredHeight();
            float ratio = (childView.getTranslationY() - stateLowTranslationY)/(stateHighTranslationY - stateLowTranslationY);
            duration = (long) (ANIMATION_DURATION_250 + (ANIMATION_DURATION_100 * (ratio)));
            if(duration < 0){
                duration = ANIMATION_DURATION_250;
            }
            interpolator = new FastOutLinearInInterpolator();
        } else if (state == STATE_OPEN) {
            value = stateLowTranslationY;
            duration = ANIMATION_DURATION_250;
            interpolator = new LinearOutSlowInInterpolator();
        } else {
            return;
        }

        if (animDuration > 0) {
            duration = animDuration;
        }

        LogUtil.i(TAG, "[animateTo] duration : " + duration + ", interpolator : " + interpolator +
                ", value : " + value + ", state : " + state);

        childView.animate()
                .translationY(value)
                .setDuration(duration)
                .setInterpolator(interpolator)
                .setUpdateListener(animation -> {
                    float y = (float) animation.getAnimatedValue() * value;
                    callback.onTranslationYChanged(y);
                })
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        if (animCallback != null) {
                            animCallback.onStart();
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        setState(state);
                        if (animCallback != null) {
                            animCallback.onEnd();
                        }
                        if (state == STATE_CLOSE) {
                            if (callback != null) {
                                callback.onSwipeClose();
                            }
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
    }

    public void animateTo(int state) {
        animateTo(state, null);
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return this.state;
    }

    public boolean isHighState() {
        return state == STATE_HIGH;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    public void setEnableHighMode(boolean enableHighMode) {
        this.enableHighMode = enableHighMode;
        updateMinMaxTransY();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private void updateMinMaxTransY() {
        int keyboardHeight = KeyboardWidgetManager.getInstance().getTotalKeyboardHeight(getContext());
        maxTransY = getMeasuredHeight() - keyboardHeight;
        if (!enableHighMode) {
            minTransY = maxTransY;
        } else  {
            minTransY = 0;
        }
    }

    public interface AnimCallback {

        void onEnd();

        void onStart();

    }

    public interface Callback {
        void onSwipeClose();

        void onTouchTopOutSideClose();

        void onDragStart();

        void onTranslationYChanged(float y);

        void onFinishOpenAnimation();

        boolean scrollOnChild();
    }

    public Rect getViewTouchableRect() {
        Rect rect = new Rect();
        rect.set(childView.getLeft(), childView.getTop(), childView.getRight(), childView.getHeight());
        return rect;
    }

    private class FlingDetector extends GestureDetector.SimpleOnGestureListener {

        static final float FRICTION_SCALE = 1.2f;

        FlingDetector() {
            super();
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            mIsFling = true;
            // 键盘高度的1/2
            int closeBoundary = stateLowTranslationY + (getMeasuredHeight() - stateLowTranslationY) / 2;
            // 预测最终静止时的位置
            float predictionTranslationY = getPredictionTranslationY(velocityY);

            // 开始Fling前最后的高度是在键盘高度以上的状态 且预测Fling后位置会低于键盘高度 停留在键盘高度
            if (childView.getTranslationY() < stateLowTranslationY && predictionTranslationY > stateLowTranslationY) {
                int d = (int) ((childView.getTranslationY() * ANIMATION_DURATION_250) / velocityY);
                if (d > ANIMATION_DURATION_250) {
                    d = ANIMATION_DURATION_250;
                }
                animateTo(STATE_LOW, d, null);
                mIsFling = false;
                return true;
            }

            // 静止时菜单顶端低于键盘高度的1/2 放弃执行FlingAnimation 以当前速度或(高度/250ms)的较大者向下滑动至退出
            if (predictionTranslationY > closeBoundary) {
                int d = (int) ((childView.getTranslationY() * ANIMATION_DURATION_250) / velocityY);
                if (d > ANIMATION_DURATION_250) {
                    d = ANIMATION_DURATION_250;
                }
                animateTo(STATE_CLOSE, d, null);
                mIsFling = false;
                return true;
            }

            // 执行FlingAnimation
            FlingAnimation flingAnimation = new FlingAnimation(childView, DynamicAnimation.TRANSLATION_Y);
            flingAnimation.setStartVelocity(velocityY)
                    .setMinValue(minTransY) // minimum translationX property
                    .setMaxValue(getMeasuredHeight())  // maximum translationX property
                    .setFriction(FRICTION_SCALE)
                    .addUpdateListener((animation, value, velocity) -> {
                        callback.onTranslationYChanged(value);
                    })
                    .addEndListener((dynamicAnimation, b, v, v1) -> {
                        mIsFling = false;
                        if (v > stateLowTranslationY) {
                            animateTo(STATE_LOW, ANIMATION_DURATION_100, null);
                        } else if (v < stateHighTranslationY) {
                            animateTo(STATE_HIGH, ANIMATION_DURATION_100, null);
                        } else if (v == stateHighTranslationY) {
                            setState(STATE_HIGH);
                            callback.onTranslationYChanged(v);
                        } else {
                            setState(STATE_MIDDLE);
                            callback.onTranslationYChanged(v);
                        }
                    })
                    .start();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        /**
         * get the formula from /frameworks/support/dynamic-animation/src/android/support/animation/FlingAnimation.java
         * without "deltaT"
         * **/
        public float getPredictionTranslationY(float velocityY) {
            float friction = (FRICTION_SCALE*-4.2f);
            return (float) (childView.getTranslationY() - velocityY / friction
                    + velocityY / friction * Math.exp(friction));
        }
    }
}
