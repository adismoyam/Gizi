package com.tiuho22bangkit.gizi

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.tiuho22bangkit.gizi.data.local.KidEntity
import com.tiuho22bangkit.gizi.databinding.ActivityKidAnalysisBinding
import com.tiuho22bangkit.gizi.helper.StuntWastClassifierHelper
import com.tiuho22bangkit.gizi.ui.profile.UpdateKidActivity
import com.tiuho22bangkit.gizi.utility.calculateMonthAge
import com.tiuho22bangkit.gizi.utility.scaleInputKidData

class KidAnalysisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKidAnalysisBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityKidAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val kid: KidEntity? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(KID_DATA, KidEntity::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(KID_DATA)
        }

        if (kid != null) {
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
                tvTanggalLahir.text = String.format(getString(R.string.tanggal_lahir_profile), kid.birthDate)
                tvUsia.text = String.format(getString(R.string.usia_profile), monthAge)
                tvTinggi.text = String.format(getString(R.string.tinggi_profile), kid.height)
                tvBerat.text = String.format(getString(R.string.berat_profile), kid.weight)

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
                
                btnAnalisis.setOnClickListener {
                    analyzeKidData(genderInput, monthAge.toFloat(), kid.height, kid.weight)
                }
            }
        }
    }

    private fun analyzeKidData(kidGender: Float, kidMonthAge: Float, kidHeight: Float, kidWeight: Float){
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
                        // Asumsikan output pertama berada di results[0] dan output kedua di results[1]
                        if (results.size == 2) {
                            val output1 = results[0] // Output pertama
                            val output2 = results[1] // Output kedua

                            Log.d("Hasil", results[0].joinToString(", "))
                            Log.d("Hasil", results[1].joinToString(", "))

                            // Daftar label
                            val labelWasting = listOf("Normal", "Risk of Overweight", "Severely Underweight", "Underweight")
                            val labelStunting = listOf("Normal", "Severely Stunted", "Stunted", "Tall")

                            // Cari indeks dengan probabilitas tertinggi
                            val maxIndex1 = output1.indices.maxByOrNull { output1[it] } ?: -1
                            val maxIndex2 = output2.indices.maxByOrNull { output2[it] } ?: -1

                            // Dapatkan label dari indeks
                            val label1 = if (maxIndex1 != -1) labelWasting[maxIndex1] else "Unknown"
                            val label2 = if (maxIndex2 != -1) labelStunting[maxIndex2] else "Unknown"

                            Log.d("Hasil", label1)
                            Log.d("Hasil", label2)


                            val resultString1 = label1
                            val resultString2 = label2

                            // Menampilkan kedua hasil di TextView
                            binding.tvHasilWasting.text = "Results:\n\n$resultString1\n"
                            binding.tvHasilStunting.text = "Results:\n\n$resultString2\n"

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