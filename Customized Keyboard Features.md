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


* [7.  Floating Keyboard[zengine v1.3]](#7)

* [8.  Download Theme[zengine v1.3.9]](#8)
    * [Get Download Theme and Status](#8.1)
    * [Download and Delete](#8.2)
    * [Resource Format](#8.3)
    * [Theme Download Info](#8.4)
    * [Fetch Theme Download Info](#8.5)
    * [Trigger Theme Download Info](#8.6)

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

[zengine v1.3]
<h2 id="7">7.  Floating Keyboard</h2>

First, you need add FloatingKeyboard layout in your input_view.xml, for example：
~~~
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    style="?attr/inputViewStyle"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <!-- This is the FloatingKeyboard layout, wrapping up your keyboard layout content. -->
    <com.nlptech.keyboardview.floatingkeyboard.FloatingKeyboard
        android:id="@+id/floating_kb"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <FrameLayout
            android:id="@+id/kb_container"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_alignParentBottom="true" />

        <LinearLayout
            android:id="@+id/toolbar_items_container"
            android:layout_width="match_parent"
            android:layout_height="@dimen/config_suggestions_strip_height"
            android:layout_alignTop="@id/kb_container"
            android:background="@android:color/black"
            android:layoutDirection="ltr" />

        <FrameLayout
            android:id="@+id/keyboard_widget_container"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />

    </com.nlptech.keyboardview.floatingkeyboard.FloatingKeyboard>

</RelativeLayout>
~~~

Add this code in the InputMethodService.onCreateInputView()：
~~~java
Agent.getInstance().onCreateInputView(kbContainer, floatinKeyboard, mIsHardwareAcceleratedDrawingEnabled);
~~~

You can use those code to:

Control the floating keyboard switch：
~~~java
KeyboardSwitcher.getInstance().switchFloatingKeyboard(context);
~~~

Get the floating keyboard switch on or off：
~~~java
KeyboardSwitcher.getInstance().isFloatingKeyboard();
~~~

Enter the floating keyboard resize mode：
~~~java
KeyboardSwitcher.getInstance().switchFloatingKeyboardResizeMode(shown);
~~~

[zengine v1.3.9]
<h2 id="8">8.  Download theme</h2>


<h3 id="8.1">Get Download Theme and Status</h4>

The SDK can automatically get all of download theme 's downlaod info. You can use this code to get DownloadTheme as a KeyboardTheme：
~~~java
KeyboardThemeManager.getInstance().getAvailableThemeArray(context)
~~~

The DownloadTheme 's themeType is KeyboardTheme.ThemeType.DOWNLOAD.

You can use this code to get the DownloadTheme 's status：
~~~java
DownloadTheme downloadTheme = (DownloadTheme) keyboarTheme;
int status = downloadTheme.getDownloadStatus();
~~~

The status value will be：

Status | Description
-----|:-----
DownloadTheme.DOWNLOAD_STATUS_DOWNLOADABLE|可下載
DownloadTheme.DOWNLOAD_STATUS_DOWNLOADING|下載中
DownloadTheme.DOWNLOAD_STATUS_DOWNLOADED|已下載的

<h3 id="8.2">Download and Delete</h4>

You can use this code to download the theme 's resource：
~~~java
ThemeDownloadInfo themeDownloadInfo = downloadTheme.getDownloadInfo();
DownloadThemeManager.getInstance().downloadTheme(context, themeDownloadInfo);
~~~

You can use this code to delete the theme 's resource：：
~~~java
DownloadThemeManager.getInstance().deleteTheme(context, downloadTheme);
~~~

Make a BroadcastReceiver to listen the DownloadTheme 's status be changed：
~~~java
private DownloadThemeReceiver receiver = new DownloadThemeReceiver();

@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
    ......
    IntentFilter intentFilter = new IntentFilter(DownloadThemeManager.ACTION_UPDATE_DOWNLOAD_THEME);
    registerReceiver(receiver, intentFilter);
}

@Override
protected void onDestroy() {
    ......
    unregisterReceiver(receiver);
}

public static class DownloadThemeReceiver : BroadcastReceiver() {
    @Override
    public void onReceive(final Context context, final Intent intent) {
            if (DownloadThemeManager.ACTION_UPDATE_DOWNLOAD_THEME != intent.action) {
                return
            }

            String  themeId = intent.getStringExtra(DownloadThemeManager.KEY_DOWNLOAD_THEME_ID);
            // The themeId is by user call setThemeId() when creating ThemeDownloadInfo
            if(themeId == null){
                return
            }
            int status = intent.getIntExtra(DownloadThemeManager.KEY_DOWNLOAD_THEME_STATUS, -1);
            // status:
            // THEME_DOWNLOAD_STATUS_DOWNLOADABLE
            // THEME_DOWNLOAD_STATUS_DOWNLOADING
            // THEME_DOWNLOAD_STATUS_DOWNLOADED
            // THEME_DOWNLOAD_STATUS_DELETED
            if (status == -1) {
                return
            }
            
            // TODO: You can refresh your theme list here.
        }
    }
~~~

If you want to create your DownloadTheme 's resource on your server, please follow [8.3](#8.3) ~ [8.6](#8.6)

<h3 id="8.3">Resource Format</h4>

Every download theme must have a resource, the format is in [Resource Format](https://github.com/NlptechProduct/Android-Keyboard/blob/2f34ff3c76d95b249c2ff21698cb012345d504a3/doc_Chinese/DownloadTheme%E8%B5%84%E6%BA%90%E5%8C%85%E6%A0%BC%E5%BC%8F.md). You can create your resource as zip  and upload to your server.

<h3 id="8.4">Theme Download Info</h4>

Your server need to provide API for APP witch implements  DownloadThemeManager.Listener to fetch all of the download theme 's download info。

Download theme 's download info has those fields：

field | type | description
-----|:-----|:-----
themeId|String|You can defind yourself.
themeName|String|Theme name.
themeUrl|String|Theme resource zip 's download url.
themeCover|String|Theme 's placeholder.
themeCoverWithBorder|String|Theme 's placeholder with border, can be null if none.
mode|Integer|0: Light、1: Dark，When the theme ids are same and mode are different , you can let yoyr user switch Light/Dark mode.
md5|String|resource zip 's md5.
size|Integer|resource zip 's file size.
version|Integer|resource version, according to version, SDK will update theme resource or not.

<h3 id="8.5">Fetch Theme Download Info</h4>

APP implements DownloadThemeManager.Listener, for example：
~~~java
public class DownloadThemeDataFetcher implements DownloadThemeManager.Listener {
    private static final String TAG = DownloadThemeDataFetcher.class.getSimpleName();

    @Override
    @WorkerThread
    public ArrayList<ThemeDownloadInfo> onFetchThemeDownloadInfo() {
        ArrayList<ThemeDownloadInfo> result = new ArrayList<>();
        // Fetch download theme 's download info from your server.
        ......
        ......
        ......

        // Create the ThemeDownloadInfo.
        for (YourDataObject item : items) {
                ThemeDownloadInfo downloadInfo = new ThemeDownloadInfo();
                downloadInfo.setThemeId(item.getTheme_id());
                downloadInfo.setThemeName(item.getTheme_name());
                downloadInfo.setThemeUrl(item.getTheme_url());
                downloadInfo.setThemeCover(item.getTheme_cover()); // url
                downloadInfo.setThemeCoverWithBorder(item.getTheme_cover_with_border()); // url
                downloadInfo.setMode(Integer.parseInt(item.getMode()));
                downloadInfo.setMd5(item.getMd5());
                downloadInfo.setSize(item.getSize());
                downloadInfo.setVersion(item.getVersion());
                result.add(downloadInfo);
        }

        return result;
    }
}
~~~

Call this code to set DownloadThemeDataFetcher(recommend in Application.onCreate())：
~~~java
DownloadThemeManager.getInstance().setDownloadThemeDataListener(new DownloadThemeDataFetcher());
~~~

<h3 id="8.6">Trigger Theme Download Info</h4>

SDK will trigger to fetch theme 's download info at the Application.onCreate() and InputMethodService.onWindowHide().

But you can trigger on demand：
~~~java
DownloadThemeManager.getInstance().triggerFetchData(context);
~~~

If it is first time or cache is time out, SDK will use DownloadThemeDataFetcher to fetch theme 's download info.

You can also use this code to set cache timeout duration, default is 12 hour：
~~~java
DownloadThemeManager.getInstance().setFetchDownloadThemeDataDuration(duration);
~~~
