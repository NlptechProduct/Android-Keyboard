##1.  Add built-in Dictionary
1.Add built-in dictionary and configuration file under the category of assets. Please contact zengine@nlptech.com for dictionary and configuration files.   
2.Add build.gradle in project, see as follows:  
**build.gradle**

~~~
aaptOptions{
           noCompress 'xz'
}
~~~
##2. Set the Theme
To Add Theme **Agent.getInstance().addExternalThemes(context, infos)**  
To Delete **Agent.getInstance().deleteExternalThemes(context, infos)**  
To Get New Theme **Agent.getInstance().getExternalThemes(context)**  
To Apply Theme **Agent.getInstance().loadTheme(context,externalId)**  

Example: Adding a New Theme
**ExampleApplication.java:**

~~~
public class ExampleApplication extends Application {
        @Override
        public void onCreate() {
                super.onCreate();
                … … … … …
                addExternalTheme()
                Agent.getInstance().loadTheme(this,"my external theme id")
                … … … … …
        }  

    private void addExternalTheme() {
        Drawable keyboardBackgroundDrawable = ContextCompat.getDrawable(this, R.drawable.test_theme_keyboard_bg);

        StateListDrawable keyBackgroundDrawable = new StateListDrawable();
        keyBackgroundDrawable.addState(new int[]{android.R.attr.state_pressed}, ContextCompat.getDrawable(this, R.drawable.test_theme_key_press));
        keyBackgroundDrawable.addState(new int[]{}, ContextCompat.getDrawable(this, R.drawable.test_theme_key_normal));

        StateListDrawable functionKeyBackgroundDrawable = new StateListDrawable();
        functionKeyBackgroundDrawable.addState(new int[]{android.R.attr.state_pressed}, ContextCompat.getDrawable(this, R.drawable.test_theme_function_key_press));
        functionKeyBackgroundDrawable.addState(new int[]{}, ContextCompat.getDrawable(this, R.drawable.test_theme_function_key_normal));

        StateListDrawable spacebarBackgroundDrawable = new StateListDrawable();
        spacebarBackgroundDrawable.addState(new int[]{android.R.attr.state_pressed}, ContextCompat.getDrawable(this, R.drawable.test_theme_space_key_press));
        spacebarBackgroundDrawable.addState(new int[]{}, ContextCompat.getDrawable(this, R.drawable.test_theme_space_key_normal));

        ExternalThemeInfo externalThemeInfo = new ExternalThemeInfo.Builder("my external theme id", "Test Theme")
                .setKeyboardBackground(keyboardBackgroundDrawable)
                .setKeyBackground(keyBackgroundDrawable)
                .setFunctionKeyBackground(functionKeyBackgroundDrawable)
                .setSpacebarBackground(spacebarBackgroundDrawable)
                .setKeyLetterSizeRatio(0.4f)
                .setKeyTextColor(Color.RED)
                .setKeyHintLetterColor(Color.BLUE)
                .setFunctionKeyTextColor(Color.YELLOW)
                .setGestureTrailColor(Color.BLACK)
                .build();
        Agent.getInstance().addExternalThemes(this, externalThemeInfo);
    }
… … … … …
~~~
##3. Set EmojiView
If the developers would like to customize the display of emojis rather than using the EmojiView integrated in KeyboardView, the developers may implant it through customized EmojiView in onDisplayEmojiKeyboard() and return true at the same time. 
##4.  AOSP SuggestionStripView
SuggestionStripView in AOSP provides the prediction bar on the keyboard. SuggestionStripView in Zengine is temporarily unavailable. Please set it as follows:
**input_view.xml:**

~~~
<RelativeLayout
   xmlns:android="http://schemas.android.com/apk/res/android"
   android:layout_width="match_parent"
   android:layout_height="match_parent"
   style="?attr/inputViewStyle">

   // Container of KeyboardView
   <FrameLayout
       android:id="@+id/kb_container"
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:layout_alignParentBottom="true"/>

   // Add SuggestionStripView from AOSP with suggestionStripViewStyle
   <com.android.inputmethod.latin.suggestions.SuggestionStripView
       android:id="@+id/suggestion_strip_view"
       android:layoutDirection="ltr"
       android:layout_width="match_parent"
       android:layout_height="@dimen/config_suggestions_strip_height"
       android:layout_above="@id/kb_container"
       style="?attr/suggestionStripViewStyle" />

</RelativeLayout>
~~~

**LatinIME.java:**

~~~

@Override
 public View onCreateInputView() {
     … … … …
     // suggestionStripViewStyle is managed by Zengine
     Context themeContext = KeyboardSwitcher.getInstance().getThemeContext();
     View currentInputView =   
                  LayoutInflater.from(themeContext).inflate(R.layout.input_view, null);
     … … … … 
     return currentInputView;
 }
~~~

##5.  Setting Input Event Callback
**LatinIME.java:**

~~~
… … … … …
      public void onCreate() {
          DebugFlags.init(PreferenceManager.getDefaultSharedPreferences(this));
          RichInputMethodManager.init(this);
          mRichImm = RichInputMethodManager.getInstance();
          Agent.getInstance().onCreate(this, mInputLogic, new LanguageCallback() {
              @Override
              public void onIMELanguageChanged(InputMethodSubtype subtype) {
                  onCurrentInputMethodSubtypeChanged(subtype);
              }
          });
   

          //Callback for input events (Optional)
          Agent.getInstance().setUserInputCallback(
              new IUserInputCallback() {
					@Override
					public void onUserTyping(String wordComposing) { }
					@Override
					public void onUserTyped(String text) { }
					@Override
					public void onTextChanged(int direction) { }
          });

      }
~~~
