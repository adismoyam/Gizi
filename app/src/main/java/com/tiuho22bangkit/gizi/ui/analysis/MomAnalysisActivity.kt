package com.tiuho22bangkit.gizi.ui.analysis

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.data.local.entity.MomEntity
import com.tiuho22bangkit.gizi.databinding.ActivityMomAnalysisBinding
import com.tiuho22bangkit.gizi.ui.ViewModelFactory

class MomAnalysisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMomAnalysisBinding
    private val viewModel: AnalysisViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private var mom: MomEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMomAnalysisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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

//        setupUI()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        Log.d(this.toString(), message)
    }

    companion object {
        const val MOM_DATA = "mom_data"
    }
}