package com.nlptech.function.theme.custom_theme

import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders

import com.android.inputmethod.latin.R
import com.nlptech.function.theme.theme_manage.ThemeManageViewModel
import com.nlptech.keyboardview.theme.custom.CustomTheme
import com.nlptech.keyboardview.theme.custom.CustomThemeManager
import java.io.File

class CreateCustomThemeDialogFragment : androidx.fragment.app.DialogFragment(), CustomThemeBackgroundTask.Listener, CustomThemeSaveTask.Listener, CustomThemeDeleteTask.Listener {

    companion object {
        /**
         * 命令型別
         * **/
        private const val KEY_ACTION_TYPE = "action_type"
        private const val ACTION_TYPE_COPY_BG_AND_SAVE_THEME = 1
        private const val ACTION_TYPE_DELETE_THEME = 2

        /**
         * 參數
         * **/
        private const val KEY_BACKGROUND_FILE_URI = "background_file_uri"
        private const val KEY_THEME_ID = "theme_id"

        fun newCopyBgAndSaveThemeInstance(backgroundFileUri: Uri): CreateCustomThemeDialogFragment {
            val arguments = Bundle()
            arguments.putInt(KEY_ACTION_TYPE, ACTION_TYPE_COPY_BG_AND_SAVE_THEME)
            arguments.putString(KEY_BACKGROUND_FILE_URI, backgroundFileUri.toString())
            val fragment = CreateCustomThemeDialogFragment()
            fragment.arguments = arguments
            return fragment
        }

        fun newDeleteThemeInstance(themeId: Int, deleteThemeListener: CustomThemeDeleteTask.Listener): CreateCustomThemeDialogFragment {
            val arguments = Bundle()
            arguments.putInt(KEY_ACTION_TYPE, ACTION_TYPE_DELETE_THEME)
            arguments.putInt(KEY_THEME_ID, themeId)
            val fragment = CreateCustomThemeDialogFragment()
            fragment.arguments = arguments
            fragment.deleteThemeListener = deleteThemeListener
            return fragment
        }
    }

    private lateinit var customTheme: CustomTheme
    private var backgroundTask: CustomThemeBackgroundTask? = null
    private var saveTask: CustomThemeSaveTask? = null
    private var deleteTask: CustomThemeDeleteTask? = null
    private var deleteThemeListener: CustomThemeDeleteTask.Listener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_custom_theme, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionType = arguments!!.getInt(KEY_ACTION_TYPE)

        when (actionType) {
            ACTION_TYPE_COPY_BG_AND_SAVE_THEME -> {
                val backgroundFileUri = arguments!!.getString(KEY_BACKGROUND_FILE_URI)
                copyImageFile(backgroundFileUri)
            }

            ACTION_TYPE_DELETE_THEME -> {
                val themeId = arguments!!.getInt(KEY_THEME_ID)
                deleteTheme(themeId)
            }
        }

    }

    override fun onPause() {
        super.onPause()
        if (backgroundTask != null && backgroundTask!!.status == AsyncTask.Status.RUNNING) {
            backgroundTask!!.cancel(true)
        }
        if (saveTask != null && saveTask!!.status == AsyncTask.Status.RUNNING) {
            saveTask!!.cancel(true)
        }
        if (deleteTask != null && deleteTask!!.status == AsyncTask.Status.RUNNING) {
            deleteTask!!.cancel(true)
        }
    }

    private fun copyImageFile(backgroundFileUri: String) {
        if (backgroundTask == null || backgroundTask!!.status == AsyncTask.Status.FINISHED) {
            backgroundTask = CustomThemeBackgroundTask(this)
        }

        customTheme = CustomTheme.createTheme(context)
        backgroundTask!!.execute(backgroundFileUri, customTheme.backgroundFilePath)
    }

    private fun deleteTheme(themeId: Int) {
        if (deleteTask == null || deleteTask!!.status == AsyncTask.Status.FINISHED) {
            deleteTask = CustomThemeDeleteTask(this)
        }
        customTheme = CustomThemeManager.getInstance().getTheme(context, themeId)
        deleteTask!!.execute(customTheme)
    }

    override fun onBackgroundResult(backgroundFile: File?) {
        if (saveTask == null || saveTask!!.status == AsyncTask.Status.FINISHED) {
            saveTask = CustomThemeSaveTask(this)
        }
        saveTask!!.execute(customTheme)

    }

    override fun onSaveResult(backgroundFile: File?) {
        CustomThemeManager.getInstance().onThemeAdded(customTheme)
        val viewModel = ViewModelProviders.of(activity!!).get(ThemeManageViewModel::class.java)
        viewModel.refreshThemes()
        dismiss()
    }

    override fun onDeleteResult() {
        CustomThemeManager.getInstance().onThemeRemoved(customTheme)
        val viewModel = ViewModelProviders.of(activity!!).get(ThemeManageViewModel::class.java)
        viewModel.refreshThemes()
        dismiss()
        if (deleteThemeListener != null) {
            deleteThemeListener!!.onDeleteResult()
        }
    }
}
