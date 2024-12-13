package com.tiuho22bangkit.gizi.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tiuho22bangkit.gizi.data.local.dao.KidAnalysisHistoryDao
import com.tiuho22bangkit.gizi.data.local.dao.KidDao
import com.tiuho22bangkit.gizi.data.local.dao.MomAnalysisHistoryDao
import com.tiuho22bangkit.gizi.data.local.entity.KidEntity
import com.tiuho22bangkit.gizi.data.local.dao.MomDao
import com.tiuho22bangkit.gizi.data.local.entity.KidAnalysisHistoryEntity
import com.tiuho22bangkit.gizi.data.local.entity.MomAnalysisHistoryEntity
import com.tiuho22bangkit.gizi.data.local.entity.MomEntity
import com.tiuho22bangkit.gizi.data.remote.ApiService
import com.tiuho22bangkit.gizi.data.remote.ChatRequest
import com.tiuho22bangkit.gizi.data.remote.ChatResponse
import com.tiuho22bangkit.gizi.data.remote.ChatbotApiService

class GiziRepository private constructor(
    private val apiService: ApiService,
    private val chatbotApiService: ChatbotApiService,
    private val kidDao: KidDao,
    private val appExecutors: AppExecutors,
    private val momDao: MomDao,
    private val momAnalysisHistoryDao: MomAnalysisHistoryDao,
    private val kidAnalysisHistoryDao: KidAnalysisHistoryDao
) {


    suspend fun sendMessageToChatbot(id: String, prompt: String): ChatResponse {
        return chatbotApiService.sendMessage(ChatRequest(id, prompt))
    }

    fun getAllKid(): LiveData<List<KidEntity>> = kidDao.getAllKids()
    fun getKid(id: String): LiveData<KidEntity> {
        return kidDao.getKidById(id)
    }


    fun getKidFromFirebase(id: String): LiveData<KidEntity?> {
        val result = MutableLiveData<KidEntity?>()
        val database = FirebaseDatabase.getInstance().reference.child("kids").child(id)

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val kid = snapshot.getValue(KidEntity::class.java)
                    result.value = kid
                } else {
                    result.value = null
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error fetching data: ${error.message}")
                result.value = null
            }
        })

        return result
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

    fun deleteKidAnalysisHistory(kidAH: KidAnalysisHistoryEntity) =
        kidAnalysisHistoryDao.deleteKidAnalysisHistory(kidAH)

    fun deleteMomAnalysisHistory(momAH: MomAnalysisHistoryEntity) =
        momAnalysisHistoryDao.deleteMomAnalysisHistory(momAH)


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