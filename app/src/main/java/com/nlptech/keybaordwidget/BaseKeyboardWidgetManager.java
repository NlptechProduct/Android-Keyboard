package com.nlptech.keybaordwidget;


import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Region;
import android.inputmethodservice.InputMethodService;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.android.inputmethod.latin.R;
import com.nlptech.inputmethod.latin.utils.ResourceUtils;
import com.nlptech.keyboardview.keyboard.KeyboardSwitcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseKeyboardWidgetManager {
    public static final String TAG = BaseKeyboardWidgetManager.class.getSimpleName();

    private ViewGroup mContainer;
    private Map<String, KeyboardWidget> mWidgetCache = new HashMap<>();
    private Map<String, KeyboardWidget> mSingletonPausedWidgetCache = new HashMap<>();

    public void onCreateInputView(View inputView) {
        clear();
        mContainer = inputView.findViewById(R.id.input_view_keyboard_widget_container);
        boolean isFloatingKeyboard = KeyboardSwitcher.getInstance().isFloatingKeyboard();
        setContainerPadding(ResourceUtils.getKeyboardResizeLeftPadding(isFloatingKeyboard), ResourceUtils.getKeyboardResizeRightPadding(isFloatingKeyboard));
    }

    public void onFinishInputView(boolean finishingInput) {
        closeAll();
    }

    public void onDestroy() {
        clear();
    }

    public void onComputeInsets(final InputMethodService.Insets outInsets) {
        List<KeyboardWidget> widgets = get();
        for (int i = 0; i < widgets.size(); i++) {
            KeyboardWidget widget = widgets.get(i);
            Rect childViewRect = widget.getViewTouchableRect();
            outInsets.touchableRegion.op(childViewRect.left, childViewRect.top, childViewRect.right, childViewRect.bottom, Region.Op.UNION);
        }
    }

    /**
     * Container Padding
     **/
    public void setContainerPadding(int leftPadding, int rightPadding) {
        if (mContainer == null) {
            return;
        }
        mContainer.setPadding(leftPadding, 0,
                rightPadding, 0);
    }

    /**
     * Open
     **/
    public void open(Class<? extends KeyboardWidget> widgetClass) {
        open(widgetClass, null);
    }

    public void open(Class<? extends KeyboardWidget> widgetClass, Intent intent) {
        KeyboardWidget module = get(widgetClass);
        if (module != null) {
            return;
        }
        String key = widgetClass.getName();
        KeyboardWidget widget = createWidget(widgetClass, intent);

        if (widget != null) {
            mWidgetCache.put(key, widget);
        }

        if (widget != null) {
            KeyboardSwitcher.getInstance().refreshFloatingKeyboard();
            widget.onResume();
        }
    }

    /**
     * Close
     **/
    public void close(Class<? extends KeyboardWidget> widgetClass) {
        KeyboardWidget widget = get(widgetClass);
        if (widget == null) {
            return;
        }
        destroyWidget(widget);
        String key = widgetClass.getName();
        mWidgetCache.remove(key);

        KeyboardSwitcher.getInstance().refreshFloatingKeyboard();
    }

    public void closeAll() {
        for (Map.Entry<String, KeyboardWidget> entry : mWidgetCache.entrySet()) {
            KeyboardWidget widget = entry.getValue();
            destroyWidget(widget);
        }
        mWidgetCache.clear();

        KeyboardSwitcher.getInstance().refreshFloatingKeyboard();
    }

    /**
     * Clear
     **/
    public void clear() {
        closeAll();
        for (KeyboardWidget module : mSingletonPausedWidgetCache.values()) {
            module.onDestroy();
        }
        mSingletonPausedWidgetCache.clear();
    }

    /**
     * Get
     **/
    public KeyboardWidget get(Class<? extends KeyboardWidget> widgetClass) {
        String key = widgetClass.getName();
        return mWidgetCache.get(key);
    }

    public List<KeyboardWidget> get() {
        List<KeyboardWidget> widgets = new ArrayList<>();
        for (Map.Entry<String, KeyboardWidget> entry : mWidgetCache.entrySet()) {
            KeyboardWidget widget = entry.getValue();
            widgets.add(widget);
        }
        return widgets;
    }

    public boolean isShown(Class<? extends KeyboardWidget> widgetClass) {
        KeyboardWidget module = get(widgetClass);
        return module != null;
    }

    public boolean hasWidgets() {
        return !mWidgetCache.isEmpty();
    }

    public ViewGroup getContainer() {
        return mContainer;
    }

    private KeyboardWidget createWidget(Class<? extends KeyboardWidget> widgetClass, Intent intent) {
        String key = widgetClass.getName();
        KeyboardWidget widget = mSingletonPausedWidgetCache.get(key);
        if (widget != null) {
            widget.onNewIntent(intent);
            mSingletonPausedWidgetCache.remove(key);
        } else {
            try {
                widget = widgetClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                Log.e(TAG, "New instance class of KeyboardWidget exception = " + e.toString());
                return null;
            }
            widget.onCreate(intent);
            widget.performCreateView(LayoutInflater.from(mContainer.getContext()), mContainer);
            widget.onViewCreated(intent);
        }
        if (widget.getView().getParent() == null) {
            mContainer.addView(widget.getView());
        }
        widget.onResume();
        return widget;
    }

    private void destroyWidget(@NonNull KeyboardWidget widget) {
        widget.onPause();
        mContainer.removeView(widget.getView());
        if (widget.getType() == KeyboardWidget.Type.SINGLETON) {
            String key = widget.getClass().getName();
            mSingletonPausedWidgetCache.put(key, widget);
        } else {
            widget.onDestroy();
        }
    }

    public boolean isExtendedInFloatingKeyboard() {
        List<KeyboardWidget> widgets = get();
        for (KeyboardWidget widget : widgets) {
            if (widget.isExtendedInFloatingKeyboard()) {
                return true;
            }
        }
        return false;
    }

    public void updatePadding() {
        boolean isFloatingKeyboard = KeyboardSwitcher.getInstance().isFloatingKeyboard();
        setContainerPadding(
                ResourceUtils.getKeyboardResizeLeftPadding(isFloatingKeyboard),
                ResourceUtils.getKeyboardResizeRightPadding(isFloatingKeyboard)
        );
    }
}
