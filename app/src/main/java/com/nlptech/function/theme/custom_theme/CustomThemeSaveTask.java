package com.nlptech.function.theme.custom_theme;

import android.os.AsyncTask;
import android.util.Log;

import com.nlptech.common.utils.FileUtils;
import com.nlptech.keyboardview.theme.custom.CustomTheme;

import java.io.File;
import java.lang.ref.WeakReference;

public class CustomThemeSaveTask extends AsyncTask<CustomTheme, Void, File[]> {

    public interface Listener {
        void onSaveResult(File[] backgroundFiles);
    }

    private WeakReference<Listener> mListener;

    public CustomThemeSaveTask(Listener listener) {
        this.mListener = new WeakReference<Listener>(listener);
    }

    @Override
    protected File[] doInBackground(CustomTheme... customThemes) {
        File[] result = new File[customThemes.length];
        for (int i = 0; i < customThemes.length; i++) {
            CustomTheme customTheme = customThemes[i];
            if (customTheme != null) {
                File themeDataFile = new File(customTheme.getThemeDataFilePath());
                FileUtils.saveObject(customTheme, themeDataFile);
                result[i] = themeDataFile;
            }

        }
        return result;
    }

    @Override
    protected void onPostExecute(File[] files) {
        super.onPostExecute(files);
        Listener listener = mListener.get();
        if (listener != null) {
            listener.onSaveResult(files);
        }
    }
}
