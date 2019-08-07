package com.nlptech.function.theme.custom_theme;

import android.os.AsyncTask;

import com.nlptech.common.utils.FileUtils;
import com.nlptech.keyboardview.theme.custom.CustomTheme;

import java.io.File;
import java.lang.ref.WeakReference;

public class CustomThemeSaveTask extends AsyncTask<CustomTheme, Void, File> {

    public interface Listener {
        void onSaveResult(File backgroundFile);
    }

    private WeakReference<Listener> mListener;

    public CustomThemeSaveTask(Listener listener) {
        this.mListener = new WeakReference<Listener>(listener);
    }

    @Override
    protected File doInBackground(CustomTheme... customThemes) {
        CustomTheme customTheme = customThemes[0];
        File themeDataFile = new File(customTheme.getThemeDataFilePath());
        FileUtils.saveObject(customTheme, themeDataFile);
        return themeDataFile;
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        Listener listener = mListener.get();
        if (listener != null) {
            listener.onSaveResult(file);
        }
    }
}
