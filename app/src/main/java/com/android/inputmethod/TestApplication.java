package com.android.inputmethod;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import androidx.core.content.ContextCompat;
import androidx.multidex.MultiDexApplication;

import com.android.inputmethod.latin.R;
import com.nlptech.Agent;
import com.nlptech.keyboardview.theme.external.ExternalThemeInfo;


public class TestApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Agent.getInstance().init(this);
        addExternalTheme();
        Agent.getInstance().loadTheme(this,"001");
    }

    private void addExternalTheme() {

        Drawable keyboardBackgroundDrawable = ContextCompat.getDrawable(this, R.drawable.test_background_lxx_light);
        Drawable morekeysBackgroundDrawable = ContextCompat.getDrawable(this, R.drawable.test_theme_more_keyboard_background);
        Drawable keyPreviewDrwable = ContextCompat.getDrawable(this, R.drawable.test_theme_preview);
        Drawable emojiIcon = ContextCompat.getDrawable(this, R.drawable.test_icon_smiley);
        Drawable deleteIcon = ContextCompat.getDrawable(this, R.drawable.test_icon_delete);
        Drawable shiftIcon = ContextCompat.getDrawable(this, R.drawable.test_icon_shift);
        Drawable shiftLockIcon = ContextCompat.getDrawable(this, R.drawable.test_icon_shift_locked);
        Drawable enterIcon = ContextCompat.getDrawable(this, R.drawable.test_icon_enter);
        Drawable languageIcon = ContextCompat.getDrawable(this, R.drawable.test_icon_language);

        StateListDrawable keyBackgroundDrawable = new StateListDrawable();
        keyBackgroundDrawable.addState(new int[]{android.R.attr.state_pressed}, ContextCompat.getDrawable(this, R.drawable.test_theme_key_press));
        keyBackgroundDrawable.addState(new int[]{}, ContextCompat.getDrawable(this, R.drawable.test_theme_key_normal));

        StateListDrawable functionKeyBackgroundDrawable = new StateListDrawable();
        functionKeyBackgroundDrawable.addState(new int[]{android.R.attr.state_pressed}, ContextCompat.getDrawable(this, R.drawable.test_theme_key_press));
        functionKeyBackgroundDrawable.addState(new int[]{}, ContextCompat.getDrawable(this, R.drawable.test_theme_function_key_normal));

        StateListDrawable morekeyDrawable = new StateListDrawable();
        morekeyDrawable.addState(new int[]{android.R.attr.state_pressed}, ContextCompat.getDrawable(this, R.drawable.test_theme_more_key_press));
        morekeyDrawable.addState(new int[]{}, ContextCompat.getDrawable(this, R.drawable.test_theme_key_normal));

        StateListDrawable spacebarBackgroundDrawable = new StateListDrawable();
        spacebarBackgroundDrawable.addState(new int[]{android.R.attr.state_pressed}, ContextCompat.getDrawable(this, R.drawable.test_theme_space_key_press));
        spacebarBackgroundDrawable.addState(new int[]{}, ContextCompat.getDrawable(this, R.drawable.test_theme_space_key_normal));

        String color = String.format("#%06X", 0xFFFFFF & 0x12f0e5);
        String emojiColor = String.format("#%06X", 0xFFFFFF & 0x048f89);
        ExternalThemeInfo externalThemeInfo = new ExternalThemeInfo.Builder("001", "Test Theme")
                .setKeyboardBackground(keyboardBackgroundDrawable)
                .setKeyBackground(keyBackgroundDrawable)
                .setFunctionKeyBackground(functionKeyBackgroundDrawable)
                .setSpacebarBackground(spacebarBackgroundDrawable)
                .setKeyPreviewBackground(keyPreviewDrwable)
                .setKeyLetterSizeRatio(0.4f)
                .setKeyTextColor(color)
                .setKeyHintLetterColor(color)
                .setFunctionKeyTextColor(color)
                .setMoreKeysKeyboardBackground(morekeysBackgroundDrawable)
                .setMoreKeysKeyBackground(morekeyDrawable)
                .setKeyPreviewTextColor(color)
                .setGestureTrailColor(color)
                .setEmojiNormalKeyIcon(emojiIcon)
                .setEmojiActionKeyIcon(emojiIcon)
                .setDeleteKeyIcon(deleteIcon)
                .setShiftKeyIcon(shiftIcon)
                .setShiftKeyShiftedIcon(shiftLockIcon)
                .setEnterKeyIcon(enterIcon)
                .setLanguageSwitchKeyIcon(languageIcon)
                .setEmojiCategoryPageIndicatorBackgroundColor(emojiColor)
                .setLanguageOnSpacebarTextColor(color)
                .build();
        Agent.getInstance().addExternalThemes(this, externalThemeInfo);
    }

}
