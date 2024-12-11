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
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.databinding.FragmentRegisterBinding
import java.util.UUID

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = FirebaseAuth.getInstance()
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

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.sendEmailVerification()?.addOnCompleteListener { emailTask ->
                            if (emailTask.isSuccessful) {
                                Toast.makeText(
                                    requireContext(),
                                    "Email verifikasi telah dikirim. Periksa email Anda.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                findNavController().navigate(R.id.navigation_login)
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Gagal mengirim email verifikasi.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Registrasi gagal: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

//            database = FirebaseDatabase.getInstance().getReference("users")
//            database.child(token).child("username").setValue(username)
//            database.child(token).child("email").setValue(encodedEmail)
//            database.child(token).child("password").setValue(password)
//            database.child(token).child("token").setValue(token)
//            findNavController().navigate(R.id.navigation_login)
//            Toast.makeText(requireContext(), "Buat Akun Berhasil! Silahkan Login!", Toast.LENGTH_SHORT).show()
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