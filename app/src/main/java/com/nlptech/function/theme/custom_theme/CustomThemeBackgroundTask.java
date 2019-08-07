package com.nlptech.function.theme.custom_theme;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

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
public class CustomThemeBackgroundTask extends AsyncTask<String, Void, File> {

    public interface Listener {
        void onBackgroundResult(File backgroundFile);

        Context getContext();
    }

    private WeakReference<Listener> mListener;

    public CustomThemeBackgroundTask(Listener listener) {
        this.mListener = new WeakReference<Listener>(listener);
    }

    @Override
    protected File doInBackground(String... strings) {
        Uri from = Uri.parse(strings[0]);
        File to = new File(strings[1]);
        try {
            final Listener listener = mListener.get();
            final Context context;
            if (listener != null) {
                context = listener.getContext();
            } else {
                return to;
            }
            File temp = Glide
                    .with(context)
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

        return to;
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        final Listener listener = mListener.get();
        if (listener != null) {
            listener.onBackgroundResult(file);
        }
    }
}
