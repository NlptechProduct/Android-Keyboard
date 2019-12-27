package com.nlptech.function.theme.download_theme;


import com.nlptech.common.api.ResultData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DownloadThemeApi {

    @GET("theme/list/get")
    Call<ResultData<List<DownloadThemeApiItem>>> fetchItems();
}
