package com.nlptech.function.theme.theme_manage

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.inputmethod.latin.R
import com.nlptech.common.utils.CheckPermissionsUtils
import com.nlptech.common.utils.LogUtil
import com.nlptech.common.utils.NetworkUtil
import com.nlptech.common.utils.ViewUtils
import com.nlptech.function.theme.custom_theme.CreateCustomThemeDialogFragment
import com.nlptech.function.theme.keyboard_preview.KeyboardPreviewSwitcher
import com.nlptech.function.theme.keyboard_preview.ThemeManagerBottomSheetFragment
import com.nlptech.inputmethod.latin.InputAttributes
import com.nlptech.inputmethod.latin.settings.Settings
import com.nlptech.keyboardview.accessibility.AccessibilityUtils
import com.nlptech.keyboardview.theme.download.DownloadThemeManager
import kotlinx.android.synthetic.main.activity_keyboard_theme_manage.*


/**
 * Theme管理頁
 * **/
class ThemeManageActivity : FragmentActivity(), ThemeManageAdapter.Listener, View.OnClickListener {
    val layoutResource: Int = R.layout.activity_keyboard_theme_manage

    companion object {
        val PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        const val REQ_GET_PERMISSION = 1
        const val REQ_PICK_IMAGE = 2
    }

    private val showDialogHandler = Handler()
    private val receiver = DownloadThemeReceiver()
    private lateinit var viewModel: ThemeManageViewModel
    private var bottomSheetFragment : ThemeManagerBottomSheetFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResource)

        val layoutManager = GridLayoutManager(this, ThemeManageAdapter.SPAN_COUNT)
        activity_keyboard_theme_manage_rv.layoutManager = layoutManager as RecyclerView.LayoutManager?

        val itemDecorationWidth = resources.getDimensionPixelSize(R.dimen.theme_manage_item_decoration_width)
        val itemDecoration = ThemeManageItemDecoration(ThemeManageAdapter.SPAN_COUNT, itemDecorationWidth, true)
        activity_keyboard_theme_manage_rv.addItemDecoration(itemDecoration)

        val adapter = ThemeManageAdapter(this, this)
        activity_keyboard_theme_manage_rv.adapter = adapter

        // TODO use factory to create custom view model
        viewModel = ViewModelProviders.of(this).get(ThemeManageViewModel::class.java)
        viewModel.getThemes().observe(this, Observer { themes ->
            if (themes != null) {
                adapter.setData(themes)
            }
        })

        activity_keyboard_theme_manage_flbtn.setOnClickListener(this)

        // init something witch will be initialized by LatinIME.
        if (Settings.getInstance().current == null) {
            val inputAttributes = InputAttributes(null,
                    false, packageName)
            val locale = resources.configuration.locale
            Settings.getInstance().loadSettings(applicationContext, locale, inputAttributes, null)
        }
        AccessibilityUtils.init(applicationContext)

        val intentFilter = IntentFilter(DownloadThemeManager.ACTION_UPDATE_DOWNLOAD_THEME)
        registerReceiver(receiver, intentFilter)
    }

    override fun onResume() {
        super.onResume()
        DownloadThemeManager.getInstance().triggerFetchData(this)
    }

    override fun onPause() {
        super.onPause()
        dismissBottomSheet()
    }

    override fun onDestroy() {
        super.onDestroy()
        showDialogHandler.removeCallbacksAndMessages(null)
        unregisterReceiver(receiver)
    }

    override fun onItemClick(view: View, themeManageItem: ThemeManageItem, position: Int) {
        LogUtil.i("ThemeManageActivity", "ThemeManageActivity.onItemClick()")
        ViewUtils.disableViewTemp(view)
        when (themeManageItem.status) {
            ThemeManageItem.STATUS_SELECTABLE -> {
                showBottomSheet(themeManageItem, position)
            }
            ThemeManageItem.STATUS_DOWNLOADABLE -> {
                if (NetworkUtil.isNetworkConnected(applicationContext)) {
                    DownloadThemeManager.getInstance().downloadTheme(this, themeManageItem.downloadInfo)
                } else {
                    Toast.makeText(applicationContext, "Network unavailable.", Toast.LENGTH_SHORT).show()
                }
            }
            ThemeManageItem.STATUS_DOWNLOADING -> {
            }
        }
    }

    private fun showBottomSheet(themeManagerItem: ThemeManageItem, position: Int) {
        dismissBottomSheet()
        bottomSheetFragment = ThemeManagerBottomSheetFragment.newInstance(themeManagerItem.keyboardThemeId, position, this)
        showDialogHandler.post(object : Runnable {
            override fun run() {
                if (isFinishing) {
                    return
                }
                bottomSheetFragment!!.show(supportFragmentManager, "ThemeManagerBottomSheetFragment")
            }
        })
    }

    private fun dismissBottomSheet(){
        if(bottomSheetFragment== null)return
        bottomSheetFragment!!.close(false)
    }

    override fun onThemeApply(view: View, position: Int) {
        LogUtil.i("ThemeManageActivity", "ThemeManageActivity.onThemeApply()")
        val themeManageItem = viewModel.getThemes().value!![position]
        viewModel.selectTheme(themeManageItem, position)
    }

    override fun onKeyBorderSwitchChanged(buttonView: CompoundButton?, checked: Boolean) {
        LogUtil.i("ThemeManageActivity", "ThemeManageActivity.onKeyBorderSwitchChanged()")
        KeyboardPreviewSwitcher.getInstance().onKeyBorderSwitchChanged(checked)
        viewModel.refreshThemes()
    }

    override fun onDarkModeChanged(buttonView: CompoundButton, checked: Boolean) {
        LogUtil.i("ThemeManageActivity", "ThemeManageActivity.onDarkModeChanged()")
        KeyboardPreviewSwitcher.getInstance().onDarkModeChanged(checked)
        viewModel.refreshThemes()
    }

    /**
     * Custom theme
     * **/
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.activity_keyboard_theme_manage_flbtn -> {
                val dontGetPermission = CheckPermissionsUtils.checkPermissions(this, PERMISSIONS)
                if (dontGetPermission.isNotEmpty()) {
                    if (CheckPermissionsUtils.shouldShowRequestPermissionRationale(this, dontGetPermission)) {
                        // TODO:告知用戶取得權限合理的理由，當用戶曾經拒絕過以後，就會觸發
                        ActivityCompat.requestPermissions(this, PERMISSIONS, REQ_GET_PERMISSION)
                    }

                    // 請求權限
                    ActivityCompat.requestPermissions(this, PERMISSIONS, REQ_GET_PERMISSION)

                } else {
                    pickImage()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQ_GET_PERMISSION -> {
                val dontGetPermission = CheckPermissionsUtils.checkPermissions(this, grantResults, PERMISSIONS)
                if (dontGetPermission.isEmpty()) {
                    pickImage()
                } else {
                    // TODO:部分權限沒有被啟用，故於此告知用戶，由於無法取得權限，您將不能使用此功能.....等訊息
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            supportFinishAfterTransition()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun pickImage() {
        LogUtil.i("ThemeManagerActivity", "ThemeManagerActivity.pickImage()")
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQ_PICK_IMAGE)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_PICK_IMAGE ->
                if (resultCode == Activity.RESULT_OK) {
                    val imageUri = data!!.data
                    val fragment = CreateCustomThemeDialogFragment.newCopyBgAndSaveThemeInstance(imageUri)
                    showDialogHandler.post(object : Runnable {
                        override fun run() {
                            if (isFinishing) {
                                return
                            }
                            fragment.show(supportFragmentManager, "CreateCustomThemeDialogFragment")
                        }
                    })
                }
        }
    }

    inner class DownloadThemeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (DownloadThemeManager.ACTION_UPDATE_DOWNLOAD_THEME != intent.action) {
                return
            }
            viewModel.refreshThemes()
        }
    }
}