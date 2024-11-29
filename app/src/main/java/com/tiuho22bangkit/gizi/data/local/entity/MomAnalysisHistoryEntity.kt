package com.tiuho22bangkit.gizi.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "mom_analysis_history_table")
@Parcelize
data class MomAnalysisHistoryEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @field:ColumnInfo(name = "name")
    val name: String? = null,

    @field:ColumnInfo(name= "lastMenstrualPeriod")
    val lastMenstrualPeriod: String,
    // hari pertama haid terakhir (HPHT)

    @field:ColumnInfo(name= "estimatedDeliveryDate")
    val estimatedDeliveryDate: String,
    // hari perkiraan lahir (HPL)

    @field:ColumnInfo(name = "birthDate")
    val birthDate: String,
    // tanggal lahir

    @field:ColumnInfo(name = "systolicBloodPressure")
    val systolicBloodPressure: Float,
    // tekanan darah sistolik (mmHg)

    @field:ColumnInfo(name = "diastolicBloodPressure")
    val diastolicBloodPressure: Float,
    // tekanan darah diastolik (mmHg)

    @field:ColumnInfo(name = "bloodSugarLevel")
    val bloodSugarLevel: Float,
    // kadar gula darah (mmol/L)

    @field:ColumnInfo(name = "bodyTemperature")
    val bodyTemperature: Float,
    // suhu tubuh

    @field:ColumnInfo(name = "heartRate")
    val heartRate: Float,
    // jumlah detak jantung per menit (bpm)

    @field:ColumnInfo(name = "datetime")
    val datetime: String,
    // tanggal dan waktu saat dianalisis

    @field:ColumnInfo(name = "result")
    val result:String
    // hasil analisis
) : Parcelable
