# 从基于AOSP的项目迁移到Zengine

如果您已经基于AOSP构建了自己的输入法项目，或者您的项目中已经基于AOSP集成了输入法的功能，您可以参考这份文档从AOSP迁移到Zengine来获得更强大的功能和更好的用户体验。

# 集成要求

## 1. AndroidX

Zengine SDK需要依赖AndroidX库。如果您的项目尚未迁移到AndroidX，请在Android Studio中执行以下操作：  
1. Android Studio → Refactor → Migrate to AndroidX  
2. 在下方Refactoring Preview框中，按下Do Refactor.  

亦可参考官方文档：[here](https://developer.android.com/jetpack/androidx/migrate)

## 2. 最低SDK版本、Java编译选项

Zengine SDK要求的最低API Level为19（Android 4.4），需要通过Java 1.8或以上版本进行编译。将如下代码添加设定至build.gradle (app)：  
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

## 1. 安裝Zengine SDK

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
     implementation  'com.nlptech.zengine:keyboardkernel:1.0.4'
     … … … … …
}
~~~

## 2. 修改AndroidManifest.xml

### 2.1 添加appkey

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
       … … … … …
~~~
如果您还没有appkey，请联系zengine@nlptech.com申请appkey和使用授权。

### 2.2 权限

**AndroidManifest.xml:**

~~~
 <uses-permission android:name="android.permission.INTERNET" />
 <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 <uses-permission android:name="android.permission.READ_CONTACTS" />
 <uses-permission android:name="android.permission.READ_PROFILE" />
 <uses-permission android:name="android.permission.READ_USER_DICTIONARY" />
 <uses-permission android:name="android.permission.READ_PHONE_STATE" />
 <uses-permission android:name="android.permission.VIBRATE" />
 <uses-permission android:name="android.permission.WRITE_USER_DICTIONARY" />
~~~

### 2.3 修改PermissionsActivity

**AndroidManifest.xml:**

~~~
<activity 
	<!--用"com.nlptech.inputmethod.latin.permissions.PermissionsActivity
                   "取代".permissions.PermissionsActivity" -->
	android:name="com.nlptech.inputmethod.latin.permissions.PermissionsActivity"
	android:theme="@android:style/Theme.Translucent.NoTitleBar"
	android:exported="false"
	android:taskAffinity="" >
</activity>
~~~

## 3. 更改method.xml

请更改method.xml中的内容如下 (删除所有subtype)：
**method.xml:**

~~~
<input-method xmlns:android="http://schemas.android.com/apk/res/android"
       android:isDefault="false"
       android:supportsSwitchingToNextInputMethod="false">
</input-method>
~~~
## 4. 使用脚本zengineScript.jar删除特定類和资源文件

zengineScript.jar可以自动扫描项目目录中集成Zengine SDK后产生的冗余类文件和资源文件，并将其删除。zengineScript.jar还会自动修改项目中类的引用变更。

**该脚本不会删除有过改动的AOSP文件，并会将所有未删除成功的文件列出，需要您手动检查逻辑并且删除。**

~~~
 java -jar zengineScript.jar 应用项目资料夹根路径
 // 示例 : java -jar zengineScript.jar /MyApp
~~~
如果您不希望通用此脚本自动删除文件，可参考[常见问题](https://github.com/NlptechProduct/Zengine/blob/master/doc_Chinese/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98.md)所列出的文件列表手动删除

## 5. 删除so文件

删除项目创建的so文件:  **libjni_latinime.so**

## 6. 修改原有AOSP内容

### 6.1 开启Auto Import on the fly的功能

为了加速修改过程，建议开启Auto Import on the fly功能：  
Android Studio → Editor → General → Auto Import → Java

### 6.2 修改代码

**LatinIME.java:**

```java
        // 需实现的KeyboardSwitcherListener接口皆與AOSP LatinIME原生接口相同
        // ImsInterface接口需实现getIME()方法
        public class LatinIME extends InputMethodService implements 
KeyboardActionListener,....,KeyboardSwitcherListener, ImsInterface {

    … … … … …    
    // 实现ImsInterface的方法
    @Override
    public InputMethodService getIME() {
        return LatinIME.this;
    }
    … … … … …
    // KeyboardSwitcher更改為IKeyboardSwitcher
    @UsedForTesting final IKeyboardSwitcher mKeyboardSwitcher;
    … … … … …
    public final UIHandler mHandler = new UIHandler(this);
  	// 将创建InputLogic的代码移动到创建UIHandler的下方
  	// 并更改InputLogic构造函数的参数
    final InputLogic mInputLogic = new InputLogic(this, mHandler, KeyboardSwitcher.getInstance() ,mDictionaryFacilitator);
    … … … … …
    @Override
    public void onCreate() {
    	… … … … …
        // KeyboardSwitcher.init(
        // final InputMethodService ims,
        // final KeyboardActionListener actionListener,
        // final KeyboardSwitcherListener switcherListene);
        KeyboardSwitcher.init(this, this, this);
        … … … … …
		// 删除第二个参数
        mStatsUtilsManager.onCreate(this);
        … … … … …
        // 请更改为不带参数
        StatsUtils.onCreate();
        … … … … 
    }

    private boolean isImeSuppressedByHardwareKeyboard() {
		// 请更改KeyboardSwitcher为IKeyboardSwitcher
       final IKeyboardSwitcher switcher = KeyboardSwitcher.getInstance();             
       … … … … 
    }

  	// 修改LatinIME.shouldShowLanguageSwitchKey()的代码，如下
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
          // 检查是否需要把键盘翻到第一页
         KeyboardSwitcher.getInstance().requestUpdatingKeyboardToFirstPage();
         // 检查是否更新变形键盘    
         KeyboardSwitcher.getInstance()
                     .requestUpdatingDeformableKeyState(mInputLogic.getTextBeforeCursor(1));
         // This will set the punctuation suggestions if next word suggestion is off;
         // otherwise it will clear the suggestion strip.
         setNeutralSuggestionStrip();
         … … … … …
  	}
  	… … … … …
  	// 需增加mDictionaryFacilitator.resetDictionaries()中的参数
  	void resetSuggestMainDict() {
        final SettingsValues settingsValues = mSettings.getCurrent();
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
         mDictionaryFacilitator.resetDictionaries(this, locale,
                     settingsValues.mUseContactsDict, settingsValues.mUsePersonalizedDicts,
                     false /* forceReloadMainDictionary */,
                     settingsValues.mAccount, "", /* dictionaryNamePrefix */
                     this /* DictionaryInitializationListener */,
                     Agent.getInstance().obtainDictionaryGetter());
       	… … … … …
     }
     … … … … …
     
  	@Override
  	public void setNeutralSuggestionStrip() {
		final SuggestedWords neutralSuggestions = currentSettings.mBigramPredictionEnabled ? SuggestedWords.getEmptyInstance()
        	// 把currentSettings换成mInputLogic
  		:mInputLogic.mSpacingAndPunctuations.mSuggestPuncList;
       … … … … …
  	}
     … … … … …
    // 在Settings.loadSettings()中，将Zengine的InputLogic实例当作最后一个参数带入
    void loadSettings() {
       … … … … …
       mSettings.loadSettings(this, locale, inputAttributes, mInputLogic);
    }
        … … … … …
  	public void getSuggestedWords(final int inputStyle, final int sequenceNumber,
      	… … … … …
        // 拿掉参数keyboard
      	mInputLogic.getSuggestedWords(mSettings.getCurrent(),mKeyboardSwitcher.getKeyboardShiftMode(), inputStyle, sequenceNumber, callback);
         … … … … …
   	}
  … … … … …
  	// 将updateStateAfterInputTransaction改为public
  	public void updateStateAfterInputTransaction(final InputTransaction inputTransaction) {
      … … … … …
      if (inputTransaction.mEvent.mKeyCode !=  
                                       CODE_SWITCH_TO_NEXT_ALPHABET_PAGE) {
        // 检查是否需要把键盘翻到第一页
        KeyboardSwitcher.getInstance().requestUpdatingKeyboardToFirstPage();
      }
      // 检查是否更新变形键盘
      KeyboardSwitcher.getInstance()
                    .requestUpdatingDeformableKeyState(mInputLogic.getTextBeforeCursor(1));
      if (inputTransaction.requiresUpdateSuggestions()) {
      … … … … …
  	}
```
**LatinIME$UIHandler.java:**

~~~
	public class LatinIME extends InputMethodService{
   	    	… … … … …
		//UIHandler implements ImeUiHandlerInterface
		public static final class UIHandler extends LeakGuardHandlerWrapper<LatinIME> 
			implements ImeUiHandlerInterface {
			… … … … …
				// 需实现的ImeUiHandlerInterface接口皆與AOSP LatinIME.UIHandler原生接口相同
			… … … … …
			@Override
			public void handleMessage(final Message msg) {
				… … … … …
				final IKeyboardSwitcher switcher = latinIme.mKeyboardSwitcher;
				… … … … …
			}
		}
	}
~~~
**AndroidSpellCheckerService.java:**

~~~
 public class AndroidSpellCheckerService extends SpellCheckerService... {
     … … … … …
     public SuggestionResults getSuggestionResults(final Locale locale, final ComposedData composedData, final NgramContext ngramContext,
         @Nonnull final Keyboard keyboard) {
         	… … … …
          	try {
                 … … … … 
                 // 请更改getSuggestionResults的第三个参数
                 return dictionaryFacilitatorForLocale.getSuggestionResults(composedData,   ngramContext,keyboard.getProximityInfo().getNativeProximityInfo(),  …)
             }
             … … … … …
      }
 }
~~~
**DictionaryFacilitatorLruCache.java:**

~~~
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

~~~
  … … … … …
  final EmojiHotKeys emojiHotKeys = new EmojiHotKeys("emoji", emojiSwitchSet) {
      @Override
      protected void action() {
          //请更改引用为IKeyboardSwitcher
          final IKeyboardSwitcher switcher = KeyboardSwitcher.getInstance();
          … … … …
      }
  };
  … … … … …
  final EmojiHotKeys symbolsHotKeys = new EmojiHotKeys("symbols", symbolsSwitchSet) {
      @Override
      protected void action() {
          //请更改引用为IKeyboardSwitcher
          final IKeyboardSwitcher switcher = KeyboardSwitcher.getInstance();
          … … … …
      }
  };
  … … … … …
~~~
**ThemeSettingsFragment.java:**

~~~
public class ThemeSettingsFragment extends SubScreenFragment implements OnRadioButtonClickedListener {            
	… … … …
   static void updateKeyboardThemeSummary(final Preference pref) {
		… … … … …
		// 改用KeyboardThemeManager的getLastUsedKeyboardTheme
		final KeyboardTheme keyboardTheme = KeyboardThemeManager.getInstance().getLastUsedKeyboardTheme(context);
	}

	@Override public void onCreate(final Bundle icicle) {
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

## 7. 代码引入

### 7.1 Agent引入

初始化，需在Application的onCreate()中调用,示例：
**ExampleApplication.java:**

~~~
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

~~~
… … … …
public class LatinIME extends InputMethodService implements 
KeyboardActionListener,....,KeyboardSwitcherListener, ImsInterface {
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
      					// if (mSuggestionStripView != null) {
      					// mSuggestionStripView.setVisibility(View.VISIBLE);
      					// }
       					break;
   					case IKeyboardActionCallback.EMOJI_KEYBOARD:
      					// if (mSuggestionStripView!= null) {
      					// mSuggestionStripView.setVisibility(View.GONE);
      					// }
       					break;
   					case IKeyboardActionCallback.SYMBOL_KEYBOARD:
       					break;
			}
		});
      	… … … …
      }

      @Override
      public void onStartInput(final EditorInfo editorInfo, final boolean restarting) {
          Agent.getInstance().onStartInput(editorInfo, restarting);
          … … … …
      }

      @Override
      public void onStartInputView(final EditorInfo editorInfo, final boolean restarting) {
          Agent.getInstance().onStartInputView(editorInfo, restarting);
          mHandler.onStartInputView(editorInfo, restarting);
          … … … …
      }

      @Override
      public void onFinishInputView(final boolean finishingInput) {
          Agent.getInstance().onFinishInputView(finishingInput);
          StatsUtils.onFinishInputView();
          … … … … 
      }
      @Override
      public void onFinishInput() {
          Agent.getInstance().onFinishInput();
          … … … …
      }

      @Override
      public void onWindowShown() {
          super.onWindowShown();
          Agent.getInstance().onWindowShown();
          … … … …
      }

      @Override
      public void onWindowHidden() {
          super.onWindowHidden();
          Agent.getInstance().onWindowHidden();
          … … … …
     }    

     @Override
     public void onUpdateSelection(final int oldSelStart, final int oldSelEnd,
                             final int newSelStart, final int newSelEnd,
                             final int composingSpanStart, final int composingSpanEnd) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd,
                 composingSpanStart, composingSpanEnd);
        Agent.getInstance().onUpdateSelection(newSelStart, newSelEnd);
        … … … …
        if (isInputViewShown()
                && mInputLogic.onUpdateSelection(oldSelStart, oldSelEnd, 
                                                 newSelStart, newSelEnd,settingsValues)) {
          KeyboardSwitcher.getInstance()
                             .requestUpdatingShiftState(getCurrentAutoCapsState(),
                                                        getCurrentRecapitalizeState());
          // 检查是否更新变形键盘
          KeyboardSwitcher.getInstance()
                    .requestUpdatingDeformableKeyState(mInputLogic.getTextBeforeCursor(1));
        }
        … … … … 
     }

     @Override
     public void onDestroy() {
         Agent.getInstance().onDestroy();
         … … … …
         super.onDestroy();
     }
    … … … … …
~~~

### 7.2 View集成

Zengine SDK中提供的KeyboardView已经整合了默认的EmojiView,开发者只需在InputMethodService.onCreateInputView()中，调用：

~~~
Agent.getInstance().onCreateInputView(ViewGroup container, boolean enable)
~~~
其中container为开发者提供的容器ViewGroup, SDK会自动将KeybaordView及EmojiView生成并加入此ViewGroup。 代码示例 ：  
**LatinIME.java:**

~~~
@Override
 public View onCreateInputView() {
     // 开发者自行生成一个xml布局
     View currentInputView =   
                  LayoutInflater.from(this).inflate(R.layout.example_input_view, null);
     // 布局提供一个容器     
     ViewGroup kbContainer = currentInputView.findViewById(R.id.kb_container);
     // 调用Agent.getInstance().onCreateInputView()， 并传入容器
     Agent.getInstance().onCreateInputView(kbContainer, mIsHardwareAcceleratedDrawingEnabled);
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
   … … … … 
   <!-- 提供給KeybaordView & EmojiView的container -->
   <FrameLayout
       android:id="@+id/kb_container"
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:layout_alignParentBottom="true"/>
   … … … … 
</RelativeLayout>
~~~
###7.3 语言管理
可通过**Agent.getInstance().getAvailableIMELanguageList()**方法获取Zengine支持的语言列表，使用**Agent.getInstance().addIMELanguage()**和**Agent.getInstance().removeIMELanguage()**方法对语言进行添加和删除的操作。词典基于已添加语言进行下载，可通过**Agent.getInstance().getAddedIMELanguageList()**查看已添加语言列表。

### 7.4 词典管理

通过调用**Agent.getInstance().downloadDictionary()**方法进行词典下载，该方法基于当前已添加语言进行批量词典下载和更新。可通过调用**Agent.getInstance().registerDictionaryDownloadListener()**注册listener监听下载状态。词典默认会在wifi和非wifi状态进行下载，如希望关闭非wifi网络状态的下载，可调用**Agent.getInstance().enableMobileDictionaryDownload(false)**进行设置。

### 7.5 其他设定

输入相关的设定(如是否打开滑行输入，自动纠错等保持AOSP项目原有设置，请勿做相关改动。

### 7.6 数据收集开关统计

通过调用**Agent.getInstance().setInputDataCollectionEnabled(value)**设置是否打开Zengine的数据收集功能。请在您所使用的埋点平台统计**setInputDataCollectionEnabled(value)**方法调用的次数与对应值。
以Firebase平台为例，可调用下述代码上报：

~~~
public void dataColectionEvent(boolean value){
   Bundle bundle = new Bundle();
   bundle.putBoolean("zengine_dc_switch",value);
   mFirebaseAnalytics.logEvent("zengine_data_collection",bundle);
}
~~~

## 8. 添加proguard内容

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
~~~

## 9. 新增或修改引用

透过Android Studio → Build → Make Project，得知还有哪些档案有error后，将他们打开并使用Show Intentin Actions → Import Class或触发Auto Import on the fly的方式，快速帮你插入缺少的引用，但少部分引用还是需要手动修改。

需要新增或修改 Zengine相关类的引用，示例：  
	1.AOSP的 AppearanceSettingsFragment.java 中須添加
		import com.nlptech.inputmethod.latin.settings.Settings;  
	2.AOSP的 HardwareKeyboardEventDecoder.java中須添加
		import com.nlptech.inputmethod.event.Event;  
		
将所有error解决后，代表您已成功将项目由AOSP迁移到Zengine SDK。

