package com.tiuho22bangkit.gizi

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.tiuho22bangkit.gizi.data.local.KidEntity
import com.tiuho22bangkit.gizi.databinding.ActivityKidAnalysisBinding
import com.tiuho22bangkit.gizi.utility.calculateMonthAge

class KidAnalysisActivity : AppCompatActivity(), View.OnClickListener {

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
            intent.getParcelableExtra(DATA, KidEntity::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(DATA)
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
                tvNamaAnak.text = kid.name
                tvGender.text = String.format(getString(R.string.jenis_kelamin_profile), kid.gender)
                tvTanggalLahir.text = String.format(getString(R.string.tanggal_lahir), kid.birthDate)
                tvUsia.text = String.format(getString(R.string.usia_profile), calculateMonthAge(kid.birthDate))
                tvTinggi.text = String.format(getString(R.string.tinggi_profile), kid.height)
                tvBerat.text = String.format(getString(R.string.berat_profile), kid.weight)
            }

        }
        binding.btnUbahData.setOnClickListener {
            val intent = Intent(this, UpdateKidActivity::class.java)
            intent.putExtra("name", kid?.name)
            intent.putExtra("gender", kid?.gender)
            intent.putExtra("birthdate", kid?.birthDate)
            intent.putExtra("height", kid?.height ?: 0f)
            intent.putExtra("weight", kid?.weight ?: 0f)
            startActivity(intent)
            }
    }

    override fun onClick(v: View?) {
        // TODO("Not yet implemented")
    }

    companion object {
        const val DATA = "data"
    }
}