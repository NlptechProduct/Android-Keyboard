## 1.  Add built-in Dictionary

1.Add built-in dictionary and configuration file under the category of assets. Please contact zengine@nlptech.com for dictionary and configuration files.   
2.Add build.gradle in project, see as follows:  
**build.gradle**

~~~
aaptOptions{
    noCompress 'xz'
}
~~~
## 2. Set the Theme

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
## 3. Set EmojiView

If the developers would like to customize the display of emojis rather than using the EmojiView integrated in KeyboardView, the developers may implant it through customized EmojiView in onDisplayEmojiKeyboard() and return true at the same time. 

## 4. Customize NeutralStrip and SuggestionStripView

NeutralStrip, SuggestionStripView, ChineseSuggestStripView and ChineseComposingTextView could be customized after LatinIME extends ZengineInputMethodService.

### NeutralStrip

NeutralStrip,such as toolbar, will appear when SuggestStripView is gone.   

Example :   

**input_view.xml:**

~~~java
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    style="?attr/inputViewStyle">

    <FrameLayout
        android:id="@+id/kb_container"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"/>

	// Add customized Neutral Strip
    <RelativeLayout
        android:id="@+id/customized_strip"
        android:layout_width="match_parent"
        android:layout_height="@dimen/config_suggestions_strip_height"
        android:layout_alignTop="@id/kb_container"
        android:layoutDirection="ltr"
        android:background="@android:color/black">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_centerInParent="true"
            android:textStyle="bold"
            android:text="Customized Strip"
            android:textColor="@android:color/white"/>

    </RelativeLayout>

</RelativeLayout>
~~~

**LatinIME.java:**


~~~java
	// Control customized NeutralStip by overriding onHideCustomizedNeutralStripView() / onShowCustomizedNeutralStripView()
	@Override
    public void onHideCustomizedNeutralStripView() {
        // Hide customized NeutralStrip
        mCustomizedStrip.setVisibility(View.GONE);
    }

    @Override
    public void onShowCustomizedNeutralStripView() {
        // Show customized NeutralStrip
        mCustomizedStrip.setVisibility(View.VISIBLE);
    }

~~~ 


### SuggestionStripView

Zengine provides default SuggestionStripView and its operations.   

And also you could customize SuggestStripView by extending com.nlptech.keyboardview.suggestions.SuggestionStripView.   
Please implement functions down below if you would like to customize.   
##### setSuggestionStripViewListener() : 
Pass SuggestionStripViewListener. You could commit suggested word by using pickSuggestionManually of listner.    

##### setSuggestions() : 
Pass suggest words.

Example :  

**CustomizedSuggestStripView.java:**

~~~java
public class CustomizedSuggestStripView extends SuggestionStripView {
	… … … …
	@Override
    public void setSuggestionStripViewListener(final SuggestionStripViewListener listener, final View inputView) {
    	// TODO something
    }
	… … … …
	@Override
    public void setSuggestions(final SuggestedWords suggestedWords, final boolean isRtlLanguage) {
	    // TODO something
    }
    … … … …
}

~~~

**LatinIME.java:**

~~~java
	//Pass customized SuggestionStripView by overriding getSuggestionView() of LatinIME.java
	@Override
    public SuggestionStripView getSuggestionView() {
    	return new CustomizedSuggestStripView(getContext());
    }
~~~


Control customized SuggestStripView by calling showSuggestionView() / hideSuggestionView() of ZengineInputMethodService

### ChineseSuggestStripView

When using Chinese Input Method, Zengine also provides default ChineseSuggestStripView and its operations.   

If you would like to customize ChineseSuggestStripView, you need to extend com.nlptech.keyboardview.keyboard.chinese.ChineseSuggestStripView and implement functions down below. 

##### setChineseSuggestStripViewListener() : 
Pass ChineseSuggestStripViewListener. You could commit chinese suggested word by using pickSuggestionManually of listner.


##### setChineseSuggestion() : 
Pass chinese suggest words.


##### getMoreSuggestionsList() : 
If there're enough suggested words, you can get more suggested words by passing fetch size.


##### getSuggestionsList() : 
Get the total suggested words.
   
Example :  

**CustomizedChineseSuggestStripView:**

~~~java
public class CustomizedChineseSuggestStripView extends ChineseSuggestStripView implements View.OnClickListener {

	 … … … …
	 @Override
    public void setChineseSuggestStripViewListener(ChineseSuggestStripViewListener chineseSuggestStripViewListener) {
        mChineseSuggestStripViewListener = chineseSuggestStripViewListener;
        // you can call below method in a appropriate situation
        // mChineseSuggestStripViewListener.pickSuggestionManually(index);
    }

    @Override
    public void setChineseSuggestion(List<String> list, boolean enableActiveHighlight) {
        // only get more 50 items
        getMoreSuggestionsList(50);
        
        // implement related behaviors
    }
    … … … …
}
~~~

**LatinIME.java:**

~~~java
    // Pass customized ChineseSuggestStripView by overriding getChineseSuggestionView() of LatinIME.java
    @Override
    public ChineseSuggestStripView getChineseSuggestionView() {
        return new CustomizedChineseSuggestionView(getContext());
    }
~~~

### ChineseComposingTextView

When using Chinese Input Method, there's a default ChineseSuggestStripView at the upper left corner of the KeyboardView.

If you would like to customize ChineseComposingTextView, you need to extend com.nlptech.keyboardview.keyboard.chinese. ChineseComposingTextView and implement function down below. 

##### setComposingText() : 
Pass current composing text.

Example : 

**CustomizedChineseComposingTextView.java:**

~~~java
public class CustomizedChineseComposingTextView extends ChineseComposingTextView {
	 … … … …
	 @Override
    public void setComposingText(String s) {
    	// implement related behaviors
        mComposingTextView.setText(s);
    }
    … … … …
}
~~~

**LatinIME.java:**

~~~java
    // Pass customized ChineseComposingTextView by overriding getChineseComposingTextView() of LatinIME.java    @Override
    public ChineseComposingTextView getChineseComposingTextView()  {
        return new Customized ChineseComposingTextView(getContext());
    }
~~~



## 5.  Setting Input Event Callback

**LatinIME.java:**

~~~java
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
