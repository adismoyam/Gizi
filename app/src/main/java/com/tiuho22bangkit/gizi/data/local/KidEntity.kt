package com.tiuho22bangkit.gizi.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "kid_table")
@Parcelize
data class KidEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @field:ColumnInfo(name = "name")
    val name: String,

    @field:ColumnInfo(name = "uri")
    val uri: String? = null,

    @field:ColumnInfo(name = "gender")
    val gender: String,

    @field:ColumnInfo(name = "birthDate")
    val birthDate: String,

    @field:ColumnInfo(name = "height")
    val height: Float,

    @field:ColumnInfo(name = "weight")
    val weight: Float,
) : Parcelable
