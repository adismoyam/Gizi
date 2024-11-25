package com.tiuho22bangkit.gizi.utility

import android.util.Log
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

//  fungsi untuk menghitung perbedaan waktu
fun calculateTimeDifference(dateString: String): String {

    // menyesuaikan format tahun-bulan-hari
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // Ubah string menjadi LocalDate
    val inputDate = LocalDate.parse(dateString, formatter)

    // Tanggal saat ini
    val currentDate = LocalDate.now()

    // Hitung selisih waktu
    val daysBetween = ChronoUnit.DAYS.between(inputDate, currentDate)
    val monthsBetween = ChronoUnit.MONTHS.between(inputDate, currentDate)
    val yearsBetween = ChronoUnit.YEARS.between(inputDate, currentDate)

    return when {
        yearsBetween > 0 -> "$yearsBetween tahun yang lalu"
        monthsBetween > 0 -> "$monthsBetween bulan yang lalu"
        daysBetween > 0 -> "$daysBetween hari yang lalu"
        else -> "Hari ini"
    }
}

//  fungsi untuk menghitung umur anak (bulan)
fun calculateMonthAge(dateString: String): Long {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val inputDate = LocalDate.parse(dateString, formatter)
    val currentDate = LocalDate.now()

    val totalMonths = ChronoUnit.MONTHS.between(inputDate, currentDate)
    return totalMonths
// Misalnya, dari "2022-05-01" ke "2024-11-23", outputnya adalah 30
}

fun scaleInputKidData(
    gender: Float, monthAge: Float, height: Float, weight: Float
): FloatArray {
    // konversi ke Double agar perhitungan menjadi lebih akurat
    val inputKidData = doubleArrayOf(gender.toDouble(), monthAge.toDouble(), height.toDouble(), weight.toDouble())

    val mean = doubleArrayOf(0.49821, 11.99258, 73.132657, 9.259256)
    val scale = doubleArrayOf(0.4999968, 7.19963506, 11.36078884, 3.30076366)

    // mengskalakan data
    val newDataScaled = DoubleArray(inputKidData.size) { index ->
        (inputKidData[index] - mean[index]) / scale[index]
    }

    // melihat hasil
    Log.d("scaleInputKidData", "Scaled Double Array: ${newDataScaled.contentToString()}")

    // Mengonversi hasil akhir ke FloatArray dan di-return
    return newDataScaled.map { it.toFloat() }.toFloatArray()
}