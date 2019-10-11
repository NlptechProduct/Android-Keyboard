package com.nlptech.function.theme.custom_theme;

import android.net.Uri;
import android.os.AsyncTask;

import com.android.inputmethod.TestApplication;
import com.bumptech.glide.Glide;
import com.nlptech.common.utils.FileUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

/**
 * 參數0:來源uri
 * 參數1:儲存檔案的路徑
 * <p>
 * 回傳:參數1
 **/
public class CustomThemeBackgroundTask extends AsyncTask<String, Void, File[]> {

    public interface Listener {
        void onBackgroundResult(File[] backgroundFiles);
    }

    private WeakReference<Listener> mListener;

    public CustomThemeBackgroundTask(Listener listener) {
        this.mListener = new WeakReference<Listener>(listener);
    }

    @Override
    protected File[] doInBackground(String... strings) {
        File[] result = new File[strings.length / 2];
        int index = 0;
        for (int i = 0; i <strings.length; i+=2) {
            Uri from = Uri.parse(strings[i]);
            File to = new File(strings[i+1]);
            try {
                File temp = Glide
                        .with(TestApplication.getInstance())
                        .downloadOnly()
                        .load(from)
                        .submit()
                        .get();
                FileUtils.copy(temp, to);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            result[index++] = to;
        }

        return result;
    }

    @Override
    protected void onPostExecute(File[] files) {
        super.onPostExecute(files);
        Listener listener = mListener.get();
        if (listener != null) {
            listener.onBackgroundResult(files);
        }
    }
}
