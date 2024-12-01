package com.tiuho22bangkit.gizi.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiuho22bangkit.gizi.data.GiziRepository
import com.tiuho22bangkit.gizi.data.local.entity.KidEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(private val giziRepository: GiziRepository) : ViewModel() {

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
