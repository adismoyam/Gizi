package com.tiuho22bangkit.gizi.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [MomEntity::class, KidEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GiziDatabase : RoomDatabase() {
    abstract fun momDao(): MomDao
    abstract fun kidDao(): KidDao

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
