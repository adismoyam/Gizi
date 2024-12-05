package com.tiuho22bangkit.gizi.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "kid_analysis_history_table")
@Parcelize
data class KidAnalysisHistoryEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @field:ColumnInfo(name = "name")
    val name: String,

    @field:ColumnInfo(name = "gender")
    val gender: String,

    @field:ColumnInfo(name = "birthDate")
    val birthDate: String,

    @field:ColumnInfo(name = "height")
    val height: Float,
    // tinggi anak saat dianalisis

    @field:ColumnInfo(name = "weight")
    val weight: Float,
    // berat anak saat dianalisis

    @field:ColumnInfo(name = "datetime")
    val datetime: String,
    // tanggal dan waktu saat dianalisis

    // hasil analisis

    @field:ColumnInfo(name = "wastingResult")
    val wastingRiskResult: String? = null,

    @field:ColumnInfo(name = "stuntingResult")
    val stuntingRiskResult: String? = null,

) : Parcelable
