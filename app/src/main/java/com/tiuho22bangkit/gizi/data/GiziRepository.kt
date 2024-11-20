package com.tiuho22bangkit.gizi.data

import com.tiuho22bangkit.gizi.data.local.KidDao
import com.tiuho22bangkit.gizi.data.local.MomDao
import com.tiuho22bangkit.gizi.data.remote.ApiService

class GiziRepository private constructor(
    private val apiService: ApiService,
    private val kidDao: KidDao,
    private val appExecutors: AppExecutors,
    private val momDao: MomDao,
)  {

    companion object {
        @Volatile
        private var instance: GiziRepository? = null
        fun getInstance(
            apiService: ApiService,
            kidDao: KidDao,
            appExecutors: AppExecutors,
            momDao: MomDao,
        ): GiziRepository =
            instance ?: synchronized(this) {
                instance ?: GiziRepository(apiService, kidDao, appExecutors, momDao)
            }.also { instance = it }
    }
}