package com.tiuho22bangkit.gizi.ui.auth

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.databinding.FragmentRegisterBinding
import java.util.UUID

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        (activity as AppCompatActivity).supportActionBar?.hide()
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://capstone-bangkit-2024-default-rtdb.firebaseio.com/")

        binding.loginText.setOnClickListener {
            findNavController().navigate(R.id.navigation_login)
        }

        binding.registerButton.setOnClickListener {

            val username = binding.usernameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()
            val encodedEmail = encodeEmail(email)
            val token = generateToken()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Semua bidang harus diisi", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (password.length < 8 || confirmPassword.length < 8) {
                Toast.makeText(requireContext(), "Password tidak boleh kurang dari 8 karakter", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(requireContext(), "Format email tidak valid", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            database = FirebaseDatabase.getInstance().getReference("users")
            database.child(token).child("username").setValue(username)
            database.child(token).child("email").setValue(encodedEmail)
            database.child(token).child("password").setValue(password)
            database.child(token).child("token").setValue(token)
            findNavController().navigate(R.id.navigation_login)
            Toast.makeText(requireContext(), "Buat Akun Berhasil! Silahkan Login!", Toast.LENGTH_SHORT).show()
//            val bundle = Bundle().apply {
//                putString("username", username)
//                putString("email", email)
//                putString("password", password)
//            }
//            findNavController().navigate(R.id.navigation_role, bundle)
        }
    }

    private fun encodeEmail(email: String): String {
        return email.replace(".", ",")
    }

    private fun generateToken(): String {
        return UUID.randomUUID().toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}