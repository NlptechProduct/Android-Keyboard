# SDK Introduction

<br/>

## Catalogue
* [Agent](#1)
    * [Initialization](#1.1)
    * [Lifecycle Callback](#1.2)
    * [Language Management](#1.3)
    * [Dictionary Management](#1.4)
    * [Theme Setting](#1.5)
    * [Other Callback Settings](#1.6)
    * [Custom Function Key](#1.7)

* [Callback](#2)
    * [LanguageCallback](#2.1)
    * [IKeyboardActionCallback](#2.2)
    * [KeyboardSwitcherListener](#2.3)
    * [ImeUiHandlerInterface](#2.4)
    * [ImsInterface](#2.5)
    * [IUserInputCallback](#2.6)
    * [SuggestionStripViewListener](#2.7)
    * [ChineseSuggestStripViewListener](#2.8)
    
* [Message](#3)
    * [ExternalThemeInfo.Builder](#3.1)
    * [CustomFunctionalKeyInfo](#3.2)
    * [LottieDrawableInfo](#3.3)

* [Rendering](#4)
    * [KeyboardRender](#4.1)
    * [DefaultKeyboardRender](#4.2)
    * [GestureTrailRender](#4.3)
    * [DefaultGestureTrailRender](#4.4)

* [Abstract](#5)
    * [SuggestionStripView](#5.1)
    * [ChineseSuggestStripView](#5.2)
    * [ChineseComposingTextView](#5.3)

<br/>

<h2 id="1">Agent</h2>

<br/>

<h3 id="1.1">Initialization</h3>

#### init

```Java
public void init (Context context)
```
When the application is activated, it allows SDK to get the information of Context. 

Parameter | Parameter Descriptions
-----|:--------
context | When the application is activated, it allows SDK to get the information of Context.

<br/>

<h3 id="1.2">Call Lifecycle</h3>

#### onCreate

```Java
public void onCreate (InputMethodService service, InputLogic mInputLogic, LanguageCallback languageCallback)
```
Respond to lifecycle of InputMethodService; Pass related objects from application

Parameter | Parameter Descriptions
-----|:--------
service | IMS Application instance 
mInputLogic | Integrate object of InputLoic of Zengine SDK's InputLoic objects
languageCallback | To apply integrated Zengine SDK needs to implement LanguageCallback

#### onCreateInputView

```Java
public void onCreateInputView (ViewGroup kbContainer, boolean isHardwareAcceleratedDrawingEnabled)
```
Respond to the InputMethodService life cycle.

Parameter | Parameter Description
-----|:--------
kbContainer | The container to put the KeyboardView
isHardwareAcceleratedDrawingEnabled | Whether to turn on hardware acceleration

#### onStartInput

```Java
public void onStartInput (final EditorInfo editorInfo, final boolean restarting)
```
Respond to InputMethodService lifecycle.

Parameter | Parameter Description
-----|:--------
editorInfo | InputMethodService life cycle parameter
restarting | InputMethodService life cycle parameter

#### onStartInputView

```Java
public void onStartInputView (final EditorInfo editorInfo, final boolean restarting)
```
Respond to InputMethodService lifecycle.

Parameter | Parameter Description
-----|:--------
editorInfo | InputMethodService life cycle parameter
restarting | InputMethodService life cycle parameter

#### onFinishInputView
```Java
public void onFinishInputView (final boolean finishingInput)
```
Respond to InputMethodService lifecycle.

Parameter | Parameter Description
-----|:--------
finishingInput | InputMethodService life cycle parameter

#### onUpdateSelection
```Java
public void onUpdateSelection (final int newSelStart, final int newSelEnd)
```
呼应InputMethodService生命周期。

Parameter | Parameter Description
-----|:--------
newSelStart | InputMethodService life cycle parameter
newSelEnd | InputMethodService life cycle parameter

#### onFinishInput

```Java
public void onFinishInput ()
```
Respond to InputMethodService lifecycle.

#### onWindowShown

```Java
public void onWindowShown ()
```
Respond to InputMethodService lifecycle.

#### onWindowHidden

```Java
public void onWindowHidden ()
```
Respond to InputMethodService lifecycle.

#### onDestroy

```Java
public void onDestroy ()
```
Respond to InputMethodService lifecycle.

<br/>

<h3 id="1.3">Language Management</h3>

#### getAvailableIMELanguageList

```Java
public List<IMELanguage> getAvailableIMELanguageList ()
```
Get the language lists that Zengine supports.

Return | Return Descriptions | 
-----|:--------
List<IMELanguage> | Zengine Language List

#### getAddedIMELanguageList

```Java
public List<IMELanguage> getAddedIMELanguageList ()
```
Get the list of added languages.

Return | Return Descriptions | 
-----|:--------
List<IMELanguage> | The list of added languages

#### onIMELanguageChanged

```Java
public void onIMELanguageChanged (IMELanguage language)
```
Switch Language.

Parameter | Parameter Description
-----|:--------
Language | The language to switch
 
#### addIMELanguage

```Java
public void addIMELanguage (IMELanguage language)
```
Add Language. 

Parameter | Parameter Description
-----|:--------
Language | The language needed to switch
 
#### removeIMELanguage

```Java
public void removeIMELanguage (IMELanguage language)
```
Remove Language.

Parameter | Parameter Description
-----|:--------
language | Language to be removed
 
#### convertToInputMethodSubtype

```Java
public InputMethodSubtype convertToInputMethodSubtype (IMELanguage language)
```
Convert IMELanguage to InputMethodSubtype.

Parameter | Parameter Description
-----|:--------
Language | IMELanguage to be converted to InputMethodSubtype

Return | Return Description | 
-----|:--------
InputMethodSubtype | Conversion result

#### convertToIMELanguage

```Java
public IMELanguage convertToIMELanguage (InputMethodSubtype inputMethodSubtype)
```
Convert InputMethodSubtype to SubtypeIME.

Parameter | Parameter Description
-----|:--------
inputMethodSubtype | IMELanguage to be converted to InputMethodSubtype

Return | Return Descriptions | 
-----|:--------
IMELanguage | Conversion result

#### obtainFuelGetter

```Java
public DictionaryFacilitator.FuelGetter obtainDictionaryGetter ()
```
Obtain Dictionary Loading List.

Return | Return Description | 
-----|:--------
DictionaryFacilitator.FuelGetter |  | 

<br/>

<h3 id="1.4">Dictionary Management</h3>

#### downloadDictionary

```Java
public void downloadDictionary ()
```
Download Dictionary of Added Language.

#### queryLocaleDownloading

```Java
public boolean queryLocaleDownloading (String locale)
```
Check on the current status of dictionary, whether downloading or not.

Parameter | Parameter Description
-----|:--------
locale | Need to query locale of the dictionary 

Return | Return Descriptions | 
-----|:--------
boolean | result

#### getCurrentDictionaryList

```Java
public List<DictionaryItem> getCurrentDictionaryList ()
```
Query the list of current downloadable dictionaries.

Return | Return Descriptions | 
-----|:--------
boolean | result

#### enableMobileDictionaryDownload

```Java
public void enableMobileDictionaryDownload (boolean enable)
```
Switch to download dictionary when in non-wifi network status

Parameter | Parameter Description
-----|:--------
enable | Whether to allow dictionary downloading when in non-wifi network status, false for download mode off, true for download mode on.

#### registerDictionaryDownloadListener

```Java
public void registerDictionaryDownloadListener (DictionaryListener listener)
```
Register the listener to see whether the download is completed.

Parameter | Parameter Description
-----|:--------
listener | The interface to monitor the download status.


#### unregisterDictionaryDownloadListener

```Java
public void unregisterDictionaryDownloadListener ()
```
Cancel the register of interface for the listerner.

#### onLayoutChanged

```Java
public void onLayoutChanged (IMELanguage imeLanguage,String newLayout)
```
Switch layout of the language.

Parameter | Parameter Description
-----|:--------
imeLanguage | the language that needs to switch layout to use
newLayout | the layout to switch to  

#### obtainLayoutList

```Java
public List<String> obtainLayoutList (IMELanguage imeLanguage)
```
Get all the layouts of the language.

Parameter | Parameter Description
-----|:--------
imeLanguage | the language that needs to get list of layouts

Return | Return Description | 
-----|:--------
List<String> | Layout List

#### enableDictionaryAutoDownload

```Java
public void enableDictionaryAutoDownload (boolean enableAutoDownload)
```
The control to trigger the switch for dictionary auto-downloading.

Parameter | Parameter Description
-----|:--------
enableAutoDownload | Whether to turn on dictionary auto-downloading, false to turn off, true to turn on

<br/>

<h3 id="1.5">Theme Setting</h3>

#### addExternalThemes

```Java
public void addExternalThemes (Context context, ExternalThemeInfo infos)
```
Add Theme

Parameter | Parameter Description
-----|:--------
context | ApplicationContext info | Set theme information via ExternalThemeInfo.Builder

#### deleteExternalThemes

```Java
public void deleteExternalThemes (Context context, ExternalThemeInfo infos)
```
Delete Theme

Parameter | Parameter Description
-----|:--------
context | ApplicationContext info | Theme information to be deleted

#### getExternalThemes

```Java
public ArrayList<ExternalThemeInfo> getExternalThemes (Context context)
```
Get all the theme information.

Parameter | Parameter Descriptions
-----|:--------
context | ApplicationContext of the application

Return | Return Description | 
-----|:--------
boolean | All the theme information

#### loadTheme

```Java
public void loadTheme (Context context, String externalId)
```
Apply Theme.

Parameter | Parameter Description
-----|:--------
context | ApplicationContext of the application
externalId | Create theme id

<br/>

<h3 id="1.6">Other Callback Settings</h3>

#### setKeyboardActionCallback

```Java
public void setKeyboardActionCallback (IKeyboardActionCallback keyboardActionCallback)
```
Observe the callback of KeyboardAction related behavior.

Parameter | Parameter Descriptions
-----|:--------
keyboardActionCallback | IKeyboardActionCallback implementation

#### setKeyboardSpecificEventCallback

```Java
public void setKeyboardSpecificEventCallback (IKeyboardSpecificEventCallback keyboardSpecificEventCallback)
```
Observe the callback about keyboard special behavior and related event.

Parameter | Parameter Descriptions
-----|:--------
keyboardSpecificEventCallback | IKeyboardSpecificEventCallback implementation


<br/>

<h3 id="1.7">Custom Function Key</h3>

#### setKeyboardSpecificEventCallback

```Java
public void addCustomFunctionalKeyToLeftOfSpace (CustomFunctionalKeyInfo customFunctionalKeyInfo)
```
Add a custom function key to the left of the spacebar.

Parameter | Parameter Descriptions
-----|:--------
customFunctionalKeyInfo | Customize the key of function key

#### setKeyboardSpecificEventCallback

```Java
public void addCustomFunctionalKeyToRightOfSpace (CustomFunctionalKeyInfo customFunctionalKeyInfo)
```
Add a custom function key to the right of the spacebar.

Parameter | Parameter Description
-----|:--------
customFunctionalKeyInfo | Customize the message of function key

<br/>

<h2 id="2">Callback</h2>

<br/>

<h3 id="2.1">LanguageCallback</h3>

Purpose : Language Switching

#### onIMELanguageChanged

```Java
public void onIMELanguageChanged (InputMethodSubtype subtype)
```
Callback for language switching.

Parameter | Patameter Description
-----|:--------
subtype | Language needs to switch to

<br/>

<h3 
id="2.2">IKeyboardActionCallback</h3>

Purpose : Keyboard Event Callback

#### onDisplayEmojiKeyboard

```Java
public boolean onDisplayEmojiKeyboard ()
```
Event of hook switch emoji keyboard.

Return | Return Descriptions | 
-----|:--------
boolean | Whether to block the original event

#### onKeyboardTypeChange

```Java
public void onKeyboardTypeChange (int keyboardType)
```
Callbacks for alphabet keyboards, emoji keyboard and symbol keyboard switches.

Parameter | Parameter Description
-----|:--------
keyboardType | Types of keyboard: <br/> 1. IKeyboardActionCallback.ALPHA_KEYBOARD is alphabet keyboard <br/>  2. IKeyboardActionCallback.EMOJI_KEYBOARDemoji is emoji keyboard  <br/> 3. IKeyboardActionCallback.SYMBOL_KEYBOARD is symbol keyboard

<br/>

<h3 id="2.3">KeyboardSwitcherListener</h3>

Purpose: Keyboard Switching Callback

#### stopShowingInputView

```Java
public void stopShowingInputView ()
```
Concatenate stopShowingInputView() in AOSP。

#### startShowingInputView

```Java
public void startShowingInputView ()
```
Concatenate startShowingInputView()in AOSP。

#### shouldShowLanguageSwitchKey

```Java
public boolean shouldShowLanguageSwitchKey ()
```
Concatenate shouldShowLanguageSwitchKey() in AOSP。

Return | Return Description | 
-----|:--------
boolean | Whether to display the language switch button

<br/>

<h3 id="2.4">ImeUiHandlerInterface</h3>

Purpose: for the interaction of AOSP LatinIME.UIHhandler

#### showGesturePreviewAndSuggestionStrip

```Java
public void showGesturePreviewAndSuggestionStrip (SuggestedWords suggestedWordsToShowSuggestions, boolean isTailBatchInput)
```
Concatenate the method with the same name in AOSP LatinIME.UIHhandler.

Parameter | Parameter Description
-----|:--------
suggestedWordsToShowSuggestions | Same parameters in the method with the same name in AOSP LatinIME.UIHhandler
isTailBatchInput | Same parameters in the  method with the same name in AOSP LatinIME.UIHhandler

#### showTailBatchInputResult

```Java
public void showTailBatchInputResult (SuggestedWords suggestedWordsToShowSuggestions)
```
Concatenate the method with the same name in AOSP LatinIME.UIHhandler.

Parameter | Parameter Description
-----|:--------
suggestedWordsToShowSuggestions | Same parameters in the method with the same name in AOSP LatinIME.UIHhandler

#### postUpdateSuggestionStrip

```Java
public void postUpdateSuggestionStrip (int inputStyleTyping)
```
Concatenate the method with the same name in AOSP LatinIME.UIHhandler.

Parameter | Parameter Description
-----|:--------
inputStyleTyping | Same parameters in the method with the same name in AOSP LatinIME.UIHhandler

#### postResumeSuggestions

```Java
public void postResumeSuggestions (boolean value)
```
Concatenate AOSP LatinIME.UIHhandler with the same method with the same name.

Parameter | Parameter Description
-----|:--------
value | Same as the method with the same name in AOSP LatinIME.UIHhandler

#### hasPendingUpdateSuggestions

```Java
public boolean hasPendingUpdateSuggestions ()
```
Concatenate the method with the same name in AOSP LatinIME.UIHhandler.

Return | Return Description | 
-----|:--------
boolean | returns the same within the method with the same name in AOSP LatinIME.UIHhandler

#### cancelUpdateSuggestionStrip

```Java
public boolean cancelUpdateSuggestionStrip ()
```
Concatenate the method with the same name in AOSP LatinIME.UIHhandler.

#### postResetCaches

```Java
public void postResetCaches (boolean tryResumeSuggestions, int remainingTries)
```
Concatenate the method with the same name in AOSP LatinIME.UIHhandler.

Parameter | Parameter Description
-----|:--------
tryResumeSuggestions | Same parameters in the method with the same name in AOSP LatinIME.UIHhandler
remainingTries | Same parameters in the method with the same name in AOSP LatinIME.UIHhandler

#### onStartInputView

```Java
public void onStartInputView (EditorInfo editorInfo, boolean restarting)
```
Concatenate the method with the same name in AOSP LatinIME.UIHhandler.

Parameter | Parameter Description
-----|:--------
editorInfo | Same parameters in the method with the same name in AOSP LatinIME.UIHhandler
Restarting | Same parameters in the method with the same name in AOSP LatinIME.UIHhandler

#### onFinishInputView

```Java
public void onFinishInputView (boolean restarting)
```
Concatenate the method with the same name in AOSP LatinIME.UIHhandler.

Parameter | Parameter Description
-----|:--------
restarting | Same parameters in the method with the same name in AOSP LatinIME.UIHhandler

#### showSuggestionStrip

```Java
public void showSuggestionStrip (SuggestedWords suggestedWords)
```
Concatenate the method with the same name in AOSP LatinIME.UIHhandler.

Parameter | Parameter Description
-----|:--------
suggestedWords | Concatenate the method with the same name as in AOSP LatinIME.UIHhandler.

<br/>

<h3 id="2.5">ImsInterface</h3>

Purpose: For the interaction with part of AOSP LatinIME features

#### getIME
```Java
public InputMethodService getIME ()
```
Return the instance of application's InputMethodService .

Return | Return Description | 
-----|:--------
InputMethodService | InputMethodService instance

#### updateStateAfterInputTransaction

```Java
public void updateStateAfterInputTransaction (InputTransaction completeInputTransaction)
```
Concatenate the method with the same name in AOSP LatinIME; need to be changed to public.

Parameter | Parameter Description
-----|:--------
completeInputTransaction | Same parameter in the method with the same name in AOSP LatinIME.UIHhandler

#### switchToNextSubtype

```Java
public void switchToNextSubtype ()
```
Concatenate the method with the same name in AOSP LatinIME.

#### getCoordinatesForCurrentKeyboard

```Java
public int[] getCoordinatesForCurrentKeyboard (int[] codePoints)
```
Concatenate with the method with the same name in AOSP LatinIME.

Parameter | Parameter Description
-----|:--------
codePoints | Same parameters in the method with the same name in AOSP LatinIME.UIHhandler

Return | Return Description | 
-----|:--------
Int[] | returns the same within the method with the same name in AOSP LatinIME.UIHhandler

#### displaySettingsDialog

```Java
public void displaySettingsDialog ()
```
Concatenate the method with the same name in AOSP LatinIME.

#### setNeutralSuggestionStrip

```Java
public void setNeutralSuggestionStrip ()
```
Concatenate the method with the same name in AOSP LatinIME; handle the Suggestion to show related operations.

#### setNeutralSuggestionStrip

```Java
public void showSuggestionStrip (SuggestedWords suggestedWords)
```
Concatenate the method with the same name in AOSP LatinIME; handles the Suggestion to show related operations.

Parameter | Parameter Description
-----|:--------
suggestedWords | Same parameters in the method with the same name as in AOSP LatinIME.UIHhandler

<br/>

<h3 id="2.6">IUserInputCallback</h3>

Purpose: callback of user's input.

#### onUserTyping

```Java
public void onUserTyping (String wordComposing)
```
Returns the word that user is composing.

Parameter | Parameter Description
-----|:--------
wordcomposing | The word currently being entered

#### onUserTyped

```Java
public void onUserTyped (String text)
```
Returns the word on the user's screen.

Parameter | Parameter Description
-----|:--------
wordcomposing | The word that user finished composing.

#### onTextChanged

```Java
public void onTextChanged ()
```
Callback when user change the content in composing. 

<br/>

<h3 id="2.7">SuggestionStripViewListener</h3>

Purpose: operation on suggested word entry.

#### pickSuggestionManually

```Java
public void pickSuggestionManually(SuggestedWordInfo word)
```
User needs to pass the corresponding information of SuggestedWordInfo when clicking on suggested word.

Parameter | Parameter Description
-----|:--------
word | SuggestedWordInfo Information

<br/>

<h3 id="2.8">ChineseSuggestStripViewListener</h3>

Purpose: operation on Chinese suggested word entry

#### pickSuggestionManually

```Java
public void pickSuggestionManually(int suggestionId)
```
User needs to pass the index of the corresponding item in all suggested words when clicking the suggested word.

Parameter | Parameter Description
-----|:--------
suggestionId | Click on the index of the item in all suggested words

<br/>

<h2 id="3">Message</h2>

<br/>

<h3 id="3.1">ExternalThemeInfo</h3>

Purpose : data structure required when adding a theme

#### Builder

```Java
public Builder (String externalId, String themeName)
```
The constructor of ExternalThemeInfo.

Parameter | Parameter Description
-----|:--------
externalId | The user's own unique identification of the subject; it can not be empty
themeName | The name of the theme which can not be empty

#### setThemePreviewImage

```Java
public Builder setThemePreviewImage (Drawable themePreviewImage)
```
The preview image when setting the theme.

Parameter | Parameter Description
-----|:--------
themePreviewImage | The preview image of the theme.

Return | Return Description | 
-----|:--------
Builder | Returned Builder 

#### setThemePreviewImageWithBorder

```Java
public Builder setThemePreviewImageWithBorder (Drawable themePreviewImageWithBorder)
```
The preview image with border when setting the theme.

Parameter | Parameter Description
-----|:--------
themePreviewImageWithBorder | The preview image with border of the theme.

Return | Return Description | 
-----|:--------
Builder | Returned Builder 

#### setKeyBackground
```Java
public Builder setKeyBackground (Drawable keyBackground)
```
Set the background of the keys.

Parameter | Parameter Description
-----|:--------
keyBackground | The background of the keys

Return | Return Description | 
-----|:--------
Builder | Returned Builder 

#### setKeyBackgroundWithBorder
```Java
public Builder setKeyBackgroundWithBorder (Drawable keyBackgroundWithBorder)
```
Set the background with border of the keys.

Parameter | Parameter Description
-----|:--------
keyBackgroundWithBorder | The background with border of the keys

Return | Return Description | 
-----|:--------
Builder | Returned Builder 

#### setFunctionKeyBackground

```Java
public Builder setFunctionKeyBackground (Drawable functionKeyBackground)
```
Set the background of the function keys.

Parameter | Parameter Description
-----|:--------
functionKeyBackground | The background of the function keys<br><br>[zengine v1.3.11]<br>Support setting the StateListDrawable to set some functional keys with R.attr.state_functional_key_shift, R.attr.state_functional_key_go_to_number, R.attr.state_functional_key_delete and R.attr.state_functional_key_enter.

Return | Return Description | 
-----|:--------
Builder | Returned Builder

#### setFunctionKeyBackgroundWithBorder

```Java
public Builder setFunctionKeyBackgroundWithBorder (Drawable functionKeyBackgroundWithBorder)
```
Set the background with border of the function keys.

Parameter | Parameter Description
-----|:--------
functionKeyBackgroundWithBorder | The background with border of the function keys<br><br>[zengine v1.3.11]<br>Support setting the StateListDrawable to set some functional keys with R.attr.state_functional_key_shift, R.attr.state_functional_key_go_to_number, R.attr.state_functional_key_delete and R.attr.state_functional_key_enter.

Return | Return Description | 
-----|:--------
Builder | Returned Builder

#### setSpacebarBackground

```Java
public Builder setSpacebarBackground (Drawable spacebarBackground)
```
Set background of spacebar.

Parameter | Parameter Description
-----|:--------
spacebarBackground | The background of spacebar

Return | Return Description | 
-----|:--------
Builder | Returned Builder

#### setSpacebarBackgroundWithBorder

```Java
public Builder setSpacebarBackgroundWithBorder (Drawable spacebarBackgroundWithBorder)
```
Set background with border of spacebar.

Parameter | Parameter Description
-----|:--------
spacebarBackgroundWithBorder | The background with border of spacebar

Return | Return Description | 
-----|:--------
Builder | Returned Builder

#### setKeyPreviewBackground

```Java
public Builder setKeyPreviewBackground (Drawable keyPreviewBackground)
```
Set the background of the preview popup of keys.

Parameter | Parameter Description
-----|:--------
keyPreviewBackground | The background of the preview popup of the keys

Return | Return Description | 
-----|:--------
Builder | Returned Builder

#### setMoreKeysKeyboardBackground

```Java
public Builder setMoreKeysKeyboardBackground (Drawable moreKeysKeyboardBackground)
```
Set the background of keyboard when long pressing the key for more keys.

Parameter | Parameter Description
-----|:--------
moreKeysKeyboardBackground | The background of keyboard when long pressing the key for more keys.

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setMoreKeysKeyBackground

```Java
public Builder setMoreKeysKeyBackground (Drawable moreKeysKeyBackground)
```
Set the background of the more keys keyboard that pops up by pressing the normal key.

Parameter | Parameter Description
-----|:--------
moreKeysKeyBackground | The background of key when long pressing the key.

Return | Return Description | 
-----|:--------
Builder | Returned Builder

#### setMoreKeysKeyBackgroundWithBorder

```Java
public Builder setMoreKeysKeyBackgroundWithBorder (Drawable moreKeysKeyBackgroundWithBorder)
```
Set the background with border of the more keys keyboard that pops up by pressing the normal key.

Parameter | Parameter Description
-----|:--------
moreKeysKeyBackgroundWithBorder | The background with border of key when long pressing the key.

Return | Return Description | 
-----|:--------
Builder | Returned Builder

#### setTypeface

```Java
public Builder setTypeface (Typeface typeface)
```
Set the font of the keyboard.

Parameter | Parameter Description
-----|:--------
typeface | The font of the keyboard

Return | Return Description | 
-----|:--------
Builder | Returned Builder

#### setKeyTextColor

```Java
public Builder setKeyTextColor (String keyTextColor)
```
Set the text color of the key.

Parameter | Parameter Description
-----|:--------
keyTextColor | The text color of the key

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setKeyTextInactivatedColor

```Java
public Builder setKeyTextInactivatedColor (String keyTextInactivatedColor)
```
Set the text color of the inactive key.

Parameter | Parameter Description
-----|:--------
keyTextInactivatedColor | The text color of the inactive key

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setFunctionKeyTextColor

```Java
public Builder setFunctionKeyTextColor (String functionKeyTextColor)
```
Set the text color of the function key.

Parameter | Parameter Description
-----|:--------
functionKeyTextColor | The text color of the function key

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setKeyHintLetterColor

```Java
public Builder setKeyHintLetterColor (String keyHintLetterColor)
```
Set the color of the Hint Letter on the key.

Parameter | Parameter Description
-----|:--------
keyHintLetterColor | The color of the Hint Letter on the key

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setKeyShiftedLetterHintActivatedColor

```Java
public Builder setSpacebarBackground (String keyShiftedLetterHintActivatedColor)
```
Set the color of Shifted Letter Hint when it's activated. 

Parameter | Parameter Description
-----|:--------
keyShiftedLetterHintActivatedColor | The color of Shifted Letter Hint when it's activated.

Return | Return Description | 
-----|:--------
Builder | Returned Builder

#### setKeyShiftedLetterHintActivatedColor

```Java
public Builder setKeyShiftedLetterHintActivatedColor (String keyShiftedLetterHintActivatedColor)
```
Set the color of Shifted Letter Hint when it's inactive.

Parameter | Parameter Description
-----|:--------
keyShiftedLetterHintActivatedColor | The color of Shifted Letter Hint when it's inactive. 

Return | Return Description| 
-----|:--------
Builder | Returned Builder

#### setKeyPreviewTextColor

```Java
public Builder setKeyPreviewTextColor (String keyPreviewTextColor)
```
Set the text color of preview popup key

Parameter | Parameter Description
-----|:--------
keyPreviewTextColor | The text color of the preview popup key

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setKeyBorderColor

```Java
public Builder setKeyBorderColor (String keyBorderColor)
```
Set the color of the key Border.

Parameter | Parameter Description
-----|:--------
keyBorderColor | The color of the key Border

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setLanguageOnSpacebarTextColor

```Java
public Builder setLanguageOnSpacebarTextColor (String languageOnSpacebarTextColor)
```
Set the text color of language on spacebar.

Parameter | Parameter Description
-----|:--------
languageOnSpacebarTextColor | The text color of language on spacebar

Return | Return Description | 
-----|:--------
Builder | Returned Builder

#### setGestureTrailColor

```Java
public Builder setGestureTrailColor (String gestureTrailColor)
```
Set the color of the sliding track

Parameter | Parameter Description
-----|:--------
gestureTrailColor | The color of the sliding track

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setEmojiCategoryPageIndicatorBackground

```Java
public Builder setEmojiCategoryPageIndicatorBackground (String emojiCategoryPageIndicatorBackgroundColor)
```
Set the background color of TabLayout under the Emoji page 

Parameter | Parameter Description
-----|:--------
emojiCategoryPageIndicatorBackgroundColor | The background color of TabLayout under the Emoji page 

Return | Return Description | 
-----|:--------
Builder | Returned Builder

#### setKeyLetterRatio

```Java
public Builder setKeyLetterRatio (@FloatRange(from = 0.0, to = 1.0) float keyLetterRatio)
```
Set text ratio of Letter.

Parameter | Parameter Description
-----|:--------
keyLetterRatio | Letter's text ratio, Text size = magnification * key height

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setKeyLabelRatio

```Java
public Builder setKeyLabelRatio (@FloatRange(from = 0.0, to = 1.0) float keyLabelRatio)
```
Set Label Ratio.

Parameter | Parameter Description
-----|:--------
keyLabelRatio | Label text ratio, Text size = ratio*key height

Return | Return Description | 
-----|:--------
Builder | Returned Builder

#### setKeyPreviewTextRatio

```Java
public Builder setKeyPreviewTextRatio (@FloatRange(from = 0.0, to = 1.0) float keyPreviewTextRatio)
```
Set the text magnification of the preview popup of the key.

Parameter | Parameter Description
-----|:--------
keyPreviewTextRatio | preview popup text rate, Text size = magnification * key height

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setShiftKeyIcon

```Java
public Builder setShiftKeyIcon (Drawable shiftKeyIcon)
```
Set icon of shift key.

Parameter | Parameter Description
-----|:--------
shiftKeyIcon | The icon of shift button

Return | Return Description | 
-----|:--------
Builder | Returned Builder

#### setShiftKeyShiftedIcon

```Java
public Builder setShiftKeyShiftedIcon (Drawable shiftKeyShiftedIcon)
```
Set the icon of shift key when it's shifted. 

Parameter | Parameter Description
-----|:--------
shiftKeyShiftedIcon | The icon when the shift key is shifted

Return | Return Description | 
-----|:--------
Builder | Returned Builder

#### setDeleteKeyIcon

```Java
public Builder setDeleteKeyIcon (Drawable deleteKeyIcon)
```
Set the icon of delete key.

Parameter | Parameter Description
-----|:--------
deleteKeyIcon | The icon of delete key

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setSettingsKeyIcon

```Java
public Builder setSettingsKeyIcon (Drawable settingsKeyIcon)
```
Set the icon for the settings key.

Parameter | Parameter Description
-----|:--------
settingsKeyIcon | The icon of settings key

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setSpacesKeyIcon

```Java
public Builder setSpacesKeyIcon (Drawable spacesKeyIcon)
```
Set the icon for the spaces key.

Parameter | Parameter Description
-----|:--------
spacesKeyIcon | the icon of spaces key

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setEnterKeyIcon

```Java
public Builder setEnterKeyIcon (Drawable enterKeyIcon)
```
Set the icon of enter key.

Parameter | Parameter Description
-----|:--------
enterKeyIcon | The icon of enter key

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setGoKeyIcon

```Java
public Builder setGoKeyIcon (Drawable goKeyIcon)
```
Set the icon for go key.

Parameter | Parameter Description
-----|:--------
goKeyIcon | The icon of go key

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setSendKeyIcon

```Java
public Builder setSendKeyIcon (Drawable sendKeyIcon)
```
Set the icon for send key.

Parameter | Parameter Description
-----|:--------
sendKeyIcon | The icon of send key

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setNextKeyIcon

```Java
public Builder setNextKeyIcon (Drawable nextKeyIcon)
```
Set the icon for next key.

Parameter | Parameter Description
-----|:--------
nextKeyIcon | The icon of next key

Return | Return Description | 
-----|:--------
Builder | Returned Builder

#### setDoneKeyIcon

```Java
public Builder setDoneKeyIcon (Drawable doneKeyIcon)
```
Set the icon for done key.

Parameter | Parameter Description
-----|:--------
doneKeyIcon | The icon of done key.

Return | Return Description | 
-----|:--------
Builder | Returned Builder

#### setPreviousKeyIcon

```Java
public Builder setPreviousKeyIcon (Drawable previousKeyIcon)
```
Set the icon for previous key.

Parameter | Parameter Description
-----|:--------
previousKeyIcon | The icon of previous key

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setTabKeyIcon

```Java
public Builder setTabKeyIcon (Drawable tabKeyIcon)
```
Set the icon for tab key.

Parameter | Parameter Description
-----|:--------
tabKeyIcon | The icon of tab key

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setShortcutKeyIcon

```Java
public Builder setShortcutKeyIcon (Drawable shortcutKeyIcon)
```
Set the icon for shortcut key.

Parameter | Parameter Description
-----|:--------
shortcutKeyIcon | The icon of shortcut key

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setSpaceKeyForNumberLayoutIcon

```Java
public Builder setSpaceKeyForNumberLayoutIcon (Drawable spaceKeyForNumberLayoutIcon)
```
Set the space key icon for number layout. 

Parameter | Parameter Description
-----|:--------
spaceKeyForNumberLayoutIcon | the space key icon for number layout

Return | Return Description | 
-----|:--------
Builder | Returned Builder

#### setLanguageSwitchKeyIcon

```Java
public Builder setLanguageSwitchKeyIcon (Drawable languageSwitchKeyIcon)
```
Set the icon for the language switch key.

Parameter | Parameter Description
-----|:--------
languageSwitchKeyIcon | the icon of the language switch key

Return | Return Description | 
-----|:--------
Builder | Returned Builder

#### setZwnjKeyIcon

```Java
public Builder setZwnjKeyIcon (Drawable zwnjKeyIcon)
```
Set the icon of ZWNJ key.

Parameter | Parameter Description
-----|:--------
zwnjKeyIcon | the icon of ZWNJ key

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setEmojiActionKeyIcon

```Java
public Builder setEmojiActionKeyIcon (Drawable emojiActionKeyIcon)
```
Set icon for emoji action key.

Parameter | Parameter Description
-----|:--------
emojiActionKeyIcon | The icon of emoji action key

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setEmojiNormalKeyIcon

```Java
public Builder setEmojiNormalKeyIcon (Drawable emojiNormalKeyIcon)
```
Set the icon of emoji normal key.

Parameter | Parameter Description
-----|:--------
emojiNormalKeyIcon | The icon of emoji normal key

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setGestureTrailDrawable

```Java
public Builder setGestureTrailDrawable (Drawable gestureTrailDrawable)
```
Set the icon of the sliding track.

Parameter | Parameter Description
-----|:--------
gestureTrailDrawable | The icon of the sliding track

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setGestureTrailStartWidth

```Java
public Builder setGestureTrailStartWidth (float gestureTrailStartWidth)
```
Set the starting width of the sliding track.

Parameter | Parameter Description
-----|:--------
gestureTrailStartWidth | the starting width of the sliding track

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setGestureTrailEndWidth

```Java
public Builder setGestureTrailEndWidth (float gestureTrailEndWidth)
```
Set the end width of the sliding track.

Parameter | Parameter Description
-----|:--------
gestureTrailEndWidth | the end width of the sliding track
Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setGestureTrailBodyRatio

```Java
public Builder setGestureTrailBodyRatio (float gestureTrailBodyRatio)
```
Sets the starting percentage of the sliding track. The range of values is [1, 100] and the default is 100.

Parameter | Parameter Description
-----|:--------
gestureTrailBodyRatio | the starting percentage of the sliding track

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setGestureTrailFadeoutStartDelay

```Java
public Builder setGestureTrailFadeoutStartDelay (float gestureTrailFadeoutStartDelay)
```
Set the length of time before the sliding track fades out.

Parameter | Parameter Description
-----|:--------
gestureTrailFadeoutStartDelay | the length of time before the sliding track fades out

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setGestureTrailFadeoutDuration

```Java
public Builder setGestureTrailFadeoutDuration (float gestureTrailFadeoutDuration)
```
Set the length of time before the sliding track fades out.

Parameter | Parameter Description
-----|:--------
gestureTrailFadeoutDuration | the length of time before the sliding track fades out

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setGestureTrailUpdateInterval

```Java
public Builder setGestureTrailUpdateInterval (float gestureTrailUpdateInterval)
```
Set the FPS of the sliding track, the default is 20.

Parameter | Parameter Description
-----|:--------
gestureTrailUpdateInterval | the FPS of the sliding track

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setSuggestionStripViewBackground

```Java
public Builder setSuggestionStripViewBackground (Drawable suggestionStripViewBackground)
```
Set the background of the list of suggested words.

Parameter | Parameter Description
-----|:--------
suggestionStripViewBackground | the background of the list of candidate words

Return | Return Description | 
-----|:--------
Builder | Returned Builder

#### setSuggestionStripDivider

```Java
public Builder setSuggestionStripDivider (Drawable suggestionStripDivider)
```
Set the separator line between suggested words on the bar.

Parameter | Parameter Description
-----|:--------
suggestionStripDivider | the separator line between suggested words

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setValidTypedWordColor

```Java
public Builder setValidTypedWordColor (String validTypedWordColor)
```
Set the text color of the suggesdted word of valid type.

Parameter | Parameter Description
-----|:--------
validTypedWordColor | the text color of the suggested word of valid type

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setTypedWordColor

```Java
public Builder setTypedWordColor (String typedWordColor)
```
Set the text color of typed suggested words。

Parameter | Parameter Description
-----|:--------
typedWordColor | the text color of typed suggested words

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setAutoCorrectColor

```Java
public Builder setAutoCorrectColor (String autoCorrectColor)
```
Set the text color of the suggested words for auto-correction.

Parameter | Parameter Description
-----|:--------
autoCorrectColor | the text color of suggested words for auto-correction

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setSuggestedColor

```Java
public Builder setSuggestedColor (String suggestedColor)
```
Set the text color of the candidate words.

Parameter | Parameter Description
-----|:--------
autoCorrectColor | the text color of the candidate words

Return | Return Description | 
-----|:--------
Builder | Returned Builder

#### setSuggestedWordSelectedBackground

```Java
public Builder setSuggestedWordSelectedBackground (Drawable suggestedWordSelectedBackground)
```
Set the background when the candidate word is selected.

Parameter | Parameter Description
-----|:--------
suggestedWordSelectedBackground |the background when the candidate word is selected

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setKeyboardLottieBackground

```Java
public Builder setKeyboardLottieBackground (LottieDrawableInfo keyboardLottieBackground)
```
Use the LottieDrawable to set the background of the keyboard.

Parameter | Parameter Description
-----|:--------
keyboardLottieBackground | the background of the keyboard

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setKeyLottieBackground

```Java
public Builder setKeyLottieBackground (LottieDrawableInfo... keyLottieBackground)
```
Use LottieDrawable to set the background of key. The first parameter is the background of keyboard, and the second parameter is the background when the key is pressed. Both can be null.

Parameter | Parameter Description
-----|:--------
keyLottieBackground | the background of the key

Return | Return Descriptions | 
-----|:--------
Builder |Returned Builder


#### setKeyLottieBackgroundWithBorder

```Java
public Builder setKeyLottieBackgroundWithBorder (LottieDrawableInfo... keyLottieBackgroundWithBorder)
```
Use LottieDrawable to set the background with border of key. The first parameter is the background of keyboard, and the second parameter is the background when the key is pressed. Both can be null.

Parameter | Parameter Description
-----|:--------
keyLottieBackgroundWithBorder | the background with border of the key

Return | Return Descriptions | 
-----|:--------
Builder |Returned Builder

#### setFunctionKeyLottieBackground

```Java
public Builder setFunctionKeyLottieBackground (LottieDrawableInfo... functionKeyLottieBackground)
```
Use the LottieDrawable to set the background of the function keys. The first parameter is the background in the normal state, and the second parameter is the background when the key is pressed. Both can be null.

Parameter | Parameter Description
-----|:--------
functionKeyLottieBackground | the background of the function keys

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setFunctionKeyLottieBackgroundWithBorder

```Java
public Builder setFunctionKeyLottieBackgroundWithBorder (LottieDrawableInfo... functionKeyLottieBackgroundWithBorder)
```
Use the LottieDrawable to set the background with border of the function keys. The first parameter is the background in the normal state, and the second parameter is the background when the key is pressed. Both can be null.

Parameter | Parameter Description
-----|:--------
functionKeyLottieBackgroundWithBorder | the background with border of the function keys

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setSpacebarLottieBackground

```Java
public Builder setSpacebarLottieBackground (LottieDrawableInfo... spacebarLottieBackground)
```
Use the LottieDrawable to set the background of the blank key. The first parameter is the background under normal state, and the second parameter is the background when the key is pressed. Both can be null.

Parameter | Parameter Description
-----|:--------
spacebarLottieBackground | the background of the blank key

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setSpacebarLottieBackgroundWithBorder

```Java
public Builder setSpacebarLottieBackgroundWithBorder (LottieDrawableInfo... spacebarLottieBackgroundWithBorder)
```
Use the LottieDrawable to set the background with border of the blank key. The first parameter is the background under normal state, and the second parameter is the background when the key is pressed. Both can be null.

Parameter | Parameter Description
-----|:--------
spacebarLottieBackgroundWithBorder | the background with border of the blank key

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setKeyPreviewLottieBackground

```Java
public Builder setKeyPreviewLottieBackground (LottieDrawableInfo keyPreviewLottieBackground)
```
Use the LottieDrawable to set the preview background for the key.

Parameter | Parameter Description
-----|:--------
keyPreviewLottieBackground | the preview background of the key

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder



#### setMoreKeysKeyboardLottieBackground

```Java
public Builder setMoreKeysKeyboardLottieBackground (LottieDrawableInfo moreKeysKeyboardLottieBackground)
```
Set the background of keyboard of more keys.

Parameter | Parameter Description
-----|:--------
moreKeysKeyboardLottieBackground | the background of keyboard of more keys

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setMoreKeysKeyLottieBackground

```Java
public Builder setMoreKeysKeyLottieBackground (LottieDrawableInfo... moreKeysKeyLottieBackground)
```
Set the background of the more keys keyboard. The first parameter is the background under normal state, and the second parameter is the background when the key is pressed. Both can be null.

Parameter | Parameter Description
-----|:--------
moreKeysKeyLottieBackground | the background of the more keys keyboard

Return | Return Description | 
-----|:--------
Builder | Returned Builder

#### setMoreKeysKeyLottieBackgroundWithBorder

```Java
public Builder setMoreKeysKeyLottieBackgroundWithBorder (LottieDrawableInfo... moreKeysKeyLottieBackgroundWithBorder)
```
Set the background with border of the more keys keyboard. The first parameter is the background under normal state, and the second parameter is the background when the key is pressed. Both can be null.

Parameter | Parameter Description
-----|:--------
moreKeysKeyLottieBackgroundWithBorder | the background with border of the more keys keyboard

Return | Return Description | 
-----|:--------
Builder | Returned Builder

#### setKeyboardClickedEffectLottieDrawable

```Java
public Builder setKeyboardClickedEffectLottieDrawable (LottieDrawableInfo keyboardClickedEffectLottieDrawable)
```
Set clicking effect of keyboard.

Parameter | Parameter Description
-----|:--------
keyboardClickedEffectLottieDrawable | the clicking effect of keyboard 

Return | Return Description | 
-----|:--------
Builder | Returned Builder

#### setCreateKeyboardRenderCallback

```Java
public Builder setCreateKeyboardRenderCallback (CreateRenderCallback<KeyboardRender> createKeyboardRenderCallback)
```
Set the callback that generates the render keyboard Render.

Parameter | Parameter Description
-----|:--------
createKeyboardRenderCallback | Set callback for creating keyboard Render

Return | Return Description | 
-----|:--------
Builder | Returned Builder

#### setCreateGestureTrailRenderCallback

```Java
public Builder setCreateGestureTrailRenderCallback (CreateRenderCallback<GestureTrailRender> createGestureTrailRenderCallback)
```
Set the callback for rendering the swipe input path

Parameter | Parameter Description
-----|:--------
createKeyboardRenderCallback | set the callback for rendering the swipe input path

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setChineseSuggestionStripOpenMorePageButton

```Java
public Builder setChineseSuggestionStripOpenMorePageButton(Drawable chineseSuggestionStripOpenMorePageButton) 
```
Open the Drawable for more suggested words page on default Chinese suggestion bar. 

Parameter | Parameter Description
-----|:--------
chineseSuggestionStripOpenMorePageButton | 
The button Drawable for more suggested words  pages

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setChineseSuggestionStripCloseMorePageButton

```Java
public Builder setChineseSuggestionStripOpenMorePageButton(Drawable chineseSuggestionStripCloseMorePageButton) 
```
Close the Drawable for more suggested words page on the default Chinese suggestion bar.

Parameter | Parameter Description
-----|:--------
chineseSuggestionStripCloseMorePageButton | Close more Drawable in candidate page buttons

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setChineseSuggestionMorePageUpEnableButton

```Java
public Builder setChineseSuggestionMorePageUpEnableButton(Drawable chineseSuggestionMorePageUpEnableButton)
```
Set the enable Drawable of the previous page button in more Chinese characters suggestion page.

Parameter | Parameter Description
-----|:--------
chineseSuggestionMorePageUpEnableButton | the enable Drawable of the previous page button

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setChineseSuggestionMorePageUpDisableButton

```Java
public Builder setChineseSuggestionMorePageUpDisableButton(Drawable chineseSuggestionMorePageDownEnableButton)
```
Set the disable Drawable of the previous page button in more Chinese characters suggestion page.

Parameter | Parameter Description
-----|:--------
chineseSuggestionMorePageDownEnableButton | the disable Drawable of the previous page button

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder


#### setChineseSuggestionMorePageDownEnableButton

```Java
public Builder setChineseSuggestionMorePageDownEnableButton(Drawable chineseSuggestionMorePageDownEnableButton)
```
Set the enable Drawable of the next page button for more Chinese characters suggestion page by default.

Parameter | Parameter Description
-----|:--------
chineseSuggestionMorePageDownEnableButton |  The next page button enable Drawable

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setChineseSuggestionMorePageDownDisableButton

```Java
public Builder setChineseSuggestionMorePageDownDisableButton(Drawable chineseSuggestionMorePageDownDisableButton)
```
Set the disable Drawable of the next page button in more Chinese suggestion.

Parameter | Parameter Description
-----|:--------
chineseSuggestionMorePageDownDisableButton | the disable Drawable of the next page button

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder


#### setChineseSuggestionMorePageDeleteButton

```Java
public Builder setChineseSuggestionMorePageDeleteButton(Drawable chineseSuggestionMorePageDownDisableButton)
```
Set Drawable in the delete button for default more Chinese characters suggestion page.

Parameter | Parameter Description
-----|:--------
chineseSuggestionMorePageDownDisableButton | Delete button Drawable

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setChineseSuggestionMorePageResetButton

```Java
public Builder setChineseSuggestionMorePageResetButton(Drawable chineseSuggestionMorePageResetButton)
```
Set Drawable in the reset button for default more Chinese characters suggestion page 

Parameter | Parameter Description
-----|:--------
chineseSuggestionMorePageResetButton | Reset button Drawable

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder


#### setChineseSuggestionMorePageBackground

```Java
public Builder setChineseSuggestionMorePageBackground(Drawable chineseSuggestionMorePageBackground)
```
Set the background of Drawable for default page of more Chinese characters suggestions page.

Parameter | Parameter Description
-----|:--------
chineseSuggestionMorePageBackground | the background of more suggested words

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder

#### setChineseSuggestionComposingViewBackground

```Java
public Builder setChineseSuggestionComposingViewBackground(Drawable chineseSuggestionComposingViewBackground)
```
Set the ComposingView background for the Chinese input.

Parameter | Parameter Description
-----|:--------
chineseSuggestionComposingViewBackground | the ComposingView background

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder


#### setChineseSuggestionComposingTextColor

```Java
public Builder setChineseSuggestionComposingTextColor(String chineseSuggestionComposingTextColor)
```
Set the text color corresponding to the default Chinese input ComposingView.

Parameter | Parameter Description
-----|:--------
chineseSuggestionComposingTextColor | the text color of ComposingView

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder


[zengine v1.3.11]
#### setGesturePreviewColor
```Java
public Builder setGesturePreviewColor (String gesturePreviewColor)
```
Set the gesture preview background color. 

Parameter | Parameter Description
-----|:--------
gesturePreviewColor | gesture preview background color

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder


#### setGesturePreviewTextColor
```Java
public Builder setGesturePreviewTextColor (String gesturePreviewTextColor)
```
Set the gesture preview text color. 

Parameter | Parameter Description
-----|:--------
gesturePreviewTextColor | gesture preview text color

Return | Return Descriptions | 
-----|:--------
Builder | Returned Builder


<br/>


<h3 id="3.2">CustomFunctionalKeyInfo</h3>

Purpose : Data structure required when adding new custom function keys

#### CustomFunctionalKeyInfo

```Java
public CustomFunctionalKeyInfo (String label, CustomFunctionalKeyCallback callback)
```
CustomFunctionalKeyInfo constructor for generating custom function keys with text.

Parameter | Parameter Description
-----|:--------
label | Text on the key
callback | Implementation when a custom function key is triggered

#### CustomFunctionalKeyInfo

```Java
public CustomFunctionalKeyInfo (Drawable icon, CustomFunctionalKeyCallback callback)
```
The constructor of CustomFunctionalKeyInfo, which is used to generate custom function keys with icons.

Parameter | Parameter Description
-----|:--------
icon | icon on the key
callback | Implementation when a custom function key is triggered

#### buildKeyInfoLanguageSwitch

```Java
public static CustomFunctionalKeyInfo buildKeyInfoLanguageSwitch ()
```
Custom a information for the language switch key.

Return | Return Descriptions | 
-----|:--------
CustomFunctionalKeyInfo | Return information for a language switch button

#### buildKeyInfoEmoji

```Java
public static CustomFunctionalKeyInfo buildKeyInfoEmoji ()
```
Generate information for Emoji key.

Return | Return Descriptions | 
-----|:--------
CustomFunctionalKeyInfo | Return information for a Emoji key

#### buildKeyInfoEmoji

```Java
public static CustomFunctionalKeyInfo buildKeyInfoComma ()
```
Generate information for Comma key.

Return | Return Descriptions | 
-----|:--------
CustomFunctionalKeyInfo | Return information for a Comma key

#### buildKeyInfoEmoji

```Java
public static CustomFunctionalKeyInfo buildKeyInfoPeriod ()
```
Generate information for Period key.

Return | Return Descriptions | 
-----|:--------
CustomFunctionalKeyInfo | Return information for Period key

#### addMoreKeyInfo

```Java
public void addMoreKeyInfo (CustomFunctionalKeyInfo... infos)
```
Add more keys to this function key.

Parameter | Parameter Description
-----|:--------
infos | the infomation of more keys

<br/>

<h3 id="3.3">LottieDrawableInfo</h3>

Purpose : Data structure required when adding LottieDrawable

#### LottieDrawableInfo

```Java
public LottieDrawableInfo(CreateLottieTaskCallback createLottieTaskCallback, float scale)
```
The constructor of LottieDrawableInfo, which is used to generate general LottieDrawable information.

Parameter | Parameter Description
-----|:--------
createLottieTaskCallback | Generate an implementation of LottieDrawable
scale | LottieDrawable's ratio, set as 1f in general

#### getLottieDrawableScale

```Java
public float getLottieDrawableScale(float viewWidth, float viewHeight, LottieComposition composition)
```
Get the LottieDrawable ratio, which is used to call setScale(float scale) on the LottieDrawable before canvas rendering the LottieDrawable.

Parameter | Parameter Description
-----|:--------
viewWidth | Render the width of this LottieDrawable
viewHeight | Render the height of this LottieDrawable
composition | LottieDrawable.getLottieComposition()

Return | Return Descriptions | 
-----|:--------
float | LottieDrawable's ratio

#### getLottieDrawableScale

```Java
public float getLottieDrawableScale(LottieComposition composition)
```
To get LottieDrawable's ratio, need to call setScale(float scale) on the LottieDrawable before rendering on the canvas. Must call setViewSize(float viewWidth, float viewHeight) before using it.

Parameter | Parameter Description
-----|:--------
composition | LottieDrawable.getLottieComposition()

Return | Return Description | 
-----|:--------
float | LottieDrawabl's ratio

#### setViewSize

```Java
public void setViewSize(float viewWidth, float viewHeight)
```
set the size of rendering range when rendering on canvase, only then can call
getLottieDrawableScale(LottieComposition composition)。

Parameter | Parameter Description
-----|:--------
viewWidth | The width rendered on canvase
viewHeight | The height rendered on canvas
     
<br/>

<h2 id="4">Rendering</h2>

<br/>

<h3 id="4.1">KeyboardRender</h3>

Purpose : The renderer used to render the keyboard

#### onDrawKeyboardBackground

```Java
public abstract void onDrawKeyboardBackground(@Nonnull Canvas canvas, Drawable background, Drawable stripContainerBackground)
```
In the same Render, to render the keyboard background, the method is the same as in onDrawKeyboardBackground(@Nonnull Canvas canvas, Drawable background, Drawable stripContainerBackground, @Nonnull LottieDrawableInfo lottieDrawableInfo). Only either one of them will be called. 

Parameter | Parameter Description
-----|:--------
canvas | canvas
background | Rendered content
stripContainerBackground | If there is nothing on the suggestion strip container, you can render a basemap at the top of the keyboard.

#### onDrawKeyboardBackground

```Java
public abstract void onDrawKeyboardBackground(@Nonnull Canvas canvas, Drawable background, Drawable stripContainerBackground, @Nonnull LottieDrawableInfo lottieDrawableInfo)
```
In the same Render, there is only one can be called between this method and onDrawKeyboardBackground(@Nonnull Canvas canvas, Drawable background, Drawable stripContainerBackground) when rendering the keyboard background by using LottieDrawable to render.

Parameter | Parameter Description
-----|:--------
canvas | canvas
background | LottieDrawable
stripContainerBackground | If there is nothing on the suggestion strip container, you can render a basemap at the top of the keyboard.
lottieDrawableInfo | The relevant information of LottieDrawable is mainly used to obtain the scale required by LottieDrawable.

#### onDrawKeyBackground

```Java
public abstract void onDrawKeyBackground(int keyboardType, @Nonnull final Key key, int keyStatus,
                                             @Nonnull KeyDrawParams keyDrawParams,
                                             @Nonnull KeyboardDrawParams keyboardDrawParams,
                                             @Nullable Drawable keyBackground,
                                             @Nonnull final Canvas canvas, @Nonnull Paint paint)
```
The background of the render key.

Parameter | Parameter Description
-----|:--------
keyboardType | A value defined in the KeyboardType is used to distinguish the main keyboard, more keys keyboard and emoji keyboard.
key | Key, the key in the native ASOP IME
keyStatus | To define a certain value in KeyStatus, in order to distinguish the status of the key
keyDrawParams | KeyDrawParams,the KeyDrawParams in native ASOP IME
keyboardDrawParams | Needs to pack requires certain values in native AOSP IME and pass to KeyboardRender for use. 
background | Rendered content
canvas | canvas
paint | paint

#### onDrawKeyBackground

```Java
public abstract void onDrawKeyTopVisuals(int keyboardType, @Nonnull final Key key, int keyStatus,
                                             @Nonnull KeyDrawParams keyDrawParams,
                                             @Nonnull KeyboardDrawParams keyboardDrawParams,
                                             @Nonnull final Canvas canvas, @Nonnull Paint paint)
```
The appearance of the render key.

Parameter | Parameter Description
-----|:--------
keyboardType | A value defined in the KeyboardType is used to distinguish the main keyboard, more keys keyboard and emoji keyboard.
key | Key, the key in the native ASOP IME
keyStatus | A value defined in KeyStatus to distinguish the state of the key
keyDrawParams | KeyDrawParams, the KeyDrawParams in the native ASOP IME
keyboardDrawParams | Needs to pack requires certain values in native AOSP IME and pass to KeyboardRender for use. 
canvas | canvas
paint | paint

#### onDrawKeyBackground

```Java
public abstract void (int keyboardType, @Nonnull final Key key, int keyStatus,
                                             @Nonnull KeyDrawParams keyDrawParams,
                                             @Nonnull KeyboardDrawParams keyboardDrawParams,
                                             @Nonnull final Canvas canvas, @Nonnull Paint paint)
```
The appearance of the render key.

Parameter | Parameter Description
-----|:--------
keyboardType | A value defined in the KeyboardType is used to distinguish the main keyboard, more keys keyboard and emoji keyboard.
key | Key，the key in the native ASOP IME
keyStatus | A value defined in KeyStatus to distinguish the state of the key
keyDrawParams | KeyDrawParams，the KeyDrawParams in the native ASOP IME
keyboardDrawParams | Needs to pack requires certain values in native AOSP IME and pass to KeyboardRender for use. 
canvas | canvas
paint | paint

#### afterDrawKeyboard

```Java
public void afterDrawKeyboard(int keyboardType, @Nonnull Keyboard keyboard,
                                  @Nonnull KeyDrawParams keyDrawParams,
                                  @Nonnull KeyboardDrawParams keyboardDrawParams,
                                  @Nonnull Canvas canvas)
```
If you need to render other things on the top layer of the keyboard, you can do it here after rendering the keyboard.

Parameter | Parameter Description
-----|:--------
keyboardType | A value defined in the KeyboardType is used to distinguish the main keyboard, more keys keyboard and emoji keyboard.
keyboard | Keyboard，the KeyDrawParams in the native ASOP IME
keyStatus | the key in the native ASOP IME
keyDrawParams | KeyDrawParams，the KeyDrawParams in the native ASOP IME
keyboardDrawParams | Part of the native ASOP IME draws the required values, unified into the KeyboardDrawParams data structure, and passed to the KeyboardRender.
canvas | canvas

#### onDrawKeyPreviewBackground

```Java
public abstract void onDrawKeyPreviewBackground(@Nonnull Canvas canvas, @Nullable Drawable backgroundDrawable)
```
The preview background of the render key.

Parameter | Parameter Description
-----|:--------
canvas | canvas
backgroundDrawable | Rendered content

#### onDrawKeyPreviewBackground

```Java
public abstract void onDrawKeyPreviewBackground(@Nonnull Canvas canvas,
                                                 @Nonnull LottieDrawable backgroundDrawable,
                                                 @Nonnull ExternalThemeInfo.LottieDrawableInfo lottieDrawableInfo)
```
Preview the background of the keys through the LottieDrawable.

Parameter | Parameter Description
-----|:--------
canvas | canvas
backgroundDrawable | Rendered Content
lottieDrawableInfo | The relevant information of LottieDrawable is mainly used to obtain the scale required by LottieDrawable.

#### onDrawKeyPreviewBackground

```Java
public abstract void onDrawKeyPreviewText(@Nonnull Canvas canvas, @Nullable Bitmap textBitmap)
```
The preview text of the render key.

Parameter | Parameter Description
-----|:--------
canvas | canvas
textBitmap | Rendered text

#### isInvalidationNeeded
```Java

public boolean isInvalidationNeeded()
```
The preview text of the render key.

Return | Return Descriptions | 
-----|:--------
boolean | Whether to call View.invidate() after afterDrawKeyboard. If you need to render effects like Lottie effects or other custom effects, you can return true to keep the keyboard rendered.

<br/>

<h3 id="4.2">DefaultKeyboardRender</h3>
Purpose : default renderer

<br/>

<h3 id="4.3">GestureTrailRender</h3>
Purpose : The renderer used to render the swipe input track

#### onDrawGestureTrail
```Java

public abstract DrawResult onDrawGestureTrail(final Canvas offscreenCanvas, final GestureTrailPoints gestureTrailPoints, final GestureTrailDrawingParams drawParams)
```
The preview background of the render key.

Parameter | Parameter Description
-----|:--------
offscreenCanvas | Track's canvas
gestureTrailPoints | Data of tracks. For further details, please refer to the implementation code in DefaultGestureTrailRender.
drawParams | Parameter of rendering track. For furhter details, please refer to the implementation code in DefaultGestureTrailRender.

Return | Return Description | 
-----|:--------
DrawResult | Needs to set a render range of Rect and isRemainingToBeDrawn to return render restult. When isRemainingToBeDrawn is true, it means there's next canva to be rendered and it will delay the call of invalidate.


<br/>

<h3 id="4.4">DefaultGestureTrailRender</h3>

Purpose : default renderer
<br/>

<h2 id="5"> Abstract </h2>

<br/>

<h3 id="5.1"> SuggestionStripView </h3>

Purpose : Custom word suggestion entry screen and operation

#### setSuggestionStripViewListener
```Java
public abstract void setSuggestionStripViewListener(final SuggestionStripViewListener listener, final View inputView)
```
Pass the listener listened to by the suggestion bar

Parameter | Parameter Description
-----|:--------
listener | [SuggestionStripViewListener](#2.7)instance
inputView | InputView instance of the keyboard


#### setSuggestions
```Java
public abstract void setSuggestions(final SuggestedWords suggestedWords, final boolean isRtlLanguage)
```
Pass in the suggestion words corresponding to the current input

Parameter | Parameter Description
-----|:--------
suggestedWords | Current suggested Words (SuggestedWords type)
isRtlLanguage | Whether the current language is RTL

<br/>

<h3 id="5.2"> ChineseSuggestStripView </h3>

Purpose : Custom Chinese suggestion entry screen and operation

#### setChineseSuggestStripViewListener

```Java
public abstract void setChineseSuggestStripViewListener(final ChineseSuggestStripViewListener listener)
```

Pass the listener listened by the Chinese suggestion bar


Parameter | Parameter Description
-----|:--------
listener | [ChineseSuggestStripViewListener](#2.8)instance


#### setChineseSuggestion

```Java
public abstract void setChineseSuggestion(List<String> suggestionsList, boolean enableActiveHighlight)
```

Pass in the Chinese suggestion list corresponding to the current Chinese input (default is 10)

Parameter | Parameter Description
-----|:--------
suggestionsList | Chinese suggestion list
enableActiveHighlight | Whether the current input process is in active highlight mode (can be ignored)


#### getMoreSuggestionsList

```Java
public List<String> getMoreSuggestionsList(int fetchSize)
```

For the current input, if there are enough suggestion words, it will return certain amount of suggestion words (and these words will be added to the content of getSuggestionsList())

Parameter | Parameter Description
-----|:--------
fetchSize | Number of suggestions to be obtained

Return | Return Descriptions | 
-----|:--------
List<String>| List of suggestion words obtained this time

#### getSuggestionsList

```Java
public List<String> getSuggestionsList()
```
List of suggestion words stored in the system for current input

Return | Return Descriptions | 
-----|:--------
List<String>| List of suggestion words
 
<br/>

<h3 id="5.3"> ChineseComposingTextView </h3>

Purpose: The screen and operation of the composing view at the top left of the keyboard when customizing Chinese input. 

#### setComposingText

```Java
public abstract void setComposingText(String text)
```
The text in composing provided by Chinese word suggestions engine for the current input.

Parameter | Parameter Description
-----|:--------
text | composing text





