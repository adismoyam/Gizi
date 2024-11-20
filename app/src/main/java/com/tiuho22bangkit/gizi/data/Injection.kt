package com.tiuho22bangkit.gizi.data

import android.content.Context
import com.tiuho22bangkit.gizi.data.local.GiziDatabase
import com.tiuho22bangkit.gizi.data.remote.ApiConfig

object Injection {
    fun provideRepository(context: Context): GiziRepository {
        val apiService = ApiConfig.getApiService()
        val database = GiziDatabase.getInstance(context)
        val kidDao = database.kidDao()
        val momDao = database.momDao()
        val appExecutors = AppExecutors()
        return GiziRepository.getInstance(apiService, kidDao, appExecutors, momDao)
    }
}
