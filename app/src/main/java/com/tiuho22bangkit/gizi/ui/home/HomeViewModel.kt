package com.tiuho22bangkit.gizi.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tiuho22bangkit.gizi.data.GiziRepository
import com.tiuho22bangkit.gizi.data.local.entity.KidAnalysisHistoryEntity
import com.tiuho22bangkit.gizi.data.local.entity.KidEntity
import com.tiuho22bangkit.gizi.data.remote.ApiConfig
import com.tiuho22bangkit.gizi.data.remote.JumainResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class HomeViewModel(private val giziRepository: GiziRepository) : ViewModel() {
    private val _isMomDataAvailable: MutableLiveData<Boolean> = MutableLiveData()
    val isMomDataAvailable: LiveData<Boolean> get() = _isMomDataAvailable

    private val _allArticles: MutableLiveData<List<JumainResponseItem>> = MutableLiveData()
    val allArticles: LiveData<List<JumainResponseItem>> get() = _allArticles

    private val _errorMessage: MutableLiveData<String?> = MutableLiveData()
    val errorMessage: MutableLiveData<String?> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadKidData()
        checkMomData()
        findArticles()
    }

    fun loadKidDataFromFirebase(token: String): LiveData<List<KidEntity>> {
        val liveData = MutableLiveData<List<KidEntity>>()
        val databaseReference = FirebaseDatabase.getInstance().getReference("kids")
        databaseReference.orderByChild("token").equalTo(token)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val kidList = mutableListOf<KidEntity>()
                    for (data in snapshot.children) {
                        val kid = data.getValue(KidEntity::class.java)
                        if (kid != null) {
                            kidList.add(kid)
                        }
                    }
                    liveData.postValue(kidList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Error fetching data: ${error.message}")
                }
            })
        return liveData
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

    fun loadKidData() = giziRepository.getAllKid()

    fun checkMomData() {
        viewModelScope.launch(Dispatchers.IO) {
            _isMomDataAvailable.postValue(giziRepository.checkTheMom())
        }
    }

    val lastKidAnalysisHistory: LiveData<KidAnalysisHistoryEntity> = giziRepository.getLastKidAnalysisHistory()

    fun loadMomData() = giziRepository.getTheMom()
}