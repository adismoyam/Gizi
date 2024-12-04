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
import com.tiuho22bangkit.gizi.data.local.dao.KidDao
import com.tiuho22bangkit.gizi.data.local.entity.KidEntity
import com.tiuho22bangkit.gizi.databinding.ActivityUpdateKidBinding
import com.tiuho22bangkit.gizi.ui.analysis.KidAnalysisActivity
import com.tiuho22bangkit.gizi.utility.DatePickerFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpdateKidActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {

    private lateinit var binding: ActivityUpdateKidBinding
    private lateinit var giziDatabase: GiziDatabase
    private lateinit var kidDao: KidDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Setup binding
        binding = ActivityUpdateKidBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize database and DAO
        giziDatabase = GiziDatabase.getInstance(this)
        kidDao = giziDatabase.kidDao()

        // Get data from Intent
        val idData = intent.getIntExtra("id", 0)
        val nameData = intent.getStringExtra("name")
        val genderData = intent.getStringExtra("gender")
        val birthdateData = intent.getStringExtra("birthdate")
        val heightData = intent.getFloatExtra("height", 0f)
        val weightData = intent.getFloatExtra("weight", 0f)

        // Set data as default values in input fields
        binding.apply {
            kidName.setText(nameData)
            kidHeight.setText(heightData.toString())
            kitWeight.setText(weightData.toString())
            tvDate.text = birthdateData

            if (genderData == "Laki-Laki") {
                rgGender.check(R.id.rb_boy)
            } else if (genderData == "Perempuan") {
                rgGender.check(R.id.rb_girl)
            }
        }

        // Date picker handler
        binding.btnDate.setOnClickListener {
            val datePickerFragment = DatePickerFragment()
            datePickerFragment.show(
                supportFragmentManager,
                DATE_PICKER_TAG
            )
        }

        // Save button handler
        binding.btnTambahData.setOnClickListener {
            val name = binding.kidName.text.toString().trim()
            val gender = when (binding.rgGender.checkedRadioButtonId) {
                R.id.rb_boy -> "Laki-Laki"
                R.id.rb_girl -> "Perempuan"
                else -> ""
            }
            val birthDate = binding.tvDate.text.toString()
            val height = binding.kidHeight.text.toString().toFloatOrNull()
            val weight = binding.kitWeight.text.toString().toFloatOrNull()

            when {
                name.isEmpty() -> {
                    binding.kidName.error = "Nama anak harus diisi"
                    binding.kidName.requestFocus()
                }

                gender.isEmpty() -> {
                    Toast.makeText(this, "Pilih jenis kelamin anak", Toast.LENGTH_SHORT).show()
                }

                birthDate.isEmpty() -> {
                    Toast.makeText(this, "Pilih tanggal lahir anak", Toast.LENGTH_SHORT).show()
                }

                height == null || height <= 0 -> {
                    binding.kidHeight.error = "Masukkan tinggi badan yang valid"
                    binding.kidHeight.requestFocus()
                }

                weight == null || weight <= 0 -> {
                    binding.kitWeight.error = "Masukkan berat badan yang valid"
                    binding.kitWeight.requestFocus()
                }

                else -> {
                     val kid = KidEntity(
                         id = idData,
                        name = name,
                        gender = gender,
                        birthDate = birthDate,
                        height = height,
                        weight = weight
                    )

                    // Simpan ke database
                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            kidDao.updateKid(kid)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@UpdateKidActivity,
                                    "Data anak berhasil disimpan",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this@UpdateKidActivity, KidAnalysisActivity::class.java)
                                intent.putExtra(KidAnalysisActivity.KID_DATA, kid)
                                startActivity(intent)
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@UpdateKidActivity,
                                    "Terjadi kesalahan, coba lagi",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
            finish()
        }

        // Close button handler
        binding.btnClose.setOnClickListener {
            finish()
        }
    }

    override fun onDialogDateSet(year: Int, month: Int, dayOfMonth: Int) {
        // Format dan tampilkan tanggal di TextView
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)
        binding.tvDate.text = formattedDate
    }

    // Menampilkan DatePickerFragment
    private fun showDatePicker() {
        val datePickerFragment = DatePickerFragment()
        datePickerFragment.show(supportFragmentManager, "DatePicker")
    }

    companion object {
        private const val DATE_PICKER_TAG = "DatePicker"
    }
}
