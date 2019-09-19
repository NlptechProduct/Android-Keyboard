package com.nlptech.function.gifsending

import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.annotation.NonNull
import com.bumptech.glide.Glide
import com.nlptech.common.utils.FileUtils
import com.nlptech.function.gifsending.dataclass.Facemoji
import com.nlptech.function.gifsending.dataclass.GifItem
import com.nlptech.function.gifsending.dataclass.Sticker
import com.nlptech.keyboardtrace.KeyboardTrace
import java.io.File
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import androidx.core.content.FileProvider
import androidx.core.view.inputmethod.EditorInfoCompat
import androidx.core.view.inputmethod.InputConnectionCompat
import androidx.core.view.inputmethod.InputContentInfoCompat
import com.android.inputmethod.TestApplication
import com.android.inputmethod.latin.LatinIME


class GifSendingManager {

    companion object {
        val instance = GifSendingManager()
        const val TAG = "GifSendingManager"
        const val GIF_CACHE_FILE_NAME = "gif_sending_cache/gif_sending_gif_cache.gif"
    }

    fun send(@NonNull gifItem: GifItem) {
        Completable.fromAction {
            val context = TestApplication.getInstance()
            val temp = Glide
                    .with(context)
                    .downloadOnly()
                    .load(gifItem.getUrl(GifItem.MediaBean.TYPE_FULL))
                    .submit()
                    .get()
            val gifFile = File(context.externalCacheDir, GIF_CACHE_FILE_NAME)
            FileUtils.copy(temp, gifFile)

            val mimeType = "image/gif"
            val contentUri = if (Build.VERSION.SDK_INT >= 24)
                FileProvider.getUriForFile(context, context.packageName + ".fileprovider", gifFile)
            else
                Uri.fromFile(gifFile)

            if (!doCommitContent(context, "", contentUri, mimeType)) {
                shareContentWithActionSend(context, contentUri, mimeType)
            }
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    fun send(sticker: Sticker) {

    }

    fun send(facemoji: Facemoji) {

    }

    private fun shareContentWithActionSend(context: Context, uri: Uri, mimeType: String) {
        val intent = Intent(Intent.ACTION_SEND)
        val packageName = LatinIME.getInstance().inputLogic.appEditorInfo.packageName
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.type = mimeType
        intent.setPackage(packageName)
        try {
            TestApplication.getInstance().startActivity(intent)
            KeyboardTrace.onImageSharedEntrance()
        } catch (e: Exception) {
            Log.e(TAG, "app:{$packageName} can not to be shared image")
        }
    }

    private fun doCommitContent(context: Context, description: String, uri: Uri, mimeType: String): Boolean {
        val editorInfo = LatinIME.getInstance().inputLogic.editorInfo

        // Validate packageName again just in case.
        if (!isCommitContentSupported(editorInfo, mimeType)) {
            return false
        }

        var contentUri = uri
        if (!TextUtils.isEmpty(description)) {
            contentUri = contentUri.buildUpon().appendQueryParameter("caption", description).build()
        }

        // As you as an IME author are most likely to have to implement your own content provider
        // to support CommitContent API, it is important to have a clear spec about what
        // applications are going to be allowed to access the content that your are going to share.
        val flag: Int
        if (Build.VERSION.SDK_INT >= 25) {
            // On API 25 and later devices, as an analogy of Intent.FLAG_GRANT_READ_URI_PERMISSION,
            // you can specify InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION to give
            // a temporary read access to the recipient application without exporting your content
            // provider.
            flag = InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION
        } else {
            // On API 24 and prior devices, we cannot rely on
            // InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION. You as an IME author
            // need to decide what access control is needed (or not needed) for content URIs that
            // you are going to expose. This sample uses Context.grantUriPermission(), but you can
            // implement your own mechanism that satisfies your own requirements.
            flag = 0
            try {
                // TODO: Use revokeUriPermission to revoke as needed.
                context.applicationContext.grantUriPermission(
                        editorInfo.packageName, contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } catch (e: Exception) {
                Log.e(TAG, "grantUriPermission failed packageName=" + editorInfo.packageName
                        + " contentUri=" + contentUri, e)
                return false
            }

        }

        return try {
            val inputContentInfoCompat = InputContentInfoCompat(
                    contentUri,
                    ClipDescription(description, arrayOf(mimeType)), null/* linkUrl */)
            InputConnectionCompat.commitContent(
                    LatinIME.getInstance().inputLogic.currentInputConnection,
                    editorInfo, inputContentInfoCompat, flag, null)
        } catch (e: Exception) {
            false
        }

    }

    private fun isCommitContentSupported(editorInfo: EditorInfo?, mimeType: String): Boolean {
        LatinIME.getInstance().inputLogic.currentInputConnection ?: return false
        val supportedMimeTypes = EditorInfoCompat.getContentMimeTypes(editorInfo)
        for (supportedMimeType in supportedMimeTypes) {
            if (ClipDescription.compareMimeTypes(mimeType, supportedMimeType)) {
                return true
            }
        }
        return false
    }
}