package com.nlptech.function.theme.theme_manage

import com.android.inputmethod.TestApplication
import com.nlptech.keyboardview.theme.KeyboardTheme
import com.nlptech.keyboardview.theme.KeyboardThemeManager
import com.nlptech.keyboardview.theme.download.DownloadTheme

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

    fun loadThemesLiveData(listener: Listener) {
        // TODO: combine theme data with theme from online
        val selectedKeyboardTheme = KeyboardThemeManager.getInstance().getLastUsedKeyboardTheme(TestApplication.getInstance())
        val keyboardThemes = KeyboardThemeManager.getInstance().getAvailableThemeArray(TestApplication.getInstance())
        val themeItems = ArrayList<ThemeManageItem>()

        for (keyboardTheme in keyboardThemes) {
            var status = ThemeManageItem.STATUS_SELECTABLE
            if (keyboardTheme is DownloadTheme) {
                val downloadTheme = keyboardTheme as DownloadTheme
                when (downloadTheme.downloadStatus) {
                    DownloadTheme.DOWNLOAD_STATUS_DOWNLOADABLE -> {
                        status = ThemeManageItem.STATUS_DOWNLOADABLE
                    }
                    DownloadTheme.DOWNLOAD_STATUS_DOWNLOADING -> {
                        status = ThemeManageItem.STATUS_DOWNLOADING
                    }
                    DownloadTheme.DOWNLOAD_STATUS_DOWNLOADED -> {
                        status = ThemeManageItem.STATUS_SELECTABLE
                    }
                }
            }
            val themeItem = ThemeManageItem(
                    "https://www.google.com.tw/",
                    keyboardTheme.themeName,
                    keyboardTheme.themeId,
                    status
            )
            val isSelected = selectedKeyboardTheme == keyboardTheme && status == ThemeManageItem.STATUS_SELECTABLE
            themeItem.isSelected = isSelected
            if (keyboardTheme is DownloadTheme) {
                val downloadTheme = keyboardTheme as DownloadTheme
                themeItem.downloadInfo = downloadTheme.downloadInfo
            }
            themeItems.add(themeItem)
        }
        listener.onThemesLoaded(themeItems)
    }
}