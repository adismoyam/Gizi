package com.tiuho22bangkit.gizi.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiuho22bangkit.gizi.data.UserRepository
import com.tiuho22bangkit.gizi.data.local.UserEntity
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository ) : ViewModel() {
    private val _loginResult = MutableLiveData<Result<UserEntity>>()
    val loginResult: LiveData<Result<UserEntity>> = _loginResult

    private val _registerResult = MutableLiveData<Result<String>>()
    val registerResult: LiveData<Result<String>> = _registerResult

    fun registerUser(username: String, email: String, password: String, role: String) {
        viewModelScope.launch {
            val user = UserEntity(username = username, email = email, password = password, role = role)
            _registerResult.postValue(repository.registerUser(user))
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.postValue(repository.loginUser(email, password))
        }
    }
}