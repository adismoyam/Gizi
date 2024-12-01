package com.tiuho22bangkit.gizi.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.tiuho22bangkit.gizi.data.local.entity.MomEntity

@Dao
interface MomDao {
    @Insert
    fun insertMomData(mom: MomEntity)

//    @Query("SELECT * FROM mom_table")
//    fun getTheMom(): LiveData<List<MomEntity>>

    @Query("SELECT * FROM mom_table ORDER BY id DESC LIMIT 1")
    fun getTheMom(): LiveData<MomEntity>


    @Query("SELECT COUNT(*) FROM mom_table")
    fun checkTheMom(): Int

    @Update
    suspend fun updateTheMom(mom: MomEntity)

    @Delete
    suspend fun deleteTheMom(mom: MomEntity)
}