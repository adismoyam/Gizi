package com.tiuho22bangkit.gizi.ui.medrec

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tiuho22bangkit.gizi.data.GiziRepository

class MedrecViewModel(private val giziRepository: GiziRepository) : ViewModel() {


    init {
        loadKidAnalysisHistoryData()
        loadMomAnalysisHistoryData()
    }

    fun loadKidAnalysisHistoryData() = giziRepository.getAllKidAnalysisHistory()
    fun loadMomAnalysisHistoryData() = giziRepository.getAllMomAnalysisHistory()

}