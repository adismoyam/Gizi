package com.tiuho22bangkit.gizi.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiuho22bangkit.gizi.data.GiziRepository
import com.tiuho22bangkit.gizi.data.local.entity.KidEntity
import kotlinx.coroutines.launch

class ProfileViewModel(private val giziRepository: GiziRepository) : ViewModel() {

    private val _kidData: MutableLiveData<List<KidEntity>> = MutableLiveData()
    val kidData: LiveData<List<KidEntity>> get() = _kidData

    init {
        loadKidData()
    }

    private fun loadKidData() {
        viewModelScope.launch {
            giziRepository.getAllKid().observeForever { kids ->
                _kidData.value = kids
            }
        }
    }

}
