package com.tiuho22bangkit.gizi.ui.medrec

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MedrecViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Medrec Fragment"
    }
    val text: LiveData<String> = _text
}