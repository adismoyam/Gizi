package com.tiuho22bangkit.gizi

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.tiuho22bangkit.gizi.data.local.KidEntity
import com.tiuho22bangkit.gizi.databinding.ActivityKidAnalysisBinding
import com.tiuho22bangkit.gizi.databinding.FragmentDetailBinding

class KidAnalysisActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityKidAnalysisBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityKidAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_kid_analysis)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val kid = intent.getParcelableExtra(DATA, KidEntity::class.java)

        binding.apply {
            if (kid?.gender == "Laki-Laki") {
                Glide.with(root.context)
                    .load(kid?.uri)
                    .placeholder(R.drawable.baby_boy)
                    .into(gambar)
            } else {
                Glide.with(root.context)
                    .load(kid?.uri)
                    .placeholder(R.drawable.baby_girl)
                    .into(gambar)
            }
            tvNamaAnak.text = kid?.name
            tvGender.text = kid?.gender
            tvTanggalLahir.text = kid?.birthDate
            tvTinggi.text = kid?.height.toString()
            tvBerat.text = kid?.weight.toString()
        }





    }

    override fun onClick(v: View?) {
        // TODO("Not yet implemented")
    }

    companion object {
        const val DATA = "data"
    }
}