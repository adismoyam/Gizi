package com.tiuho22bangkit.gizi.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.databinding.FragmentRegisterBinding
import com.tiuho22bangkit.gizi.databinding.FragmentVerifyBinding
import java.util.UUID

class VerifyFragment : Fragment() {

    private var _binding: FragmentVerifyBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference

    val args: VerifyFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        (activity as AppCompatActivity).supportActionBar?.hide()
        _binding = FragmentVerifyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = args.email
        val username = args.username
        val password = args.password
        val token = args.token

        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://capstone-bangkit-2024-default-rtdb.firebaseio.com/")

        binding.verifyOtpButton.setOnClickListener {
            val otp = binding.otpEditText.text.toString().trim()

            if (FirebaseAuth.getInstance().isSignInWithEmailLink(otp)) {
                FirebaseAuth.getInstance().signInWithEmailLink(email, otp)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Verifikasi berhasil, simpan data pengguna
                            saveUserData(email, password, username, token)
                            Toast.makeText(requireContext(), "Verifikasi berhasil!", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.navigation_login)
                        } else {
                            Toast.makeText(requireContext(), "Verifikasi gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(requireContext(), "Link OTP tidak valid", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserData(email: String, password: String, username: String, token: String) {
        val encodedEmail = encodeEmail(email)

        database.child(token).child("username").setValue(username)
        database.child(token).child("email").setValue(encodedEmail)
        database.child(token).child("password").setValue(password)
        database.child(token).child("token").setValue(token)
    }

    private fun encodeEmail(email: String): String {
        return email.replace(".", ",")
    }
}