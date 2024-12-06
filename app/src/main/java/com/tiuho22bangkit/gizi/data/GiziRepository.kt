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
import com.tiuho22bangkit.gizi.data.remote.ChatbotApiService
import com.tiuho22bangkit.gizi.data.remote.MessageRequest
import com.tiuho22bangkit.gizi.data.remote.MessageResponse

class GiziRepository private constructor(
    private val apiService: ApiService,
    private val chatbotApiService: ChatbotApiService,
    private val kidDao: KidDao,
    private val appExecutors: AppExecutors,
    private val momDao: MomDao,
    private val momAnalysisHistoryDao: MomAnalysisHistoryDao,
    private val kidAnalysisHistoryDao: KidAnalysisHistoryDao
) {

    suspend fun sendMessageToChatbot(message: String): String? {
        return try {
            val response = chatbotApiService.sendMessage(MessageRequest(message))
            if (response.isSuccessful) {
                response.body()?.reply
            } else {
                response.errorBody()?.string() ?: "Unknown error"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Error: ${e.message}"
        }
    }

    fun getAllKid(): LiveData<List<KidEntity>> = kidDao.getAllKids()
    fun getKid(id: Int): LiveData<KidEntity> {
        return kidDao.getKidById(id)
    }

    fun getLastKidAnalysisHistory(): LiveData<KidAnalysisHistoryEntity> {
        return kidAnalysisHistoryDao.getLastKidAnalysisHistory()
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
            chatbotApiService: ChatbotApiService,
            kidDao: KidDao,
            appExecutors: AppExecutors,
            momDao: MomDao,
            momAnalysisHistoryDao: MomAnalysisHistoryDao,
            kidAnalysisHistoryDao: KidAnalysisHistoryDao,

            ): GiziRepository =
            instance ?: synchronized(this) {
                instance ?: GiziRepository(
                    apiService,
                    chatbotApiService,
                    kidDao,
                    appExecutors,
                    momDao,
                    momAnalysisHistoryDao,
                    kidAnalysisHistoryDao
                )
            }.also { instance = it }
    }
}