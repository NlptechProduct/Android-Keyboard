package com.android.inputmethod;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import androidx.core.content.ContextCompat;
import androidx.multidex.MultiDexApplication;

import com.airbnb.lottie.LottieCompositionFactory;
import com.android.inputmethod.latin.R;
import com.nlptech.Agent;
import com.nlptech.common.utils.DensityUtil;
import com.nlptech.function.keyboardrender.RGBKeyboardRender;
import com.nlptech.keyboardview.theme.external.ExternalThemeInfo;


public class TestApplication extends MultiDexApplication {

    private static TestApplication instance;

    public static TestApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Agent.getInstance().init(this);
        addExternalThemeDefault();
        addExternalThemeRGB();
        Agent.getInstance().loadTheme(this, "001");
    }

    private void addExternalThemeDefault() {

        Drawable keyboardBackgroundDrawable = ContextCompat.getDrawable(this, R.drawable.test_background_lxx_light);
        Drawable morekeysBackgroundDrawable = ContextCompat.getDrawable(this, R.drawable.test_theme_more_keyboard_background);
        Drawable keyPreviewDrwable = ContextCompat.getDrawable(this, R.drawable.test_theme_preview);
        Drawable emojiIcon = ContextCompat.getDrawable(this, R.drawable.test_icon_smiley);
        Drawable deleteIcon = ContextCompat.getDrawable(this, R.drawable.test_icon_delete);
        Drawable shiftIcon = ContextCompat.getDrawable(this, R.drawable.test_icon_shift);
        Drawable shiftLockIcon = ContextCompat.getDrawable(this, R.drawable.test_icon_shift_locked);
        Drawable enterIcon = ContextCompat.getDrawable(this, R.drawable.test_icon_enter);
        Drawable languageIcon = ContextCompat.getDrawable(this, R.drawable.test_icon_language);
        Drawable chineseSuggestionMorePageBackground = new ColorDrawable(Color.parseColor("#3c3c3c"));
        Drawable chineseSuggestionComposingViewBackground = new ColorDrawable(Color.parseColor("#AA000000"));


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

        ExternalThemeInfo.LottieDrawableInfo lottieDrawableInfo = new ExternalThemeInfo.LottieDrawableInfo(() -> LottieCompositionFactory
                .fromAsset(this, "test_lottie_click_effect.json"), 1);

        int dividerW = DensityUtil.dp2px(this, 1);
        int dividerH = DensityUtil.dp2px(this, 20);
        Bitmap bitmap = Bitmap.createBitmap(dividerW, dividerH, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#12f0e5"));
        canvas.drawRect(new Rect(0, 0, dividerW, dividerH), paint);
        Drawable suggestDivider = new BitmapDrawable(getResources(), bitmap);

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
                .setSuggestedAutoCorrectColor(color)
                .setSuggestedTextColor(color)
                .setSuggestedTypedWordColor(color)
                .setSuggestedValidTypedWordColor(color)
                .setSuggestionStripDivider(suggestDivider)
                .setSuggestionStripViewBackground(keyboardBackgroundDrawable)
                .setKeyboardClickedEffectLottieDrawable(lottieDrawableInfo)
                .setThemePreviewImage(ContextCompat.getDrawable(this, R.drawable.test_thumbnail))
                .setChineseSuggestionMorePageBackground(chineseSuggestionMorePageBackground)
                .setChineseSuggestionStripOpenMorePageButton(ContextCompat.getDrawable(this, R.drawable.ic_external_theme_rgb_cn_open_moepage))
                .setChineseSuggestionStripCloseMorePageButton(ContextCompat.getDrawable(this, R.drawable.ic_external_theme_rgb_cn_close_moepage))
                .setChineseSuggestionComposingViewBackground(chineseSuggestionComposingViewBackground)
                .setChineseSuggestionComposingTextColor("#009393")
                .setChineseSuggestionMorePageUpEnableButton(ContextCompat.getDrawable(this, R.drawable.ic_external_theme_rgb_cn_suggest_up_arrow_enable))
                .setChineseSuggestionMorePageUpDisableButton(ContextCompat.getDrawable(this, R.drawable.ic_external_theme_rgb_cn_suggest_up_arrow_disable))
                .setChineseSuggestionMorePageDownEnableButton(ContextCompat.getDrawable(this, R.drawable.ic_external_theme_rgb_cn_suggest_down_arrow_enable))
                .setChineseSuggestionMorePageDownDisableButton(ContextCompat.getDrawable(this, R.drawable.ic_external_theme_rgb_cn_suggest_down_arrow_disable))
                .setChineseSuggestionMorePageDeleteButton(ContextCompat.getDrawable(this, R.drawable.ic_external_theme_rgb_cn_suggest_delete))
                .setChineseSuggestionMorePageResetButton(ContextCompat.getDrawable(this, R.drawable.ic_external_theme_rgb_cn_suggest_retransfusion))
                .build();
        Agent.getInstance().addExternalThemes(this, externalThemeInfo);
    }

    private void addExternalThemeRGB() {
        ExternalThemeInfo.Builder builder = new ExternalThemeInfo.Builder("002", "RGB");
        builder.setThemePreviewImage(ContextCompat.getDrawable(this, R.drawable.img_external_theme_preview_rgb));

        ColorDrawable colorDrawable;
        String color;

        // keyboard
        colorDrawable = new ColorDrawable(Color.BLACK);
        builder.setKeyboardBackground(colorDrawable);
        colorDrawable = new ColorDrawable(Color.TRANSPARENT);
        builder.setKeyBackground(colorDrawable);
        colorDrawable = new ColorDrawable(Color.TRANSPARENT);
        builder.setFunctionKeyBackground(colorDrawable);

        builder.setSpacebarBackground(ContextCompat.getDrawable(this, R.drawable.bg_external_theme_rgb_spacebar));

        colorDrawable = new ColorDrawable(Color.parseColor("#1c2935"));
        builder.setKeyPreviewBackground(colorDrawable);
        color = String.format("#%06X", 0xFFFFFF & Color.TRANSPARENT);
        builder.setEmojiCategoryPageIndicatorBackgroundColor(color);

        // more keys keyboard
        colorDrawable = new ColorDrawable(Color.parseColor("#000000"));
        builder.setMoreKeysKeyboardBackground(colorDrawable);
        builder.setMoreKeysKeyBackground(ContextCompat.getDrawable(this, R.drawable.bg_external_theme_rgb_more_key));

        // Chinese Suggest MorePage
        colorDrawable = new ColorDrawable(Color.parseColor("#3c3c3c"));
        builder.setChineseSuggestionMorePageBackground(colorDrawable);
        builder.setChineseSuggestionStripOpenMorePageButton(ContextCompat.getDrawable(this, R.drawable.ic_external_theme_rgb_cn_open_moepage));
        builder.setChineseSuggestionStripCloseMorePageButton(ContextCompat.getDrawable(this, R.drawable.ic_external_theme_rgb_cn_close_moepage));
        colorDrawable = new ColorDrawable(Color.parseColor("#AA000000"));
        builder.setChineseSuggestionComposingViewBackground(colorDrawable);
        builder.setChineseSuggestionComposingTextColor("#ff5151");
        builder.setChineseSuggestionMorePageUpEnableButton(ContextCompat.getDrawable(this, R.drawable.ic_external_theme_rgb_cn_suggest_up_arrow_enable));
        builder.setChineseSuggestionMorePageUpDisableButton(ContextCompat.getDrawable(this, R.drawable.ic_external_theme_rgb_cn_suggest_up_arrow_disable));
        builder.setChineseSuggestionMorePageDownEnableButton(ContextCompat.getDrawable(this, R.drawable.ic_external_theme_rgb_cn_suggest_down_arrow_enable));
        builder.setChineseSuggestionMorePageDownDisableButton(ContextCompat.getDrawable(this, R.drawable.ic_external_theme_rgb_cn_suggest_down_arrow_disable));
        builder.setChineseSuggestionMorePageDeleteButton(ContextCompat.getDrawable(this, R.drawable.ic_external_theme_rgb_cn_suggest_delete));
        builder.setChineseSuggestionMorePageResetButton(ContextCompat.getDrawable(this, R.drawable.ic_external_theme_rgb_cn_suggest_retransfusion));

        // color
        color = String.format("#%06X", 0xFFFFFF & Color.WHITE);
        builder.setKeyTextColor(color);
        builder.setFunctionKeyTextColor(color);
        builder.setLanguageOnSpacebarTextColor(color);
        builder.setKeyHintLabelColor(color);
        builder.setKeyHintLetterColor(color);
        builder.setKeyTextInactivatedColor(color);
        builder.setKeyShiftedLetterHintActivatedColor(color);
        builder.setKeyShiftedLetterHintInactivatedColor(color);
        builder.setKeyPreviewTextColor(color);
        builder.setKeyBorderColor(color);
        builder.setCreateKeyboardRenderCallback(RGBKeyboardRender::new);

        ColorDrawable suggestColor = new ColorDrawable(Color.parseColor("#000000"));
        builder.setSuggestedTextColor("#ffffff");
        builder.setSuggestedAutoCorrectColor("#ffffff");
        builder.setSuggestionStripViewBackground(suggestColor);


        // add theme
        ExternalThemeInfo info = builder.build();
        Agent.getInstance().addExternalThemes(this, info);
    }

}
