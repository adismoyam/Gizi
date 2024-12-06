package com.tiuho22bangkit.gizi.ui.article.ijustwannasavethiscode

//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.tiuho22bangkit.gizi.data.remote.ApiConfig
////import com.tiuho22bangkit.gizi.data.remote.ArticlesItem
//import com.tiuho22bangkit.gizi.data.remote.JumainResponseItem
////import com.tiuho22bangkit.gizi.data.remote.NewsAPIResponse
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.io.IOException
//
//class ArticleViewModel : ViewModel() {
//
//    private val _articleData: MutableLiveData<List<JumainResponseItem>> = MutableLiveData()
////    private val _articleData: MutableLiveData<List<ArticlesItem>> = MutableLiveData()
//    val articleData: LiveData<List<JumainResponseItem>> get() = _articleData
////    val articleData: LiveData<List<ArticlesItem>> get() = _articleData
//
//    private val _errorMessage: MutableLiveData<String?> = MutableLiveData()
//    val errorMessage: MutableLiveData<String?> get() = _errorMessage
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    fun findArticles() {
//        _isLoading.value = true
//        val client = ApiConfig.getApiService().getArticles()
//
////        client.enqueue(object : Callback<NewsAPIResponse> {
//        client.enqueue(object : Callback<List<JumainResponseItem>> {
////            override fun onResponse(call: Call<NewsAPIResponse>, response: Response<NewsAPIResponse>) {
//            override fun onResponse(call: Call<List<JumainResponseItem>>, response: Response<List<JumainResponseItem>>) {
//                _isLoading.value = false
//
//                if (response.isSuccessful) {
//                    val articleData = response.body()?.filter { it.url != "https://removed.com" } ?: emptyList()
////                    val articleData = response.body()?.articles?.filter { it.url != "https://removed.com" } ?: emptyList()
//                    if (articleData.isNotEmpty()) {
//                        Log.d("MyViewModel", "Data received: $articleData")
//                        _articleData.value = articleData
//                        _errorMessage.value = null
//                    } else {
//                        _errorMessage.value = "No data available."
//                        Log.e("MyViewModel", "No data received")
//                    }
//                } else {
//                    _errorMessage.value = "Failed to load data. Please try again."
//                    Log.e("MyViewModel", "onFailure: ${response.message()}")
//                }
//            }
//
////            override fun onFailure(call: Call<NewsAPIResponse>, t: Throwable) {
//            override fun onFailure(call: Call<List<JumainResponseItem>>, t: Throwable) {
//                _isLoading.value = false
//                Log.e("MyViewModel", "onFailure: ${t.message}")
//
//                if (t is IOException) {
//                    Log.e("MyViewModel", "onFailure: No internet connection", t)
//                    _errorMessage.value = "No internet connection. Please check your connection."
//                } else {
//                    Log.e("MyViewModel", "onFailure: ${t.message}", t)
//                    _errorMessage.value = "Failed to load data. Please try again."
//                }
//            }
//        })
//    }
//}