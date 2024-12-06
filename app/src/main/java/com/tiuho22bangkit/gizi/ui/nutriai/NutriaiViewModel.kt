package com.tiuho22bangkit.gizi.ui.nutriai

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiuho22bangkit.gizi.data.GiziRepository
import kotlinx.coroutines.launch

class NutriaiViewModel(private val repository: GiziRepository) : ViewModel() {

    private val _chatResponse = MutableLiveData<String>()
    val chatResponse: LiveData<String> = _chatResponse

    fun sendMessage(message: String) {
        viewModelScope.launch {
            val response = repository.sendMessageToChatbot(message)
            _chatResponse.value = response
        }
    }
}