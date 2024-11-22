package com.tiuho22bangkit.gizi.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface KidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKidData(kid: KidEntity)

    @Query("SELECT * FROM kid_table")
    fun getAllKids(): LiveData<List<KidEntity>>

    @Query("SELECT * FROM kid_table WHERE id = :userId")
    fun getKidById(userId: Int): KidEntity?

    @Update
    fun updateKid(kid: KidEntity)

    @Delete
    fun deleteKid(kid: KidEntity)
}
// suspend yang hanya bisa dijalankan dalam coroutine.