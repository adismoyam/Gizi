package com.tiuho22bangkit.gizi.data

import androidx.lifecycle.LiveData
import com.tiuho22bangkit.gizi.data.local.dao.KidAnalysisHistoryDao
import com.tiuho22bangkit.gizi.data.local.dao.KidDao
import com.tiuho22bangkit.gizi.data.local.dao.MomAnalysisHistoryDao
import com.tiuho22bangkit.gizi.data.local.entity.KidEntity
import com.tiuho22bangkit.gizi.data.local.dao.MomDao
import com.tiuho22bangkit.gizi.data.local.entity.KidAnalysisHistoryEntity
import com.tiuho22bangkit.gizi.data.local.entity.MomAnalysisHistoryEntity
import com.tiuho22bangkit.gizi.data.local.entity.MomEntity
import com.tiuho22bangkit.gizi.data.remote.ApiService

class GiziRepository private constructor(
    private val apiService: ApiService,
    private val kidDao: KidDao,
    private val appExecutors: AppExecutors,
    private val momDao: MomDao,
    private val momAnalysisHistoryDao: MomAnalysisHistoryDao,
    private val kidAnalysisHistoryDao: KidAnalysisHistoryDao
) {

    fun getAllKid(): LiveData<List<KidEntity>> = kidDao.getAllKids()
    fun getKid(id: Int): LiveData<KidEntity> {
        return kidDao.getKidById(id)
    }
    fun getTheMom(): LiveData<MomEntity> = momDao.getTheMom()

    fun addKidAnalysisHistory(kidAnalysisHistoryEntity: KidAnalysisHistoryEntity) {
        kidAnalysisHistoryDao.insertKidAnalysisHistoryData(kidAnalysisHistoryEntity)
    }

    fun addMomAnalysisHistory(momAnalysisHistoryEntity: MomAnalysisHistoryEntity) {
        momAnalysisHistoryDao.insertMomAnalysisHistoryData(momAnalysisHistoryEntity)
    }

    fun checkTheMom(): Boolean {
        return momDao.checkTheMom() > 0
    }

    fun getAllKidAnalysisHistory(): LiveData<List<KidAnalysisHistoryEntity>> =
        kidAnalysisHistoryDao.getAllKidAnalysisHistories()

    fun getAllMomAnalysisHistory(): LiveData<List<MomAnalysisHistoryEntity>> =
        momAnalysisHistoryDao.getAllMomAnalysisHistories()

    companion object {
        @Volatile
        private var instance: GiziRepository? = null
        fun getInstance(
            apiService: ApiService,
            kidDao: KidDao,
            appExecutors: AppExecutors,
            momDao: MomDao,
            momAnalysisHistoryDao: MomAnalysisHistoryDao,
            kidAnalysisHistoryDao: KidAnalysisHistoryDao,

            ): GiziRepository =
            instance ?: synchronized(this) {
                instance ?: GiziRepository(
                    apiService,
                    kidDao,
                    appExecutors,
                    momDao,
                    momAnalysisHistoryDao,
                    kidAnalysisHistoryDao
                )
            }.also { instance = it }
    }
}