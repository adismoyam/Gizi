package com.tiuho22bangkit.gizi.ui.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiuho22bangkit.gizi.data.GiziRepository
import com.tiuho22bangkit.gizi.data.local.entity.KidAnalysisHistoryEntity
import com.tiuho22bangkit.gizi.data.local.entity.KidEntity
import com.tiuho22bangkit.gizi.data.local.entity.MomAnalysisHistoryEntity
import com.tiuho22bangkit.gizi.data.local.entity.MomEntity
import com.tiuho22bangkit.gizi.utility.millisToDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AnalysisViewModel(private val giziRepository: GiziRepository) : ViewModel() {

    fun saveKidAnalysisResult(kid: KidEntity, wastingResult: String, stuntingResult: String) {
        val resultData = KidAnalysisHistoryEntity(
            name = kid.name,
            gender = kid.gender,
            birthDate = kid.birthDate,
            height = kid.height,
            weight = kid.weight,
            datetime = millisToDate(System.currentTimeMillis()),
            wastingRiskResult = wastingResult,
            stuntingRiskResult = stuntingResult
        )

        viewModelScope.launch(Dispatchers.IO) {
            giziRepository.addKidAnalysisHistory(resultData)
        }
    }

    fun saveMomAnalysisResult(mom: MomEntity, result: String){
        val resultData = MomAnalysisHistoryEntity(
            name = mom.name,
            lastMenstrualPeriod = mom.lastMenstrualPeriod,
            estimatedDeliveryDate = mom.estimatedDeliveryDate,
            birthDate = mom.birthDate,
            systolicBloodPressure = mom.systolicBloodPressure,
            diastolicBloodPressure = mom.diastolicBloodPressure,
            bloodSugarLevel = mom.bloodSugarLevel,
            bodyTemperature = mom.bodyTemperature,
            heartRate = mom.heartRate,
            datetime = millisToDate(System.currentTimeMillis()),
            result = result
        )
        viewModelScope.launch(Dispatchers.IO) {
            giziRepository.addMomAnalysisHistory(resultData)
        }
    }

}