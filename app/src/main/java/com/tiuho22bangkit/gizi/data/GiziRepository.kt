package com.tiuho22bangkit.gizi.data

import androidx.lifecycle.LiveData
import com.tiuho22bangkit.gizi.data.local.dao.KidDao
import com.tiuho22bangkit.gizi.data.local.entity.KidEntity
import com.tiuho22bangkit.gizi.data.local.dao.MomDao
import com.tiuho22bangkit.gizi.data.remote.ApiService

class GiziRepository private constructor(
    private val apiService: ApiService,
    private val kidDao: KidDao,
    private val appExecutors: AppExecutors,
    private val momDao: MomDao,
)  {

    fun getAllKid(): LiveData<List<KidEntity>> = kidDao.getAllKids()

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