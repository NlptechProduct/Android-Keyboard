package com.nlptech.function.theme.keyboard_preview

import android.app.Dialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.CompoundButton
import android.widget.ImageButton
import android.widget.Switch
import com.nlptech.keyboardview.keyboard.KeyboardSwitcher
import com.nlptech.keyboardview.theme.KeyboardTheme
import com.android.inputmethod.latin.R
import com.nlptech.inputmethod.latin.settings.Settings
import com.nlptech.inputmethod.latin.utils.RecapitalizeStatus
import com.nlptech.common.constant.Constants
import com.nlptech.function.theme.custom_theme.CreateCustomThemeDialogFragment
import com.nlptech.function.theme.custom_theme.CustomThemeDeleteTask
import com.nlptech.function.theme.theme_manage.ThemeManageAdapter
import com.nlptech.keyboardview.theme.KeyboardThemeManager


/**
 * Display Theme preview
 * **/
class ThemeManagerBottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener, CompoundButton.OnCheckedChangeListener, CustomThemeDeleteTask.Listener {

    companion object {
        private const val KEY_THEME_ID = "theme_id"
        private const val KEY_ITEM_POSITION = "item_position"

        fun newInstance(keyboardThemeId: Int, itemPosition: Int, themeListener: ThemeManageAdapter.Listener): ThemeManagerBottomSheetFragment {
            val fragment = ThemeManagerBottomSheetFragment()
            val bundle = Bundle()
            bundle.putInt(KEY_THEME_ID, keyboardThemeId)
            bundle.putInt(KEY_ITEM_POSITION, itemPosition)
            fragment.arguments = bundle
            fragment.setOnThemeApplyListener(themeListener)
            return fragment
        }
    }

    private var themeId: Int = 0
    private var itemPosition: Int = 0
    private var mBehavior: BottomSheetBehavior<*>? = null
    private var dialog: BottomSheetDialog? = null
    private var listener: ThemeManageAdapter.Listener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        themeId = arguments!!.getInt(KEY_THEME_ID)
        itemPosition = arguments!!.getInt(KEY_ITEM_POSITION)

        val rootView = View.inflate(context, R.layout.fragment_theme_manage_preview_bottom_sheet, null)
        val applyButton = rootView.findViewById<View>(R.id.fragment_theme_manage_preview_apply_button)
        val closeButton = rootView.findViewById<View>(R.id.fragment_theme_manage_preview_close_button)
        val showKeyBoarderSwitch: Switch = rootView.findViewById<Switch>(R.id.fragment_theme_manage_preview_show_key_board_sw)
        val deleteButton = rootView.findViewById<ImageButton>(R.id.fragment_theme_manage_preview_delete_btn)

        applyButton.setOnClickListener(this)
        closeButton.setOnClickListener(this)
        showKeyBoarderSwitch.isChecked = Settings.readKeyBorderShown(PreferenceManager.getDefaultSharedPreferences(context))
        showKeyBoarderSwitch.setOnCheckedChangeListener(this)

        deleteButton.setOnClickListener(this)
        deleteButton.visibility = if (KeyboardThemeManager.getInstance().getKeyboardTheme(context, themeId).themeType != KeyboardTheme.ThemeType.LOCAL) View.VISIBLE else View.GONE

        dialog!!.setContentView(rootView)
        mBehavior = BottomSheetBehavior.from<View>(rootView!!.getParent() as View)
        mBehavior!!.isHideable = true
        return dialog as BottomSheetDialog
    }

    override fun onStart() {
        super.onStart()
        mBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED

        val inputViewContainer = dialog!!.findViewById<ViewGroup>(R.id.fragment_theme_manage_keyboard_preview_container)
        inputViewContainer!!.removeAllViews()
        val keyboardTheme = KeyboardThemeManager.getInstance().getKeyboardTheme(context, themeId)
        val themeContext = ContextThemeWrapper(context, keyboardTheme.styleId)
        val currentInputView = LayoutInflater.from(themeContext).inflate(
                R.layout.fragment_theme_manage_preview_input_view, null)
        inputViewContainer.addView(currentInputView)
        val container = currentInputView.findViewById<ViewGroup>(R.id.kb_container)
        updateThemeAndCreateInputView(container)
    }

    override fun onResume() {
        super.onResume()
        KeyboardThemeManager.getInstance().isInKeyboardThemePreview = true
    }

    override fun onPause() {
        super.onPause()
        KeyboardThemeManager.getInstance().isInKeyboardThemePreview = false
        KeyboardSwitcher.getInstance().updateKeyboardTheme(true)
    }

    private fun updateThemeAndCreateInputView(inputViewContainer: ViewGroup) {
        KeyboardThemeManager.getInstance().isInKeyboardThemePreview = true
        val keyboardTheme = KeyboardThemeManager.getInstance().getKeyboardTheme(context, themeId)
        val switcher = KeyboardPreviewSwitcher.getInstance() as KeyboardPreviewSwitcher

        // 在KeyboardPreviewSwitcher中建立ThemeContext和InputView
        switcher.updateKeyboardTheme(inputViewContainer, keyboardTheme)

        KeyboardPreviewSwitcher.getInstance().updateKeyboardAdditionalNumberRow()
        KeyboardPreviewSwitcher.getInstance().loadKeyboard(
                EditorInfo(),
                Settings.getInstance().current,
                Constants.TextUtils.CAP_MODE_OFF,
                RecapitalizeStatus.NOT_A_RECAPITALIZE_MODE)
    }

    fun close(isAnimation: Boolean) {
        if (isAnimation) {
            if (mBehavior != null)
                mBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
        } else {
            dismiss()
        }
    }

    fun setOnThemeApplyListener(themeListener: ThemeManageAdapter.Listener) {
        this.listener = themeListener
    }

    override fun show(manager: androidx.fragment.app.FragmentManager?, tag: String?) {
        try {
            manager!!.beginTransaction().remove(this).commit()
            super.show(manager, tag)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.fragment_theme_manage_preview_apply_button -> {
                listener!!.onThemeApply(v, itemPosition)
                close(false)
            }

            R.id.fragment_theme_manage_preview_close_button -> {
                close(false)
            }

            R.id.fragment_theme_manage_preview_delete_btn -> {
                deleteTheme()
            }
        }
    }

    private fun deleteTheme() {
        val keyboardTheme = KeyboardThemeManager.getInstance().getKeyboardTheme(context, themeId)
        when (keyboardTheme.themeType) {
            KeyboardTheme.ThemeType.CUSTOM -> {
                val dialog = CreateCustomThemeDialogFragment.newDeleteThemeInstance(keyboardTheme.themeId, this)
                dialog.show(childFragmentManager, "CreateCustomThemeDialogFragment")
            }

            KeyboardTheme.ThemeType.DOWNLOAD -> {
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        Settings.writeKeyBoarderShown(PreferenceManager.getDefaultSharedPreferences(context), isChecked)
        listener!!.onKeyBorderSwitchChanged(buttonView, isChecked)
    }

    override fun onDeleteResult() {
        close(false)
    }
}