package com.nlptech.function.theme.theme_manage

import androidx.databinding.Bindable
import android.view.View

/**
 * Item view model
 * **/
class ThemeManageItem(
        val imageUrl: String,
        val themeName: String,
        val keyboardThemeId: Int,
        val status: Int
) {

    companion object {
        const val STATUS_SELECTABLE = 0
        const val STATUS_DOWNLOADABLE = 1
    }

    var isSelected: Boolean = false

    @Bindable
    fun getDownloadable(): Int {
        return if (status == STATUS_DOWNLOADABLE) View.VISIBLE else View.GONE
    }

    @Bindable
    fun getSelectable(): Int {
        return if (status == STATUS_SELECTABLE) View.VISIBLE else View.GONE
    }


}