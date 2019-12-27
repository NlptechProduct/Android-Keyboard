package com.nlptech.function.theme.download_theme;

import android.util.Log;

import androidx.annotation.WorkerThread;

import com.nlptech.common.api.RequestManager;
import com.nlptech.common.api.ResultData;
import com.nlptech.keyboardview.theme.download.DownloadThemeManager;
import com.nlptech.keyboardview.theme.download.ThemeDownloadInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class DownloadThemeDataFetcher implements DownloadThemeManager.Listener {
    private static final String TAG = DownloadThemeDataFetcher.class.getSimpleName();

    @Override
    @WorkerThread
    public ArrayList<ThemeDownloadInfo> onFetchThemeDownloadInfo() {
        ArrayList<ThemeDownloadInfo> result = new ArrayList<>();
        Call<ResultData<List<DownloadThemeApiItem>>> call = RequestManager.getInstance()
                .obtainRetrofit().create(DownloadThemeApi.class).fetchItems();
        try {
            Response<ResultData<List<DownloadThemeApiItem>>> response = call.execute();
            ResultData<List<DownloadThemeApiItem>> resultData = response.body();
            if (resultData == null) {
                return result;
            }
            List<DownloadThemeApiItem> items = resultData.data;
            if (items == null) {
                return result;
            }
            for (DownloadThemeApiItem item : items) {
                ThemeDownloadInfo downloadInfo = new ThemeDownloadInfo();
                downloadInfo.setThemeId(item.getTheme_id());
                downloadInfo.setThemeName(item.getTheme_name());
                downloadInfo.setThemeUrl(item.getTheme_url());
                downloadInfo.setThemeCover(item.getTheme_cover());
                downloadInfo.setThemeCoverWithBorder(item.getTheme_cover_with_border());
                downloadInfo.setMode(Integer.parseInt(item.getMode()));
                downloadInfo.setMd5(item.getMd5());
                downloadInfo.setSize(item.getSize());
                downloadInfo.setVersion(item.getVersion());
                result.add(downloadInfo);
            }
            return result;
        } catch (IOException e) {
            Log.e(TAG, "Fetch download theme data e: " + e);
            return result;
        }
    }
}
