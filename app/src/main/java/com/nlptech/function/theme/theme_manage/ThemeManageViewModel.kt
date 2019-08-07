package com.nlptech.function.theme.theme_manage

import android.app.Application
import android.preference.PreferenceManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.nlptech.keyboardview.theme.KeyboardThemeManager

class ThemeManageViewModel(application: Application) : AndroidViewModel(application), ThemeManageRepository.Listener {

    private var themesLiveData: MutableLiveData<List<ThemeManageItem>>? = null
    private var selectedPosition = 0

    fun getThemes(): MutableLiveData<List<ThemeManageItem>> {
        if (themesLiveData == null) {
            themesLiveData = MutableLiveData<List<ThemeManageItem>>()
            ThemeManageRepository.instance.loadThemesLiveData(this)
        }
        return themesLiveData as MutableLiveData<List<ThemeManageItem>>
    }

    override fun onThemesLoaded(themeItems: ArrayList<ThemeManageItem>) {
        themesLiveData!!.postValue(themeItems)

        val keyboardTheme = ThemeManageRepository.instance.getLastUsedKeyboardTheme()
        for ((i, item) in themeItems.withIndex()) {
            if (item.keyboardThemeId == keyboardTheme.themeId) {
                selectedPosition = i
            }
        }

    }

    fun download(themeManageItem: ThemeManageItem, position: Int) {
        // TODO: download
    }

    fun selectTheme(themeManageItem: ThemeManageItem, position: Int) {
        KeyboardThemeManager.getInstance().saveLastUsedKeyboardThemeId(themeManageItem.keyboardThemeId, PreferenceManager.getDefaultSharedPreferences(getApplication()))
        themesLiveData!!.value!![selectedPosition].isSelected = false
        selectedPosition = position
        themesLiveData!!.value!![position].isSelected = true
        themesLiveData!!.postValue(themesLiveData!!.value)
    }

    fun refreshThemes() {
        ThemeManageRepository.instance.loadThemesLiveData(this)
    }
}
