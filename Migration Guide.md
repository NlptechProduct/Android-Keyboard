# Migrating to Zengine from an AOSP-based software keyboard

Please read this migration guide if your product is derived from AOSP’s built-in keyboard.

# Prerequisites

## 1. AndroidX

Zengine SDK depends on AndroidX. If you are not yet migrated to AndroidX, please do the following in Android Studio: 
1. Android Studio → Refactor → Migrate to AndroidX  
2. Press “Do Refactor” in “Refactoring Preview” panel 

Or reference the official document:：[here](https://developer.android.com/jetpack/androidx/migrate)

## 2. APILevel and Java version

Zengine SDK supports API Level 19 (Android 4.4) or above, plus Java 1.8.Zengine SDK supports armeabi-v7a, arm64-v8a, x86 and x86_64. Specifies the ABI configurations of your native libraries in build.gradle(app).
Make sure relevant options are added to build.gradle:  
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
     implementation  'com.nlptech.zengine:keyboardkernel:1.3.12'
     … … … … …
}
~~~

## 2. AndroidManifest.xml

### Add appkey

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

~~~
If you do not have your own appkey, get it [here](http://zengine.nlptech.com/register). If you have difficulty in getting the appkey, please contact zengine@nlptech.com.


## 3. method.xml

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
Remove pinyin so ： **libejni_pinyinime.so** [zengine v1.2]

## 6. Customize AOSP

### 6.1 Open Auto Import on the fly

We suggest turn on Auto Import on the fly:
Android Studio → Editor → General → Auto Import → Java
### 6.2 Update code

**LatinIME.java:**

```java
// Please extends ZengineInputMethodService
public class LatinIME extends ZengineInputMethodService implements 
    KeyboardActionListener,....{
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
        // add arguments of KeyboardSwitcher.init()
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

    // modify LatinIME.shouldShowLanguageSwitchKey() as below
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
        // Please call KeyboardSwitcher.requestUpdatingKeyboardToFirstPage()
        KeyboardSwitcher.getInstance().requestUpdatingKeyboardToFirstPage();
        // Please call KeyboardSwitcher.requestUpdatingDeformableKeyState()
        KeyboardSwitcher.getInstance()
            .requestUpdatingDeformableKeyState(mInputLogic.getTextBeforeCursor(Constants.DEFORMABLE_KEY_CHECK_TEXT_COUNT_BEFORE_CURSOR));
        … … … … …
    }
  	… … … … …
    void resetSuggestMainDict() {
        final SettingsValues settingsValues = mSettings.getCurrent();
        // Arguments of mDictionaryFacilitator.resetDictionaries() are changed
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
        // Arguments of mDictionaryFacilitator.resetDictionaries() are changed
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
         // Arguments of mDictionaryFacilitator.resetDictionaries() are changed
         mDictionaryFacilitator.resetDictionaries(this, locale,
                     settingsValues.mUseContactsDict, settingsValues.mUsePersonalizedDicts,
                     false /* forceReloadMainDictionary */,
                     settingsValues.mAccount, "", /* dictionaryNamePrefix */
                     this /* DictionaryInitializationListener */,
                     Agent.getInstance().obtainDictionaryGetter());
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
        // remove argument keyboard
      	mInputLogic.getSuggestedWords(mSettings.getCurrent(),mKeyboardSwitcher.getKeyboardShiftMode(), inputStyle, sequenceNumber, callback);
         … … … … …
    }
    … … … … …
  	// change updateStateAfterInputTransaction to public
    public void updateStateAfterInputTransaction(final InputTransaction inputTransaction) {
        … … … … …
        // Please call KeyboardSwitcher.requestUpdatingKeyboardToFirstPage()
        if (inputTransaction.mEvent.mKeyCode !=  CODE_SWITCH_TO_NEXT_ALPHABET_PAGE) {
            KeyboardSwitcher.getInstance().requestUpdatingKeyboardToFirstPage();
        }
        // Please call KeyboardSwitcher.requestUpdatingDeformableKeyState()
        KeyboardSwitcher.getInstance()
                .requestUpdatingDeformableKeyState(mInputLogic.getTextBeforeCursor(Constants.DEFORMABLE_KEY_CHECK_TEXT_COUNT_BEFORE_CURSOR));
        
        if (inputTransaction.requiresUpdateSuggestions()) {
            … … … … …
        }
        [zengine v1.2]
        // Add code down below,
        else {
            setNeutralSuggestionStrip();	
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
        // Functions in ImeUiHandlerInterface work the same in LatinIME.UIHandler
        … … … … …
        @Override
        public void handleMessage(final Message msg) {
            … … … … …
            // KeyboardSwitcher-->IKeyboardSwitcher
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
            // Please change the third parameter in getSuggestionResults
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

### 6.3 Delete Code [zengine v1.2]
**LatinIME.java:**

```java
public class LatinIME extends ZengineInputMethodService implements 
        // Delete interface:SuggestionStripView.Listener and SuggestionStripViewAccessor
...., S̶u̶g̶g̶e̶s̶t̶i̶o̶n̶S̶t̶r̶i̶p̶V̶i̶e̶w̶.̶L̶i̶s̶t̶e̶n̶e̶r̶,̶ ̶S̶u̶g̶g̶e̶s̶t̶i̶o̶n̶S̶t̶r̶i̶p̶V̶i̶e̶w̶A̶c̶c̶e̶s̶s̶o̶r̶ {

	… … … … 
	
	// Delete mSuggestionStripView
	p̶r̶i̶v̶a̶t̶e̶ ̶S̶u̶g̶g̶e̶s̶t̶i̶o̶n̶S̶t̶r̶i̶p̶V̶i̶e̶w̶ ̶m̶S̶u̶g̶g̶e̶s̶t̶i̶o̶n̶S̶t̶r̶i̶p̶V̶i̶e̶w̶;̶
	
	… … … …
	
	public void setInputView(final View view) {
		m̶S̶u̶g̶g̶e̶s̶t̶i̶o̶n̶S̶t̶r̶i̶p̶V̶i̶e̶w̶ ̶=̶ ̶(̶S̶u̶g̶g̶e̶s̶t̶i̶o̶n̶S̶t̶r̶i̶p̶V̶i̶e̶w̶)̶v̶i̶e̶w̶.̶f̶i̶n̶d̶V̶i̶e̶w̶B̶y̶I̶d̶(̶R̶.̶i̶d̶.̶s̶u̶g̶g̶e̶s̶t̶i̶o̶n̶_̶s̶t̶r̶i̶p̶_̶v̶i̶e̶w̶)̶;̶
	}
	
	… … … … 
	
	public void onComputeInsets(final Insets outInsets) {
		m̶S̶u̶g̶g̶e̶s̶t̶i̶o̶n̶S̶t̶r̶i̶p̶V̶i̶e̶w̶.̶s̶e̶t̶M̶o̶r̶e̶S̶u̶g̶g̶e̶s̶t̶i̶o̶n̶s̶H̶e̶i̶g̶h̶t̶(̶v̶i̶s̶i̶b̶l̶e̶T̶o̶p̶Y̶)̶;̶
	}
	
	… … … … 
	
	// Delete function down below : hasSuggestionStripView(),setNeutralSuggestionStrip(), 
	// showSuggestionStrip(), setSuggestedWords(), showImportantNoticeContents()
	p̶u̶b̶l̶i̶c̶ ̶b̶o̶o̶l̶e̶a̶n̶ ̶h̶a̶s̶S̶u̶g̶g̶e̶s̶t̶i̶o̶n̶S̶t̶r̶i̶p̶V̶i̶e̶w̶(̶)̶
	p̶r̶i̶v̶a̶t̶e̶ ̶v̶o̶i̶d̶ ̶s̶e̶t̶S̶u̶g̶g̶e̶s̶t̶e̶d̶W̶o̶r̶d̶s̶(̶f̶i̶n̶a̶l̶ ̶S̶u̶g̶g̶e̶s̶t̶e̶d̶W̶o̶r̶d̶s̶ ̶s̶u̶g̶g̶e̶s̶t̶e̶d̶W̶o̶r̶d̶s̶)̶
	p̶u̶b̶l̶i̶c̶ ̶v̶o̶i̶d̶ ̶s̶h̶o̶w̶S̶u̶g̶g̶e̶s̶t̶i̶o̶n̶S̶t̶r̶i̶p̶(̶f̶i̶n̶a̶l̶ ̶S̶u̶g̶g̶e̶s̶t̶e̶d̶W̶o̶r̶d̶s̶ ̶s̶u̶g̶g̶e̶s̶t̶e̶d̶W̶o̶r̶d̶s̶)̶
	p̶u̶b̶l̶i̶c̶ ̶v̶o̶i̶d̶ ̶s̶e̶t̶N̶e̶u̶t̶r̶a̶l̶S̶u̶g̶g̶e̶s̶t̶i̶o̶n̶S̶t̶r̶i̶p̶(̶)̶
	p̶u̶b̶l̶i̶c̶ ̶v̶o̶i̶d̶ ̶s̶h̶o̶w̶I̶m̶p̶o̶r̶t̶a̶n̶t̶N̶o̶t̶i̶c̶e̶C̶o̶n̶t̶e̶n̶t̶s̶(̶)̶
	
	… … … … 
	// Delete ImportantNoticeUtils
	public void onRequestPermissionsResult(boolean allGranted) {
		I̶m̶p̶o̶r̶t̶a̶n̶t̶N̶o̶t̶i̶c̶e̶U̶t̶i̶l̶s̶.̶u̶p̶d̶a̶t̶e̶C̶o̶n̶t̶a̶c̶t̶s̶N̶o̶t̶i̶c̶e̶S̶h̶o̶w̶n̶(̶t̶h̶i̶s̶ ̶/̶*̶ ̶c̶o̶n̶t̶e̶x̶t̶ ̶*̶/̶)̶;̶
	}
```

**input_view.xml:**

~~~
// Delete SuggestionStripView
<̶c̶o̶m̶.̶a̶n̶d̶r̶o̶i̶d̶.̶i̶n̶p̶u̶t̶m̶e̶t̶h̶o̶d̶.̶l̶a̶t̶i̶n̶.̶s̶u̶g̶g̶e̶s̶t̶i̶o̶n̶s̶.̶S̶u̶g̶g̶e̶s̶t̶i̶o̶n̶S̶t̶r̶i̶p̶V̶i̶e̶w̶
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
KeyboardActionListener,...{
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
       					break;
   					case IKeyboardActionCallback.EMOJI_KEYBOARD:
       					break;
   					case IKeyboardActionCallback.SYMBOL_KEYBOARD:
       					break;
			}
		});
      	… … … … …
      }
      
    [zengine v1.2]
    @Override
    public void onStartInput(final EditorInfo editorInfo, final boolean restarting) {
        // Please call super.onStartInput(editorInfo, restarting)
        super.onStartInput(editorInfo, restarting);
        … … … …
    }

    @Override
    public void onStartInputView(final EditorInfo editorInfo, final boolean restarting) {
        // Please call super.onStartInputView(editorInfo,restarting)
        super.onStartInputView(editorInfo,restarting);
        … … … … 
    }

    [zengine v1.2]
    @Override
    public void onFinishInput() {
        // Please call super.onFinishInput()
        super.onFinishInput();
        … … … …
    }
    
    @Override
    public void onFinishInputView(final boolean finishingInput) {
        // Please call super.onFinishInputView(finishingInput)
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
        // Add code down below
        if (isInputViewShown()
            && mInputLogic.onUpdateSelection(oldSelStart, oldSelEnd, 
            newSelStart, newSelEnd,  settingsValues)) {
            // Please call KeyboardSwitcher.requestUpdatingShiftState()
            KeyboardSwitcher.getInstance()
                .requestUpdatingShiftState(getCurrentAutoCapsState(),
                getCurrentRecapitalizeState());
            // Please call KeyboardSwitcher.requestUpdatingDeformableKeyState()
            KeyboardSwitcher.getInstance()
                .requestUpdatingDeformableKeyState(mInputLogic.getTextBeforeCursor(Constants.DEFORMABLE_KEY_CHECK_TEXT_COUNT_BEFORE_CURSOR));
        }
        … … … …
     }
~~~

### 7.2 Integrating KeyboardView

Developer only needs to call:

~~~java
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
### 7.3 Language Management

Through **Agent.getInstance().getAvailableIMELanguageList()** one can get supported language list of Zengine. Use **Agent.getInstance().addIMELanguage()** and **Agent.getInstance().removeIMELanguage()** to add/remove enabled languages, and use **Agent.getInstance().getAddedIMELanguageList()** to get a list of enabled languages. Only dictionaries of enabled languages are downloaded.

### 7.4 Dictionary Management

Initiate dictionary download through calling **Agent.getInstance().downloadDictionary()**. Downloading progress can be observed by registering a listener through **Agent.getInstance().registerDictionaryDownloadListener()**. By default, downloading is enabled even on cellular network, if you want to turn this off, please call **Agent.getInstance().enableMobileDictionaryDownload(false)**.
dictionaries downloading will be activated under these circumstances:First, add keyboard. Second, add/delete one language. Third, there is an eight-hour gap or above between the time keyboard was downloaded last time and this time while the keyboard is hidden. If would like to turn off auto-download dictionary, call **Agent.getInstance().enableDictionaryAutoDownload(false)**.

### 7.5 Other Settings

Please try to keep detailed input-related settings, for example, sliding input or auto-correction, untouched.

### 7.6 Change layout of language

If would like to change layout, call **Agent.getInstance().onLayoutChanged(IMELanguage imeLanguage,String newLayout)**. If would like to fetch the list of layouts under one language, call **Agent.getInstance().obtainLayoutList(IMELanguage imeLanguage)**.

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

## 9. Resolve remaining import issues

After build (By “Android Studio → Build → Make Project”), find erroronus sources and use “Show Intentin Actions → Import Class” or “Auto Import on the fly” to fix issues quickly. But some issues still need manual config.  

For example:  
	1.AppearanceSettingsFragment.java needs extra import:
import com.nlptech.inputmethod.latin.settings.Settings;  
	2.HardwareKeyboardEventDecoder.java needs extra import:
import com.nlptech.inputmethod.event.Event;
  
		
Fix all remaining issue to finish the migration from AOSP to Zengine SDK.

