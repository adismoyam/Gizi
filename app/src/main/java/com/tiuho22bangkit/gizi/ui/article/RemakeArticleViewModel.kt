package com.tiuho22bangkit.gizi.ui.article

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tiuho22bangkit.gizi.data.remote.ApiConfig
//import com.tiuho22bangkit.gizi.data.remote.ArticlesItem
import com.tiuho22bangkit.gizi.data.remote.JumainResponseItem
//import com.tiuho22bangkit.gizi.data.remote.NewsAPIResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class RemakeArticleViewModel : ViewModel() {

    private val _allArticles: MutableLiveData<List<JumainResponseItem>> = MutableLiveData()
    val allArticles: LiveData<List<JumainResponseItem>> get() = _allArticles

    private val _errorMessage: MutableLiveData<String?> = MutableLiveData()
    val errorMessage: MutableLiveData<String?> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData untuk setiap kategori
    private val _rekomendasiArticles = MutableLiveData<List<JumainResponseItem>>()
    val rekomendasiArticles: LiveData<List<JumainResponseItem>> get() = _rekomendasiArticles

    private val _kehamilanArticles = MutableLiveData<List<JumainResponseItem>>()
    val kehamilanArticles: LiveData<List<JumainResponseItem>> get() = _kehamilanArticles

    private val _nutrisiArticles = MutableLiveData<List<JumainResponseItem>>()
    val nutrisiArticles: LiveData<List<JumainResponseItem>> get() = _nutrisiArticles

    private val _parentingArticles = MutableLiveData<List<JumainResponseItem>>()
    val parentingArticles: LiveData<List<JumainResponseItem>> get() = _parentingArticles

    init {
        findArticles()
    }

    fun findArticles() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getArticles()

        client.enqueue(object : Callback<List<JumainResponseItem>> {
            override fun onResponse(call: Call<List<JumainResponseItem>>, response: Response<List<JumainResponseItem>>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    val articles = response.body()?.filter { it.url != "https://removed.com" } ?: emptyList()
                    if (articles.isNotEmpty()) {
                        Log.d("MyViewModel", "Data received: $articles")
                        _allArticles.value = articles
                        _errorMessage.value = null
                        distributeArticlesByCategory(articles) // Memproses data untuk kategori
                    } else {
                        _errorMessage.value = "No data available."
                        Log.e("MyViewModel", "No data received")
                    }
                } else {
                    _errorMessage.value = "Failed to load data. Please try again."
                    Log.e("MyViewModel", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<JumainResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e("MyViewModel", "onFailure: ${t.message}")

                if (t is IOException) {
                    Log.e("MyViewModel", "onFailure: No internet connection", t)
                    _errorMessage.value = "No internet connection. Please check your connection."
                } else {
                    Log.e("MyViewModel", "onFailure: ${t.message}", t)
                    _errorMessage.value = "Failed to load data. Please try again."
                }
            }
        })
    }

    private fun distributeArticlesByCategory(articles: List<JumainResponseItem>) {
        // Memfilter data berdasarkan kategori
        _rekomendasiArticles.value = articles.filter { it.category == "rekomendasi" }
        _kehamilanArticles.value = articles.filter { it.category == "kehamilan" }
        _nutrisiArticles.value = articles.filter { it.category == "nutrisi" }
        _parentingArticles.value = articles.filter { it.category == "parenting" }
    }
}
