package com.tiuho22bangkit.gizi.ui.analysis

import android.annotation.SuppressLint
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
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.data.local.entity.KidEntity
import com.tiuho22bangkit.gizi.databinding.ActivityKidAnalysisBinding
import com.tiuho22bangkit.gizi.helper.StuntWastClassifierHelper
import com.tiuho22bangkit.gizi.ui.ViewModelFactory
import com.tiuho22bangkit.gizi.ui.profile.KidAnalysisDialogFragment
import com.tiuho22bangkit.gizi.ui.profile.UpdateKidActivity
import com.tiuho22bangkit.gizi.utility.calculateMonthAge
import com.tiuho22bangkit.gizi.utility.scaleInputKidData

class KidAnalysisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKidAnalysisBinding
    private val viewModel: AnalysisViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private var kid: KidEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        supportActionBar?.hide()
        binding = ActivityKidAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        kid = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(KID_DATA, KidEntity::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(KID_DATA)
        }

        if (kid == null) {
            showToast("Kid data is missing.")
            finish()
            return
        }

        val id = kid!!.id
        viewModel.getKid(id).observe(this) { kid ->
            if (kid == null) {
                showToast("Kid data not found in the database.")
                finish()
            } else {
                updateUI(kid)
                setupUI()
            }
        }
    }

    private fun updateUI(kid: KidEntity) {
        binding.apply {
            Glide.with(root.context)
                .load(kid.uri)
                .placeholder(if (kid.gender == "Laki-Laki") R.drawable.baby_boy else R.drawable.baby_girl)
                .into(gambar)

            tvNamaAnak.text = kid.name
            tvGender.text = String.format(getString(R.string.jenis_kelamin_profile), kid.gender)
            tvTanggalLahir.text = String.format(getString(R.string.tanggal_lahir_anak_profile), kid.birthDate)
            tvUsia.text = String.format(getString(R.string.usia_anak_profile), calculateMonthAge(kid.birthDate))
            tvTinggi.text = String.format(getString(R.string.tinggi_anak_profile), kid.height)
            tvBerat.text = String.format(getString(R.string.berat_anak_profile), kid.weight)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun setupUI(){
        kid?.let{ kid ->
            binding.apply {
                if (kid.gender == "Laki-Laki") {
                    Glide.with(root.context)
                        .load(kid.uri)
                        .placeholder(R.drawable.baby_boy)
                        .into(gambar)
                } else {
                    Glide.with(root.context)
                        .load(kid.uri)
                        .placeholder(R.drawable.baby_girl)
                        .into(gambar)
                }
                val monthAge = calculateMonthAge(kid.birthDate)

                tvNamaAnak.text = kid.name
                tvGender.text = String.format(getString(R.string.jenis_kelamin_profile), kid.gender)
                tvTanggalLahir.text =
                    String.format(getString(R.string.tanggal_lahir_anak_profile), kid.birthDate)
                tvUsia.text = String.format(getString(R.string.usia_anak_profile), monthAge)
                tvTinggi.text = String.format(getString(R.string.tinggi_anak_profile), kid.height)
                tvBerat.text = String.format(getString(R.string.berat_anak_profile), kid.weight)

                val genderInput = if (kid.gender == "Laki-laki") 1f else 0f

                btnUbahData.setOnClickListener {
                    val intent = Intent(root.context, UpdateKidActivity::class.java)
                    intent.putExtra("id", kid.id)
                    intent.putExtra("name", kid.name)
                    intent.putExtra("gender", kid.gender)
                    intent.putExtra("birthdate", kid.birthDate)
                    intent.putExtra("height", kid.height)
                    intent.putExtra("weight", kid.weight)
                    root.context.startActivity(intent)
                }

                btnDelete.setOnClickListener {
                    val database = FirebaseDatabase.getInstance()
                    val kidRef = database.getReference("kids").child(kid.id)
                        kidRef.removeValue()
                    showToast("Data Anak Berhasil di Hapus!")
                    finish()
                }

                btnAnalisis.setOnClickListener {
                    analyzeKidData(genderInput, monthAge.toFloat(), kid.height, kid.weight)
                }
            }
        }
    }

    private fun analyzeKidData(
        kidGender: Float,
        kidMonthAge: Float,
        kidHeight: Float,
        kidWeight: Float
    ) {
        val inputData = scaleInputKidData(kidGender, kidMonthAge, kidHeight, kidWeight)

        // Membuat instance NumericalClassifierHelper dan mengirimkan input data untuk klasifikasi
        val stuntWastHelper = StuntWastClassifierHelper(
            context = this,
            classifierListener = object : StuntWastClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    showToast(error)
                }

                override fun onResults(results: Array<FloatArray>) {
                    runOnUiThread {
                        if (results.size == 2) {
                            val output1 = results[0] // Output pertama
                            val output2 = results[1] // Output kedua

                            Log.d("Hasil", results[0].joinToString(", "))
                            Log.d("Hasil", results[1].joinToString(", "))

                            // Daftar label
                            val wastingLabel = listOf(
                                "Normal",
                                "Risk of Overweight",
                                "Severely Underweight",
                                "Underweight"
                            )
                            val stuntingLabel =
                                listOf(
                                    "Normal",
                                    "Severely Stunted",
                                    "Stunted",
                                    "Tall"
                                )

                            // Cari indeks dengan probabilitas tertinggi
                            val maxIndex1 = output1.indices.maxByOrNull { output1[it] } ?: -1
                            val maxIndex2 = output2.indices.maxByOrNull { output2[it] } ?: -1

                            // Dapatkan label dari indeks
                            val labeledWastingResult =
                                if (maxIndex1 != -1) wastingLabel[maxIndex1] else "Unknown"
                            val labeledStuntingResult =
                                if (maxIndex2 != -1) stuntingLabel[maxIndex2] else "Unknown"

                            Log.d("Hasil", labeledWastingResult)
                            Log.d("Hasil", labeledStuntingResult)

                            val heightResult = kidHeight.toInt().toString()
                            val weightResult = kidWeight.toInt().toString()

                            val dialog = KidAnalysisDialogFragment.newInstance(labeledWastingResult, labeledStuntingResult, heightResult, weightResult)
                            dialog.show(supportFragmentManager, "AnalysisResultDialog")

                            kid?.let { viewModel.saveKidAnalysisResult(it, labeledWastingResult, labeledStuntingResult) }
                        } else {
                            showToast("Unexpected results format.")
                        }
                    }
                }
            }
        )
        // Memanggil fungsi classify untuk melakukan inferensi
        stuntWastHelper.classifyInput(inputData)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        Log.d(this.toString(), message)
    }

    companion object {
        const val KID_DATA = "kid_data"
    }
}