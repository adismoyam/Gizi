package com.tiuho22bangkit.gizi.ui.auth

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.databinding.FragmentLoginBinding
import com.tiuho22bangkit.gizi.ui.ViewModelFactory

class LoginFragment : Fragment() {
    private val userViewModel: UserViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = context?.let {
            TransitionInflater.from(it)
                .inflateTransition(android.R.transition.move)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = FirebaseAuth.getInstance()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        (activity as AppCompatActivity).supportActionBar?.hide()
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun encodeEmail(email: String): String {
        return email.replace(".", ",")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://capstone-bangkit-2024-default-rtdb.firebaseio.com/")

        binding.registerText.setOnClickListener {
            findNavController().navigate(R.id.navigation_register)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val encodeEmail = encodeEmail(email)

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Email dan password harus diisi",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(requireContext(), "Format email tidak valid", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            if (user?.isEmailVerified == true) {
                                val uid = user.uid // Dapatkan UID di sini

                                // Simpan UID ke SharedPreferences
                                val sharedPreferences = requireContext().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString("auth_token", uid) // Simpan UID sebagai token
                                editor.apply()

                                Toast.makeText(requireContext(), "Login berhasil", Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.navigation_home)
                            } else {
                                // Email belum diverifikasi
                                Toast.makeText(
                                    requireContext(),
                                    "Email belum diverifikasi. Silakan periksa email Anda.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                auth.signOut() // Logout jika email belum diverifikasi
                            }
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Login gagal: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

//                database.child("users").orderByChild("email").equalTo(encodeEmail)
//                    .addListenerForSingleValueEvent(object : ValueEventListener {
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            if (snapshot.exists()) {
//                                for (userSnapshot in snapshot.children) {
//                                    val storedPassword = userSnapshot.child("password").getValue(String::class.java)
//                                    if (storedPassword == password) {
//
//                                        val userId = userSnapshot.child("token").getValue(String::class.java) //.key ?: ""
//                                        val sharedPreference = requireContext().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
//                                        val editor = sharedPreference.edit()
//                                        editor.putString("auth_token", userId)
//                                        editor.apply()
//
//                                        Toast.makeText(requireContext(), "Login Berhasil!", Toast.LENGTH_SHORT).show()
//                                        findNavController().navigate(R.id.navigation_home)
//                                        break
//                                    }
//                                }
//                            } else {
//                                Toast.makeText(requireContext(), "Email tidak ditemukan!", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//
//                        override fun onCancelled(error: DatabaseError) {
//                            Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
//                        }
//                    })
            }
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        userViewModel.loginResult.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "Login berhasil: ${it.username}", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.navigation_home)
            }.onFailure {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}