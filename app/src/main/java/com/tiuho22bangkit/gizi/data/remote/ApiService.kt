package com.tiuho22bangkit.gizi.data.remote;

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("articles")
    fun getArticles(
    ): Call<List<JumainResponseItem>>
}

