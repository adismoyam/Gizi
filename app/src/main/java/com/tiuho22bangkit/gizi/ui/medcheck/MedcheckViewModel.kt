package com.tiuho22bangkit.gizi.ui.medcheck

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MedcheckViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Medcheck Fragment"
    }
    val text: LiveData<String> = _text
}