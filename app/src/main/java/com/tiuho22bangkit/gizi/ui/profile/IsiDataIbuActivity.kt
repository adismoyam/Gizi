package com.tiuho22bangkit.gizi.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.data.local.GiziDatabase
import com.tiuho22bangkit.gizi.data.local.dao.MomDao
import com.tiuho22bangkit.gizi.data.local.entity.MomEntity
import com.tiuho22bangkit.gizi.databinding.ActivityIsiDataIbuBinding
import com.tiuho22bangkit.gizi.ui.analysis.MomAnalysisActivity
import com.tiuho22bangkit.gizi.utility.DatePickerFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class IsiDataIbuActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {

    private lateinit var binding: ActivityIsiDataIbuBinding
    private lateinit var giziDatabase: GiziDatabase
    private lateinit var momDao: MomDao

    private var currentSelectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        supportActionBar?.hide()

        binding = ActivityIsiDataIbuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.isi_data_ibu)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Inisialisasi database
        giziDatabase = GiziDatabase.getInstance(this)
        momDao = giziDatabase.momDao()

        binding.apply {
            btnLmpDate.setOnClickListener {
                currentSelectedDate = "LMP"
                showDatePicker()
            }

            btnEddDate.setOnClickListener {
                currentSelectedDate = "EDD"
                showDatePicker()
            }

            btnBirthDate.setOnClickListener {
                currentSelectedDate = "BIRTH"
                showDatePicker()
            }

            btnClose.setOnClickListener {
                finish()
            }
            // Tombol tambah data
            btnTambahData.setOnClickListener {
                val name = name.text.toString().trim()

                val lmpDate = btnLmpDate.text.toString()
                val eddDate = btnEddDate.text.toString()
                val birthDate = btnBirthDate.text.toString()

                val systolicBloodPressure =
                    etSystolicBloodPressure.text.toString().toFloatOrNull()
                val diastolicBloodPressure =
                    etDiastolicBloodPressure.text.toString().toFloatOrNull()

                val bloodSugarLevel = etBloodSugarLevel.text.toString().toFloatOrNull()
                val bodyTemperature = etBodyTemperature.text.toString().toFloatOrNull()
                val heartRate = etHeartRate.text.toString().toFloatOrNull()

                when {
                    lmpDate.isEmpty() -> {
                        Toast.makeText(
                            this@IsiDataIbuActivity,
                            "Pilih tanggal HPHT",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    eddDate.isEmpty() -> {
                        Toast.makeText(
                            this@IsiDataIbuActivity,
                            "Pilih tanggal HPL",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    birthDate.isEmpty() -> {
                        Toast.makeText(
                            this@IsiDataIbuActivity,
                            "Pilih tanggal lahir",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    systolicBloodPressure == null || systolicBloodPressure <= 0 -> {
                        etSystolicBloodPressure.error =
                            "Masukkan tekanan darah sistolik yang valid"
                        etSystolicBloodPressure.requestFocus()
                    }

                    diastolicBloodPressure == null || diastolicBloodPressure <= 0 -> {
                        etDiastolicBloodPressure.error =
                            "Masukkan tekanan darah diastolik yang valid"
                        etDiastolicBloodPressure.requestFocus()
                    }

                    bloodSugarLevel == null || bloodSugarLevel <= 0 -> {
                        etBloodSugarLevel.error =
                            "Masukkan tekanan darah diastolik yang valid"
                        etBloodSugarLevel.requestFocus()
                    }

                    bodyTemperature == null || bodyTemperature <= 0 -> {
                        etDiastolicBloodPressure.error = "Masukkan suhu tubuh yang valid"
                        etDiastolicBloodPressure.requestFocus()
                    }

                    heartRate == null || heartRate <= 0 -> {
                        etDiastolicBloodPressure.error =
                            "Masukkan jumlah detak jantung yang valid"
                        etDiastolicBloodPressure.requestFocus()
                    }

                    else -> {
                        val mom = MomEntity(
                            name = name,
                            lastMenstrualPeriod = lmpDate,
                            estimatedDeliveryDate = eddDate,
                            birthDate = birthDate,
                            systolicBloodPressure = systolicBloodPressure,
                            diastolicBloodPressure = diastolicBloodPressure,
                            bloodSugarLevel = bloodSugarLevel,
                            bodyTemperature = bodyTemperature,
                            heartRate = heartRate
                        )

                        // Simpan ke database
                        lifecycleScope.launch(Dispatchers.IO) {
                            try {
                                momDao.insertMomData(mom)
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        this@IsiDataIbuActivity,
                                        "Data berhasil disimpan",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(
                                        this@IsiDataIbuActivity,
                                        MomAnalysisActivity::class.java
                                    ).apply {
                                        putExtra(MomAnalysisActivity.MOM_DATA, mom)
                                    }
                                    startActivity(intent)
                                    finish()
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        this@IsiDataIbuActivity,
                                        "Terjadi kesalahan, coba lagi",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Tampilkan DatePicker
    private fun showDatePicker() {
        val datePickerFragment = DatePickerFragment()
        datePickerFragment.show(supportFragmentManager, DATE_PICKER_TAG)
    }

    // Callback saat tanggal dipilih
    override fun onDialogDateSet(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)

        when (currentSelectedDate) {
            "LMP" -> binding.btnLmpDate.text = formattedDate
            "EDD" -> binding.btnEddDate.text = formattedDate
            "BIRTH" -> binding.btnBirthDate.text = formattedDate
        }
    }

    companion object {
        private const val DATE_PICKER_TAG = "DatePicker"
    }
}