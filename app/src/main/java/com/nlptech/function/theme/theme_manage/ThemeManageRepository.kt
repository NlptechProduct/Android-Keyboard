package com.nlptech.function.theme.theme_manage

import com.android.inputmethod.TestApplication
import com.nlptech.keyboardview.theme.KeyboardTheme
import com.nlptech.keyboardview.theme.KeyboardThemeManager

class ThemeManageRepository {

    interface Listener {
        fun onThemesLoaded(themeItems: ArrayList<ThemeManageItem>)
    }

    companion object {
        val instance = ThemeManageRepository()
    }

    fun getLastUsedKeyboardTheme(): KeyboardTheme {
        return KeyboardThemeManager.getInstance().getLastUsedKeyboardTheme(TestApplication.getInstance())
    }

    fun loadThemesLiveData(listener : Listener ) {
        // TODO: combine theme data with theme from online
        val selectedKeyboardTheme = KeyboardThemeManager.getInstance().getLastUsedKeyboardTheme(TestApplication.getInstance())
        val keyboardThemes = KeyboardThemeManager.getInstance().getAvailableThemeArray(TestApplication.getInstance())
        val themeItems = ArrayList<ThemeManageItem>()

        for (keyboardTheme in keyboardThemes) {
            val isSelected = selectedKeyboardTheme == keyboardTheme
            val themeItem = ThemeManageItem(
                    "https://www.google.com.tw/",
                    keyboardTheme.themeName,
                    keyboardTheme.themeId,
                    ThemeManageItem.STATUS_SELECTABLE
            )
            themeItem.isSelected = isSelected
            themeItems.add(themeItem)
        }
        listener.onThemesLoaded(themeItems)
    }
}