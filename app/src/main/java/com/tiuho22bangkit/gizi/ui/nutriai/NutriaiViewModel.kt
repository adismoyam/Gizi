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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun sendChatToApi(id: String, prompt: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.sendMessageToChatbot(id, prompt)
                _response.value = result.response
            } catch (e: Exception) {
                _response.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
