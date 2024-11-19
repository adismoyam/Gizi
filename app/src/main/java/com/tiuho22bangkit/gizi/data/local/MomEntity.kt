package com.tiuho22bangkit.gizi.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "mom_table")
@Parcelize
data class MomEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @field:ColumnInfo(name = "name")
    val name: String,

    @field:ColumnInfo(name = "uri")
    val uri: String? = null,

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

    @field:ColumnInfo(name = "heartRate")
    val heartRate: Int,
    // jumlah detak jantung per menit (bpm)

) : Parcelable
