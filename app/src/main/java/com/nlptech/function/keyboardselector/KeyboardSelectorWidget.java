package com.nlptech.function.keyboardselector;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputConnection;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.inputmethod.TestApplication;
import com.android.inputmethod.latin.LatinIME;
import com.android.inputmethod.latin.R;
import com.nlptech.function.keyboardclipboard.ClipManager;
import com.nlptech.keybaordwidget.KeyboardWidgetManager;
import com.nlptech.keybaordwidget.draggable.DraggableKeyboardWidget;
import com.nlptech.keyboardview.theme.KeyboardThemeManager;

public class KeyboardSelectorWidget extends DraggableKeyboardWidget implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {

    private final static long sMoveInterval = 200L;

    private ImageView mTop, mBottom, mLeft, mRight;
    private View mCopy, mPaste, mDelete, mSelectAll;
    private ImageView mImgSelectAll;
    private TextView mTextSelectAll;

    private ImageView mImgCopy;
    private TextView mTextCopy;
    private ImageView mImgPaste;
    private TextView mTextPaste;
    private ImageView mImgDelete;
    private TextView mTextDelete;


    private TextView mSelect;
    private int selStart, selEnd;

    private int mToolBarItemcolor = 0xff000000;
    private int mOperationEnableColor = 0Xff666666;
    private int mOperationDisableColor = 0Xffdbdbdb;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private enum Direct {
        top, bottom, left, right,
    }

    private enum Status {
        select, direct
    }

    private Status mStatus = Status.direct;

    private MoveTask mMoveTask = new MoveTask();

    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.keyboard_selector, null);
    }

    @Override
    public void onViewCreated(Intent intent) {
        super.onViewCreated(intent);
        View view = getView();

        View mainLayout = view.findViewById(R.id.keyboard_selector_main_layout);
        ViewGroup.LayoutParams lp = mainLayout.getLayoutParams();
        lp.height = KeyboardWidgetManager.getInstance().getTotalKeyboardHeight(getContext());
        mainLayout.setLayoutParams(lp);

        // Background
        View rootView = view.findViewById(R.id.keyboard_selector);
        KeyboardThemeManager.getInstance().setUiModuleBackground(rootView);

        // Title and close
        KeyboardThemeManager.getInstance().colorUiModuleTitleText(view.findViewById(R.id.keyboard_selector_title_tv));

        ImageButton close = view.findViewById(R.id.keyboard_selector_top_close_btn);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(close, mToolBarItemcolor);
        close.setOnClickListener(this);

        // set other view's click listener
        mSelect = view.findViewById(R.id.select);
        mSelect.setOnClickListener(this);
        KeyboardThemeManager.getInstance().colorUiModuleText(mSelect);


        mTop = view.findViewById(R.id.arrow_up);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(mTop, mOperationEnableColor);
        mTop.setOnClickListener(this);
        mTop.setOnLongClickListener(this);
        mTop.setOnTouchListener(this);

        mBottom = view.findViewById(R.id.arrow_down);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(mBottom, mOperationEnableColor);
        mBottom.setOnClickListener(this);
        mBottom.setOnLongClickListener(this);
        mBottom.setOnTouchListener(this);

        mLeft = view.findViewById(R.id.arrow_left);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(mLeft, mOperationEnableColor);
        mLeft.setOnClickListener(this);
        mLeft.setOnLongClickListener(this);
        mLeft.setOnTouchListener(this);

        mRight = view.findViewById(R.id.arrow_right);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(mRight, mOperationEnableColor);
        mRight.setOnClickListener(this);
        mRight.setOnLongClickListener(this);
        mRight.setOnTouchListener(this);

        mCopy = view.findViewById(R.id.copy);
        mCopy.setOnClickListener(this);
        mImgCopy = view.findViewById(R.id.copy_iv);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(mImgCopy, mOperationEnableColor);
        mTextCopy = view.findViewById(R.id.copy_tv);
        KeyboardThemeManager.getInstance().colorUiModuleText(mTextCopy);

        mPaste = view.findViewById(R.id.paste);
        mPaste.setOnClickListener(this);
        mImgPaste = view.findViewById(R.id.paste_iv);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(mImgPaste, mOperationEnableColor);
        mTextPaste = view.findViewById(R.id.paste_tv);
        KeyboardThemeManager.getInstance().colorUiModuleText(mTextPaste);

        mDelete = view.findViewById(R.id.delete);
        mDelete.setOnClickListener(this);
        mImgDelete = view.findViewById(R.id.delete_iv);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(mImgDelete, mOperationEnableColor);
        mTextDelete = view.findViewById(R.id.delete_tv);
        KeyboardThemeManager.getInstance().colorUiModuleText(mTextDelete);

        mSelectAll = view.findViewById(R.id.select_all);
        mSelectAll.setOnClickListener(this);
        mImgSelectAll = view.findViewById(R.id.select_all_iv);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(mImgSelectAll, mOperationEnableColor);
        mTextSelectAll = view.findViewById(R.id.select_all_tv);
        KeyboardThemeManager.getInstance().colorUiModuleText(mTextSelectAll);

        updateSelector();
        updateStatus();
        recordSelect();
    }

    @Override
    public void onResume() {
        super.onResume();

        mStatus = Status.direct;
        cancelSelectWithSelection();
        updateStatus();
        updateSelector();
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelSelectWithSelection();
    }

    @Override
    public boolean isExtendedInFloatingKeyboard() {
        return false;
    }

    @NonNull
    @Override
    protected boolean isEnableHeightMode(Context context) {
        return false;
    }

    @Override
    public boolean onLongClick(View v) {
        Direct d;
        if (mTop == v) {
            d = (Direct.top);
        } else if (mBottom == v) {
            d = (Direct.bottom);
        } else if (mLeft == v) {
            d = (Direct.left);
        } else if (mRight == v) {
            d = (Direct.right);
        } else {
            return true;
        }
        if (mMoveTask == null) {
            mMoveTask = new MoveTask();
        }
        mMoveTask.changeDir(d);
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int act = event.getAction();
        if (act == MotionEvent.ACTION_UP || act == MotionEvent.ACTION_CANCEL) {
            mMoveTask.changeDir(null);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        InputConnection ic;
        switch (v.getId()) {
            case R.id.keyboard_selector_top_close_btn:
                close();
                break;
            case R.id.select:
                if (mStatus == Status.direct) {
                    mStatus = Status.select;
                    beginSelect();
                } else {
                    mStatus = Status.direct;
                    cancelSelectWithSelection();
                }
                updateSelector();
                break;
            case R.id.select_all:
                ic = getCurrentIC();
                if (ic == null)
                    return;
                boolean hasSelection = (Boolean) mSelectAll.getTag();
                if (hasSelection) {
                    ic.performContextMenuAction(android.R.id.cut);
                } else {
                    ic.performContextMenuAction(android.R.id.selectAll);
                }
//                cancelSelectWithSelection();
                break;
            case R.id.arrow_up:
                moveCursor(Direct.top);
                break;
            case R.id.arrow_down:
                moveCursor(Direct.bottom);
                break;
            case R.id.arrow_left:
                moveCursor(Direct.left);
                break;
            case R.id.arrow_right:
                moveCursor(Direct.right);
                break;
            case R.id.copy:
                ic = getCurrentIC();
                if (ic == null || TextUtils.isEmpty(ic.getSelectedText(0)))
                    return;
                ic.performContextMenuAction(android.R.id.copy);
                cancelSelectWithSelection();
                Toast.makeText(TestApplication.getInstance(), R.string.keyboard_clipboard_copy_success, Toast.LENGTH_LONG).show();
                break;
            case R.id.paste:
                ic = getCurrentIC();
                if (null == ic) {
                    return;
                }
                LatinIME.getInstance().getInputLogic().onPasteFromClipBoardEvent(ClipManager.getInstance().getPasteTipText());
                ic.performContextMenuAction(android.R.id.paste);
                cancelSelect();
                break;
            case R.id.delete:
                ic = getCurrentIC();
                if (ic != null) {
                    if (TextUtils.isEmpty(ic.getSelectedText(0))) {
                        LatinIME.getInstance().getInputLogic().onDeleteFromClipBoardEvent(ic.getTextBeforeCursor(1, 0).toString());
                    } else {
                        LatinIME.getInstance().getInputLogic().onDeleteFromClipBoardEvent(ic.getSelectedText(0).toString());
                    }
                }
                LatinIME.getInstance().getInputLogic().sendDownUpKeyEvent(KeyEvent.KEYCODE_DEL);
                break;
            default:
                break;
        }

        mHandler.post(this::updateStatus);
    }

    private void updateStatus() {
        InputConnection ic = getCurrentIC();
        if (ic == null) {
            return;
        }
        CharSequence cs = ic.getSelectedText(0);
        boolean hasSelection = !TextUtils.isEmpty(cs);
        boolean hasClipData = ClipManager.getInstance().hasClipData();
        updateSelectAllStatus(hasSelection);
        updateCopyStatus(hasSelection);
        updatePasteStatus(hasClipData);
        updateDeleteStatus(hasSelection || !TextUtils.isEmpty(ic.getTextBeforeCursor(1, 0)));
    }

    private void updateSelectAllStatus(boolean hasSelection) {
        mSelectAll.setTag(hasSelection);
        mTextSelectAll.setTextColor(mOperationEnableColor);
        if (hasSelection) {
            mTextSelectAll.setText(R.string.keyboard_selector_cut);
            mImgSelectAll.setImageResource(R.drawable.ic_selector_cut);
            KeyboardThemeManager.getInstance().colorUiModuleIcon(mImgSelectAll, mOperationEnableColor);
        } else {
            mTextSelectAll.setText(R.string.keyboard_selector_select_all);
            mImgSelectAll.setImageResource(R.drawable.ic_selector_select_all);
            KeyboardThemeManager.getInstance().colorUiModuleIcon(mImgSelectAll, mOperationEnableColor);
        }
    }

    private void updateCopyStatus(boolean enable) {
        mCopy.setEnabled(enable);
        int color = enable ? mOperationEnableColor : mOperationDisableColor;
        mTextCopy.setTextColor(color);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(mImgCopy, color);
    }

    private void updatePasteStatus(boolean enable) {
        mPaste.setEnabled(enable);
        int color = enable ? mOperationEnableColor : mOperationDisableColor;
        mTextPaste.setTextColor(color);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(mImgPaste, color);
    }

    private void updateDeleteStatus(boolean enable) {
        mDelete.setEnabled(enable);
        int color = enable ? mOperationEnableColor : mOperationDisableColor;
        mTextDelete.setTextColor(color);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(mImgDelete, color);
    }

    private void updateSelector() {
        if (mSelect == null) {
            return;
        }

        mSelect.setSelected(mStatus != Status.direct);
    }

    private InputConnection getCurrentIC() {
        return LatinIME.getInstance().getInputLogic().getCurrentInputConnection();
    }

    private void sendKeyEvent(KeyEvent key) {
        if (getCurrentIC() != null) {
            getCurrentIC().sendKeyEvent(key);
        }
    }

    private void cancelSelectWithSelection() {
        cancelSelect();
        revertSelect();

    }

    private void cancelSelect() {
        if (getCurrentIC() == null) {
            return;
        }
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_SHIFT_LEFT);
        sendKeyEvent(event);
        mStatus = Status.direct;
        updateSelector();

    }

    private void beginSelect() {
        if (getCurrentIC() == null) {
            return;
        }
        recordSelect();
        if (mStatus != Status.select) {
            cancelSelectWithSelection();
            return;
        }

        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SHIFT_LEFT);
        sendKeyEvent(event);
    }

    private void recordSelect() {
        selStart = LatinIME.getInstance().getInputLogic().getCurrentSelection()[0];
        selEnd = LatinIME.getInstance().getInputLogic().getCurrentSelection()[1];
    }

    private void revertSelect() {
        if (selStart < 0) {
            selStart = selEnd = LatinIME.getInstance().getInputLogic().getCurrentSelection()[0];
            return;
        }
        LatinIME.getInstance().getInputLogic().setSelection(selStart, selEnd);
    }

    private void moveCursor(Direct d) {
        if (getCurrentIC() == null) {
            return;
        }
        if (d != null) {
            if (d == Direct.top) {
                LatinIME.getInstance().getInputLogic().sendDownUpKeyEvent(KeyEvent.KEYCODE_DPAD_UP);
            } else if (d == Direct.bottom) {
                LatinIME.getInstance().getInputLogic().sendDownUpKeyEvent(KeyEvent.KEYCODE_DPAD_DOWN);
            } else if (d == Direct.left) {
                LatinIME.getInstance().getInputLogic().sendDownUpKeyEvent(KeyEvent.KEYCODE_DPAD_LEFT);
            } else if (d == Direct.right) {
                LatinIME.getInstance().getInputLogic().sendDownUpKeyEvent(KeyEvent.KEYCODE_DPAD_RIGHT);
            }
        }
    }

    private class MoveTask implements Runnable {
        Direct dir = null; // top, bottom, left, right
        long interval = sMoveInterval;
        private boolean isRunning = false;

        public void run() {
            mHandler.removeCallbacks(this);
            if (dir == null) {
                isRunning = false;
                return;
            }
            action();
            isRunning = true;
            mHandler.postDelayed(this, interval);
        }

        private void action() {
            if (null == dir) {
                return;
            }
            moveCursor(dir);
            updateStatus();
        }

        void changeDir(Direct d) {
            if (dir != d) {
                dir = d;
                if (dir == Direct.right || dir == Direct.left) {
                    interval = sMoveInterval;
                } else if (dir == Direct.top || dir == Direct.bottom) {
                    interval = 3 * sMoveInterval / 2;
                }
            }

            if (dir != null && !isRunning) {
                isRunning = true;
                mHandler.postDelayed(mMoveTask, interval);
            } else {

            }
        }
    }
}
