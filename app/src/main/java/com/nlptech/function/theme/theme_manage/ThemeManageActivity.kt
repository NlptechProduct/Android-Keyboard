package com.nlptech.function.theme.theme_manage

import android.Manifest
import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.inputmethod.latin.R
import com.nlptech.function.theme.custom_theme.CreateCustomThemeDialogFragment
import com.nlptech.function.theme.keyboard_preview.KeyboardPreviewSwitcher
import com.nlptech.function.theme.keyboard_preview.ThemeManagerBottomSheetFragment
import com.nlptech.common.utils.CheckPermissionsUtils
import com.nlptech.common.utils.LogUtil
import com.nlptech.common.utils.ViewUtils
import com.nlptech.inputmethod.latin.InputAttributes
import com.nlptech.inputmethod.latin.settings.Settings
import com.nlptech.keyboardview.accessibility.AccessibilityUtils
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
        val viewModel = ViewModelProviders.of(this).get(ThemeManageViewModel::class.java)
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
    }

    override fun onItemClick(view: View, themeManageItem: ThemeManageItem, position: Int) {
        LogUtil.i("ThemeManageActivity", "ThemeManageActivity.onItemClick()")
        ViewUtils.disableViewTemp(view)
        val viewModel = ViewModelProviders.of(this).get(ThemeManageViewModel::class.java)
        showBottomSheet(themeManageItem, position)
    }

    private fun showBottomSheet(themeManagerItem: ThemeManageItem, position: Int) {
        val fragment = ThemeManagerBottomSheetFragment.newInstance(themeManagerItem.keyboardThemeId, position, this)
        fragment.show(supportFragmentManager, "ThemeManageBottomSheetFragment")
    }

    override fun onThemeApply(view: View, position: Int) {
        LogUtil.i("ThemeManageActivity", "ThemeManageActivity.onThemeApply()")
        val viewModel = ViewModelProviders.of(this).get(ThemeManageViewModel::class.java)
        viewModel.selectTheme(viewModel.getThemes().value!![position], position)
    }

    override fun onKeyBorderSwitchChanged(buttonView: CompoundButton?, checked: Boolean) {
        LogUtil.i("ThemeManageActivity", "ThemeManageActivity.onKeyBorderSwitchChanged()")
        KeyboardPreviewSwitcher.getInstance().onKeyBorderSwitchChanged(checked)

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
                    val dialog = CreateCustomThemeDialogFragment.newCopyBgAndSaveThemeInstance(imageUri)
                    dialog.show(supportFragmentManager, "CreateCustomThemeDialogFragment")
                }
        }
    }

}