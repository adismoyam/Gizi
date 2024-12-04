package com.tiuho22bangkit.gizi.data.remote;

import com.tiuho22bangkit.gizi.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate

interface ApiService {
    @GET("everything")
    fun getArticles(
        @Query("q") query: String = "hamil OR asi OR gizi OR stunting OR balita OR mental",
        @Query("from") from: String = LocalDate.now().minusMonths(1)
            .toString(),  // batasnya cuma sebulan yang lalu
        @Query("language") language: String = "id",
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): Call<NewsAPIResponse>
}

//https://newsapi.org/v2/everything?q=hamil%20OR%20asi%20OR%20gizi%20OR%20stunting%20OR%20balita%20OR%20mental&from=2024-10-19&language=id&apiKey=a2c7c8f11d164471a4b66dc1696b5186
