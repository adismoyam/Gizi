package com.tiuho22bangkit.gizi.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.data.local.GiziDatabase
import com.tiuho22bangkit.gizi.data.local.dao.KidDao
import com.tiuho22bangkit.gizi.databinding.FragmentIsiDataAnakBinding
import com.tiuho22bangkit.gizi.utility.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID


class IsiDataAnakFragment : Fragment(), DatePickerFragment.DialogDateListener {

    private lateinit var giziDatabase: GiziDatabase
    private lateinit var kidDao: KidDao

    private var _binding: FragmentIsiDataAnakBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIsiDataAnakBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://capstone-bangkit-2024-default-rtdb.firebaseio.com/")

        giziDatabase = GiziDatabase.getInstance(requireContext())
        kidDao = giziDatabase.kidDao()

        // tanggal lahir handler
        val dateButton = view.findViewById<Button>(R.id.btn_date)
        dateButton.setOnClickListener {
            showDatePicker()
        }

        binding.btnClose.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnTambahData.setOnClickListener {
            val id = generateUUID()
            val name = binding.kidName.text.toString().trim()
            val gender = when (binding.rgGender.checkedRadioButtonId) {
                R.id.rb_boy -> "Laki-Laki"
                R.id.rb_girl -> "Perempuan"
                else -> ""
            }
            val birthDate = binding.btnDate.text.toString()
            val height = binding.kidHeight.text.toString().toFloatOrNull()
            val weight = binding.kidWeight.text.toString().toFloatOrNull()
            val uri = ""

            val sharedPreferences =
                requireContext().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("auth_token", null)

            when {
                name.isEmpty() -> {
                    binding.kidName.error = "Nama anak harus diisi"
                    binding.kidName.requestFocus()
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
                    binding.kidHeight.error = "Masukkan tinggi badan yang valid"
                    binding.kidHeight.requestFocus()
                }

                weight == null || weight <= 0 -> {
                    binding.kidWeight.error = "Masukkan berat badan yang valid"
                    binding.kidWeight.requestFocus()
                }

                else -> {
                    database = FirebaseDatabase.getInstance().getReference("kids")
                    database.child(id).apply {
                        child("id").setValue(id)
                        child("name").setValue(name)
                        child("gender").setValue(gender)
                        child("weight").setValue(weight)
                        child("height").setValue(height)
                        child("birthDate").setValue(birthDate)
                        child("token").setValue(token)
                        child("uri").setValue(uri)
                    }
                    Toast.makeText(
                        requireContext(),
                        "Data anak berhasil disimpan",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.navigation_home)
                }
            }
        }
    }

    private fun generateUUID(): String {
        return UUID.randomUUID().toString()
    }

    override fun onDialogDateSet(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)
        binding.btnDate.text = formattedDate
    }

    private fun showDatePicker() {
        val datePickerFragment = DatePickerFragment()
        datePickerFragment.show(childFragmentManager, DATE_PICKER_TAG)
    }

    companion object {
        private const val DATE_PICKER_TAG = "DatePicker"
    }
}
