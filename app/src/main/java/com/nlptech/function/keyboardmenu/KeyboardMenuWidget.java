package com.nlptech.function.keyboardmenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

import com.android.inputmethod.latin.LatinIME;
import com.android.inputmethod.latin.R;
import com.android.inputmethod.latin.settings.SettingsActivity;
import com.nlptech.common.constant.Constants;
import com.nlptech.common.utils.DisplayUtil;
import com.nlptech.common.utils.PrefUtil;
import com.nlptech.function.theme.theme_manage.ThemeManageActivity;
import com.nlptech.inputmethod.latin.settings.Settings;
import com.nlptech.inputmethod.latin.utils.ResourceUtils;
import com.nlptech.keybaordwidget.KeyboardWidgetManager;
import com.nlptech.keybaordwidget.draggable.DraggableKeyboardWidget;
import com.nlptech.keyboardtrace.KeyboardTrace;
import com.nlptech.keyboardview.keyboard.KeyboardSwitcher;
import com.nlptech.keyboardview.theme.KeyboardThemeManager;

import static com.nlptech.inputmethod.latin.settings.Settings.PREF_ADDITIONAL_NUMBER_ROW_SHOWN;
import static com.nlptech.inputmethod.latin.settings.Settings.PREF_AUTO_CORRECTION;
import static com.nlptech.inputmethod.latin.settings.Settings.PREF_FLOATING_MODE;
import static com.nlptech.inputmethod.latin.settings.Settings.PREF_GESTURE_INPUT;
import static com.nlptech.inputmethod.latin.settings.Settings.PREF_SHOW_SUGGESTIONS;
import static com.nlptech.inputmethod.latin.settings.Settings.PREF_SOUND_ON;
import static com.nlptech.inputmethod.latin.settings.Settings.PREF_VIBRATE_ON;
import static com.nlptech.keyboardtrace.trace.scepter.domain.TraceOtherItem.OP_AUTO_CORRECTION;
import static com.nlptech.keyboardtrace.trace.scepter.domain.TraceOtherItem.OP_GESTURE_INPUT;
import static com.nlptech.keyboardtrace.trace.scepter.domain.TraceOtherItem.OP_SHOW_NUMBER_ROW;
import static com.nlptech.keyboardtrace.trace.scepter.domain.TraceOtherItem.OP_SHOW_SUGGESTION;

public class KeyboardMenuWidget extends DraggableKeyboardWidget implements View.OnClickListener, NestedScrollView.OnScrollChangeListener, CompoundButton.OnCheckedChangeListener
//        , BackupManager.Listener 
{
    public static final String TAG = KeyboardMenuWidget.class.getSimpleName();

    private View mContentView;
    private KeyboardMenuScrollView mScrollView;
    private boolean mCanScroll;
    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;

    /**
     * Top items
     **/
    private ImageButton mTopAccountBtn;
    private ImageButton mTopCloseBtn;

    /**
     * Content items
     **/
    private View mLanguage;
    private View mTheme;
    private View mSettings;
    private View mFloatingKeyboard;
    private View mAdjustSize;
    private View mBackup;
    private View mRestore;
    private View mHideKeyboard;
    private View mDivider;

    /**
     * Content items with switch
     **/
    private View mShowSuggestions;
    private Switch mShowSuggestionsSwitch;
    private View mAutoCorrection;
    private Switch mAutoCorrectionSwitch;
    private View mSlideInputS;
    private Switch mSlideInputSwitch;
    private View mVibrate;
    private Switch mVibrateSwitch;
    private View mSound;
    private Switch mSoundSwitch;
    private View mShowNumberRow;
    private Switch mShowNumberRowSwitch;

    public KeyboardMenuWidget() {
        mCanScroll = false;
    }

    @Override
    public void onCreate(Intent intent) {
        super.onCreate(intent);
    }

    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.keyboard_menu, null);
    }

    @NonNull
    @Override
    protected boolean isEnableHeightMode(Context context) {
        return DisplayUtil.isOrientationPortrait(context);
    }

    @Override
    public void onViewCreated(Intent intent) {
        super.onViewCreated(intent);
        mContentView = getView().findViewById(R.id.keyboard_menu_content);
        mScrollView = getView().findViewById(R.id.keyboard_menu_content_scroll_view);
        mScrollView.setOnScrollChangeListener(this);

        // Background
        KeyboardThemeManager.getInstance().setUiModuleBackground(mContentView);
        int color = 0xff000000;

        // Top items
        mTopAccountBtn = getView().findViewById(R.id.keyboard_menu_top_account_btn);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(mTopAccountBtn, color);
        mTopAccountBtn.setOnClickListener(this);

        mTopCloseBtn = getView().findViewById(R.id.keyboard_menu_top_close_btn);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(mTopCloseBtn, color);
        mTopCloseBtn.setOnClickListener(this);

        KeyboardThemeManager.getInstance().colorUiModuleTitleText(getView().findViewById(R.id.keyboard_menu_tv));

        // Content items
        mLanguage = getView().findViewById(R.id.keyboard_menu_content_item_language);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(getView().findViewById(R.id.keyboard_menu_content_item_language_iv), color);
        KeyboardThemeManager.getInstance().colorUiModuleText(getView().findViewById(R.id.keyboard_menu_content_item_language_tv));
        mLanguage.setOnClickListener(this);

        mTheme = getView().findViewById(R.id.keyboard_menu_content_item_theme);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(getView().findViewById(R.id.keyboard_menu_content_item_theme_iv), color);
        KeyboardThemeManager.getInstance().colorUiModuleText(getView().findViewById(R.id.keyboard_menu_content_item_theme_tv));

        mTheme.setOnClickListener(this);

        mSettings = getView().findViewById(R.id.keyboard_menu_content_item_settings);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(getView().findViewById(R.id.keyboard_menu_content_item_settings_iv), color);
        KeyboardThemeManager.getInstance().colorUiModuleText(getView().findViewById(R.id.keyboard_menu_content_item_settings_tv));
        mSettings.setOnClickListener(this);

        mFloatingKeyboard = getView().findViewById(R.id.keyboard_menu_content_item_floating_keyboard);
        KeyboardThemeManager.getInstance().colorUiModuleText(getView().findViewById(R.id.keyboard_menu_content_item_floating_keyboard_tv));
        ImageView floatingKeyboardIcon = getView().findViewById(R.id.keyboard_menu_content_item_floating_keyboard_iv);
        if (Settings.readFloatingMode(getContext())) {
            floatingKeyboardIcon.setImageResource(R.drawable.ic_keyboard_menu_floating_keyboard_selected);
        } else {
            floatingKeyboardIcon.setImageResource(R.drawable.ic_keyboard_menu_floating_keyboard);
        }
        KeyboardThemeManager.getInstance().colorUiModuleIcon(getView().findViewById(R.id.keyboard_menu_content_item_floating_keyboard_iv), color);
        mFloatingKeyboard.setOnClickListener(this);

        mAdjustSize = getView().findViewById(R.id.keyboard_menu_content_item_adjust_size);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(getView().findViewById(R.id.keyboard_menu_content_item_adjust_size_iv), color);
        KeyboardThemeManager.getInstance().colorUiModuleText(getView().findViewById(R.id.keyboard_menu_content_item_adjust_size_tv));
        mAdjustSize.setOnClickListener(this);
        View adjustSizeDivider = getView().findViewById(R.id.keyboard_menu_content_item_adjust_size_divider);
        if (KeyboardSwitcher.getInstance().isFloatingKeyboard()) {
            mAdjustSize.setVisibility(View.VISIBLE);
            adjustSizeDivider.setVisibility(View.VISIBLE);
        } else {
            mAdjustSize.setVisibility(View.GONE);
            adjustSizeDivider.setVisibility(View.GONE);

        }

        mBackup = getView().findViewById(R.id.keyboard_menu_content_item_backup);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(getView().findViewById(R.id.keyboard_menu_content_item_backup_iv), color);
        KeyboardThemeManager.getInstance().colorUiModuleText(getView().findViewById(R.id.keyboard_menu_content_item_backup_tv));
        mBackup.setOnClickListener(this);
        mBackup.setVisibility(View.GONE);

        mRestore = getView().findViewById(R.id.keyboard_menu_content_item_restore);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(getView().findViewById(R.id.keyboard_menu_content_item_restore_iv), color);
        KeyboardThemeManager.getInstance().colorUiModuleText(getView().findViewById(R.id.keyboard_menu_content_item_restore_tv));
        mRestore.setOnClickListener(this);
        mRestore.setVisibility(View.GONE);

        mHideKeyboard = getView().findViewById(R.id.keyboard_menu_content_item_hide_keyboard);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(getView().findViewById(R.id.keyboard_menu_content_item_hide_keyboard_iv), color);
        KeyboardThemeManager.getInstance().colorUiModuleText(getView().findViewById(R.id.keyboard_menu_content_item_hide_keyboard_tv));
        mHideKeyboard.setOnClickListener(this);

        mShowSuggestions = getView().findViewById(R.id.keyboard_menu_content_item_show_suggestions);
        mShowSuggestionsSwitch = getView().findViewById(R.id.keyboard_menu_content_item_show_suggestions_switch);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(getView().findViewById(R.id.keyboard_menu_content_item_show_suggestions_iv), color);
        KeyboardThemeManager.getInstance().colorUiModuleText(getView().findViewById(R.id.keyboard_menu_content_item_show_suggestions_tv));
        mShowSuggestionsSwitch.setChecked(PrefUtil.getBoolean(getContext(), PREF_SHOW_SUGGESTIONS, true));
        mShowSuggestionsSwitch.setOnCheckedChangeListener(this);

        mAutoCorrection = getView().findViewById(R.id.keyboard_menu_content_item_auto_correction);
        mAutoCorrectionSwitch = getView().findViewById(R.id.keyboard_menu_content_item_auto_correction_switch);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(getView().findViewById(R.id.keyboard_menu_content_item_auto_correction_iv), color);
        KeyboardThemeManager.getInstance().colorUiModuleText(getView().findViewById(R.id.keyboard_menu_content_item_auto_correction_tv));
        mAutoCorrectionSwitch.setChecked(Settings.readAutoCorrectEnabled(PreferenceManager.getDefaultSharedPreferences(getContext()), getResources()));
        mAutoCorrectionSwitch.setOnCheckedChangeListener(this);

        mSlideInputS = getView().findViewById(R.id.keyboard_menu_content_item_slide_input);
        mSlideInputSwitch = getView().findViewById(R.id.keyboard_menu_content_item_slide_input_switch);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(getView().findViewById(R.id.keyboard_menu_content_item_slide_input_iv), color);
        KeyboardThemeManager.getInstance().colorUiModuleText(getView().findViewById(R.id.keyboard_menu_content_item_slide_input_tv));
        mSlideInputSwitch.setChecked(Settings.readGestureInputEnabled(PreferenceManager.getDefaultSharedPreferences(getContext())));
        mSlideInputSwitch.setOnCheckedChangeListener(this);

        mVibrate = getView().findViewById(R.id.keyboard_menu_content_item_vibrate);
        mVibrateSwitch = getView().findViewById(R.id.keyboard_menu_content_item_vibrate_switch);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(getView().findViewById(R.id.keyboard_menu_content_item_vibrate_iv), color);
        KeyboardThemeManager.getInstance().colorUiModuleText(getView().findViewById(R.id.keyboard_menu_content_item_vibrate_tv));
        mVibrateSwitch.setChecked(Settings.readVibrationEnabled(PreferenceManager.getDefaultSharedPreferences(getContext()), getResources()));
        mVibrateSwitch.setOnCheckedChangeListener(this);

        mSound = getView().findViewById(R.id.keyboard_menu_content_item_sound);
        mSoundSwitch = getView().findViewById(R.id.keyboard_menu_content_item_sound_switch);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(getView().findViewById(R.id.keyboard_menu_content_item_sound_iv), color);
        KeyboardThemeManager.getInstance().colorUiModuleText(getView().findViewById(R.id.keyboard_menu_content_item_sound_tv));
        mSoundSwitch.setChecked(Settings.readKeypressSoundEnabled(PreferenceManager.getDefaultSharedPreferences(getContext()), getResources()));
        mSoundSwitch.setOnCheckedChangeListener(this);

        mShowNumberRow = getView().findViewById(R.id.keyboard_menu_content_item_show_number_row);
        mShowNumberRowSwitch = getView().findViewById(R.id.keyboard_menu_content_item_show_number_row_switch);
        KeyboardThemeManager.getInstance().colorUiModuleIcon(getView().findViewById(R.id.keyboard_menu_content_item_show_number_row_iv), color);
        KeyboardThemeManager.getInstance().colorUiModuleText(getView().findViewById(R.id.keyboard_menu_content_item_show_number_row_tv));
        mShowNumberRowSwitch.setChecked(Settings.readAdditionalNumberRowShown(PreferenceManager.getDefaultSharedPreferences(getContext())));
        mShowNumberRowSwitch.setOnCheckedChangeListener(this);

        if (!DisplayUtil.isOrientationPortrait(getContext())) {
            ViewGroup.LayoutParams lp = mScrollView.getLayoutParams();
            // TODO should calculate the correct height of the area in landscape mode
            lp.height = ResourceUtils.getKeyboardHeight(getContext(), KeyboardSwitcher.getInstance().isFloatingKeyboard());
            mScrollView.setLayoutParams(lp);
        }
    }

    @Override
    public boolean isExtendedInFloatingKeyboard() {
        return true;
    }

    @Override
    public void onScrollChange(NestedScrollView view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (scrollY == 0) {
            mCanScroll = false;
        } else {
            mCanScroll = true;
        }
    }

    @Override
    public boolean scrollOnChild() {
        return mCanScroll;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.keyboard_menu_top_account_btn:
                // TODO: open account page
                break;

            case R.id.keyboard_menu_top_close_btn:
                close();
                break;

            case R.id.keyboard_menu_content_item_language:
                KeyboardWidgetManager.getInstance().close(getClass());
//                KeyboardWidgetManager.getInstance().open(SubtypeSwitchWidget.class);
                LatinIME.getInstance().onCustomRequest(Constants.CUSTOM_CODE_SHOW_INPUT_METHOD_PICKER);
                break;

            case R.id.keyboard_menu_content_item_theme:
                intent = new Intent(getContext(), ThemeManageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                break;
            case R.id.keyboard_menu_content_item_adjust_size:
                KeyboardWidgetManager.getInstance().close(getClass());
                boolean isFloatingKeyboard = KeyboardSwitcher.getInstance().isFloatingKeyboard();
                if (isFloatingKeyboard) {
                    KeyboardSwitcher.getInstance().switchFloatingKeyboardResizeMode(true);
                } else {
//                KeyboardWidgetManager.getInstance().open(KeyboardResizeWidget.class);
                }
                break;

            case R.id.keyboard_menu_content_item_backup:
//                BackupManager.instance.backup(getContext(), this);
                break;

            case R.id.keyboard_menu_content_item_restore:
//                BackupManager.instance.restore(getContext(), this);
                break;

            case R.id.keyboard_menu_content_item_hide_keyboard:
                LatinIME.getInstance().hideWindow();
                break;

            case R.id.keyboard_menu_content_item_settings:
                intent = new Intent(getContext(), SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                break;

            case R.id.keyboard_menu_content_item_floating_keyboard:
                KeyboardWidgetManager.getInstance().close(getClass());
                KeyboardSwitcher.getInstance().switchFloatingKeyboard(getContext());
                getView().requestLayout();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.keyboard_menu_content_item_show_suggestions_switch:
                PrefUtil.putBoolean(getContext(), PREF_SHOW_SUGGESTIONS, isChecked);
                KeyboardTrace.onAttributeChangeEvent(OP_SHOW_SUGGESTION
                        , PrefUtil.getBoolean(getContext(), PREF_SHOW_SUGGESTIONS, true));
                break;

            case R.id.keyboard_menu_content_item_auto_correction_switch:
                PrefUtil.putBoolean(getContext(), PREF_AUTO_CORRECTION, isChecked);
                KeyboardTrace.onAttributeChangeEvent(OP_AUTO_CORRECTION
                        , Settings.readAutoCorrectEnabled(PreferenceManager.getDefaultSharedPreferences(getContext()), getResources()));
                break;

            case R.id.keyboard_menu_content_item_slide_input_switch:
                PrefUtil.putBoolean(getContext(), PREF_GESTURE_INPUT, isChecked);
                KeyboardTrace.onAttributeChangeEvent(OP_GESTURE_INPUT
                        , Settings.readGestureInputEnabled(PreferenceManager.getDefaultSharedPreferences(getContext())));
                break;

            case R.id.keyboard_menu_content_item_vibrate_switch:
                PrefUtil.putBoolean(getContext(), PREF_VIBRATE_ON, isChecked);
                break;

            case R.id.keyboard_menu_content_item_sound_switch:
                PrefUtil.putBoolean(getContext(), PREF_SOUND_ON, isChecked);
                break;

            case R.id.keyboard_menu_content_item_show_number_row_switch:
                PrefUtil.putBoolean(getContext(), PREF_ADDITIONAL_NUMBER_ROW_SHOWN, isChecked);
                KeyboardTrace.onAttributeChangeEvent(OP_SHOW_NUMBER_ROW
                        , Settings.readAdditionalNumberRowShown(PreferenceManager.getDefaultSharedPreferences(getContext())));
                KeyboardSwitcher.getInstance().reLoadKeyboard();
                getView().requestLayout();
                break;
        }
        LatinIME.getInstance().loadKeyboard();
    }
//
//    @Override
//    public void onBackupResult() {
//        //TODO: show toast ?
//
//    }
//
//    @Override
//    public void onRestoreResult() {
//        // refresh ui
//        KeyboardSwitcher.getInstance().reLoadKeyboard();
//        getView().requestLayout();
//        KeyboardTrace.reloadLocalActionDbItem();
//    }
}
