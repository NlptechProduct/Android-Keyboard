package com.nlptech.function.theme.theme_manage

import android.graphics.drawable.Drawable
import android.text.TextUtils
import androidx.recyclerview.widget.RecyclerView
import com.android.inputmethod.TestApplication
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.ViewholderThemeBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.nlptech.inputmethod.latin.settings.Settings
import com.nlptech.keyboardview.theme.KeyboardTheme
import com.nlptech.keyboardview.theme.KeyboardThemeManager
import com.nlptech.keyboardview.theme.custom.CustomTheme
import com.nlptech.keyboardview.theme.download.DownloadTheme
import com.nlptech.keyboardview.theme.external.ExternalTheme
import kotlinx.android.synthetic.main.viewholder_theme.view.*

class ThemeManageItemViewHolder(
        val binding: ViewholderThemeBinding,
        itemWidth: Int,
        itemHeight: Int
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.viewholder_theme.layoutParams.width = itemWidth
        binding.root.viewholder_theme_iv.layoutParams.height = (itemWidth * 0.7f).toInt()
        binding.executePendingBindings()
    }

    fun bind(themeManageItem: ThemeManageItem, listener: ThemeManageAdapter.Listener, position: Int) {
        binding.themeManageItem = themeManageItem
        binding.listener = listener
        binding.position = position
        binding.executePendingBindings()
        setRadioButtonSelected(themeManageItem.isSelected)
        setImageDrawable(themeManageItem.keyboardThemeId)
    }

    private fun setRadioButtonSelected(isSelected: Boolean) {
        itemView.viewholder_theme_top_visual.isSelected = isSelected
        itemView.viewholder_theme_radio_button_ib.isSelected = isSelected
    }

    private fun setImageDrawable(keyboardThemeId: Int) {
        val context = itemView.context
        val keyboardTheme = KeyboardThemeManager.getInstance().getKeyboardTheme(context, keyboardThemeId)

        when (keyboardTheme.themeType) {
            KeyboardTheme.ThemeType.LOCAL -> {
                val previewDrawableResId = KeyboardThemeManager.getInstance().getLocalThemePreviewDrawableResId(TestApplication.getInstance(), keyboardTheme)
                Glide.with(itemView.context)
                        .load(previewDrawableResId)
                        .placeholder(R.drawable.img_download_theme_default_preview)
                        .transition(withCrossFade())
                        .into(itemView.viewholder_theme_iv)
            }

            KeyboardTheme.ThemeType.CUSTOM -> {
                val customTheme = keyboardTheme as CustomTheme
                val previewImageFilePath = customTheme.previewImageFilePath
                Glide.with(itemView.context)
                        .load(previewImageFilePath)
                        .placeholder(R.drawable.img_download_theme_default_preview)
                        .transition(withCrossFade())
                        .into(itemView.viewholder_theme_iv)
            }

            KeyboardTheme.ThemeType.DOWNLOAD -> {
                val downloadTheme = keyboardTheme as DownloadTheme
                val borderShown = Settings.getInstance().current.mKeyBorderShown
                var url = downloadTheme.downloadInfo.themeCover
                if (borderShown && !TextUtils.isEmpty(downloadTheme.downloadInfo.themeCoverWithBorder)) {
                    url = downloadTheme.downloadInfo.themeCoverWithBorder
                }
                Glide.with(itemView.context)
                        .load(url)
                        .placeholder(R.drawable.img_download_theme_default_preview)
                        .transition(withCrossFade())
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                itemView.viewholder_theme_iv.setImageResource(R.drawable.img_download_theme_default_preview)
                                return true
                            }

                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                return false
                            }

                        })
                        .into(itemView.viewholder_theme_iv)
            }

            KeyboardTheme.ThemeType.EXTERNAL -> {
                val externalTheme = keyboardTheme as ExternalTheme
                val borderShown = Settings.getInstance().current.mKeyBorderShown
                val o = externalTheme.info.getThemePreviewImage(borderShown)
                if (o is Drawable) {
                    Glide.with(itemView.context)
                            .load(o)
                            .placeholder(R.drawable.img_download_theme_default_preview)
                            .transition(withCrossFade())
                            .into(itemView.viewholder_theme_iv)
                } else {
                    Glide.with(itemView.context)
                            .load(o as Int)
                            .placeholder(R.drawable.img_download_theme_default_preview)
                            .transition(withCrossFade())
                            .into(itemView.viewholder_theme_iv)
                }

            }
        }
    }
}