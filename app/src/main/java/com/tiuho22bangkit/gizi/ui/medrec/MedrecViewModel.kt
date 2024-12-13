package com.tiuho22bangkit.gizi.ui.medrec

import androidx.lifecycle.ViewModel
import com.tiuho22bangkit.gizi.data.GiziRepository
import com.tiuho22bangkit.gizi.data.local.entity.KidAnalysisHistoryEntity
import com.tiuho22bangkit.gizi.data.local.entity.MomAnalysisHistoryEntity

class MedrecViewModel(private val giziRepository: GiziRepository) : ViewModel() {

    init {
        loadKidAnalysisHistoryData()
        loadMomAnalysisHistoryData()
    }

    fun loadKidAnalysisHistoryData() = giziRepository.getAllKidAnalysisHistory()
    fun loadMomAnalysisHistoryData() = giziRepository.getAllMomAnalysisHistory()

    fun deleteKidAnalysisHistory(kidAH: KidAnalysisHistoryEntity) =
        giziRepository.deleteKidAnalysisHistory(kidAH)

    fun deleteMomAnalysisHistory(momAH: MomAnalysisHistoryEntity) =
        giziRepository.deleteMomAnalysisHistory(momAH)

}