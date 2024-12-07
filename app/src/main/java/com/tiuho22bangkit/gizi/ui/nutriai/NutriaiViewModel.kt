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

    fun sendChatToApi(prompt: String) {
        viewModelScope.launch {
            try {
                val result = repository.sendMessageToChatbot(prompt) // Panggil fungsi API dari repository
                _response.value = result.response // Pastikan sesuai dengan format API
            } catch (e: Exception) {
                _response.value = "Error: ${e.message}" // Tangani error
            }
        }
    }
}
