package com.nlptech.function.theme.keyboard_preview

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.Switch
import com.android.inputmethod.latin.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nlptech.Agent
import com.nlptech.function.theme.custom_theme.CreateCustomThemeDialogFragment
import com.nlptech.function.theme.custom_theme.CustomThemeDeleteTask
import com.nlptech.function.theme.theme_manage.ThemeManageAdapter
import com.nlptech.inputmethod.latin.settings.Settings
import com.nlptech.inputmethod.latin.utils.ResourceUtils
import com.nlptech.keyboardview.keyboard.KeyboardSwitcher
import com.nlptech.keyboardview.theme.KeyboardTheme
import com.nlptech.keyboardview.theme.KeyboardThemeManager
import com.nlptech.keyboardview.theme.download.DownloadTheme
import com.nlptech.keyboardview.theme.download.DownloadThemeManager


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
        val showKeyBoarderSwitchLauout = rootView.findViewById<View>(R.id.fragment_theme_manage_preview_show_key_board_layout)
        val showKeyBoarderSwitch: Switch = rootView.findViewById<Switch>(R.id.fragment_theme_manage_preview_key_border_shown_switch)
        val darkModeSwitchLauout = rootView.findViewById<View>(R.id.fragment_theme_manage_preview_dark_mode_switch_layout)
        val darkModeSwitch: Switch = rootView.findViewById<Switch>(R.id.fragment_theme_manage_preview_dark_mode_switch)
        val deleteButton = rootView.findViewById<ImageButton>(R.id.fragment_theme_manage_preview_delete_button)
        val cardViewContainer = rootView.findViewById<View>(R.id.fragment_theme_manage_card_view_container)
        val inputViewContainer = rootView.findViewById<RelativeLayout>(R.id.fragment_theme_manage_preview_container)

        applyButton.setOnClickListener(this)
        closeButton.setOnClickListener(this)

        val keyboardTheme = KeyboardThemeManager.getInstance().getKeyboardTheme(context, themeId)
        showKeyBoarderSwitchLauout.visibility = if (keyboardTheme.mBorderMode == KeyboardTheme.BorderMode.BOTH) View.VISIBLE else View.GONE
        showKeyBoarderSwitch.isChecked = Settings.readKeyBorderShown(PreferenceManager.getDefaultSharedPreferences(context))
        showKeyBoarderSwitch.setOnCheckedChangeListener(this)

        darkModeSwitchLauout.visibility = if (keyboardTheme.switchedModeThemeId != KeyboardTheme.ThemeId.CAN_NOT_SWITCH) View.VISIBLE else View.GONE
        darkModeSwitch.isChecked = Settings.readDarkMode(PreferenceManager.getDefaultSharedPreferences(context))
        darkModeSwitch.setOnCheckedChangeListener(this)

        deleteButton.setOnClickListener(this)
        val themeType = KeyboardThemeManager.getInstance().getKeyboardTheme(context, themeId).themeType
        deleteButton.visibility =
                if (themeType == KeyboardTheme.ThemeType.CUSTOM
                        || themeType == KeyboardTheme.ThemeType.DOWNLOAD)
                    View.VISIBLE
                else
                    View.GONE

        inputViewContainer.layoutParams.width = getDefaultKeyboardWidth()

        dialog!!.setContentView(rootView)
        mBehavior = BottomSheetBehavior.from<View>(rootView!!.parent as View)
        mBehavior!!.isHideable = true
        return dialog as BottomSheetDialog
    }


    private fun getDefaultKeyboardHeight(): Int {
        return ResourceUtils.getDefaultKeyboardHeight(context, KeyboardSwitcher.getInstance().isFloatingKeyboard()) * getDefaultKeyboardWidth() / ResourceUtils.getDefaultKeyboardWidth(context, KeyboardSwitcher.getInstance().isFloatingKeyboard())
    }

    private fun getDefaultKeyboardWidth(): Int {
        return ResourceUtils.getDefaultKeyboardWidth(context, KeyboardSwitcher.getInstance().isFloatingKeyboard()) - resources.getDimensionPixelSize(com.nlptech.keyboardview.R.dimen.theme_manage_preview_margin_vertical) * 2
    }

    private fun getSuggestionStripViewHeight(): Int {
        return ResourceUtils.getSuggestionStripViewHeight(context)
    }

    override fun onStart() {
        super.onStart()
        mBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED

        val inputViewContainer = dialog!!.findViewById<RelativeLayout>(R.id.fragment_theme_manage_preview_container)
        inputViewContainer!!.removeAllViews()
        val themeContext: Context = Agent.getInstance().getThemeContext(context, themeId)
        val currentInputView = LayoutInflater.from(themeContext).inflate(
                R.layout.fragment_theme_manage_preview_input_view, null)
        inputViewContainer.addView(currentInputView)
        val container = currentInputView.findViewById<ViewGroup>(R.id.kb_container)
        Agent.getInstance().showThemePreview(container, themeId)
//        updateThemeAndCreateInputView(container)
    }

//    private fun updateThemeAndCreateInputView(inputViewContainer: ViewGroup) {
//        KeyboardThemeManager.getInstance().isInKeyboardThemePreview = true
//        val keyboardTheme = KeyboardThemeManager.getInstance().getKeyboardTheme(context, themeId)
//        val switcher = KeyboardPreviewSwitcher.getInstance() as KeyboardPreviewSwitcher
//
//        // 在KeyboardPreviewSwitcher中建立ThemeContext和InputView
//        switcher.updateKeyboardTheme(inputViewContainer, keyboardTheme)
//
//        KeyboardPreviewSwitcher.getInstance().updateKeyboardAdditionalNumberRow()
//        KeyboardPreviewSwitcher.getInstance().loadKeyboard(
//                EditorInfo(),
//                Settings.getInstance().current,
//                Constants.TextUtils.CAP_MODE_OFF,
//                RecapitalizeStatus.NOT_A_RECAPITALIZE_MODE)
//    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        Agent.getInstance().dismissThemePreview()
    }

    fun close(isAnimation: Boolean) {
        if (isAnimation) {
            if (mBehavior != null)
                mBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
        } else {
            if (activity != null && !activity!!.isFinishing && isAdded) {
                dismiss()
            }
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

            R.id.fragment_theme_manage_preview_delete_button -> {
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
                DownloadThemeManager.getInstance().deleteTheme(context, keyboardTheme as DownloadTheme?)
                onDeleteResult()
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView!!.id) {
            R.id.fragment_theme_manage_preview_key_border_shown_switch -> {
                Settings.writeKeyBoarderShown(PreferenceManager.getDefaultSharedPreferences(context), isChecked)
                listener!!.onKeyBorderSwitchChanged(buttonView, isChecked)
            }
            R.id.fragment_theme_manage_preview_dark_mode_switch -> {
                Settings.writeDarkMode(PreferenceManager.getDefaultSharedPreferences(context), isChecked)
                listener!!.onDarkModeChanged(buttonView, isChecked)
            }
        }
    }

    override fun onDeleteResult() {
        KeyboardThemeManager.getInstance().clearLastUsedKeyboardTheme(context)
        close(false)
    }
}