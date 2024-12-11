package com.tiuho22bangkit.gizi.utility

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale
import java.time.LocalDateTime

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
}   // Misalnya, dari "2022-05-01" ke "2024-11-28", outputnya adalah 31

fun calculateYearAge(dateString: String): Long {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val inputDate = LocalDate.parse(dateString, formatter)
    val currentDate = LocalDate.now()

    val totalYears = ChronoUnit.YEARS.between(inputDate, currentDate)
    return totalYears
// Misalnya, dari "2022-05-01" ke "2024-11-28", outputnya adalah 2
}

fun calculateMonthDifference(birthDate: String, timestamp: String): Long {
    val birthDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    val birthDatee = LocalDate.parse(birthDate, birthDateFormatter)
    val timestampp = LocalDateTime.parse(timestamp, timestampFormatter).toLocalDate()

    // Hitung total bulan menggunakan ChronoUnit.MONTHS
    return ChronoUnit.MONTHS.between(birthDatee, timestampp)
}

fun calculateYearDifference(birthDate: String, timestamp: String): Long {
    val birthDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    val birthDatee = LocalDate.parse(birthDate, birthDateFormatter)
    val timestampp = LocalDateTime.parse(timestamp, timestampFormatter).toLocalDate()

    return ChronoUnit.YEARS.between(birthDatee, timestampp)
}

fun scaleInputKidData(
    gender: Float, monthAge: Float, height: Float, weight: Float
): FloatArray {
    // konversi ke Double agar perhitungan menjadi lebih akurat
    val inputKidData =
        doubleArrayOf(gender.toDouble(), monthAge.toDouble(), height.toDouble(), weight.toDouble())

    val mean = doubleArrayOf(0.49821, 11.99258, 73.132657, 9.259256)
    val scale = doubleArrayOf(0.4999968, 7.19963506, 11.36078884, 3.30076366)

    // mengskalakan data
    val scaledData = DoubleArray(inputKidData.size) { index ->
        (inputKidData[index] - mean[index]) / scale[index]
    }

    // melihat hasil
    Log.d("scaleInputKidData", "Scaled Double Array: ${scaledData.contentToString()}")

    // Mengonversi hasil akhir ke FloatArray dan di-return
    return scaledData.map { it.toFloat() }.toFloatArray()
}

fun scaleInputMomData(
    age: Float,
    systolicBloodPressure: Float,
    diastolicBloodPressure: Float,
    bloodSugarLevel: Float,
    bodyTemperature: Float,
    heartRate: Float
): FloatArray {

    // Input dalam tipe Double untuk perhitungan akurat
    val inputKidData = doubleArrayOf(
        age.toDouble(),
        systolicBloodPressure.toDouble(),
        diastolicBloodPressure.toDouble(),
        bloodSugarLevel.toDouble(),
        bodyTemperature.toDouble(),
        heartRate.toDouble()
    )

    // Mean dan scale menggunakan DoubleArray
    val mean = doubleArrayOf(
        29.87179487,
        113.19822485,
        76.46055227,
        8.72598619,
        37.03616042,
        74.30177515
    )
    val scale =
        doubleArrayOf(
            13.46773972,
            18.39483561,
            13.878947,
            3.29190729,
            0.76150444,
            8.08471278
        )

    val scaledData = DoubleArray(inputKidData.size) { index ->
        (inputKidData[index] - mean[index]) / scale[index]
    }

    Log.d("scaleInputPregnantData", "Scaled Double Array: ${scaledData.contentToString()}")
    return scaledData.map { it.toFloat() }.toFloatArray()
}

fun millisToDate(millis: Long): String {
    val formatTanggal = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val kalender = Calendar.getInstance()
    kalender.timeInMillis = millis
    return formatTanggal.format(kalender.time)
}

fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: Observer<T>) {
    observe(owner, object : Observer<T> {
        override fun onChanged(value: T) {
            removeObserver(this)
            observer.onChanged(value)
        }
    })
}

/* Easter Egg, shhh.... Don't tell anyone about this.
    val formattedResponse = response
        // Baris baru
        .replace("\n", "<br>")
        // Heading 1-6
        .replace("^###### (.*?)<br>".toRegex(RegexOption.MULTILINE), "<h6>$1</h6>")
        .replace("^##### (.*?)<br>".toRegex(RegexOption.MULTILINE), "<h5>$1</h5>")
        .replace("^#### (.*?)<br>".toRegex(RegexOption.MULTILINE), "<h4>$1</h4>")
        .replace("^### (.*?)<br>".toRegex(RegexOption.MULTILINE), "<h3>$1</h3>")
        .replace("^## (.*?)<br>".toRegex(RegexOption.MULTILINE), "<h2>$1</h2>")
        .replace("^# (.*?)<br>".toRegex(RegexOption.MULTILINE), "<h1>$1</h1>")
        // Bold
        .replace("\\*\\*(.*?)\\*\\*".toRegex(), "<b>$1</b>")
        // Italic
        .replace("\\*(.*?)\\*".toRegex(), "<i>$1</i>")
        // Link
        .replace("\\[(.*?)\\]\\((.*?)\\)".toRegex(), "<a href=\"$2\">$1</a>")
        // Inline code
        .replace("`(.*?)`".toRegex(), "<code>$1</code>")
        // Block code
        .replace("```([\\s\\S]*?)```".toRegex(), "<pre><code>$1</code></pre>")
        // Blockquote
        .replace("^> (.*?)<br>".toRegex(RegexOption.MULTILINE), "<blockquote>$1</blockquote>")
        // Horizontal line
        .replace("^(---|\\*\\*\\*)<br>".toRegex(RegexOption.MULTILINE), "<hr>")
        // Unordered list
        .replace("^\\* (.*?)<br>".toRegex(RegexOption.MULTILINE), "<li>$1</li>")
        .replace("<li>(.*?)</li>".toRegex(), "<ul><li>$1</li></ul>")
        // Ordered list
        .replace("^\\d+\\. (.*?)<br>".toRegex(RegexOption.MULTILINE), "<li>$1</li>")
        .replace("<li>(.*?)</li>".toRegex(), "<ol><li>$1</li></ol>")
        // Image
        .replace("!\\[(.*?)\\]\\((.*?)\\)".toRegex(), "<img src=\"$2\" alt=\"$1\" />")
 */