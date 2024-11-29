package com.tiuho22bangkit.gizi.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tiuho22bangkit.gizi.data.local.entity.KidAnalysisHistoryEntity

@Dao
interface KidAnalysisHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKidAnalysisHistoryData(kidAnalysisHistory: KidAnalysisHistoryEntity)

    @Query("SELECT * FROM kid_analysis_history_table")
    fun getAllKidAnalysisHistories(): LiveData<List<KidAnalysisHistoryEntity>>

    @Query("SELECT * FROM kid_analysis_history_table WHERE id = :kidAnalysisHistoryId")
    fun getKidAnalysisHistoryById(kidAnalysisHistoryId: Int): KidAnalysisHistoryEntity?

    @Update
    fun updateKidAnalysisHistory(kidAnalysisHistory: KidAnalysisHistoryEntity)

    @Delete
    fun deleteKidAnalysisHistory(kidAnalysisHistory: KidAnalysisHistoryEntity)
}