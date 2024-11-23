package com.tiuho22bangkit.gizi.utility

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
