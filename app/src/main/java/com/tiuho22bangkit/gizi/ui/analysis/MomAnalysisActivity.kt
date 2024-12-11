package com.tiuho22bangkit.gizi.ui.analysis

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.data.local.GiziDatabase
import com.tiuho22bangkit.gizi.data.local.dao.MomDao
import com.tiuho22bangkit.gizi.data.local.entity.MomEntity
import com.tiuho22bangkit.gizi.databinding.ActivityMomAnalysisBinding
import com.tiuho22bangkit.gizi.helper.PregnancyClassifierHelper
import com.tiuho22bangkit.gizi.ui.ViewModelFactory
import com.tiuho22bangkit.gizi.ui.profile.UpdateMomActivity
import com.tiuho22bangkit.gizi.utility.calculateYearAge
import com.tiuho22bangkit.gizi.utility.scaleInputMomData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MomAnalysisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMomAnalysisBinding
    private lateinit var giziDatabase: GiziDatabase
    private val viewModel: AnalysisViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private var mom: MomEntity? = null
    private lateinit var momDao: MomDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        supportActionBar?.hide()

        binding = ActivityMomAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        giziDatabase = GiziDatabase.getInstance(this)
        momDao = giziDatabase.momDao()

        binding.btnBack.setOnClickListener {
            finish()
        }

        mom = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(MOM_DATA, MomEntity::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(MOM_DATA)
        }

        if (mom == null) {
            showToast("Mom data is missing.")
            finish()
            return
        }

        setupUI()
    }

    private fun setupUI() {
        mom?.let { mom ->
            binding.apply {
                Glide.with(root.context)
                    .load(mom.uri)
                    .placeholder(R.drawable.mother)
                    .into(gambar)

                val yearAge = calculateYearAge(mom.birthDate)

                if (mom.name != null) {
                    binding.tvNama.text = mom.name
                }

                tvTanggalLahir.text =
                    String.format(getString(R.string.tanggal_lahir_profile), mom.birthDate)

                tvUsia.text = String.format(getString(R.string.usia_profile), yearAge)
                tvTekananDarah.text = String.format(
                    getString(R.string.tekanan_darah_profile),
                    mom.systolicBloodPressure.toInt(),
                    mom.diastolicBloodPressure.toInt()
                )
                tvKadarGulaDarah.text = String.format(
                    getString(R.string.kadar_gula_darah_profile),
                    mom.bloodSugarLevel
                )
                tvSuhuTubuh.text = String.format(
                    getString(R.string.suhu_tubuh_profile),
                    mom.bodyTemperature
                )
                tvDetakJantung.text = String.format(
                    getString(R.string.detak_jantung_profile),
                    mom.heartRate.toInt()
                )

                btnUbahData.setOnClickListener {
                    val intent = Intent(root.context, UpdateMomActivity::class.java)
                    intent.putExtra("mom_data", mom)
                    root.context.startActivity(intent)
                }

                btnDelete.setOnClickListener {
                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            momDao.deleteTheMom(mom)
                            withContext(Dispatchers.Main) {
                                showToast("Data Kehamilan Berhasil di Hapus!")
                                finish()
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                showToast("Terjadi kesalahan, coba lagi")
                            }
                        }
                    }
                }

                btnAnalisis.setOnClickListener {
                    analyzeMomData(
                        yearAge.toFloat(),
                        mom.systolicBloodPressure,
                        mom.diastolicBloodPressure,
                        mom.bloodSugarLevel,
                        mom.bodyTemperature,
                        mom.heartRate
                    )
                }
            }
        }
    }


    private fun analyzeMomData(
        momAge: Float,
        systolicBloodPressure: Float,
        diastolicBloodPressure: Float,
        bloodSugarLevel: Float,
        bodyTemperature: Float,
        heartRate: Float
    ) {

        val inputData = scaleInputMomData(
            momAge,
            systolicBloodPressure,
            diastolicBloodPressure,
            bloodSugarLevel,
            bodyTemperature,
            heartRate
        )

        // Membuat instance PregClassicHelper dan mengirimkan input data untuk klasifikasi
        val pregnancyHelper = PregnancyClassifierHelper(
            context = this,
            classifierListener = object : PregnancyClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    // Menampilkan error dengan Toast
                    showToast(error)
                }

                override fun onResults(results: Array<FloatArray>) {
                    runOnUiThread {
                        val output = results[0] // Output pertama
                        Log.d("Hasil", results[0].joinToString(", "))

                        val pregnancyLabel = listOf(
                            "Normal",
                            "Mid Risk",
                            "High Risk"
                        )

                        val maxIndex = output.indices.maxByOrNull { output[it] } ?: -1

                        val labeledPregnancy =
                            if (maxIndex != -1) pregnancyLabel[maxIndex] else "Unknown"

                        Log.d("Hasil", labeledPregnancy)

                        binding.tvHasil.text = "Results:\n$labeledPregnancy"
                        mom?.let { viewModel.saveMomAnalysisResult(it, labeledPregnancy) }
                    }
                }
            }
        )
        // Memanggil fungsi classify untuk melakukan inferensi
        pregnancyHelper.classifyInput(inputData)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        Log.d(this.toString(), message)
    }

    companion object {
        const val MOM_DATA = "mom_data"
    }
}