# SDK元件介绍

<br/>

## 目录
* [Agent类](#1)
    * [初始化](#1.1)
    * [生命周期调用](#1.2)
    * [语言管理](#1.3)
    * [词典管理](#1.4)
    * [Theme设定](#1.5)
    * [其他回调设定](#1.6)
    * [自定义功能键](#1.7)

* [回调类](#2)
    * [LanguageCallback](#2.1)
    * [IKeyboardActionCallback](#2.2)
    * [KeyboardSwitcherListener](#2.3)
    * [ImeUiHandlerInterface](#2.4)
    * [ImsInterface](#2.5)
    * [IUserInputCallback](#2.6)
    
* [讯息类](#3)
    * [ExternalThemeInfo.Builder](#3.1)
    * [CustomFunctionalKeyInfo](#3.2)
    * [LottieDrawableInfo](#3.3)

* [渲染类](#4)
    * [KeyboardRender](#4.1)
    * [DefaultKeyboardRender](#3.2)
    * [GestureTrailRender](#4.3)
    * [DefaultGestureTrailRender](#4.4)

<br/>

<h2 id="1">Agent类</h2>

<br/>

<h3 id="1.1">初始化</h3>

#### init
```Java
public void init (Context context)
```
应用启动时呼叫，使得SDK可以获得应用Context资讯。

参数 | 参数说明
-----|:--------
context | 应用启动时呼叫 使得SDK可以获得应用Context资讯

<br/>

<h3 id="1.2">生命周期调用</h3>

#### onCreate
```
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

<br/>

<h3 id="1.3">语言管理</h3>

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

<br/>

<h3 id="1.4">词典管理</h3>

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

#### onLayoutChanged
```Java
public void onLayoutChanged (IMELanguage imeLanguage,String newLayout)
```
切换该语言的layout。

参数 | 参数说明
-----|:--------
imeLanguage | 需切换layout的语言
newLayout | 需切换到的layout

#### obtainLayoutList
```Java
public List<String> obtainLayoutList (IMELanguage imeLanguage)
```
获取该语言所支持的layout。

参数 | 参数说明
-----|:--------
imeLanguage | 需获取layout列表的语言

返回 | 返回说明 | 
-----|:--------
List<String> | layout列表

#### enableDictionaryAutoDownload
```Java
public void enableDictionaryAutoDownload (boolean enableAutoDownload)
```
词典自动下载触发开关控制。

参数 | 参数说明
-----|:--------
enableAutoDownload | 是否打开自动词典下载，false为关闭自动下载，true为打开自动下载

<br/>

<h3 id="1.5">Theme设定</h3>

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

<br/>

<h3 id="1.6">其他回调设定</h3>

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


<br/>

<h3 id="1.7">自定义功能键</h3>

#### setKeyboardSpecificEventCallback
```Java
public void addCustomFunctionalKeyToLeftOfSpace (CustomFunctionalKeyInfo customFunctionalKeyInfo)
```
在空白键的左方，新增自定义的功能键。

参数 | 参数说明
-----|:--------
customFunctionalKeyInfo | 自定义功能键的讯息

#### setKeyboardSpecificEventCallback
```Java
public void addCustomFunctionalKeyToRightOfSpace (CustomFunctionalKeyInfo customFunctionalKeyInfo)
```
在空白键的右方，新增自定义的功能键。

参数 | 参数说明
-----|:--------
customFunctionalKeyInfo | 自定义功能键的讯息

<br/>

<h2 id="2">回调类</h2>

<br/>

<h3 id="2.1">LanguageCallback</h3>
用途 : 语言切换

#### onIMELanguageChanged
```Java
public void onIMELanguageChanged (InputMethodSubtype subtype)
```
语言切换的回调。

参数 | 参数说明
-----|:--------
subtype | 需切换的语言

<br/>

<h3 id="2.2">IKeyboardActionCallback</h3>
用途 : 键盘事件回调

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
keyboardType | 键盘的种类: <br/> 1. IKeyboardActionCallback.ALPHA_KEYBOARD 为字符键盘类 <br/>  2. IKeyboardActionCallback.EMOJI_KEYBOARDemoji 为键盘类 <br/> 3. IKeyboardActionCallback.SYMBOL_KEYBOARD 为符号键盘

<br/>

<h3 id="2.3">KeyboardSwitcherListener</h3>
用途：用于键盘切换回调

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

<br/>

<h3 id="2.4">ImeUiHandlerInterface</h3>
用途：用于与AOSP LatinIME.UIHhandler交互

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

<br/>

<h3 id="2.5">ImsInterface</h3>
用途：用于与AOSP LatinIME部分功能交互

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

<br/>

<h3 id="2.6">IUserInputCallback</h3>
用途：用于用户输入回调

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

<br/>

<h2 id="3">讯息类</h2>

<br/>

<h3 id="3.1">ExternalThemeInfo</h3>
用途 : 新增主题时需要的数据结构

#### Builder
```Java
public Builder (String externalId, String themeName)
```
ExternalThemeInfo的建构器。

参数 | 参数说明
-----|:--------
externalId | 用户自行给予主题的 unique identification，不能为空
themeName | 主题的名称，不能为空

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
设置长按一般键而跳出的more keys键盘的背景。

参数 | 参数说明
-----|:--------
moreKeysKeyboardBackground | 长按一般键而跳出的more keys键盘的背景

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setMoreKeysKeyBackground
```Java
public Builder setMoreKeysKeyBackground (Drawable moreKeysKeyBackground)
```
设置长按一般键而跳出的more keys键盘的键的背景。

参数 | 参数说明
-----|:--------
moreKeysKeyBackground | 长按一般键而跳出的more keys键盘的键的背景

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
public Builder setKeyTextColor (String keyTextColor)
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
public Builder setKeyTextInactivatedColor (String keyTextInactivatedColor)
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
public Builder setFunctionKeyTextColor (String functionKeyTextColor)
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
public Builder setKeyHintLetterColor (String keyHintLetterColor)
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
public Builder setSpacebarBackground (String keyShiftedLetterHintActivatedColor)
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
public Builder setKeyShiftedLetterHintActivatedColor (String keyShiftedLetterHintActivatedColor)
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
public Builder setKeyPreviewTextColor (String keyPreviewTextColor)
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
public Builder setKeyBorderColor (String keyBorderColor)
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
public Builder setLanguageOnSpacebarTextColor (String languageOnSpacebarTextColor)
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
public Builder setGestureTrailColor (String gestureTrailColor)
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
public Builder setEmojiCategoryPageIndicatorBackground (String emojiCategoryPageIndicatorBackgroundColor)
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

#### setGestureTrailDrawable
```Java
public Builder setGestureTrailDrawable (Drawable gestureTrailDrawable)
```
设置滑行轨迹的icon。

参数 | 参数说明
-----|:--------
gestureTrailDrawable | 滑行轨迹的icon

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setGestureTrailStartWidth
```Java
public Builder setGestureTrailStartWidth (float gestureTrailStartWidth)
```
设置滑行轨迹的起始宽度。

参数 | 参数说明
-----|:--------
gestureTrailStartWidth | 滑行轨迹的起始宽度

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setGestureTrailEndWidth
```Java
public Builder setGestureTrailEndWidth (float gestureTrailEndWidth)
```
设置滑行轨迹的结束宽度。

参数 | 参数说明
-----|:--------
gestureTrailEndWidth | 滑行轨迹的结束宽度

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setGestureTrailBodyRatio
```Java
public Builder setGestureTrailBodyRatio (float gestureTrailBodyRatio)
```
设置滑行轨迹的起始百分比，值的范围是[1, 100]，预设为100。

参数 | 参数说明
-----|:--------
gestureTrailBodyRatio | 滑行轨迹的起始百分比

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setGestureTrailFadeoutStartDelay
```Java
public Builder setGestureTrailFadeoutStartDelay (float gestureTrailFadeoutStartDelay)
```
设置滑行轨迹淡出前的时间长。

参数 | 参数说明
-----|:--------
gestureTrailFadeoutStartDelay | 滑行轨迹淡出前的时间长

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setGestureTrailFadeoutDuration
```Java
public Builder setGestureTrailFadeoutDuration (float gestureTrailFadeoutDuration)
```
设置滑行轨迹淡出的时间长。

参数 | 参数说明
-----|:--------
gestureTrailFadeoutDuration | 滑行轨迹淡出的时间长

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setGestureTrailUpdateInterval
```Java
public Builder setGestureTrailUpdateInterval (float gestureTrailUpdateInterval)
```
设置滑行轨迹的FPS，预设为20。

参数 | 参数说明
-----|:--------
gestureTrailUpdateInterval | 滑行轨迹的FPS

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setSuggestionStripViewBackground
```Java
public Builder setSuggestionStripViewBackground (Drawable suggestionStripViewBackground)
```
设置候选词列表的背景。

参数 | 参数说明
-----|:--------
suggestionStripViewBackground | 候选词列表的背景

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setSuggestionStripDivider
```Java
public Builder setSuggestionStripDivider (Drawable suggestionStripDivider)
```
设置候选词列表的分隔线。

参数 | 参数说明
-----|:--------
suggestionStripDivider | 候选词列表的分隔线

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setValidTypedWordColor
```Java
public Builder setValidTypedWordColor (String validTypedWordColor)
```
设置valid typed的候选词的文字颜色。

参数 | 参数说明
-----|:--------
validTypedWordColor | valid typed的候选词的文字颜色

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setTypedWordColor
```Java
public Builder setTypedWordColor (String typedWordColor)
```
设置typed的候选词的文字颜色。

参数 | 参数说明
-----|:--------
typedWordColor | typed的候选词的文字颜色

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setAutoCorrectColor
```Java
public Builder setAutoCorrectColor (String autoCorrectColor)
```
设置自动纠错的候选词的文字颜色。

参数 | 参数说明
-----|:--------
autoCorrectColor | 自动纠错的候选词的文字颜色

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setSuggestedColor
```Java
public Builder setSuggestedColor (String suggestedColor)
```
设置候选词的文字颜色。

参数 | 参数说明
-----|:--------
autoCorrectColor | 候选词的文字颜色

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setSuggestedWordSelectedBackground
```Java
public Builder setSuggestedWordSelectedBackground (Drawable suggestedWordSelectedBackground)
```
设置候选词被选择时的背景。

参数 | 参数说明
-----|:--------
suggestedWordSelectedBackground |候选词被选择时的背景

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setKeyboardLottieBackground
```Java
public Builder setKeyboardLottieBackground (LottieDrawableInfo keyboardLottieBackground)
```
使用LottieDrawable设置键盘的背景。

参数 | 参数说明
-----|:--------
keyboardLottieBackground | 键盘的背景

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setKeyLottieBackground
```Java
public Builder setKeyLottieBackground (LottieDrawableInfo... keyLottieBackground)
```
使用LottieDrawable设置键的背景，第一个参数为常态下的背景，第二个参数为按压键时的背景，皆可为null。

参数 | 参数说明
-----|:--------
keyLottieBackground | 键的背景

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setFunctionKeyLottieBackground
```Java
public Builder setFunctionKeyLottieBackground (LottieDrawableInfo... functionKeyLottieBackground)
```
使用LottieDrawable设置功能键的背景，第一个参数为常态下的背景，第二个参数为按压键时的背景，皆可为null。

参数 | 参数说明
-----|:--------
functionKeyLottieBackground | 功能键的背景

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setSpacebarLottieBackground
```Java
public Builder setSpacebarLottieBackground (LottieDrawableInfo... spacebarLottieBackground)
```
使用LottieDrawable设置空白键的背景，第一个参数为常态下的背景，第二个参数为按压键时的背景，皆可为null。

参数 | 参数说明
-----|:--------
spacebarLottieBackground | 空白键的背景

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setKeyPreviewLottieBackground
```Java
public Builder setKeyPreviewLottieBackground (LottieDrawableInfo keyPreviewLottieBackground)
```
使用LottieDrawable设置键的预览背景。

参数 | 参数说明
-----|:--------
keyPreviewLottieBackground | 键的预览背景

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder



#### setMoreKeysKeyboardLottieBackground
```Java
public Builder setMoreKeysKeyboardLottieBackground (LottieDrawableInfo moreKeysKeyboardLottieBackground)
```
设置more keys键盘的背景。

参数 | 参数说明
-----|:--------
moreKeysKeyboardLottieBackground | more keys键盘的背景

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setMoreKeysKeyLottieBackground
```Java
public Builder setMoreKeysKeyLottieBackground (LottieDrawableInfo... moreKeysKeyLottieBackground)
```
设置more keys键盘的键的背景，第一个参数为常态下的背景，第二个参数为按压键时的背景，皆可为null。

参数 | 参数说明
-----|:--------
ggg | ggg

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setKeyboardClickedEffectLottieDrawable
```Java
public Builder setKeyboardClickedEffectLottieDrawable (LottieDrawableInfo keyboardClickedEffectLottieDrawable)
```
设置键盘点击的效果。

参数 | 参数说明
-----|:--------
keyboardClickedEffectLottieDrawable | 键盘点击的效果

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setCreateKeyboardRenderCallback
```Java
public Builder setCreateKeyboardRenderCallback (CreateRenderCallback<KeyboardRender> createKeyboardRenderCallback)
```
设置生成渲染键盘Render的回调。

参数 | 参数说明
-----|:--------
createKeyboardRenderCallback | 生成渲染键盘Render的回调

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

#### setCreateGestureTrailRenderCallback
```Java
public Builder setCreateGestureTrailRenderCallback (CreateRenderCallback<GestureTrailRender> createGestureTrailRenderCallback)
```
设置生成渲染滑行轨迹Render的回调。

参数 | 参数说明
-----|:--------
createKeyboardRenderCallback | 生成渲染滑行轨迹Render的回调

返回 | 返回说明 | 
-----|:--------
Builder | 返回的Builder

<br/>

<h3 id="3.2">CustomFunctionalKeyInfo</h3>
用途 : 新增自定义功能键时需要的数据结构

#### CustomFunctionalKeyInfo
```Java
public CustomFunctionalKeyInfo (String label, CustomFunctionalKeyCallback callback)
```
CustomFunctionalKeyInfo的建构器，用来生成带有文本的自定义功能键。

参数 | 参数说明
-----|:--------
label | 键上的文本
callback | 自定义功能键被触发时的实现

#### CustomFunctionalKeyInfo
```Java
public CustomFunctionalKeyInfo (Drawable icon, CustomFunctionalKeyCallback callback)
```
CustomFunctionalKeyInfo的建构器，用来生成带有icon的自定义功能键。

参数 | 参数说明
-----|:--------
icon | 键上的icon
callback | 自定义功能键被触发时的实现

#### buildKeyInfoLanguageSwitch
```Java
public static CustomFunctionalKeyInfo buildKeyInfoLanguageSwitch ()
```
生成一个语言切换键的讯息。

返回 | 返回说明 | 
-----|:--------
CustomFunctionalKeyInfo | 返回一个语言切换键的讯息

#### buildKeyInfoEmoji
```Java
public static CustomFunctionalKeyInfo buildKeyInfoEmoji ()
```
生成一个Emoji键的讯息。

返回 | 返回说明 | 
-----|:--------
CustomFunctionalKeyInfo | 返回一个Emoji键的讯息。

#### buildKeyInfoEmoji
```Java
public static CustomFunctionalKeyInfo buildKeyInfoComma ()
```
生成一个Comma键的讯息。

返回 | 返回说明 | 
-----|:--------
CustomFunctionalKeyInfo | 返回一个Comma键的讯息。

#### buildKeyInfoEmoji
```Java
public static CustomFunctionalKeyInfo buildKeyInfoPeriod ()
```
生成一个Period键的讯息。

返回 | 返回说明 | 
-----|:--------
CustomFunctionalKeyInfo | 返回一个Period键的讯息。

#### addMoreKeyInfo
```Java
public void addMoreKeyInfo (CustomFunctionalKeyInfo... infos)
```
在此功能键上加入more keys。

参数 | 参数说明
-----|:--------
infos | more keys的讯息

<br/>

<h3 id="3.3">LottieDrawableInfo</h3>
用途 : 新增LottieDrawable时需要的数据结构

#### LottieDrawableInfo
```Java
public LottieDrawableInfo(CreateLottieTaskCallback createLottieTaskCallback, float scale)
```
LottieDrawableInfo的建构器，用来生成一般LottieDrawable的讯息。

参数 | 参数说明
-----|:--------
createLottieTaskCallback | 生成LottieDrawable的实现
scale | LottieDrawable的缩放倍率，一般会设置1f

#### getLottieDrawableScale
```Java
public float getLottieDrawableScale(float viewWidth, float viewHeight, LottieComposition composition)
```
取得LottieDrawable的缩放倍率，用于在canvase渲染LottieDrawable以前，需要对LottieDrawable调用setScale(float scale)。

参数 | 参数说明
-----|:--------
viewWidth | 渲染此LottieDrawable的宽度
viewHeight | 渲染此LottieDrawable的高度
composition | LottieDrawable.getLottieComposition()

返回 | 返回说明 | 
-----|:--------
float | LottieDrawable的缩放倍率

#### getLottieDrawableScale
```Java
public float getLottieDrawableScale(LottieComposition composition)
```
取得LottieDrawable的缩放倍率，用于在canvase上渲染以前，需要对LottieDrawable调用setScale(float scale)。使用前必须调用setViewSize(float viewWidth, float viewHeight)。

参数 | 参数说明
-----|:--------
composition | LottieDrawable.getLottieComposition()

返回 | 返回说明 | 
-----|:--------
float | LottieDrawable的缩放倍率

#### setViewSize
```Java
public void setViewSize(float viewWidth, float viewHeight)
```
设置在canvase上渲染时，渲染的范围长宽大小，必须调用此方法后，才能调用getLottieDrawableScale(LottieComposition composition)。

参数 | 参数说明
-----|:--------
viewWidth | 在canvase上渲染的范围长
viewHeight | 在canvase上渲染的范围宽
     
<br/>

<h2 id="4">渲染类</h2>

<br/>

<h3 id="4.1">KeyboardRender</h3>
用途 : 用来渲染键盘的渲染器

#### onDrawKeyboardBackground
```Java
public abstract void onDrawKeyboardBackground(@Nonnull Canvas canvas, Drawable background)
```
渲染键盘背景，同一个Render中，此方法和onDrawKeyboardBackground(@Nonnull Canvas canvas, Drawable background, @Nonnull LottieDrawableInfo lottieDrawableInfo)两者之间只会有一个被调用。

参数 | 参数说明
-----|:--------
canvas | 画布
background | 渲染的内容

#### onDrawKeyboardBackground
```Java
public abstract void onDrawKeyboardBackground(@Nonnull Canvas canvas, Drawable background, @Nonnull LottieDrawableInfo lottieDrawableInfo)
```
透过LottieDrawable来渲染键盘背景，同一个Render中，此方法和onDrawKeyboardBackground(@Nonnull Canvas canvas, Drawable background)两者之间只会有一个被调用。

参数 | 参数说明
-----|:--------
canvas | 画布
background | LottieDrawable
lottieDrawableInfo | LottieDrawable的相关讯息，主要是用来取得LottieDrawable所需的scale

#### onDrawKeyBackground
```Java
public abstract void onDrawKeyBackground(int keyboardType, @Nonnull final Key key, int keyStatus,
                                             @Nonnull KeyDrawParams keyDrawParams,
                                             @Nonnull KeyboardDrawParams keyboardDrawParams,
                                             @Nullable Drawable keyBackground,
                                             @Nonnull final Canvas canvas, @Nonnull Paint paint)
```
渲染键的背景。

参数 | 参数说明
-----|:--------
keyboardType | 为定义在KeyboardType中的某一个值，用来区分主键盘、more keys键盘或emoji键盘
key | Key，原生ASOP IME中的Key
keyStatus | 为定义在KeyStatus中的某一个值，用来区分键的状态
keyDrawParams | KeyDrawParams，原生ASOP IME中的KeyDrawParams
keyboardDrawParams | 原生ASOP IME中的部分绘制所需的值，统一包到KeyboardDrawParams这个数据结构里，传给KeyboardRender使用
background | 渲染的内容
canvas | 画布
paint | 画笔

#### onDrawKeyBackground
```Java

public abstract void onDrawKeyTopVisuals(int keyboardType, @Nonnull final Key key, int keyStatus,
                                             @Nonnull KeyDrawParams keyDrawParams,
                                             @Nonnull KeyboardDrawParams keyboardDrawParams,
                                             @Nonnull final Canvas canvas, @Nonnull Paint paint)
```
渲染键的外观。

参数 | 参数说明
-----|:--------
keyboardType | 为定义在KeyboardType中的某一个值，用来区分主键盘、more keys键盘或emoji键盘
key | Key，原生ASOP IME中的Key
keyStatus | 为定义在KeyStatus中的某一个值，用来区分键的状态
keyDrawParams | KeyDrawParams，原生ASOP IME中的KeyDrawParams
keyboardDrawParams | 原生ASOP IME中的部分绘制所需的值，统一包到KeyboardDrawParams这个数据结构里，传给KeyboardRender使用
canvas | 画布
paint | 画笔

#### onDrawKeyBackground
```Java

public abstract void (int keyboardType, @Nonnull final Key key, int keyStatus,
                                             @Nonnull KeyDrawParams keyDrawParams,
                                             @Nonnull KeyboardDrawParams keyboardDrawParams,
                                             @Nonnull final Canvas canvas, @Nonnull Paint paint)
```
渲染键的外观。

参数 | 参数说明
-----|:--------
keyboardType | 为定义在KeyboardType中的某一个值，用来区分主键盘、more keys键盘或emoji键盘
key | Key，原生ASOP IME中的Key
keyStatus | 为定义在KeyStatus中的某一个值，用来区分键的状态
keyDrawParams | KeyDrawParams，原生ASOP IME中的KeyDrawParams
keyboardDrawParams | 原生ASOP IME中的部分绘制所需的值，统一包到KeyboardDrawParams这个数据结构里，传给KeyboardRender使用
canvas | 画布
paint | 画笔

#### afterDrawKeyboard
```Java

public void afterDrawKeyboard(int keyboardType, @Nonnull Keyboard keyboard,
                                  @Nonnull KeyDrawParams keyDrawParams,
                                  @Nonnull KeyboardDrawParams keyboardDrawParams,
                                  @Nonnull Canvas canvas)
```
渲染完键盘后。如果需要再键盘的最上层渲染其他东西，可以实现在这里。

参数 | 参数说明
-----|:--------
keyboardType | 为定义在KeyboardType中的某一个值，用来区分主键盘、more keys键盘或emoji键盘
keyboard | Keyboard，原生ASOP IME中的Keyboard
keyStatus | 为定义在KeyStatus中的某一个值，用来区分键的状态
keyDrawParams | KeyDrawParams，原生ASOP IME中的KeyDrawParams
keyboardDrawParams | 原生ASOP IME中的部分绘制所需的值，统一包到KeyboardDrawParams这个数据结构里，传给KeyboardRender使用
canvas | 画布

#### onDrawKeyPreviewBackground
```Java

public abstract void onDrawKeyPreviewBackground(@Nonnull Canvas canvas, @Nullable Drawable backgroundDrawable)
```
渲染键的预览背景。

参数 | 参数说明
-----|:--------
canvas | 画布
backgroundDrawable | 渲染的内容

#### onDrawKeyPreviewBackground
```Java

public abstract void onDrawKeyPreviewBackground(@Nonnull Canvas canvas,
                                                 @Nonnull LottieDrawable backgroundDrawable,
                                                 @Nonnull ExternalThemeInfo.LottieDrawableInfo lottieDrawableInfo)
```
透过LottieDrawable渲染键的预览背景。

参数 | 参数说明
-----|:--------
canvas | 画布
backgroundDrawable | 渲染的内容
lottieDrawableInfo | LottieDrawable的相关讯息，主要是用来取得LottieDrawable所需的scale

#### onDrawKeyPreviewBackground
```Java

public abstract void onDrawKeyPreviewText(@Nonnull Canvas canvas, @Nullable Bitmap textBitmap)
```
渲染键的预览文字。

参数 | 参数说明
-----|:--------
canvas | 画布
textBitmap | 渲染的文字

#### isInvalidationNeeded
```Java

public boolean isInvalidationNeeded()
```
渲染键的预览文字。

返回 | 返回说明 | 
-----|:--------
boolean | 是否在afterDrawKeyboard之后，调用View.invidate()。如果需要渲染像是Lottie动效或其他自定义的动效，可以返回true，使键盘持续被渲染。






