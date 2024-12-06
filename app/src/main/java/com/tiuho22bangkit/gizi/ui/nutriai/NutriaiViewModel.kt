package com.tiuho22bangkit.gizi.ui.nutriai

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiuho22bangkit.gizi.data.GiziRepository
import kotlinx.coroutines.launch

class NutriaiViewModel(private val repository: GiziRepository) : ViewModel() {

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response

    fun sendMessage(prompt: String) {
        viewModelScope.launch {
            val result = repository.sendMessageToChatbot(prompt)
            _response.postValue(result?.response ?: "Failed to get response")
        }
    }
}