package com.tiuho22bangkit.gizi.ui.nutriai

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NutriaiViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is NutriAI Fragment"
    }
    val text: LiveData<String> = _text
}