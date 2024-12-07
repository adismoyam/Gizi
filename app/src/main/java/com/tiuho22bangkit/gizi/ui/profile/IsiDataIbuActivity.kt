package com.tiuho22bangkit.gizi.ui.profile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.data.local.GiziDatabase
import com.tiuho22bangkit.gizi.data.local.dao.MomDao
import com.tiuho22bangkit.gizi.data.local.entity.MomEntity
import com.tiuho22bangkit.gizi.databinding.ActivityIsiDataIbuBinding
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
        binding = ActivityIsiDataIbuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi database
        giziDatabase = GiziDatabase.getInstance(this)
        momDao = giziDatabase.momDao()

        // Tombol pilih tanggal lahir
        binding.btnLmpDate.setOnClickListener {
            currentSelectedDate = "LMP"
            showDatePicker()
        }

        binding.btnEddDate.setOnClickListener {
            currentSelectedDate = "EDD"
            showDatePicker()
        }

        binding.btnBirthDate.setOnClickListener {
            currentSelectedDate = "BIRTH"
            showDatePicker()
        }

        binding.btnClose.setOnClickListener {
            finish() // Kembali ke activity sebelumnya
        }

        // Tombol tambah data
        binding.btnTambahData.setOnClickListener {
            val name = binding.name.text.toString().trim()

            val lmpDate = binding.tvLmpDate.text.toString()
            val eddDate = binding.tvEddDate.text.toString()
            val birthDate = binding.tvBirthDate.text.toString()

            val systolicBloodPressure = binding.etSystolicBloodPressure.text.toString().toFloatOrNull()
            val diastolicBloodPressure = binding.etDiastolicBloodPressure.text.toString().toFloatOrNull()

            val bloodSugarLevel = binding.etBloodSugarLevel.text.toString().toFloatOrNull()
            val bodyTemperature = binding.etBodyTemperature.text.toString().toFloatOrNull()
            val heartRate = binding.etHeartRate.text.toString().toFloatOrNull()

            when {
                lmpDate.isEmpty() || lmpDate == getString(R.string.hari_pertama_haid_terakhir)-> {
                    Toast.makeText(this, "Pilih tanggal HPHT", Toast.LENGTH_SHORT).show()
                }

                eddDate.isEmpty() || eddDate == getString(R.string.hari_perkiraan_lahir) -> {
                    Toast.makeText(this, "Pilih tanggal HPL", Toast.LENGTH_SHORT).show()
                }

                birthDate.isEmpty() || birthDate == getString(R.string.tanggal_lahir)-> {
                    Toast.makeText(this, "Pilih tanggal lahir", Toast.LENGTH_SHORT).show()
                }

                systolicBloodPressure == null || systolicBloodPressure <= 0 -> {
                    binding.etSystolicBloodPressure.error = "Masukkan tekanan darah sistolik yang valid"
                    binding.etSystolicBloodPressure.requestFocus()
                }

                diastolicBloodPressure == null || diastolicBloodPressure <= 0 -> {
                    binding.etDiastolicBloodPressure.error = "Masukkan tekanan darah diastolik yang valid"
                    binding.etDiastolicBloodPressure.requestFocus()
                }

                bloodSugarLevel == null || bloodSugarLevel <= 0 -> {
                    binding.etBloodSugarLevel.error = "Masukkan tekanan darah diastolik yang valid"
                    binding.etBloodSugarLevel.requestFocus()
                }

                bodyTemperature == null || bodyTemperature <= 0 -> {
                    binding.etDiastolicBloodPressure.error = "Masukkan suhu tubuh yang valid"
                    binding.etDiastolicBloodPressure.requestFocus()
                }

                heartRate == null || heartRate <= 0 -> {
                    binding.etDiastolicBloodPressure.error = "Masukkan jumlah detak jantung yang valid"
                    binding.etDiastolicBloodPressure.requestFocus()
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
                                Toast.makeText(this@IsiDataIbuActivity, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                                finish() // Kembali ke activity sebelumnya
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@IsiDataIbuActivity, "Terjadi kesalahan, coba lagi", Toast.LENGTH_SHORT).show()
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
            "LMP" -> binding.tvLmpDate.text = formattedDate
            "EDD" -> binding.tvEddDate.text = formattedDate
            "BIRTH" -> binding.tvBirthDate.text = formattedDate
        }
    }

    companion object {
        private const val DATE_PICKER_TAG = "DatePicker"
    }
}