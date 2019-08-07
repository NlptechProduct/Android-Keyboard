package com.nlptech.function.theme.custom_theme;

import android.os.AsyncTask;

import com.nlptech.common.utils.FileUtils;
import com.nlptech.keyboardview.theme.custom.CustomTheme;

import java.io.File;
import java.lang.ref.WeakReference;

public class CustomThemeDeleteTask extends AsyncTask<CustomTheme, Void, Boolean> {

    public interface Listener {
        void onDeleteResult();
    }

    private WeakReference<Listener> mListener;

    public CustomThemeDeleteTask(Listener listener) {
        this.mListener = new WeakReference<Listener>(listener);
    }

    @Override
    protected Boolean doInBackground(CustomTheme... customThemes) {
        CustomTheme customTheme = customThemes[0];
        File themeDir = new File(customTheme.getThemeDirPath());
        FileUtils.deleteDir(themeDir);
        return true;
    }

    @Override
    protected void onPostExecute(Boolean b) {
        super.onPostExecute(b);
        Listener listener = mListener.get();
        if (listener != null) {
            listener.onDeleteResult();
        }
    }
}
