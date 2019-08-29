<br/>

## Catalogue
* [1.  Add Dictionary in APK](#1)

* [2.  Theme Setting](#2)
    
* [3.  Custom EmojiView](#3)

* [4.  Custom NeutralStrip and SuggestionStripView](#4)
    * [NeutralStrip](#4.1)
    * [SuggestionStripView](#4.2)
        * [setSuggestionStripViewListener](#4.2.1)
        * [setSuggestions](#4.2.2)
    * [ChineseSuggestStripView](#4.3)
        * [setChineseSuggestStripViewListener](#4.3.1)
        * [setChineseSuggestion](#4.3.2)
        * [getMoreSuggestionsList](#4.3.3)
        * [getSuggestionsList](#4.3.4)
    * [ChineseComposingTextView](#4.4)
        * [setComposingText](#4.4.1)

* [5.  InputEvent Callback](#5)

* [6.  CustomThemePreview](#6)

<br/>


<h2 id="1">1.  Add built-in Dictionary</h2>

1.Add the built-in dictionary and configuration files under the assets directory. Please contact zengine@nlptech.com for dictionary and configuration files.  
2.Add the following code into build.gradle file:

**build.gradle**

~~~
aaptOptions{
    noCompress 'xz'
}
~~~

<h2 id="2">2. Theme Setting</h2>

Add themes by calling **Agent.getInstance().addExternalThemes(context, infos)**.  
Delete themes by calling **Agent.getInstance().deleteExternalThemes(context, infos)**. 
Get new themes by calling **Agent.getInstance().getExternalThemes(context)**. 
Apply themes by calling **Agent.getInstance().loadTheme(context,externalId)**. 

For example, add a theme like this:  
**ExampleApplication.java:**

~~~java
public class ExampleApplication extends Application {
        @Override
        public void onCreate() {
                super.onCreate();
                … … … … …
                addExternalTheme();
                Agent.getInstance().loadTheme(this,"my external theme id");
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

<h2 id="3">3. Custom EmojiView</h2>

If you wants to implement your own EmojiView rather than using the default EmojiView provided by SDK, you may implement custom EmojiView in onDisplayEmojiKeyboard() and return True at the same time.

<h2 id="4">4. Custom NeutralStrip and SuggestionStripView</h2>

NeutralStrip, SuggestionStripView, ChineseSuggestStripView and ChineseComposingTextView could be customized after LatinIME extends ZengineInputMethodService.


<h3 id="4.1">NeutralStrip</h3>

NeutralStrip,such as toolbar, will appear when SuggestStripView is gone. 

For example:

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

    // Add a custom Neutral Strip
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
        // Hide custom NeutralStrip
        mCustomizedStrip.setVisibility(View.GONE);
    }

    @Override
    public void onShowCustomizedNeutralStripView() {
        // Show custom NeutralStrip
        mCustomizedStrip.setVisibility(View.VISIBLE);
    }

~~~ 

<h3 id="4.2">SuggestionStripView</h3>

Zengine provides default SuggestionStripView and its operations.   

And also you could customize SuggestStripView by extending com.nlptech.keyboardview.suggestions.SuggestionStripView.   
Please implement functions down below if you would like to customize. 

<h4 id="4.2.1">setSuggestionStripViewListener() :</h4>

This method will ba called by ZengineInputMethodService.class. You need to update the SuggestionStripViewListener and make sure the method pickSuggestionManually() will be called when a word is chosen from the SuggestionStripView.

<h4 id="4.2.2">setSuggestions() : </h4>

Update and display suggestion words.

Example :  

**CustomizedSuggestStripView.java:**

~~~java
public class CustomizedSuggestStripView extends SuggestionStripView {
    … … … …
    @Override
    public void setSuggestionStripViewListener(final SuggestionStripViewListener listener, final View inputView) {
        //TODO something
        mSuggestionStripViewListener = listener 
        // Call this in a proper position
        // mSuggestionStripViewListener.pickSuggestionManually(index);
    }
    … … … …
    @Override
    public void setSuggestions(final SuggestedWords suggestedWords, final boolean isRtlLanguage) {
        //TODO something
    }
    … … … …
}

~~~

**LatinIME.java:**

~~~java
    //Pass customized SuggestionStripView by overriding getSuggestionView() of LatinIME.java
    @Override
    public SuggestionStripView getSuggestionView() {
        // Return the custom keyboard suggestions bar the developer defined
        return new CustomizedSuggestStripView(getContext());
    }
~~~


ZengineInputMethodService provides showSuggestionView() / hideSuggestionView() methods to help the developer to control the display/hide SuggestStripView. 

<h3 id="4.3">ChineseSuggestStripView</h3>

When using Chinese Input Method, Zengine also provides default ChineseSuggestStripView and its operations.   

If you would like to customize ChineseSuggestStripView, you need to extend com.nlptech.keyboardview.keyboard.chinese.ChineseSuggestStripView and implement functions down below.

<h4 id="4.3.1">setChineseSuggestStripViewListener() :</h4>

This method will ba called by ZengineInputMethodService.class. You need to update the SuggestionStripViewListener and make sure the method pickSuggestionManually() will be called when a word is chosen from the SuggestionStripView. Due to the data structure designed of the Chinese characters, the parameter of this method is "the index of your chosen word".

<h4 id="4.3.2">setChineseSuggestion() : </h4>

Pass candidate words (if there are enough suggestion words, it will provide 10 words at first by default)

<h4 id="4.3.3">getMoreSuggestionsList() : </h4>

If there're enough suggested words, you can get more suggested words by passing fetch size.

<h4 id="4.3.4">getSuggestionsList() : </h4>

Return a list of suggestion words that have been retrieved for the current input.  
  
Example :  

**CustomizedChineseSuggestStripView:**

~~~java
public class CustomizedChineseSuggestStripView extends ChineseSuggestStripView implements View.OnClickListener {

	 … … … …
	 @Override
    public void setChineseSuggestStripViewListener(ChineseSuggestStripViewListener chineseSuggestStripViewListener) {
        mChineseSuggestStripViewListener = chineseSuggestStripViewListener;
        // Call this in a proper position
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
        // Return the View of of the custom keyboard suggestion bar
        return new CustomizedChineseSuggestionView(getContext());
    }
~~~



<h3 id="4.4">ChineseComposingTextView</h3>

When using Chinese Input Method, there's a default ChineseSuggestStripView at the upper left corner of the KeyboardView.

If you would like to customize ChineseComposingTextView, you need to extend com.nlptech.keyboardview.keyboard.chinese. ChineseComposingTextView and implement function down below.

<h4 id="4.4.1">setComposingText() : </h4>

Update the corresponding content under the current composing state
  
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
    // Pass customized ChineseSuggestStripView by overriding getChineseSuggestionView() of LatinIME.java
    @Override
    public ChineseComposingTextView getChineseComposingTextView()  {
        // Return View of suggestion bar customized by the developer
        return new Customized ChineseComposingTextView(getContext());
    }
~~~



<h2 id="5">5.  Input Event Callback</h2>

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

<h2 id="6">6.  Custom ThemePreview</h2>

You can get all the Id of Theme through：

~~~java
Agent.getInstance().getThemeIds(context);
~~~

To create ThemeContext, build a view container and use Agent to put theme preview in.

~~~java
Context themeContext = Agent.getInstance().getThemeContext(context, themeId);
~~~

Example：

~~~java
View myView = LayoutInflater.from(themeContext).inflate(R.layout.my_view, null);
ViewGroup myContainer = myView.findViewById(R.id.my_view_preview_container);
Agent.getInstance().showThemePreview(myContainer, themeId)
~~~

Last, you can call it in the component's onPause or onDestroy：

~~~java
Agent.getInstance().dismissThemePreview();
~~~
