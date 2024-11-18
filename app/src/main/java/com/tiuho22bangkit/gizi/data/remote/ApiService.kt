package com.tiuho22bangkit.gizi.data.remote;

import com.tiuho22bangkit.gizi.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("everything")
    fun getArticles(
        @Query("q") query: String = "hamil OR asi OR gizi OR stunting OR balita OR mental",
        @Query("from") from: String= "2024-10-18",  // sa sengaja batasi artikelnya muncul yang diupload
                                                    // sejak 15 hari yang lalu supaya nda tro banyak muncul
        @Query("language") language: String = "id",
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): Call<NewsAPIResponse>
}

