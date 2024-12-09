package com.tiuho22bangkit.gizi.ui.profile

import android.content.Intent
import android.os.Build
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
import com.tiuho22bangkit.gizi.data.local.entity.KidEntity
import com.tiuho22bangkit.gizi.data.local.entity.MomEntity
import com.tiuho22bangkit.gizi.databinding.ActivityUpdateKidBinding
import com.tiuho22bangkit.gizi.databinding.ActivityUpdateMomBinding
import com.tiuho22bangkit.gizi.ui.analysis.KidAnalysisActivity
import com.tiuho22bangkit.gizi.ui.analysis.KidAnalysisActivity.Companion.KID_DATA
import com.tiuho22bangkit.gizi.ui.analysis.MomAnalysisActivity
import com.tiuho22bangkit.gizi.ui.profile.IsiDataIbuActivity.Companion
import com.tiuho22bangkit.gizi.utility.DatePickerFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpdateMomActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {

    private lateinit var binding: ActivityUpdateMomBinding
    private lateinit var giziDatabase: GiziDatabase
    private var currentSelectedDate: String = ""
    private lateinit var momDao: MomDao
    private var mom: MomEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        supportActionBar?.hide()
        binding = ActivityUpdateMomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.update_mom)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        giziDatabase = GiziDatabase.getInstance(this)
        momDao = giziDatabase.momDao()


        mom = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("mom_data", MomEntity::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("mom_data")
        }



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
            mom?.let { mom ->
                etName.setText(mom.name)
                tvLmpDate.text = mom.lastMenstrualPeriod
                tvEddDate.text = mom.estimatedDeliveryDate
                tvBirthDate.text = mom.birthDate
                etSystolicBloodPressure.setText(mom.systolicBloodPressure.toString())
                etDiastolicBloodPressure.setText(mom.diastolicBloodPressure.toString())
                etBloodSugarLevel.setText(mom.bloodSugarLevel.toString())
                etBodyTemperature.setText(mom.bodyTemperature.toString())
                etHeartRate.setText(mom.heartRate.toString())

            }

            btnPerbaruiData.setOnClickListener {
                val momId = mom?.id

                val name = binding.etName.text.toString().trim()

                val lmpDate = binding.tvLmpDate.text.toString()
                val eddDate = binding.tvEddDate.text.toString()
                val birthDate = binding.tvBirthDate.text.toString()

                val systolicBloodPressure =
                    binding.etSystolicBloodPressure.text.toString().toFloatOrNull()
                val diastolicBloodPressure =
                    binding.etDiastolicBloodPressure.text.toString().toFloatOrNull()

                val bloodSugarLevel = binding.etBloodSugarLevel.text.toString().toFloatOrNull()
                val bodyTemperature = binding.etBodyTemperature.text.toString().toFloatOrNull()
                val heartRate = binding.etHeartRate.text.toString().toFloatOrNull()


                when {
                    lmpDate.isEmpty() || lmpDate == getString(R.string.hari_pertama_haid_terakhir) -> {
                        Toast.makeText(this@UpdateMomActivity, "Pilih tanggal HPHT", Toast.LENGTH_SHORT).show()
                    }

                    eddDate.isEmpty() || eddDate == getString(R.string.hari_perkiraan_lahir) -> {
                        Toast.makeText(this@UpdateMomActivity, "Pilih tanggal HPL", Toast.LENGTH_SHORT).show()
                    }

                    birthDate.isEmpty() || birthDate == getString(R.string.tanggal_lahir) -> {
                        Toast.makeText(this@UpdateMomActivity, "Pilih tanggal lahir", Toast.LENGTH_SHORT).show()
                    }

                    systolicBloodPressure == null || systolicBloodPressure <= 0 -> {
                        binding.etSystolicBloodPressure.error =
                            "Masukkan tekanan darah sistolik yang valid"
                        binding.etSystolicBloodPressure.requestFocus()
                    }

                    diastolicBloodPressure == null || diastolicBloodPressure <= 0 -> {
                        binding.etDiastolicBloodPressure.error =
                            "Masukkan tekanan darah diastolik yang valid"
                        binding.etDiastolicBloodPressure.requestFocus()
                    }

                    bloodSugarLevel == null || bloodSugarLevel <= 0 -> {
                        binding.etBloodSugarLevel.error =
                            "Masukkan tekanan darah diastolik yang valid"
                        binding.etBloodSugarLevel.requestFocus()
                    }

                    bodyTemperature == null || bodyTemperature <= 0 -> {
                        binding.etDiastolicBloodPressure.error = "Masukkan suhu tubuh yang valid"
                        binding.etDiastolicBloodPressure.requestFocus()
                    }

                    heartRate == null || heartRate <= 0 -> {
                        binding.etDiastolicBloodPressure.error =
                            "Masukkan jumlah detak jantung yang valid"
                        binding.etDiastolicBloodPressure.requestFocus()
                    }

                    else -> {

                        val mom = MomEntity(
                            id = momId!!,
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
                                momDao.updateTheMom(mom)
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        this@UpdateMomActivity,
                                        "Data berhasil disimpan",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(
                                        this@UpdateMomActivity,
                                        MomAnalysisActivity::class.java
                                    ).apply {
                                        putExtra(MomAnalysisActivity.MOM_DATA, mom)
                                        flags =
                                            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                    }
                                    startActivity(intent)
                                    finish()
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        this@UpdateMomActivity,
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

    private fun showDatePicker() {
        val datePickerFragment = DatePickerFragment()
        datePickerFragment.show(supportFragmentManager, DATE_PICKER_TAG)
    }

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