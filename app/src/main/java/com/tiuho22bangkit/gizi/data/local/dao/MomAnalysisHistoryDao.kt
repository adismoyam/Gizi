package com.tiuho22bangkit.gizi.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tiuho22bangkit.gizi.data.local.entity.MomAnalysisHistoryEntity

@Dao
interface MomAnalysisHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMomAnalysisHistoryData(momAnalysisHistory: MomAnalysisHistoryEntity)

    @Query("SELECT * FROM mom_analysis_history_table")
    fun getAllMomAnalysisHistories(): LiveData<List<MomAnalysisHistoryEntity>>

    @Query("SELECT * FROM mom_analysis_history_table WHERE id = :momAnalysisHistoryId")
    fun getMomAnalysisHistoryById(momAnalysisHistoryId: Int): MomAnalysisHistoryEntity?

    @Update
    fun updateMomAnalysisHistory(momAnalysisHistory: MomAnalysisHistoryEntity)

    @Delete
    fun deleteMomAnalysisHistory(momAnalysisHistory: MomAnalysisHistoryEntity)
}