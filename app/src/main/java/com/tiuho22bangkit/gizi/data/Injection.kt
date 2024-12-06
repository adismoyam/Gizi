package com.tiuho22bangkit.gizi.data

import android.content.Context
import com.tiuho22bangkit.gizi.data.local.GiziDatabase
import com.tiuho22bangkit.gizi.data.remote.ApiConfig
import com.tiuho22bangkit.gizi.data.remote.ChatbotApiConfig

object Injection {
    fun provideGiziRepository(context: Context): GiziRepository {
        val apiService = ApiConfig.getApiService()
        val chatbotApiService = ChatbotApiConfig.getChatbotApiService()
        val database = GiziDatabase.getInstance(context)
        val kidDao = database.kidDao()
        val appExecutors = AppExecutors()
        val momDao = database.momDao()
        val momAnalysisHistoryDao = database.momAnalysisHistory()
        val kidAnalysisHistoryDao = database.kidAnalysisHistory()
        return GiziRepository.getInstance(apiService, chatbotApiService, kidDao, appExecutors, momDao, momAnalysisHistoryDao, kidAnalysisHistoryDao)
    }
    fun provideUserRepository(context: Context): UserRepository {
        val database = GiziDatabase.getInstance(context)
        val userDao = database.userDao()
        val appExecutors = AppExecutors()
        return UserRepository.getInstance(userDao, appExecutors)
    }
}
