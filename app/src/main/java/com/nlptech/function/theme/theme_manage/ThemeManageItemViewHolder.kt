package com.nlptech.function.theme.theme_manage

import androidx.recyclerview.widget.RecyclerView
import com.android.inputmethod.TestApplication
import com.android.inputmethod.latin.databinding.ViewholderThemeBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.nlptech.inputmethod.latin.settings.Settings
import com.nlptech.keyboardview.theme.KeyboardTheme
import com.nlptech.keyboardview.theme.KeyboardThemeManager
import com.nlptech.keyboardview.theme.custom.CustomTheme
import com.nlptech.keyboardview.theme.external.ExternalTheme
import kotlinx.android.synthetic.main.viewholder_theme.view.*

class ThemeManageItemViewHolder(
        val binding: ViewholderThemeBinding,
        itemWidth: Int,
        itemHeight: Int
) : RecyclerView.ViewHolder(binding.root) {

    /**
     * 用來記錄當前的preview image為何
     * **/
    private var previewTag = ""

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
                val tag = previewDrawableResId.toString()
                if (tag != previewTag) {
                    previewTag = tag
                    Glide.with(itemView.context)
                            .load(previewDrawableResId)
                            .transition(withCrossFade())
                            .into(itemView.viewholder_theme_iv)
                }
            }

            KeyboardTheme.ThemeType.CUSTOM -> {
                val customTheme = keyboardTheme as CustomTheme
                val previewImageFilePath = customTheme.previewImageFilePath
                val tag = previewImageFilePath
                if (tag != previewTag) {
                    previewTag = tag
                    Glide.with(itemView.context)
                            .load(previewTag)
                            .transition(withCrossFade())
                            .into(itemView.viewholder_theme_iv)
                }
            }

            KeyboardTheme.ThemeType.DOWNLOAD -> {
                // TODO
            }

            KeyboardTheme.ThemeType.EXTERNAL -> {
                val externalTheme = keyboardTheme as ExternalTheme
                val borderShown = Settings.getInstance().current.mKeyBorderShown;

                // external theme tag的生成方式為borderShown+switchedModeThemeId
                var tag = ""
                if (externalTheme.mBorderMode == KeyboardTheme.BorderMode.BOTH) {
                    tag += borderShown.toString()
                } else {
                    tag += "noConsiderBorderShown"
                }
                tag += externalTheme.switchedModeThemeId

                if (tag != previewTag) {
                    previewTag = tag
                    Glide.with(itemView.context)
                            .load(externalTheme.info.getThemePreviewImage(borderShown))
                            .transition(withCrossFade())
                            .into(itemView.viewholder_theme_iv)
                }
            }
        }


    }
}