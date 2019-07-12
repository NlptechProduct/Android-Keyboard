# SDK元件介绍

## Agent类

### 初始化
---

#### init
```Java
public void init (Context context)
```
应用启动时呼叫，使得SDK可以获得应用Context资讯。
参数 | 参数说明
-----|:--------
context | 应用启动时呼叫 使得SDK可以获得应用Context资讯

### 生命周期调用
---

#### onCreate
```Java
public void onCreate (InputMethodService service, InputLogic mInputLogic, LanguageCallback languageCallback)
```
呼应InputMethodService生命周期 ; 带入应用端相关实体。
参数 | 参数说明
-----|:--------
service | 应用的IMS实体
mInputLogic | 应用集成Zengine SDK的InputLoic实体
languageCallback | 应用集成Zengine SDK需实现LanguageCallback

#### onCreateInputView
```Java
public void onCreateInputView (ViewGroup kbContainer, boolean isHardwareAcceleratedDrawingEnabled)
```
呼应InputMethodService生命周期。
参数 | 参数说明
-----|:--------
kbContainer | 放置KeyboardView的容器
isHardwareAcceleratedDrawingEnabled | 是否开启硬体加速

#### onStartInput
```Java
public void onStartInput (final EditorInfo editorInfo, final boolean restarting)
```
呼应InputMethodService生命周期。
参数 | 参数说明
-----|:--------
editorInfo | InputMethodService生命周期参数
restarting | InputMethodService生命周期参数

#### onStartInputView
```Java
public void onStartInputView (final EditorInfo editorInfo, final boolean restarting)
```
呼应InputMethodService生命周期。
参数 | 参数说明
-----|:--------
editorInfo | InputMethodService生命周期参数
restarting | InputMethodService生命周期参数

#### onFinishInputView
```Java
public void onFinishInputView (final boolean finishingInput)
```
呼应InputMethodService生命周期。
参数 | 参数说明
-----|:--------
finishingInput | InputMethodService生命周期参数

#### onUpdateSelection
```Java
public void onUpdateSelection (final int newSelStart, final int newSelEnd)
```
呼应InputMethodService生命周期。
参数 | 参数说明
-----|:--------
newSelStart | InputMethodService生命周期参数
newSelEnd | InputMethodService生命周期参数

#### onFinishInput
```Java
public void onFinishInput ()
```
呼应InputMethodService生命周期。

#### onWindowShown
```Java
public void onWindowShown ()
```
呼应InputMethodService生命周期。

#### onWindowHidden
```Java
public void onWindowHidden ()
```
呼应InputMethodService生命周期。

#### onDestroy
```Java
public void onDestroy ()
```
呼应InputMethodService生命周期。

### 语言管理
---

#### getAvailableIMELanguageList
```Java
public List<IMELanguage> getAvailableIMELanguageList ()
```
获取Zengine语言支持列表。
返回 | 返回说明 | 
-----|:--------
List<IMELanguage> | Zengine语言支持列表

#### getAddedIMELanguageList
```Java
public List<IMELanguage> getAddedIMELanguageList ()
```
获取已添加语言列表。
返回 | 返回说明 | 
-----|:--------
List<IMELanguage> | 已添加语言列表

#### onIMELanguageChanged
```Java
public void onIMELanguageChanged (IMELanguage language)
```
切换语言。
参数 | 参数说明
-----|:--------
language | 需切换的语言
 
#### addIMELanguage
```Java
public void addIMELanguage (IMELanguage language)
```
添加语言。
参数 | 参数说明
-----|:--------
language | 需添加的语言
 
#### removeIMELanguage
```Java
public void removeIMELanguage (IMELanguage language)
```
移除语言。
参数 | 参数说明
-----|:--------
language | 需移除的语言
 
#### convertToInputMethodSubtype
```Java
public InputMethodSubtype convertToInputMethodSubtype (IMELanguage language)
```
将IMELanguage转换为InputMethodSubtype。
参数 | 参数说明
-----|:--------
language | 需转换成InputMethodSubtype的IMELanguage

返回 | 返回说明 | 
-----|:--------
InputMethodSubtype | 转换结果

#### convertToIMELanguage
```Java
public IMELanguage convertToIMELanguage (InputMethodSubtype inputMethodSubtype)
```
将InputMethodSubtype转换为SubtypeIME。
参数 | 参数说明
-----|:--------
inputMethodSubtype | 需转换成IMELanguage的InputMethodSubtype

返回 | 返回说明 | 
-----|:--------
IMELanguage | 转换结果

#### obtainFuelGetter
```Java
public DictionaryFacilitator.FuelGetter obtainDictionaryGetter ()
```
获取词典加载列表。
返回 | 返回说明 | 
-----|:--------
DictionaryFacilitator.FuelGetter |  | 

### 词典管理
---

#### downloadDictionary
```Java
public void downloadDictionary ()
```
下载已添加语言的词典。

#### queryLocaleDownloading
```Java
public boolean queryLocaleDownloading (String locale)
```
查询某语言词典是否在下载状态中。
参数 | 参数说明
-----|:--------
locale | 需查询词典下载状态的语言locale

返回 | 返回说明 | 
-----|:--------
boolean | 查询结果

#### getCurrentDictionaryList
```Java
public List<DictionaryItem> getCurrentDictionaryList ()
```
查询当前可加载词典列表。
返回 | 返回说明 | 
-----|:--------
boolean | 查询结果


#### enableMobileDictionaryDownload
```Java
public void enableMobileDictionaryDownload (boolean enable)
```
非wifi网络状态词典下载控制开关。
参数 | 参数说明
-----|:--------
enable | 是否打开非wifi网络下的词典下载，false为关闭下载，true为打开下载

#### registerDictionaryDownloadListener
```Java
public void registerDictionaryDownloadListener (DictionaryListener listener)
```
注册监听下载是否完成的接口。
参数 | 参数说明
-----|:--------
listener | 下载状态监听的接口

#### unregisterDictionaryDownloadListener
```Java
public void unregisterDictionaryDownloadListener ()
```
注销下载状态监听接口。

### Theme设定
---

#### addExternalThemes
```Java
public void addExternalThemes (Context context, ExternalThemeInfo infos)
```
新增主题。
参数 | 参数说明
-----|:--------
context | 应用的 ApplicationContext
info | 主题资讯，通过ExternalThemeInfo.Builder建置

#### deleteExternalThemes
```Java
public void deleteExternalThemes (Context context, ExternalThemeInfo infos)
```
删除主题。
参数 | 参数说明
-----|:--------
context | 应用的 ApplicationContext
info | 要被删除的主题的资讯

#### getExternalThemes
```Java
public ArrayList<ExternalThemeInfo> getExternalThemes (Context context)
```
取得所有主题资讯。
参数 | 参数说明
-----|:--------
context | 应用的 ApplicationContext

返回 | 返回说明 | 
-----|:--------
boolean | 所有主题资讯

#### loadTheme
```Java
public void loadTheme (Context context, String externalId)
```
应用主题。
参数 | 参数说明
-----|:--------
context | 应用的 ApplicationContext
externalId | 创建theme的id

### 其他回调设定
---

#### setKeyboardActionCallback
```Java
public void setKeyboardActionCallback (IKeyboardActionCallback keyboardActionCallback)
```
观察KeyboardAction相关行为回调。
参数 | 参数说明
-----|:--------
keyboardActionCallback | IKeyboardActionCallback实现

#### setKeyboardSpecificEventCallback
```Java
public void setKeyboardSpecificEventCallback (IKeyboardSpecificEventCallback keyboardSpecificEventCallback)
```
观察Keyboard特殊行为事件相关回调。
参数 | 参数说明
-----|:--------
keyboardSpecificEventCallback | IKeyboardSpecificEventCallback实现

## 回调类

### LanguageCallback
用途 : 语言切换

---

#### onIMELanguageChanged
```Java
public void onIMELanguageChanged (InputMethodSubtype subtype)
```
语言切换的回调。
参数 | 参数说明
-----|:--------
subtype | 需切换的语言

### IKeyboardActionCallback
用途 : 键盘事件回调

---

#### onDisplayEmojiKeyboard
```Java
public boolean onDisplayEmojiKeyboard ()
```
hook切换emoji键盘事件。
返回 | 返回说明 | 
-----|:--------
boolean | 是否要屏蔽原有事件

#### onKeyboardTypeChange
```Java
public void onKeyboardTypeChange (int keyboardType)
```
字符键盘、emoji键盘和符号键盘切换的回调。
参数 | 参数说明
-----|:--------
keyboardType | 键盘的种类 <br/> IKeyboardActionCallback.ALPHA_KEYBOARD为字符键盘类 <br/>  IKeyboardActionCallback.EMOJI_KEYBOARDemoji为键盘类 <br/> IKeyboardActionCallback.SYMBOL_KEYBOARD为符号键盘

### KeyboardSwitcherListener
 用途：用于键盘切换回调

---

#### stopShowingInputView
```Java
public void stopShowingInputView ()
```
串接AOSP的stopShowingInputView()。

#### startShowingInputView
```Java
public void startShowingInputView ()
```
串接AOSP的startShowingInputView()。

#### shouldShowLanguageSwitchKey
```Java
public boolean shouldShowLanguageSwitchKey ()
```
串接AOSP的shouldShowLanguageSwitchKey()。
返回 | 返回说明 | 
-----|:--------
boolean | 是否显示语言切换按钮

### ImeUiHandlerInterface
用途：用于與AOSP LatinIME.UIHhandler交互

---

#### showGesturePreviewAndSuggestionStrip
```Java
public void showGesturePreviewAndSuggestionStrip (SuggestedWords suggestedWordsToShowSuggestions, boolean isTailBatchInput)
```
与AOSP LatinIME.UIHhandler内相同名称方法串接。
参数 | 参数说明
-----|:--------
suggestedWordsToShowSuggestions | 与AOSP LatinIME.UIHhandler内相同名称方法内, 参数相同
isTailBatchInput | 与AOSP LatinIME.UIHhandler内相同名称方法内, 参数相同

#### showTailBatchInputResult
```Java
public void showTailBatchInputResult (SuggestedWords suggestedWordsToShowSuggestions)
```
与AOSP LatinIME.UIHhandler内相同名称方法串接。
参数 | 参数说明
-----|:--------
suggestedWordsToShowSuggestions | 与AOSP LatinIME.UIHhandler内相同名称方法内, 参数相同

#### postUpdateSuggestionStrip
```Java
public void postUpdateSuggestionStrip (int inputStyleTyping)
```
与AOSP LatinIME.UIHhandler内相同名称方法串接。
参数 | 参数说明
-----|:--------
inputStyleTyping | 与AOSP LatinIME.UIHhandler内相同名称方法内, 参数相同

#### postResumeSuggestions
```Java
public void postResumeSuggestions (boolean value)
```
与AOSP LatinIME.UIHhandler内相同名称方法串接。
参数 | 参数说明
-----|:--------
value | 与AOSP LatinIME.UIHhandler内相同名称方法内, 参数相同

#### hasPendingUpdateSuggestions
```Java
public boolean hasPendingUpdateSuggestions ()
```
与AOSP LatinIME.UIHhandler内相同名称方法串接。
返回 | 返回说明 | 
-----|:--------
boolean | 与AOSP LatinIME.UIHhandler内相同名称方法内, 返回相同

#### cancelUpdateSuggestionStrip
```Java
public boolean cancelUpdateSuggestionStrip ()
```
与AOSP LatinIME.UIHhandler内相同名称方法串接。

#### postResetCaches
```Java
public void postResetCaches (boolean tryResumeSuggestions, int remainingTries)
```
与AOSP LatinIME.UIHhandler内相同名称方法串接。
参数 | 参数说明
-----|:--------
tryResumeSuggestions | 与AOSP LatinIME.UIHhandler内相同名称方法内, 参数相同
remainingTries | 与AOSP LatinIME.UIHhandler内相同名称方法内, 参数相同

#### onStartInputView
```Java
public void onStartInputView (EditorInfo editorInfo, boolean restarting)
```
与AOSP LatinIME.UIHhandler内相同名称方法串接。
参数 | 参数说明
-----|:--------
editorInfo | 与AOSP LatinIME.UIHhandler内相同名称方法内, 参数相同
restarting | 与AOSP LatinIME.UIHhandler内相同名称方法内, 参数相同

#### onFinishInputView
```Java
public void onFinishInputView (boolean restarting)
```
与AOSP LatinIME.UIHhandler内相同名称方法串接。
参数 | 参数说明
-----|:--------
restarting | 与AOSP LatinIME.UIHhandler内相同名称方法内, 参数相同

#### showSuggestionStrip
```Java
public void showSuggestionStrip (SuggestedWords suggestedWords)
```
与AOSP LatinIME.UIHhandler内相同名称方法串接。
参数 | 参数说明
-----|:--------
suggestedWords | 与AOSP LatinIME.UIHhandler内相同名称方法内, 参数相同

### ImsInterface
用途：用于與AOSP LatinIME部分功能交互

---

#### getIME
```Java
public InputMethodService getIME ()
```
返回应用方的InputMethodService实例。
返回 | 返回说明 | 
-----|:--------
InputMethodService | InputMethodService实例

#### updateStateAfterInputTransaction
```Java
public void updateStateAfterInputTransaction (InputTransaction completeInputTransaction)
```
与AOSP LatinIME内相同名称方法串接 ; 需改成public。
参数 | 参数说明
-----|:--------
completeInputTransaction | 与AOSP LatinIME.UIHhandler内相同名称方法内, 参数相同

#### switchToNextSubtype
```Java
public void switchToNextSubtype ()
```
与AOSP LatinIME内相同名称方法串接。

#### getCoordinatesForCurrentKeyboard
```Java
public int[] getCoordinatesForCurrentKeyboard (int[] codePoints)
```
与AOSP LatinIME内相同名称方法串接。
参数 | 参数说明
-----|:--------
codePoints | 与AOSP LatinIME.UIHhandler内相同名称方法内, 参数相同

返回 | 返回说明 | 
-----|:--------
int[] | 与AOSP LatinIME.UIHhandler内相同名称方法内, 返回相同

#### displaySettingsDialog
```Java
public void displaySettingsDialog ()
```
与AOSP LatinIME内相同名称方法串接。

#### setNeutralSuggestionStrip
```Java
public void setNeutralSuggestionStrip ()
```
与AOSP LatinIME内相同名称方法串接 ; 处理Suggestion展示相关操作。

#### setNeutralSuggestionStrip
```Java
public void showSuggestionStrip (SuggestedWords suggestedWords)
```
与AOSP LatinIME内相同名称方法串接 ; 处理Suggestion展示相关操作。
参数 | 参数说明
-----|:--------
suggestedWords | 与AOSP LatinIME.UIHhandler内相同名称方法内, 参数相同

### IUserInputCallback
用途：用于用户输入回调

---

#### onUserTyping
```Java
public void onUserTyping (String wordComposing)
```
返回用户正在输入的词。
参数 | 参数说明
-----|:--------
wordcomposing | 当前正在输入的词

#### onUserTyped
```Java
public void onUserTyped (String text)
```
返回用户上屏的词。
参数 | 参数说明
-----|:--------
wordcomposing | 用户上屏的词

#### onTextChanged
```Java
public void onTextChanged ()
```
用户输入内容发生改变的回调。

## Builder类

### ExternalThemeInfo
用途 : 新增主题时需要的数据结构

---

#### Builder
```Java
public Builder (String externalId, String themeName)
```
ExternalThemeInfo的建构器。
参数 | 参数说明
-----|:--------
externalId | 用戶自行給予主题的 unique identification，不能為空
themeName | 主题的名称，不能為空

#### setThemePreviewImage
```Java
public Builder setThemePreviewImage (Drawable themePreviewImage)
```
设置主题的预载图。
参数 | 参数说明
-----|:--------
themePreviewImage | 主题的预载图

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setFunctionKeyBackground
```Java
public Builder setFunctionKeyBackground (Drawable functionKeyBackground)
```
设置功能键的背景。
参数 | 参数说明
-----|:--------
functionKeyBackground | 功能键的背景

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setSpacebarBackground
```Java
public Builder setSpacebarBackground (Drawable spacebarBackground)
```
设置空白键的背景。
参数 | 参数说明
-----|:--------
spacebarBackground | 空白键的背景

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setKeyPreviewBackground
```Java
public Builder setKeyPreviewBackground (Drawable keyPreviewBackground)
```
设置键的preview popup的背景。
参数 | 参数说明
-----|:--------
keyPreviewBackground | 键的preview popup的背景

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setMoreKeysKeyboardBackground
```Java
public Builder setMoreKeysKeyboardBackground (Drawable moreKeysKeyboardBackground)
```
设置长按一般键而跳出的多键键盘的背景。
参数 | 参数说明
-----|:--------
moreKeysKeyboardBackground | 长按一般键而跳出的多键键盘的背景

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setTypeface
```Java
public Builder setTypeface (Typeface typeface)
```
设置键盘的字体。
参数 | 参数说明
-----|:--------
typeface | 键盘的字体

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setKeyTextColor
```Java
public Builder setKeyTextColor (@ColorInt int keyTextColor)
```
设置键的文字颜色。
参数 | 参数说明
-----|:--------
keyTextColor | 键的文字颜色

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setKeyTextInactivatedColor
```Java
public Builder setKeyTextInactivatedColor (@ColorInt int keyTextInactivatedColor)
```
设置非激活键的文字颜色。
参数 | 参数说明
-----|:--------
keyTextInactivatedColor | 非激活键的文字颜色

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setFunctionKeyTextColor
```Java
public Builder setFunctionKeyTextColor (@ColorInt int functionKeyTextColor)
```
设置功能键的文字颜色。
参数 | 参数说明
-----|:--------
functionKeyTextColor | 功能键的文字颜色

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setKeyHintLetterColor
```Java
public Builder setKeyHintLetterColor (@ColorInt int keyHintLetterColor)
```
设置键上Hint Letter的颜色。
参数 | 参数说明
-----|:--------
keyHintLetterColor | 键上Hint Letter的颜色

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setKeyShiftedLetterHintActivatedColor
```Java
public Builder setSpacebarBackground (@ColorInt int keyShiftedLetterHintActivatedColor)
```
设置Shifted Letter Hint激活的颜色。
参数 | 参数说明
-----|:--------
keyShiftedLetterHintActivatedColor | Shifted Letter Hint激活的颜色

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setKeyShiftedLetterHintActivatedColor
```Java
public Builder setKeyShiftedLetterHintActivatedColor (@ColorInt int keyShiftedLetterHintActivatedColor)
```
设置Shifted Letter Hint非激活的颜色。
参数 | 参数说明
-----|:--------
keyShiftedLetterHintActivatedColor | Shifted Letter Hint非激活的颜色

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setKeyPreviewTextColor
```Java
public Builder setKeyPreviewTextColor (@ColorInt int keyPreviewTextColor)
```
设置键的preview popup的文字颜色。
参数 | 参数说明
-----|:--------
keyPreviewTextColor | 键的preview popup的文字颜色

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setKeyBorderColor
```Java
public Builder setKeyBorderColor (@ColorInt int keyBorderColor)
```
设置按键Border的颜色。
参数 | 参数说明
-----|:--------
keyBorderColor | 按键Border的颜色

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setLanguageOnSpacebarTextColor
```Java
public Builder setLanguageOnSpacebarTextColor (@ColorInt int languageOnSpacebarTextColor)
```
设置空白键上的语言文字颜色。
参数 | 参数说明
-----|:--------
languageOnSpacebarTextColor | 空白键上的语言文字颜色

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setGestureTrailColor
```Java
public Builder setGestureTrailColor (@ColorInt int gestureTrailColor)
```
设置滑行轨迹的颜色。
参数 | 参数说明
-----|:--------
gestureTrailColor | 滑行轨迹的颜色

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setEmojiCategoryPageIndicatorBackground
```Java
public Builder setEmojiCategoryPageIndicatorBackground (@ColorInt int emojiCategoryPageIndicatorBackgroundColor)
```
设置Emoji页面下方的分类TabLayout的背景颜色。
参数 | 参数说明
-----|:--------
emojiCategoryPageIndicatorBackgroundColor | Emoji页面下方的分类TabLayout的背景颜色

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setKeyLetterRatio
```Java
public Builder setKeyLetterRatio (@FloatRange(from = 0.0, to = 1.0) float keyLetterRatio)
```
设置Letter的文字倍率。
参数 | 参数说明
-----|:--------
keyLetterRatio | Letter的文字倍率，文字大小=倍率*键高

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setKeyLabelRatio
```Java
public Builder setKeyLabelRatio (@FloatRange(from = 0.0, to = 1.0) float keyLabelRatio)
```
设置Label的文字倍率。
参数 | 参数说明
-----|:--------
keyLabelRatio | Label的文字倍率，文字大小=倍率*键高

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setKeyPreviewTextRatio
```Java
public Builder setKeyPreviewTextRatio (@FloatRange(from = 0.0, to = 1.0) float keyPreviewTextRatio)
```
设置键的preview popup的文字倍率。
参数 | 参数说明
-----|:--------
keyPreviewTextRatio | preview popup的文字倍率，文字大小=倍率*键高

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setShiftKeyIcon
```Java
public Builder setShiftKeyIcon (Drawable shiftKeyIcon)
```
设置shift键的icon。
参数 | 参数说明
-----|:--------
shiftKeyIcon | shift键的icon

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setShiftKeyShiftedIcon
```Java
public Builder setShiftKeyShiftedIcon (Drawable shiftKeyShiftedIcon)
```
设置shift键shifted时的icon。
参数 | 参数说明
-----|:--------
shiftKeyShiftedIcon | shift键shifted时的icon

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setDeleteKeyIcon
```Java
public Builder setDeleteKeyIcon (Drawable deleteKeyIcon)
```
设置删除键的icon。
参数 | 参数说明
-----|:--------
deleteKeyIcon | 删除键的icon

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setSettingsKeyIcon
```Java
public Builder setSettingsKeyIcon (Drawable settingsKeyIcon)
```
设置settings键的icon。
参数 | 参数说明
-----|:--------
settingsKeyIcon | settings键的icon

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setSpacesKeyIcon
```Java
public Builder setSpacesKeyIcon (Drawable spacesKeyIcon)
```
设置spaces键的icon。
参数 | 参数说明
-----|:--------
spacesKeyIcon | spaces键的icon

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setEnterKeyIcon
```Java
public Builder setEnterKeyIcon (Drawable enterKeyIcon)
```
设置enter键的icon。
参数 | 参数说明
-----|:--------
enterKeyIcon | enter键的icon

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setGoKeyIcon
```Java
public Builder setGoKeyIcon (Drawable goKeyIcon)
```
设置go键的icon。
参数 | 参数说明
-----|:--------
goKeyIcon | go键的icon

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setSendKeyIcon
```Java
public Builder setSendKeyIcon (Drawable sendKeyIcon)
```
设置send键的icon。
参数 | 参数说明
-----|:--------
sendKeyIcon | send键的icon

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setNextKeyIcon
```Java
public Builder setNextKeyIcon (Drawable nextKeyIcon)
```
设置next键的icon。
参数 | 参数说明
-----|:--------
nextKeyIcon | next键的icon

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setDoneKeyIcon
```Java
public Builder setDoneKeyIcon (Drawable doneKeyIcon)
```
设置done键的icon。
参数 | 参数说明
-----|:--------
doneKeyIcon | done键的icon

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setPreviousKeyIcon
```Java
public Builder setPreviousKeyIcon (Drawable previousKeyIcon)
```
设置previous键的icon。
参数 | 参数说明
-----|:--------
previousKeyIcon | previous键的icon

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setTabKeyIcon
```Java
public Builder setTabKeyIcon (Drawable tabKeyIcon)
```
设置tab键的icon。
参数 | 参数说明
-----|:--------
tabKeyIcon | tab键的icon

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setShortcutKeyIcon
```Java
public Builder setShortcutKeyIcon (Drawable shortcutKeyIcon)
```
设置shortcut键的icon。
参数 | 参数说明
-----|:--------
shortcutKeyIcon | shortcut键的icon

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setSpaceKeyForNumberLayoutIcon
```Java
public Builder setSpaceKeyForNumberLayoutIcon (Drawable spaceKeyForNumberLayoutIcon)
```
设置数字键盘的空白键的icon。
参数 | 参数说明
-----|:--------
spaceKeyForNumberLayoutIcon | 数字键盘的空白键的icon

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setLanguageSwitchKeyIcon
```Java
public Builder setLanguageSwitchKeyIcon (Drawable languageSwitchKeyIcon)
```
设置语言切换键的icon。
参数 | 参数说明
-----|:--------
languageSwitchKeyIcon | 语言切换键的icon

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setZwnjKeyIcon
```Java
public Builder setZwnjKeyIcon (Drawable zwnjKeyIcon)
```
设置零宽不连字键的icon。
参数 | 参数说明
-----|:--------
zwnjKeyIcon | 零宽不连字键的icon

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setEmojiActionKeyIcon
```Java
public Builder setEmojiActionKeyIcon (Drawable emojiActionKeyIcon)
```
设置emoji action键的icon。
参数 | 参数说明
-----|:--------
emojiActionKeyIcon | emoji action键的icon

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setEmojiNormalKeyIcon
```Java
public Builder setEmojiNormalKeyIcon (Drawable emojiNormalKeyIcon)
```
设置emoji normal键的icon。
参数 | 参数说明
-----|:--------
emojiNormalKeyIcon | emoji normal键的icon

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder
