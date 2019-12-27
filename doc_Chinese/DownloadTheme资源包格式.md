
# DownloadTheme资源包格式

<br/>

## 目录
* [结构](#1)
* [打包](#2)
* [Data](#3)
* [Icon](#4)
* [Keyboard](#5)
* [Lottie Json](#6)

<br/>

<h2 id="1">结构</h2>

<br/>

资源包的档案夹
|- data
|- icon 
|- keyboard
|- lottie_json

- data为json档，里面记录了一些关于主题的讯息。

- 在json的参数中：

型别未定义 | 值
-----|:-----
颜色未定义|"unknown"
文字尺寸未定义|-1.0f
Integer未定义|-2147483648
Float未定义|1.4e-45

- icon、keyboard和lottie_json为档案夹，用来放置键盘的各种图档资源。

- 如果图档为.9图，必须使用aapt工具编译过(aapt s -i 图档)。

<br/>

<h2 id="2">打包</h2>

<br/>

<br/>

<h2 id="3">Data</h2>

<br/>

json内容：

~~~

{
  "autoCorrectColor": "#333333", // 候选词纠错时的颜色
  "chineseSuggestionComposingTextColor": "#000000",
  "emojiCategoryIconBackgroundColor": "#BBC2C9",
  "emojiCategoryPageIndicatorBackgroundColor": "#999999",
  "emojiCategoryPageIndicatorForegroundColor": "#666666",
  "emojiFunctionContainerBackgroundColor": "unknown",
  "emojiSeparatorColor": "unknown",
  "functionKeyTextColor": "#000000", // 功能键的文字颜色
  "gestureTrailBodyRatio": -2147483648,
  "gestureTrailColor": "unknown", // 滑行轨迹的颜色
  "gestureTrailEndWidth": 1.4e-45,
  "gestureTrailFadeoutDuration": -2147483648,
  "gestureTrailFadeoutStartDelay": -2147483648,
  "gestureTrailStartWidth": 1.4e-45,
  "gestureTrailUpdateInterval": -2147483648,
  "keyDeformableTextColor": "#3070A0", // 变形键的文字颜色
  "keyHintLabelColor": "#000000", // 键右上方的小字的颜色
  "keyHintLetterColor": "#000000", // 键右上方的小字的颜色
  "keyLabelSizeRatio": -1,
  "keyLetterSizeRatio": -1,
  "keyPreviewTextColor": "#000000", // 键的preivew的文字颜色
  "keyPreviewTextRatio": -1,
  "keyShiftedLetterHintActivatedColor": "#000000",
  "keyShiftedLetterHintInactivatedColor": "#000000",
  "keyTextColor": "#000000", // 键的文字颜色
  "keyTextInactivatedColor": "#000000",
  "languageOnSpacebarTextColor": "#000000",  // 空白键的文字颜色
  "suggestedColor": "#333333", // 候选词的文字颜色
  "typedWordColor": "#000000", // 用户输入的词的文字颜色
  "uiBottomLineColor": "unknown",
  "uiContentTextColor": "unknown",
  "uiFunctionEntryIconColor": "#000000", // 工具栏icon颜色
  "uiIconColor": "unknown",
  "uiNegativeTextColor": "unknown",
  "uiPositiveTextColor": "unknown",
  "uiTextColor": "unknown",
  "uiTitleTextColor": "unknown",
  "validTypedWordColor": "#000000", // 用户输入的词的文字颜色
  "keyboardBackgroundColor": "#D6D9DF", // 键盘背景色，当找不到keyboard_background时，会渲染此色为背景
  "chineseSuggestionMorePageBackgroundColor": "#FFFFFF" // 中文的候选词下拉选单的背景色
}

~~~

<br/>

<h2 id="4">Icon</h2>

<br/>

档名，以下副档名必须是.png或.9.png：
- ic_shift
	- ic_shifted
	- ic_shifted_lock
	- ic_delete
	- ic_settings
	- ic_space
	- ic_enter
	- ic_go
	- ic_search
	- ic_send
	- ic_next
	- ic_done
	- ic_previous
	- ic_tab
	- ic_shortcut
	- ic_space_for_number_layout
	- ic_language_switch
	- ic_zwnj
	- ic_zwjK
	- ic_emoji
	- ic_keyboard_menu

<br/>

<h2 id="5">Keyboard</h2>

<br/>

档名： 
- typeface.ttf

档名，以下副档名必须是.png或.9.png：
- functional_key_normal 
- functional_key_pressed
- functional_key_normal_with_border
- functional_key_pressed_with_border

- functional_key_shift_normal
- functional_key_shift_pressed
- functional_key_shift_normal_with_border
- functional_key_shift_pressed_with_border

- functional_key_go_to_number_normal
- functional_key_go_to_number_pressed
- functional_key_go_to_number_normal_with_border
- functional_key_go_to_number_pressed_with_border

- functional_key_delete_normal
- functional_key_delete_pressed
- functional_key_delete_normal_with_border
- functional_key_delete_pressed_with_border

- functional_key_enter_normal
- functional_key_enter_pressed
- functional_key_enter_normal_with_border
- functional_key_enter_pressed_with_border

- key_normal
- key_pressed
- key_normal_with_border
- key_pressed_with_border

- key_preview_more
- key_preview

- keyboard_background

- more_keys_keyboard_background
- more_keys_keyboard_key_pressed
- more_keys_keyboard_key_pressed_with_border


- spacebar_normal
- spacebar_pressed
- spacebar_normal_with_border
- spacebar_pressed_with_border

- theme_preview
- theme_preview_with_border

- ui_background
- ui_contentBackground
- ui_title_background
- ui_button_background

- gesture_trail_drawable

- suggest_strip_background
- suggest_strip_divider
- suggest_word_background_selected

- chinese_suggestion_strip_open_more_page_button
- chinese_suggestion_strip_close_more_page_button
- chinese_suggestion_more_page_up_enable_button
- chinese_suggestion_more_page_up_disable_button
- chinese_suggestion_more_page_down_enable_button
- chinese_suggestion_more_page_down_disable_button
- chinese_suggestion_more_page_delete_button
- chinese_suggestion_more_page_reset_button
- chinese_suggestion_more_page_background
- chinese_suggestion_composing_view_background

- keyboard_clicked_effect

<br/>

<h2 id="6">Lottie Json</h2>

<br/>

档名，以下副档名必须为.json：
- functional_key_normal
- functional_key_pressed
- functional_key_normal_with_border
- functional_key_pressed_with_border
- key_normal
- key_pressed
- key_normal_with_border
- key_pressed_with_border
- more_keys_keyboard_key_normal
- more_keys_keyboard_key_pressed
- more_keys_keyboard_key_normal_with_border
- more_keys_keyboard_key_pressed_with_border
- spacebar_normal
- spacebar_pressed
- spacebar_normal_with_border
- spacebar_pressed_with_border

- keyboard_background
- more_keys_keyboard_background
- key_preview
- keyboard_clicked_effect

<br/>

