# Migrating to Zengine from an AOSP-based software keyboard

Please read this migration guide if your product is derived from AOSP’s built-in keyboard.

# Prerequisites

## 1. AndroidX

Zengine SDK depends on AndroidX. If you are not yet migrated to AndroidX, please do the following in Android Studio: 
1. Android Studio → Refactor → Migrate to AndroidX  
2. Press “Do Refactor” in “Refactoring Preview” panel 

Or reference the official document:：[here](https://developer.android.com/jetpack/androidx/migrate)

## 2. APILevel and Java version

Zengine SDK supports APILevel 19 (Android 4.4) or above, plus Java 1.8. Make sure relevant options are added to build.gradle:  
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

# Integration

## 1. Importing Zengine SDK into project

Add our Maven repo in build.gradle like below:  
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
Add dependencies:
**build.gradle:**

~~~
dependencies { 
     … … … … …
     implementation  'com.nlptech.zengine:keyboardkernel:1.1.0'
     … … … … …
}
~~~

## 2. AndroidManifest.xml

### 添加appkey

Add Zengine appkey into AndroidManifest.xml:  
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
Please contact zengine@nlptech.com to get appkey and license if you don’t have one yet.

## 3. 更改method.xml

Please modify the content in method.xml as follows (remove all subtypes):
**method.xml:**

~~~
<input-method xmlns:android="http://schemas.android.com/apk/res/android"
       android:isDefault="false"
       android:supportsSwitchingToNextInputMethod="false">
</input-method>
~~~
## 4. Remove duplicated classes and resources

You can remove duplicated Java sources and resources by bundled zengineScript.jar. It scans the source tree and try to remove duplicated ones, and patching import statements automatically.

**If the tool detected that the file is modified by you (compared to vanilla AOSP), a message will be printed and no file is deleted. Please manually review those files.**

~~~
 java -jar zengineScript.jar <your project folder>
 // e.g : java -jar zengineScript.jar /MyApp
~~~
If the tool does not work well for you, please see [FAQ](https://github.com/NlptechProduct/Zengine/blob/master/FAQ.md) to manually do the cleanup

## 5. Native JNI library

Remove unneeded JNI library:  **libjni_latinime.so**

## 6. Customize AOSP

### 6.1 Open Auto Import on the fly

We suggest turn on Auto Import on the fly:
Android Studio → Editor → General → Auto Import → Java
### 6.2 Update code

**LatinIME.java:**

```java
        // 需实现的KeyboardSwitcherListener接口皆與AOSP LatinIME原生接口相同
        // ImsInterface接口需实现getIME()方法
        // 需集成ZengineInputMethodService
        public class LatinIME extends ZengineInputMethodService implements 
KeyboardActionListener,.... {

    … … … … …
    // KeyboardSwitcher-->IKeyboardSwitcher
    @UsedForTesting final IKeyboardSwitcher mKeyboardSwitcher;
    … … … … …
    public final UIHandler mHandler = new UIHandler(this);
    // Move creation of InputLogic after creating UIHandler
    // and modify signature of InputLogic constructor
    final InputLogic mInputLogic = new InputLogic(this,mHandler,KeyboardSwitcher.getInstance(),mDictionaryFacilitator);
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
		// Remove second argument
        mStatsUtilsManager.onCreate(this);
        … … … … …
        // No argument here
        StatsUtils.onCreate();
        … … … … 
    }

    private boolean isImeSuppressedByHardwareKeyboard() {
		// KeyboardSwitcher -> IKeyboardSwitcher
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
          // add code as below
         KeyboardSwitcher.getInstance().requestUpdatingKeyboardToFirstPage();   
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
        	// currentSettings-->mInputLogic
  		:mInputLogic.mSpacingAndPunctuations.mSuggestPuncList;
       … … … … …
    }
     … … … … …
    // In Settings.loadSettings()，add Zengine's InputLogic as last argument
    void loadSettings() {
       … … … … …
       mSettings.loadSettings(this, locale, inputAttributes, mInputLogic);
    }
        … … … … …
    public void getSuggestedWords(final int inputStyle, final int sequenceNumber,...)
      	… … … … …
        // remove argument  keyboard
      	mInputLogic.getSuggestedWords(mSettings.getCurrent(),mKeyboardSwitcher.getKeyboardShiftMode(), inputStyle, sequenceNumber, callback);
         … … … … …
    }
  … … … … …
  	// 将updateStateAfterInputTransaction改为public
    public void updateStateAfterInputTransaction(final InputTransaction inputTransaction) {
      … … … … …
      //Add code down below
      if (inputTransaction.mEvent.mKeyCode !=  
                                       CODE_SWITCH_TO_NEXT_ALPHABET_PAGE) {
        KeyboardSwitcher.getInstance().requestUpdatingKeyboardToFirstPage();
      }
      KeyboardSwitcher.getInstance()
                    .requestUpdatingDeformableKeyState(mInputLogic.getTextBeforeCursor(1));
      if (inputTransaction.requiresUpdateSuggestions()) {
         … … … … …
      }
    }
```
**LatinIME$UIHandler.java:**

~~~java
	public class LatinIME extends ZengineInputMethodService{
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

~~~java
 public class AndroidSpellCheckerService extends SpellCheckerService... {
     … … … … ...
     public SuggestionResults getSuggestionResults(final Locale locale, final ComposedData composedData, final NgramContext ngramContext,
         @Nonnull final Keyboard keyboard) {
         	… … … …
          	try {
                 … … … … 
                 // Please change the third parameter in getSuggestionResults
                 return dictionaryFacilitatorForLocale.getSuggestionResults(composedData,   ngramContext,keyboard.getProximityInfo().getNativeProximityInfo(),  …)
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
	//Please add last parameter in Agent.getInstance().obtainDictionaryGetter()
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
          //KeyboardSwitcher --> IKeyboardSwitcher
          final IKeyboardSwitcher switcher = KeyboardSwitcher.getInstance();
          … … … …
      }
  };
  … … … … …
  final EmojiHotKeys symbolsHotKeys = new EmojiHotKeys("symbols", symbolsSwitchSet) {
      @Override
      protected void action() {
          //KeyboardSwitcher --> IKeyboardSwitcher
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
		// Use KeyboardThemeManager's getLastUsedKeyboardTheme instead
		final KeyboardTheme keyboardTheme = KeyboardThemeManager.getInstance().getLastUsedKeyboardTheme(context);
	}

	@Override public void onCreate(final Bundle icicle) {
		 … … … … …
		// Use KeyboardThemeManager's getLastUsedKeyboardTheme instead
		final KeyboardTheme keyboardTheme = KeyboardThemeManager.getInstance().getLastUsedKeyboardTheme(context);
	}
	… … … …
 
	@Override
	public void onPause() {
		super.onPause();
		// Use KeyboardThemeManager's saveLastUsedKeyboardThemeId instead
		KeyboardThemeManager.getInstance().saveLastUsedKeyboardThemeId(mSelectedThemeId, getSharedPreferences());
	}
    … … … … 
~~~

## 7. Import Code

### 7.1 Import Agent

Initialize agent in Application.onCreate().  
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
Need to call lifecycle in LatinIME  
For example:
**LatinIME.java:**

~~~java
… … … …
public class LatinIME extends ZengineInputMethodService implements 
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
          
          // Set callback to detect keyboard switch
          Agent.getInstance().
                  setKeyboardActionCallback(new IKeyboardActionCallback() {
              @Override
              // Returns true if you want to use Emoji keyboard provided by Zengine, false otherwise
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
      	… … … … …
      }

      @Override
      public void onStartInputView(final EditorInfo editorInfo, final boolean restarting) {
          //请添加对父类onStartInputView的调用
          super.onStartInputView(editorInfo,restarting);
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
                                                 newSelStart, newSelEnd,  settingsValues)) {
          KeyboardSwitcher.getInstance()
                             .requestUpdatingShiftState(getCurrentAutoCapsState(),
                                                        getCurrentRecapitalizeState());
          KeyboardSwitcher.getInstance()
                    .requestUpdatingDeformableKeyState(mInputLogic.getTextBeforeCursor(1));
        }
        … … … … ... 
     }
~~~

### 7.2 Integrating KeyboardView

Developer only needs to call:

~~~
Agent.getInstance().onCreateInputView(ViewGroup container, boolean enable)
~~~
in InputMethodService.onCreateInputView(), where container stands for parent view of the KeyboardView. SDK will automatically create KeyboardView and EmojiView, and add them into ViewGroup.  
**LatinIME.java:**

~~~java
@Override
 public View onCreateInputView() {
     //  XML layout of your project
     View currentInputView =   
                  LayoutInflater.from(this).inflate(R.layout.example_input_view, null);
     // There is a container...     
     ViewGroup kbContainer = currentInputView.findViewById(R.id.kb_container);
     // Call Agent.getInstance().onCreateInputView() with the container
     Agent.getInstance().onCreateInputView(kbContainer, mIsHardwareAcceleratedDrawingEnabled);
     // return the View
     return currentInputView;
 }

~~~
Layour example:  
**example_input_view.xml:**

~~~

<RelativeLayout
   xmlns:android="http://schemas.android.com/apk/res/android"
   android:layout_width="match_parent"
   android:layout_height="match_parent"
   style="?attr/inputViewStyle">
   … … … … 
   <!-- container for KeybaordView & EmojiView -->
   <FrameLayout
       android:id="@+id/kb_container"
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:layout_alignParentBottom="true"/>
   … … … … 
</RelativeLayout>
~~~
###7.3 语言管理
可通过 **Agent.getInstance().getAvailableIMELanguageList()** 方法获取Zengine支持的语言列表，使用 **Agent.getInstance().addIMELanguage()** 和 **Agent.getInstance().removeIMELanguage()** 方法对语言进行添加和删除的操作。词典基于已添加语言进行下载，可通过 **Agent.getInstance().getAddedIMELanguageList()** 查看已添加语言列表。

### 7.4 词典管理

通过调用 **Agent.getInstance().downloadDictionary()** 方法进行词典下载，该方法基于当前已添加语言进行批量词典下载和更新。可通过调用 **Agent.getInstance().registerDictionaryDownloadListener()** 注册listener监听下载状态。词典默认会在wifi和非wifi状态进行下载，如希望关闭非wifi网络状态的下载，可调用 **Agent.getInstance().enableMobileDictionaryDownload(false)** 进行设置。
词典下载会在下述情况被自动触发：添加键盘，添加/删除了一种语言，键盘落下并且与上次成功的词典请求时间的间隔超过8小时。如果希望关闭词典自动下载功能，可通过调用 **Agent.getInstance().enableDictionaryAutoDownload(false)** 关闭。

Through **Agent.getInstance().getAvailableIMELanguageList()** one can get supported language list of Zengine. Use **Agent.getInstance().addIMELanguage()** and **Agent.getInstance().removeIMELanguage()** to add/remove enabled languages, and use **Agent.getInstance().getAddedIMELanguageList()** to get a list of enabled languages. Only dictionaries of enabled languages are downloaded.

### 7.4 Dictionary Management

### 7.6 语言Layout切换

可通过调用 **Agent.getInstance().onLayoutChanged(IMELanguage imeLanguage,String newLayout)** 方法切换对应语言的layout。通过调用 **Agent.getInstance().obtainLayoutList(IMELanguage imeLanguage)** 方法获取对应语言的所有layout列表。


## 8. Proguard settings

~~~
# all ppl want this...
-ignorewarnings

# Keep below for Zengine
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

## 9. Resolve remaining import issues

After build (By “Android Studio → Build → Make Project”), find erroronus sources and use “Show Intentin Actions → Import Class” or “Auto Import on the fly” to fix issues quickly. But some issues still need manual config.  

For example:  
	1.AppearanceSettingsFragment.java needs extra import:
import com.nlptech.inputmethod.latin.settings.Settings;  
	2.HardwareKeyboardEventDecoder.java needs extra import:
import com.nlptech.inputmethod.event.Event;
  
		
Fix all remaining issue to finish the migration from AOSP to Zengine SDK.

