package com.tiuho22bangkit.gizi.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiuho22bangkit.gizi.data.GiziRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val giziRepository: GiziRepository) : ViewModel() {
    private val _isMomDataAvailable: MutableLiveData<Boolean> = MutableLiveData()
    val isMomDataAvailable: LiveData<Boolean> get() = _isMomDataAvailable

    init {
        loadKidData()
        checkMomData()
    }
    fun loadKidData() = giziRepository.getAllKid()

    fun checkMomData() {
        viewModelScope.launch(Dispatchers.IO) {
            _isMomDataAvailable.postValue(giziRepository.checkTheMom())
        }
    }

    fun loadMomData() = giziRepository.getTheMom()
}