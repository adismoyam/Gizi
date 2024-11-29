package com.tiuho22bangkit.gizi.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tiuho22bangkit.gizi.data.local.dao.KidAnalysisHistoryDao
import com.tiuho22bangkit.gizi.data.local.dao.KidDao
import com.tiuho22bangkit.gizi.data.local.dao.MomAnalysisHistoryDao
import com.tiuho22bangkit.gizi.data.local.dao.MomDao
import com.tiuho22bangkit.gizi.data.local.dao.UserDao
import com.tiuho22bangkit.gizi.data.local.entity.KidAnalysisHistoryEntity
import com.tiuho22bangkit.gizi.data.local.entity.KidEntity
import com.tiuho22bangkit.gizi.data.local.entity.MomAnalysisHistoryEntity
import com.tiuho22bangkit.gizi.data.local.entity.MomEntity
import com.tiuho22bangkit.gizi.data.local.entity.UserEntity

@Database(
    entities = [MomEntity::class, KidEntity::class, UserEntity::class, MomAnalysisHistoryEntity::class, KidAnalysisHistoryEntity::class],
    version = 5,
    exportSchema = false
)
abstract class GiziDatabase : RoomDatabase() {
    abstract fun momDao(): MomDao
    abstract fun kidDao(): KidDao
    abstract fun userDao(): UserDao
    abstract fun momAnalysisHistory(): MomAnalysisHistoryDao
    abstract fun kidAnalysisHistory(): KidAnalysisHistoryDao

    companion object {
        @Volatile
        private var instance: GiziDatabase? = null

        fun getInstance(context: Context): GiziDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    GiziDatabase::class.java, "Gizi.db"
                ).fallbackToDestructiveMigration()
                    .build()
            }
    }
}
