# 目录

* [从基于AOSP的项目迁移到Zengine](#1)

* [集成要求](#2)
    * [1. AndroidX](#2.1)
    * [2. 最低SDK版本、Java编译选项](#2.2)
    
* [开始集成](#3)
    * [1. 安裝Zengine SDK](#3.1)
    * [2. 修改AndroidManifest.xml](#3.2)
    * [3. 更改method.xml](#3.3)
    * [4. 使用脚本zengineScript.jar删除特定類和资源文件](#3.4)
    * [5. 删除so文件](#3.5)
    * [6. 修改原有AOSP内容](#3.6)
    * [7. 代码引入](#3.7)
    * [8. 添加proguard内容](#3.8)
    * [9. 新增或修改引用](#3.9)
    
<br/>

<h1 id="1">从基于AOSP的项目迁移到Zengine</h1>

如果您已经基于AOSP构建了自己的输入法项目，或者您的项目中已经基于AOSP集成了输入法的功能，您可以参考这份文档从AOSP迁移到Zengine来获得更强大的功能和更好的用户体验。

<h1 id="2">集成要求</h1>

<h2 id="2.1">1. AndroidX</h2>

Zengine SDK需要依赖AndroidX库。如果您的项目尚未迁移到AndroidX，请在Android Studio中执行以下操作：  
1. Android Studio → Refactor → Migrate to AndroidX  
2. 在下方Refactoring Preview框中，按下Do Refactor.  

亦可参考官方文档：[here](https://developer.android.com/jetpack/androidx/migrate)

<h2 id="2.2">2. 最低SDK版本、Java编译选项</h2>

Zengine SDK要求的最低API Level为19（Android 4.4），需要通过Java 1.8或以上版本进行编译。Zengine SDK支持'armeabi-v7a', 'arm64-v8a', 'x86', 'x86_64'架构，可在build.gradle(app)对ABI配置进行选择。  
将如下代码添加设定至build.gradle (app)：  
**app/build.gradle:**

~~~
… … … …
android {
    … … … … ...
    defaultConfig {
        … … … … …
        minSdkVersion 19
        multiDexEnabled true
        … … … …
        
        ndk {
            // Specifies the ABI configurations of your native
            // libraries Gradle should build and package with your APK.
            abiFilters 'armeabi-v7a', 'arm64-v8a'/*, 'x86', 'x86_64'*/
        }
    }
    … … … …
    compileOptions {
        sourceCompatibility 1.8
        targetCompatibility 1.8
    }
}
… … … … 
~~~

# 开始集成
<h1 id="3">开始集成</h1>

<h2 id="3.1">1. 安裝Zengine SDK</h2>

在工程build.gradle配置脚本中allprojects代码段中添加Zengine SDK Maven仓库地址。如下:  
**build.gradle:**

~~~
allprojects {
    repositories {
       … … … … …
       maven { url 'https://repo.nlpte.ch/repository/maven-releases/' }
       … … … … …
    }
}
~~~
在工程App 对应build.gradle配置脚本dependencies段中添加Zengine SDK依赖
**build.gradle:**

~~~
dependencies { 
     … … … … …
     implementation  'com.nlptech.zengine:keyboardkernel:1.3.11'
     … … … … …
}
~~~

<h2 id="3.2">2. 修改AndroidManifest.xml</h2>

### 添加appkey

将appkey添加到AndroidManifest.xml中  
**AndroidManifest.xml:**

~~~
… … … … …
<application
    … … … … …
    <meta-data
        android:name="nlptech_appkey"
        android:value="{appkey_value}" />
        … … … … …
</application>

~~~
如果您还没有appkey，请联系zengine@nlptech.com申请appkey和使用授权。

<h2 id="3.3">3. 更改method.xml</h2>

请更改method.xml中的内容如下 (删除所有subtype)：
**method.xml:**

~~~
<input-method xmlns:android="http://schemas.android.com/apk/res/android"
       android:isDefault="false"
       android:supportsSwitchingToNextInputMethod="false">
</input-method>
~~~

<h2 id="3.4">4. 使用脚本zengineScript.jar删除特定類和资源文件</h2>

zengineScript.jar可以自动扫描项目目录中集成Zengine SDK后产生的冗余类文件和资源文件，并将其删除。zengineScript.jar还会自动修改项目中类的引用变更。

**该脚本不会删除有过改动的AOSP文件，并会将所有未删除成功的文件列出，需要您手动检查逻辑并且删除。**

~~~
 java -jar zengineScript.jar 应用项目资料夹根路径
 // 示例 : java -jar zengineScript.jar /MyApp
~~~
如果您不希望通用此脚本自动删除文件，可参考[常见问题](https://github.com/NlptechProduct/Zengine/blob/master/doc_Chinese/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98.md)所列出的文件列表手动删除

<h2 id="3.5">5. 删除so文件</h2>

删除项目创建的so文件:  **libjni_latinime.so**   
如果您的项目中包含pinyin so文件，请删除： **libejni_pinyinime.so** [zengine v1.2]

<h2 id="3.6">6. 修改原有AOSP内容</h2>

### 6.1 开启Auto Import on the fly的功能

为了加速修改过程，建议开启Auto Import on the fly功能：  
Android Studio → Editor → General → Auto Import → Java

### 6.2 修改代码

**LatinIME.java:**

```java
// 需继承ZengineInputMethodService
public class LatinIME extends ZengineInputMethodService implements 
    KeyboardActionListener,.... {

    … … … … …
    // 请将KeyboardSwitcher更改为IKeyboardSwitcher
    @UsedForTesting final IKeyboardSwitcher mKeyboardSwitcher;
    … … … … …
    public final UIHandler mHandler = new UIHandler(this);
    // 将创建InputLogic的代码移动到创建UIHandler的下方
    // 并更改InputLogic构造函数的参数
    final InputLogic mInputLogic = new InputLogic(this, mHandler, KeyboardSwitcher.getInstance() 
            ,mDictionaryFacilitator);
    … … … … …
    @Override
    public void onCreate() {
    	… … … … …
        // 请增加KeyboardSwitcher.init()的参数
        KeyboardSwitcher.init(this, this, this);
        … … … … …
        // 删除第二个参数
        mStatsUtilsManager.onCreate(this);
        … … … … …
        // 请更改为不带参数
        StatsUtils.onCreate();
        … … … … 
    }

    [zengine v1.3]
    p̶r̶i̶v̶a̶t̶e̶ ̶b̶o̶o̶l̶e̶a̶n̶ ̶i̶s̶I̶m̶e̶S̶u̶p̶p̶r̶e̶s̶s̶e̶d̶B̶y̶H̶a̶r̶d̶w̶a̶r̶e̶K̶e̶y̶b̶o̶a̶r̶d̶(̶)̶ ̶{̶
        // 请将KeyboardSwitcher更改为IKeyboardSwitcher
        f̵i̵n̵a̵l̵ ̵I̵K̵e̵y̵b̵o̵a̵r̵d̵S̵w̵i̵t̵c̵h̵e̵r̵ ̵s̵w̵i̵t̵c̵h̵e̵r̵ ̵=̵ ̵K̵e̵y̵b̵o̵a̵r̵d̵S̵w̵i̵t̵c̵h̵e̵r̵.̵g̵e̵t̵I̵n̵s̵t̵a̵n̵c̵e̵(̵)̵;̵             
        … … … … 
    }̵ 

    // 请修改LatinIME.shouldShowLanguageSwitchKey()的代码，如下
    @Override
  	public boolean shouldShowLanguageSwitchKey() {
      	return mRichImm.hasMultipleEnabledIMEsOrSubtypes(false);
    }
    … … … … …
    @Override
    void onStartInputViewInternal(final EditorInfo editorInfo, final boolean restarting) {
        … … … … …
        mRichImm.refreshSubtypeCaches();
        final IKeyboardSwitcher switcher = mKeyboardSwitcher;
        switcher.updateKeyboardTheme(false);
        switcher.updateKeyboardAdditionalNumberRow();
        final MainKeyboardView mainKeyboardView = switcher.getMainKeyboardView();
        … … … … …
        // 请增加对KeyboardSwitcher.requestUpdatingKeyboardToFirstPage()的调用
        KeyboardSwitcher.getInstance().requestUpdatingKeyboardToFirstPage();
        // 请增加对KeyboardSwitcher.requestUpdatingDeformableKeyState()的调用
        KeyboardSwitcher.getInstance()
            .requestUpdatingDeformableKeyState(mInputLogic.getTextBeforeCursor(Constants.DEFORMABLE_KEY_CHECK_TEXT_COUNT_BEFORE_CURSOR));
        … … … … …
    }
    … … … … …
    [zengine v1.3]
    @Override
    public boolean onEvaluateFullscreenMode() {
        final SettingsValues settingsValues = mSettings.getCurrent();
        … … … … …
        // 需增加此条件，在悬浮键盘时disable fullscreen mode
        if (KeyboardSwitcher.getInstance().isFloatingKeyboard()) {
            return false;
        }
        … … … … …
    }
    … … … … …
    void resetSuggestMainDict() {
        final SettingsValues settingsValues = mSettings.getCurrent();
        // 需增加mDictionaryFacilitator.resetDictionaries()中的参数
        mDictionaryFacilitator.resetDictionaries(this /* context */,
        mDictionaryFacilitator.getLocale(), settingsValues.mUseContactsDict,
                settingsValues.mUsePersonalizedDicts,
                true /* forceReloadMainDictionary */,
                settingsValues.mAccount, "" /* dictNamePrefix */,
                this /* DictionaryInitializationListener */,
                Agent.getInstance().obtainDictionaryGetter());
    }
    … … … … …
    private void resetDictionaryFacilitator(final Locale locale) {
        … … … … …
        // 需增加mDictionaryFacilitator.resetDictionaries()中的参数
        mDictionaryFacilitator.resetDictionaries(this /* context */, locale,
                settingsValues.mUseContactsDict, settingsValues.mUsePersonalizedDicts,
                false /* forceReloadMainDictionary */,
                settingsValues.mAccount, "" /* dictNamePrefix */,
                this /* DictionaryInitializationListener */,
                Agent.getInstance().obtainDictionaryGetter());
                … … … … …
    }
     … … … … …
    void replaceDictionariesForTest(final Locale locale) {
        … … … … …
        // 需增加mDictionaryFacilitator.resetDictionaries()中的参数
        mDictionaryFacilitator.resetDictionaries(this, locale,
            settingsValues.mUseContactsDict, settingsValues.mUsePersonalizedDicts,
            false /* forceReloadMainDictionary */,
            settingsValues.mAccount, "", /* dictionaryNamePrefix */
            this /* DictionaryInitializationListener */,
            Agent.getInstance().obtainDictionaryGetter());
        … … … … …
    }
    … … … … …
    // 在Settings.loadSettings()中，将Zengine的InputLogic实例当作最后一个参数带入
    void loadSettings() {
        … … … … …
        mSettings.loadSettings(this, locale, inputAttributes, mInputLogic);
    }
    … … … … …
    public void getSuggestedWords(final int inputStyle, final int sequenceNumber,...)
        … … … … …
        // 拿掉参数keyboard
      	mInputLogic.getSuggestedWords(mSettings.getCurrent(),mKeyboardSwitcher.getKeyboardShiftMode()
      	    , inputStyle, sequenceNumber, callback);
         … … … … …
    }
    … … … … …
    // 将updateStateAfterInputTransaction改为public
    public void updateStateAfterInputTransaction(final InputTransaction inputTransaction) {
        … … … … …
        // 请增加对KeyboardSwitcher.requestUpdatingKeyboardToFirstPage()的调用
        if (inputTransaction.mEvent.mKeyCode !=  CODE_SWITCH_TO_NEXT_ALPHABET_PAGE) {
            KeyboardSwitcher.getInstance().requestUpdatingKeyboardToFirstPage();
        }
        // 请增加对KeyboardSwitcher.requestUpdatingDeformableKeyState()的调用
        KeyboardSwitcher.getInstance()
                .requestUpdatingDeformableKeyState(mInputLogic.getTextBeforeCursor(Constants.DEFORMABLE_KEY_CHECK_TEXT_COUNT_BEFORE_CURSOR));
        
        if (inputTransaction.requiresUpdateSuggestions()) {
            … … … … …
        }
        [zengine v1.2]
        // 针对SuggestStripView相关行为补充，请添加以下代码
        else {
            setNeutralSuggestionStrip();	
        }
    }
    … … … … …
    [zengine v1.3]
    // 实现当悬浮键盘开或关的时候，需要做什么事情
    @Override
    public void onFloatingKeyboardVisibilityChanged(boolean shown) {
         // TODO: 也许你有一些东西需要在此时关闭...
        … … … … …
        KeyboardWidgetManager.getInstance().closeAll();
        KeyboardWidgetManager.getInstance().updatePadding();
        super.onFloatingKeyboardVisibilityChanged(shown);
    }
    … … … … …
```
**LatinIME$UIHandler.java:**

~~~java
public class LatinIME extends ZengineInputMethodService{
    … … … … …
    //UIHandler 需实现接口 ImeUiHandlerInterface
    public static final class UIHandler extends LeakGuardHandlerWrapper<LatinIME> 
        implements ImeUiHandlerInterface {
        … … … … …
        // 需实现的ImeUiHandlerInterface接口皆與AOSP LatinIME.UIHandler原生接口相同
        … … … … …
        @Override
        public void handleMessage(final Message msg) {
            … … … … …
            // 请将KeyboardSwitcher更改为IKeyboardSwitcher
            final IKeyboardSwitcher switcher = latinIme.mKeyboardSwitcher;
            … … … … …
        }
    }
}
~~~
**AndroidSpellCheckerService.java:**

~~~java
public class AndroidSpellCheckerService extends SpellCheckerService... {
    … … … … …
    public SuggestionResults getSuggestionResults(final Locale locale, final ComposedData composedData, final NgramContext ngramContext,
        @Nonnull final Keyboard keyboard) {
        … … … …
        try {
            … … … … 
            // 请更改getSuggestionResults的第三个参数
            return dictionaryFacilitatorForLocale.getSuggestionResults(composedData
                ,ngramContext,keyboard.getProximityInfo().getNativeProximityInfo(),  …)
        }
        … … … … …
    }
}
~~~
**DictionaryFacilitatorLruCache.java:**

~~~java
… … … … …
private void resetDictionariesForLocaleLocked() {
    … … … … …
    //请增加Agent.getInstance().obtainDictionaryGetter()参数
    mDictionaryFacilitator.resetDictionaries(mContext, mLocale,
        mUseContactsDictionary, false /* usePersonalizedDicts */,
        false /* forceReloadMainDictionary */, null /* account */,
        mDictionaryNamePrefix, null /* listener */,
        Agent.getInstance().obtainDictionaryGetter());
}
… … … … …
~~~
**EmojiAltPhysicalKeyDetector.java:**

~~~java
… … … … …
final EmojiHotKeys emojiHotKeys = new EmojiHotKeys("emoji", emojiSwitchSet) {
    @Override
    protected void action() {
        // 请将KeyboardSwitcher更改为IKeyboardSwitcher
        final IKeyboardSwitcher switcher = KeyboardSwitcher.getInstance();
        … … … …
    }
};
… … … … …
final EmojiHotKeys symbolsHotKeys = new EmojiHotKeys("symbols", symbolsSwitchSet) {
    @Override
    protected void action() {
        // 请将KeyboardSwitcher更改为IKeyboardSwitcher
        final IKeyboardSwitcher switcher = KeyboardSwitcher.getInstance();
        … … … …
    }
};
… … … … …
~~~
**ThemeSettingsFragment.java:**

~~~java
public class ThemeSettingsFragment extends SubScreenFragment implements OnRadioButtonClickedListener {            
    … … … …
    static void updateKeyboardThemeSummary(final Preference pref) {
        … … … … …
        // 改用KeyboardThemeManager的getLastUsedKeyboardTheme
        final KeyboardTheme keyboardTheme = KeyboardThemeManager.getInstance().getLastUsedKeyboardTheme(context);
	}

	@Override 
	public void onCreate(final Bundle icicle) {
        … … … … …
        // 改用KeyboardThemeManager的getLastUsedKeyboardTheme
        final KeyboardTheme keyboardTheme = KeyboardThemeManager.getInstance().getLastUsedKeyboardTheme(context);
    }
	… … … …
	@Override
	public void onPause() {
        super.onPause();
        // KeyboardTheme.saveKeyboardThemeId(mSelectedThemeId,   
        // getSharedPreferences());改用
        KeyboardThemeManager.getInstance().saveLastUsedKeyboardThemeId(mSelectedThemeId, getSharedPreferences());
    }
    … … … … 
~~~

### 6.3 刪除代码 [zengine v1.2]
**LatinIME.java:**

```java
public class LatinIME extends ZengineInputMethodService implements 
        // 删除SuggestStripView相关的implement interface
...., S̶u̶g̶g̶e̶s̶t̶i̶o̶n̶S̶t̶r̶i̶p̶V̶i̶e̶w̶.̶L̶i̶s̶t̶e̶n̶e̶r̶,̶ ̶S̶u̶g̶g̶e̶s̶t̶i̶o̶n̶S̶t̶r̶i̶p̶V̶i̶e̶w̶A̶c̶c̶e̶s̶s̶o̶r̶ {

	… … … … 
	
	// 删除mSuggestionStripView相关代码
	p̶r̶i̶v̶a̶t̶e̶ ̶S̶u̶g̶g̶e̶s̶t̶i̶o̶n̶S̶t̶r̶i̶p̶V̶i̶e̶w̶ ̶m̶S̶u̶g̶g̶e̶s̶t̶i̶o̶n̶S̶t̶r̶i̶p̶V̶i̶e̶w̶;̶
	
	… … … …
	
	public void setInputView(final View view) {
		m̶S̶u̶g̶g̶e̶s̶t̶i̶o̶n̶S̶t̶r̶i̶p̶V̶i̶e̶w̶ ̶=̶ ̶(̶S̶u̶g̶g̶e̶s̶t̶i̶o̶n̶S̶t̶r̶i̶p̶V̶i̶e̶w̶)̶v̶i̶e̶w̶.̶f̶i̶n̶d̶V̶i̶e̶w̶B̶y̶I̶d̶(̶R̶.̶i̶d̶.̶s̶u̶g̶g̶e̶s̶t̶i̶o̶n̶_̶s̶t̶r̶i̶p̶_̶v̶i̶e̶w̶)̶;̶
	}
	
	… … … … 
	
	public void onComputeInsets(final Insets outInsets) {
        super.onComputeInsets(outInsets);
        // This method may be called before {@link #setInputView(View)}.
        if (mInputView == null) {
            return;
        }
                if (mInputView == null) {
            return;
        }
        [zengine v1.3] 
        // 如果需要支持悬浮键盘，那么请删除以下代码
        f̵i̵n̵a̵l̵ ̵S̵e̵t̵t̵i̵n̵g̵s̵V̵a̵l̵u̵e̵s̵ ̵s̵e̵t̵t̵i̵n̵g̵s̵V̵a̵l̵u̵e̵s̵ ̵=̵ ̵m̵S̵e̵t̵t̵i̵n̵g̵s̵.̵g̵e̵t̵C̵u̵r̵r̵e̵n̵t̵(̵)̵;̵
        final View visibleKeyboardView = KeyboardSwitcher.getInstance().getVisibleKeyboardView();
        if (visibleKeyboardView == null || !hasSuggestionStripView()) {
            return;
        }
        [zengine v1.3]
        // 如果需要支持悬浮键盘，那么请删除以下代码
        f̵i̵n̵a̵l̵ ̵i̵n̵t̵ ̵i̵n̵p̵u̵t̵H̵e̵i̵g̵h̵t̵ ̵=̵ ̵m̵I̵n̵p̵u̵t̵V̵i̵e̵w̵.̵g̵e̵t̵H̵e̵i̵g̵h̵t̵(̵)̵;̵
        i̵f̵ ̵(̵i̵s̵I̵m̵e̵S̵u̵p̵p̵r̵e̵s̵s̵e̵d̵B̵y̵H̵a̵r̵d̵w̵a̵r̵e̵K̵e̵y̵b̵o̵a̵r̵d̵(̵)̵ ̵&̵&̵ ̵!̵v̵i̵s̵i̵b̵l̵e̵K̵e̵y̵b̵o̵a̵r̵d̵V̵i̵e̵w̵.̵i̵s̵S̵h̵o̵w̵n̵(̵)̵)̵ ̵{̵
            // If there is a hardware keyboard and a visible software keyboard view has been hidden,
            // no visual element will be shown on the screen.
            o̵u̵t̵I̵n̵s̵e̵t̵s̵.̵c̵o̵n̵t̵e̵n̵t̵T̵o̵p̵I̵n̵s̵e̵t̵s̵ ̵=̵ ̵i̵n̵p̵u̵t̵H̵e̵i̵g̵h̵t̵;̵
            o̵u̵t̵I̵n̵s̵e̵t̵s̵.̵v̵i̵s̵i̵b̵l̵e̵T̵o̵p̵I̵n̵s̵e̵t̵s̵ ̵=̵ ̵i̵n̵p̵u̵t̵H̵e̵i̵g̵h̵t̵;̵
            m̵I̵n̵s̵e̵t̵s̵U̵p̵d̵a̵t̵e̵r̵.̵s̵e̵t̵I̵n̵s̵e̵t̵s̵(̵o̵u̵t̵I̵n̵s̵e̵t̵s̵)̵;̵
            r̵e̵t̵u̵r̵n̵;̵
        }̵
        f̵i̵n̵a̵l̵ ̵i̵n̵t̵ ̵v̵i̵s̵i̵b̵l̵e̵T̵o̵p̵Y̵ ̵=̵ ̵i̵n̵p̵u̵t̵H̵e̵i̵g̵h̵t̵ ̵-̵ ̵v̵i̵s̵i̵b̵l̵e̵K̵e̵y̵b̵o̵a̵r̵d̵V̵i̵e̵w̵.̵g̵e̵t̵H̵e̵i̵g̵h̵t̵(̵)̵;̵
        // Need to set expanded touchable region only if a keyboard view is being shown.
        i̵f̵ ̵(̵v̵i̵s̵i̵b̵l̵e̵K̵e̵y̵b̵o̵a̵r̵d̵V̵i̵e̵w̵.̵i̵s̵S̵h̵o̵w̵n̵(̵)̵)̵ ̵{̵
            f̵i̵n̵a̵l̵ ̵i̵n̵t̵ ̵t̵o̵u̵c̵h̵L̵e̵f̵t̵ ̵=̵ ̵0̵;̵
            f̵i̵n̵a̵l̵ ̵i̵n̵t̵ ̵t̵o̵u̵c̵h̵T̵o̵p̵ ̵=̵ ̵K̵e̵y̵b̵o̵a̵r̵d̵S̵w̵i̵t̵c̵h̵e̵r̵.̵g̵e̵t̵I̵n̵s̵t̵a̵n̵c̵e̵(̵)̵.̵i̵s̵S̵h̵o̵w̵i̵n̵g̵M̵o̵r̵e̵K̵e̵y̵s̵P̵a̵n̵e̵l̵(̵)̵ ̵?̵ ̵0̵ ̵:̵ ̵v̵i̵s̵i̵b̵l̵e̵T̵o̵p̵Y̵;̵
            f̶i̶n̶a̶l̶ ̶i̶n̶t̶ ̶t̶o̶u̶c̶h̶R̶i̶g̶h̶t̶ ̶=̶ ̶v̶i̶s̶i̶b̶l̶e̶K̶e̶y̶b̶o̶a̶r̶d̶V̶i̶e̶w̶.̶g̶e̶t̶W̶i̶d̶t̶h̶(̶)̶;̶
            f̶i̶n̶a̶l̶ ̶i̶n̶t̶ ̶t̶o̶u̶c̶h̶B̶o̶t̶t̶o̶m̶ ̶=̶ ̶i̶n̶p̶u̶t̶H̶e̶i̶g̶h̶t̶
                    // Extend touchable region below the keyboard.
                    +̶ ̶E̶X̶T̶E̶N̶D̶E̶D̶_̶T̶O̶U̶C̶H̶A̶B̶L̶E̶_̶R̶E̶G̶I̶O̶N̶_̶H̶E̶I̶G̶H̶T̶;̶
            o̶u̶t̶I̶n̶s̶e̶t̶s̶.̶t̶o̶u̶c̶h̶a̶b̶l̶e̶I̶n̶s̶e̶t̶s̶ ̶=̶ ̶I̶n̶s̶e̶t̶s̶.̶T̶O̶U̶C̶H̶A̶B̶L̶E̶_̶I̶N̶S̶E̶T̶S̶_̶R̶E̶G̶I̶O̶N̶;̶
            ̶o̶u̶t̶I̶n̶s̶e̶t̶s̶.̶t̶o̶u̶c̶h̶a̶b̶l̶e̶R̶e̶g̶i̶o̶n̶.̶s̶e̶t̶(̶t̶o̶u̶c̶h̶L̶e̶f̶t̶,̶ ̶t̶o̶u̶c̶h̶T̶o̶p̶,̶ ̶t̶o̶u̶c̶h̶R̶i̶g̶h̶t̶,̶ ̶t̶o̶u̶c̶h̶B̶o̶t̶t̶o̶m̶)̶;̶
        }̶
        o̶u̶t̶I̶n̶s̶e̶t̶s̶.̶c̶o̶n̶t̶e̶n̶t̶T̶o̶p̶I̶n̶s̶e̶t̶s̶ ̶=̶ ̶v̶i̶s̶i̶b̶l̶e̶T̶o̶p̶Y̶;̶
        o̶u̶t̶I̶n̶s̶e̶t̶s̶.̶v̶i̶s̶i̶b̶l̶e̶T̶o̶p̶I̶n̶s̶e̶t̶s̶ ̶=̶ ̶v̶i̶s̶i̶b̶l̶e̶T̶o̶p̶Y̶;̶
        m̶S̶u̶g̶g̶e̶s̶t̶i̶o̶n̶S̶t̶r̶i̶p̶V̶i̶e̶w̶.̶s̶e̶t̶M̶o̶r̶e̶S̶u̶g̶g̶e̶s̶t̶i̶o̶n̶s̶H̶e̶i̶g̶h̶t̶(̶v̶i̶s̶i̶b̶l̶e̶T̶o̶p̶Y̶)̶;̶
        mInsetsUpdater.setInsets(outInsets);
        KeyboardWidgetManager.getInstance().onComputeInsets(outInsets);
	}
	
	… … … … 
	
	// 删除以下方法 : hasSuggestionStripView(),setNeutralSuggestionStrip(), 
	// showSuggestionStrip(), setSuggestedWords(), showImportantNoticeContents()
	p̶u̶b̶l̶i̶c̶ ̶b̶o̶o̶l̶e̶a̶n̶ ̶h̶a̶s̶S̶u̶g̶g̶e̶s̶t̶i̶o̶n̶S̶t̶r̶i̶p̶V̶i̶e̶w̶(̶)̶
	p̶r̶i̶v̶a̶t̶e̶ ̶v̶o̶i̶d̶ ̶s̶e̶t̶S̶u̶g̶g̶e̶s̶t̶e̶d̶W̶o̶r̶d̶s̶(̶f̶i̶n̶a̶l̶ ̶S̶u̶g̶g̶e̶s̶t̶e̶d̶W̶o̶r̶d̶s̶ ̶s̶u̶g̶g̶e̶s̶t̶e̶d̶W̶o̶r̶d̶s̶)̶
	p̶u̶b̶l̶i̶c̶ ̶v̶o̶i̶d̶ ̶s̶h̶o̶w̶S̶u̶g̶g̶e̶s̶t̶i̶o̶n̶S̶t̶r̶i̶p̶(̶f̶i̶n̶a̶l̶ ̶S̶u̶g̶g̶e̶s̶t̶e̶d̶W̶o̶r̶d̶s̶ ̶s̶u̶g̶g̶e̶s̶t̶e̶d̶W̶o̶r̶d̶s̶)̶
	p̶u̶b̶l̶i̶c̶ ̶v̶o̶i̶d̶ ̶s̶e̶t̶N̶e̶u̶t̶r̶a̶l̶S̶u̶g̶g̶e̶s̶t̶i̶o̶n̶S̶t̶r̶i̶p̶(̶)̶
	p̶u̶b̶l̶i̶c̶ ̶v̶o̶i̶d̶ ̶s̶h̶o̶w̶I̶m̶p̶o̶r̶t̶a̶n̶t̶N̶o̶t̶i̶c̶e̶C̶o̶n̶t̶e̶n̶t̶s̶(̶)̶
	
	… … … … 
	// 删除ImportantNoticeUtils相关代码
	public void onRequestPermissionsResult(boolean allGranted) {
		I̶m̶p̶o̶r̶t̶a̶n̶t̶N̶o̶t̶i̶c̶e̶U̶t̶i̶l̶s̶.̶u̶p̶d̶a̶t̶e̶C̶o̶n̶t̶a̶c̶t̶s̶N̶o̶t̶i̶c̶e̶S̶h̶o̶w̶n̶(̶t̶h̶i̶s̶ ̶/̶*̶ ̶c̶o̶n̶t̶e̶x̶t̶ ̶*̶/̶)̶;̶
	}
```

**input_view.xml:**

~~~
// 删除原生SuggestionStripView的使用
<̶c̶o̶m̶.̶a̶n̶d̶r̶o̶i̶d̶.̶i̶n̶p̶u̶t̶m̶e̶t̶h̶o̶d̶.̶l̶a̶t̶i̶n̶.̶s̶u̶g̶g̶e̶s̶t̶i̶o̶n̶s̶.̶S̶u̶g̶g̶e̶s̶t̶i̶o̶n̶S̶t̶r̶i̶p̶V̶i̶e̶w̶
~~~

<h2 id="3.7">7. 代码引入</h2>

### 7.1 Agent引入

初始化，需在Application的onCreate()中调用,示例：
**ExampleApplication.java:**

~~~java
public class ExampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Agent.getInstance().init(this);
    }
… … … … 
~~~
需在LatinIME生命周期调用,示例：
**LatinIME.java:**

~~~java
… … … …
public class LatinIME extends ZengineInputMethodService implements 
    KeyboardActionListener,....{
    … … … … …
    public void onCreate() {
        DebugFlags.init(PreferenceManager.getDefaultSharedPreferences(this));
        RichInputMethodManager.init(this);
        mRichImm = RichInputMethodManager.getInstance();
        Agent.getInstance().onCreate(this, mInputLogic, new LanguageCallback(){
            @Override
            public void onIMELanguageChanged(InputMethodSubtype subtype) {
                onCurrentInputMethodSubtypeChanged(subtype);
              }
        });
          
        // 设置键盘切换回调
        Agent.getInstance().
            setKeyboardActionCallback(new IKeyboardActionCallback() {
            @Override
            // 返回值：是否要显示开发者自己的Emoji Keyboard
            public boolean onDisplayEmojiKeyboard() {
                // Show your own Emoji Keyboard if needed
                return false;
            }
            
            @Override
            public void onKeyboardTypeChange(int keyboardType) {
                switch (keyboardType){
                    case IKeyboardActionCallback.ALPHA_KEYBOARD:
                        break;
                    case IKeyboardActionCallback.EMOJI_KEYBOARD:
                        break;
                    case IKeyboardActionCallback.SYMBOL_KEYBOARD:
                        break;
                }
            });
        … … … …
    }
      
    [zengine v1.2]
    @Override
    public void onStartInput(final EditorInfo editorInfo, final boolean restarting) {
        //请添加对父类super.onStartInput的调用
        super.onStartInput(editorInfo, restarting);
        … … … …
    }

    @Override
    public void onStartInputView(final EditorInfo editorInfo, final boolean restarting) {
        //请添加对父类onStartInputView的调用
        super.onStartInputView(editorInfo,restarting);
        … … … …
    }
      
    [zengine v1.2]
    @Override
    public void onFinishInput() {
        //请添加对父类onFinishInput的调用
        super.onFinishInput();
        … … … …
    }

    @Override
    public void onFinishInputView(final boolean finishingInput) {
        //请添加对父类onFinishInputView的调用
        super.onFinishInputView(finishingInput);
        … … … … 
    }

    @Override
    public void onUpdateSelection(final int oldSelStart, final int oldSelEnd,
            final int newSelStart, final int newSelEnd,
            final int composingSpanStart, final int composingSpanEnd) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd,
            composingSpanStart, composingSpanEnd);
        … … … …
        if (isInputViewShown()
            && mInputLogic.onUpdateSelection(oldSelStart, oldSelEnd, 
            newSelStart, newSelEnd,settingsValues)) {
            // 请增加对KeyboardSwitcher.requestUpdatingShiftState()的调用
            KeyboardSwitcher.getInstance()
                .requestUpdatingShiftState(getCurrentAutoCapsState(),
                getCurrentRecapitalizeState());
            // 请增加对KeyboardSwitcher.requestUpdatingDeformableKeyState()的调用
            KeyboardSwitcher.getInstance()
                .requestUpdatingDeformableKeyState(mInputLogic.getTextBeforeCursor(Constants.DEFORMABLE_KEY_CHECK_TEXT_COUNT_BEFORE_CURSOR));
        }
        … … … … 
    }
~~~

### 7.2 View集成

Zengine SDK中提供的KeyboardView已经整合了默认的EmojiView,开发者只需在InputMethodService.onCreateInputView()中，调用：

~~~java
Agent.getInstance().onCreateInputView(ViewGroup container, boolean enable)

~~~

[zengine v1.3]
如果需要支持悬浮键盘，那么请改调用：

~~~java
Agent.getInstance().onCreateInputView(ViewGroup container, FloatingKeyboard floatingKeyboard, boolean enable)

~~~

其中container为开发者提供的容器ViewGroup, SDK会自动将KeybaordView及EmojiView生成并加入此ViewGroup。 代码示例 ：  
**LatinIME.java:**

~~~java
@Override
public View onCreateInputView() {
    // 开发者自行生成一个xml布局
    View currentInputView =   
        LayoutInflater.from(this).inflate(R.layout.example_input_view, null);

    // 布局提供一个容器     
    ViewGroup kbContainer = currentInputView.findViewById(R.id.kb_container);
    [zengine v1.3]
    FloatingKeyboard floatingkeyboard = currentInputView.findViewById(R.id.floating_kb);

    [zengine v1.3]
    // 如果有需要，对悬浮键盘设置是否展开或收起的监听器，否则预设会是收起的。
    // 在键盘悬浮的状态下，收起代表FloatingKeyboard的高度会与键盘一样高，
    // 展开代表FloatingKeyboard的高度会比键盘高些，让键盘上方有空间。
    floatingKeyboard.setExtendableListener(() -> KeyboardWidgetManager.getInstance().isExtendedInFloatingKeyboard());

    // 调用Agent.getInstance().onCreateInputView()， 并传入容器
    [zengine v1.3]
    Agent.getInstance().onCreateInputView(kbContainer, floatingkeyboard, mIsHardwareAcceleratedDrawingEnabled);
    // 如果沒有加上 FloatingKeyboard，那麼可以使用 Agent.getInstance().onCreateInputView(kbContainer, mIsHardwareAcceleratedDrawingEnabled)
    // 或是 Agent.getInstance().onCreateInputView(kbContainer, null, mIsHardwareAcceleratedDrawingEnabled)

    // 返回布局
    return currentInputView;
}

~~~
布局代码示例 ：  
**example_input_view.xml:**

~~~
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    style="?attr/inputViewStyle">

    [zengine v1.3]
    <!-- 这层是为了实现悬浮键盘而使用的，可以选择不加 -->
    <com.nlptech.keyboardview.floatingkeyboard.FloatingKeyboard
        android:id="@+id/floating_kb"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        … … … … 
        <!-- 提供給KeybaordView & EmojiView的container -->
        <FrameLayout
            android:id="@+id/kb_container"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_alignParentBottom="true"/>
        … … … … 

    </com.nlptech.keyboardview.floatingkeyboard.FloatingKeyboard>

</RelativeLayout>
~~~
### 7.3 语言管理
可通过 **Agent.getInstance().getAvailableIMELanguageList()** 方法获取Zengine支持的语言列表，使用 **Agent.getInstance().addIMELanguage()** 和 **Agent.getInstance().removeIMELanguage()** 方法对语言进行添加和删除的操作。词典基于已添加语言进行下载，可通过 **Agent.getInstance().getAddedIMELanguageList()** 查看已添加语言列表。

### 7.4 词典管理

通过调用 **Agent.getInstance().downloadDictionary()** 方法进行词典下载，该方法基于当前已添加语言进行批量词典下载和更新。可通过调用 **Agent.getInstance().registerDictionaryDownloadListener()** 注册listener监听下载状态。词典默认会在wifi和非wifi状态进行下载，如希望关闭非wifi网络状态的下载，可调用 **Agent.getInstance().enableMobileDictionaryDownload(false)** 进行设置。
词典下载会在下述情况被自动触发：添加键盘，添加/删除了一种语言，键盘落下并且与上次成功的词典请求时间的间隔超过8小时。如果希望关闭词典自动下载功能，可通过调用 **Agent.getInstance().enableDictionaryAutoDownload(false)** 关闭。

### 7.5 其他设定

输入相关的设定，如是否打开滑行输入，自动纠错等保持AOSP项目原有设置，请勿做相关改动。

### 7.6 语言Layout切换

可通过调用 **Agent.getInstance().onLayoutChanged(IMELanguage imeLanguage,String newLayout)** 方法切换对应语言的layout。通过调用 **Agent.getInstance().obtainLayoutList(IMELanguage imeLanguage)** 方法获取对应语言的所有layout列表。

<h2 id="3.8">8. 添加proguard内容</h2>

~~~
# 基本设定
-ignorewarnings

# Zengine需Keep的类
-keep class com.nlptech.inputmethod.keyboard.ProximityInfo { *; }
-keep class com.nlptech.inputmethod.latin.DicTraverseSession { *; }
-keep class com.nlptech.inputmethod.latin.BinaryDictionary { *; }
-keep class com.nlptech.inputmethod.latin.utils.BinaryDictionaryUtils { *; }
-keep class com.nlptech.inputmethod.latin.makedict.* { *; }
-keep class com.nlptech.inputmethod.latin.personalization.UserHistoryDictionary { *; }
-keep class com.nlptech.inputmethod.latin.utils.WordInputEventForPersonalization { *; }
-keep class com.nlptech.keyboardtrace.KeyboardTrace  { *; }
-keep class com.nlptech.keyboardtrace.KeyboardTraceCallback  { *; }
-keep class com.nlptech.keyboardtrace.trace.TraceEventSettingsValues  { *; }
-keep class com.nlptech.keyboardtrace.trace.TraceEventSettingsValues$Builder  { *; }
-keep class com.nlptech.keyboardtrace.trace.TraceSuggestedWords  { *; }
-keep class com.nlptech.keyboardtrace.trace.TraceSuggestedWords$SuggestedWordInfo  { *; }
-keep class com.nlptech.keyboardtrace.trace.scepter.domain.**  { *; }
-keep class com.nlptech.keyboardtrace.trace.scepter.TraceEvent  { *; }
-keep class com.nlptech.keyboardtrace.trace.scepter.TraceEvent$Layout { *; }
-keep class com.nlptech.keyboardtrace.trace.upload.TraceUploadStrategy { *; }
-keep class com.nlptech.keyboardtrace.trace.upload.TraceUploadStrategy$* { *; }
-keep class com.nlptech.keyboardtrace.trace.scepter.TraceEditorInfo { *; }
-keep class com.nlptech.common.domain.** { *; }
-keep class com.nlptech.common.api.ResultData { *; }
-keep class com.nlptech.keyboardtrace.trace.upload.PublicField { *; }
# [zengine v1.1.1]
-keep class com.nlptech.keyboardtrace.trace.aether.AetherEvent { *; }
-keep class com.nlptech.keyboardtrace.trace.tesseract.TesseractEvent { *; }
# [zengine v1.2]
-keep class com.nlptech.keyboardview.suggestions.*{ *; } 
-keep class com.android.inputmethod.pinyin.PinyinDecoderService {*;}
# [zengine v1.2.3]
-keep class com.nlptech.inputmethod.latin.ContactsBinaryDictionary { *; }
-keep class com.nlptech.inputmethod.latin.UserBinaryDictionary { *; }
-keep class com.nlptech.inputmethod.latin.personalization.UserHistoryDictionary { *; }
# [zengine v1.2.6]
-keep class com.nlptech.keyboardtrace.AgentWorkManagerInitializer {*;}
# [zengine v1.3.9]
-keep class com.nlptech.keyboardview.theme.download.DownloadThemeDomainData {*;}
-keep class com.nlptech.keyboardview.theme.download.ThemeDownloadInfoBundle {*;}
-keep class com.nlptech.keyboardview.theme.download.ThemeDownloadInfo {*;}
-keep class com.nlptech.keyboardview.theme.download.DownloadThemeInfo {*;}
-keep class com.nlptech.keyboardview.theme.download.api.DownloadThemeApiItem {*;}
~~~

<h2 id="3.9">9. 新增或修改引用</h2>

透过Android Studio → Build → Make Project，得知还有哪些档案有error后，将他们打开并使用Show Intentin Actions → Import Class或触发Auto Import on the fly的方式，快速帮你插入缺少的引用，但少部分引用还是需要手动修改。

需要新增或修改 Zengine相关类的引用，示例：  
	1.AOSP的 AppearanceSettingsFragment.java 中須添加
		import com.nlptech.inputmethod.latin.settings.Settings;  
	2.AOSP的 HardwareKeyboardEventDecoder.java中須添加
		import com.nlptech.inputmethod.event.Event;  
		
将所有error解决后，代表您已成功将项目由AOSP迁移到Zengine SDK。

