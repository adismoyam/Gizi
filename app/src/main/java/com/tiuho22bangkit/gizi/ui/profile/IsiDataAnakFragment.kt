package com.tiuho22bangkit.gizi.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.data.local.GiziDatabase
import com.tiuho22bangkit.gizi.data.local.KidDao
import com.tiuho22bangkit.gizi.data.local.KidEntity
import com.tiuho22bangkit.gizi.ui.article.ArticleFragmentDirections
import com.tiuho22bangkit.gizi.utility.DatePickerFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class IsiDataAnakFragment : Fragment(), DatePickerFragment.DialogDateListener {

    private lateinit var giziDatabase: GiziDatabase
    private lateinit var kidDao: KidDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_isi_data_anak, container, false)

        giziDatabase = GiziDatabase.getInstance(requireContext())
        kidDao = giziDatabase.kidDao()

        val kidNameET = view.findViewById<EditText>(R.id.kidName)
        val kidHeightET = view.findViewById<EditText>(R.id.kidHeight)
        val kidWeightET = view.findViewById<EditText>(R.id.kitWeight)
        val kidGenderRG = view.findViewById<RadioGroup>(R.id.rg_gender)
        val kidBirthDayTV = view.findViewById<TextView>(R.id.tv_date)

        val btnTambahData = view.findViewById<Button>(R.id.btn_tambah_data)

        // tanggal lahir handler
        val dateButton = view.findViewById<Button>(R.id.btn_date)
        dateButton.setOnClickListener {
            val datePickerFragment = DatePickerFragment()
            datePickerFragment.show(
                childFragmentManager,  // karena fragment ini child dari Profile
                DATE_PICKER_TAG
            )
        }

        btnTambahData.setOnClickListener {
            val name = kidNameET.text.toString().trim()
            val gender = when (kidGenderRG.checkedRadioButtonId) {
                R.id.rb_boy -> "Laki-Laki"
                R.id.rb_girl -> "Perempuan"
                else -> ""
            }
            val birthDate = kidBirthDayTV.text.toString()
            val height = kidHeightET.text.toString().toFloatOrNull()
            val weight = kidWeightET.text.toString().toFloatOrNull()

            when {
                name.isEmpty() -> {
                    kidNameET.error = "Nama anak harus diisi"
                    kidNameET.requestFocus()
                }

                gender.isEmpty() -> {
                    Toast.makeText(requireContext(), "Pilih jenis kelamin anak", Toast.LENGTH_SHORT)
                        .show()
                }

                birthDate.isEmpty() -> {
                    Toast.makeText(requireContext(), "Pilih tanggal lahir anak", Toast.LENGTH_SHORT)
                        .show()
                }

                height == null || height <= 0 -> {
                    kidHeightET.error = "Masukkan tinggi badan yang valid"
                    kidHeightET.requestFocus()
                }

                weight == null || weight <= 0 -> {
                    kidWeightET.error = "Masukkan berat badan yang valid"
                    kidWeightET.requestFocus()
                }

                else -> {
                    val kid = KidEntity(
                        name = name,
                        gender = gender,
                        birthDate = birthDate,
                        height = height,
                        weight = weight
                    )

                    // simpan ke database
                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            kidDao.insertKidData(kid)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), "Data anak berhasil disimpan", Toast.LENGTH_SHORT).show()
                                findNavController().navigateUp()
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), "Terjadi kesalahan, coba lagi", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
        return view
    }

    override fun onDialogDateSet(year: Int, month: Int, dayOfMonth: Int) {
        // Siapkan date formatter-nya terlebih dahulu
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Set text dari textview once
        val formattedDate = dateFormat.format(calendar.time)
        view?.findViewById<TextView>(R.id.tv_date)?.text = formattedDate
    }

    companion object {
        private const val DATE_PICKER_TAG = "DatePicker"
    }
}
