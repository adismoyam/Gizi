package com.tiuho22bangkit.gizi.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MomDao {
    @Insert
    fun insertMomData(mom: MomEntity)

    @Query("SELECT * FROM mom_table")
    suspend fun getTheMom(): List<MomEntity>

    @Update
    suspend fun updateTheMom(mom: MomEntity)

    @Delete
    suspend fun deleteTheMom(mom: MomEntity)
}