package com.nlptech.function.theme.theme_manage

import android.view.View
import androidx.databinding.Bindable
import com.nlptech.keyboardview.theme.download.ThemeDownloadInfo

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
        const val STATUS_DOWNLOADING = 2
    }

    var isSelected: Boolean = false
    var downloadInfo: ThemeDownloadInfo? = null

    @Bindable
    fun getDownloadable(): Int {
        return if (status == STATUS_DOWNLOADABLE) View.VISIBLE else View.GONE
    }

    @Bindable
    fun getDownloading(): Int {
        return if (status == STATUS_DOWNLOADING) View.VISIBLE else View.GONE
    }

    @Bindable
    fun getSelectable(): Int {
        return if (status == STATUS_SELECTABLE) View.VISIBLE else View.GONE
    }
}