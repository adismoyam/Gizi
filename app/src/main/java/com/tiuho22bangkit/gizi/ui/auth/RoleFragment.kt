package com.tiuho22bangkit.gizi.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.databinding.FragmentLoginBinding
import com.tiuho22bangkit.gizi.databinding.FragmentRoleBinding
import com.tiuho22bangkit.gizi.ui.ViewModelFactory
import java.util.UUID

class RoleFragment : Fragment() {
    private val userViewModel: UserViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private var _binding: FragmentRoleBinding? = null
    private val binding get() = _binding!!
    private lateinit var selectedRole: String

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        (activity as AppCompatActivity).supportActionBar?.hide()
        _binding = FragmentRoleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://capstone-bangkit-2024-default-rtdb.firebaseio.com/")

        val username = arguments?.getString("username")
        val email = arguments?.getString("email")
        val password = arguments?.getString("password")
        val token = generateToken()
        val encodedEmail = encodeEmail(email!!)

        binding.rolePregnant.setOnClickListener {
            selectedRole = "Pregnant"
            highlightSelectedRole(binding.rolePregnant)
        }

        binding.roleBreastfeeding.setOnClickListener {
            selectedRole = "Breastfeeding"
            highlightSelectedRole(binding.roleBreastfeeding)
        }

        binding.roleHavingToddler.setOnClickListener {
            selectedRole = "Having Toddler"
            highlightSelectedRole(binding.roleHavingToddler)
        }

        binding.registerButton.setOnClickListener {
            if (::selectedRole.isInitialized) {
                database = FirebaseDatabase.getInstance().getReference("users")
                database.child(encodedEmail).child("username").setValue(username)
                database.child(encodedEmail).child("email").setValue(encodedEmail)
                database.child(encodedEmail).child("password").setValue(password)
                database.child(encodedEmail).child("token").setValue(token)
                database.child(encodedEmail).child("role").setValue(selectedRole)
                findNavController().navigate(R.id.navigation_login)
                Toast.makeText(requireContext(), "Buat Akun Berhasil! Silahkan Login!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Pilih Tipe Pengguna terlebih dahulu!", Toast.LENGTH_SHORT).show()
            }
        }
        observeViewModel()
    }

    private fun encodeEmail(email: String): String {
        return email.replace(".", ",")
    }

    private fun observeViewModel() {
        userViewModel.registerResult.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.navigation_login)
            }.onFailure {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun generateToken(): String {
        return UUID.randomUUID().toString()
    }

    private fun highlightSelectedRole(selectedCard: CardView) {
        binding.rolePregnant.setCardBackgroundColor(resources.getColor(android.R.color.white))
        binding.roleBreastfeeding.setCardBackgroundColor(resources.getColor(android.R.color.white))
        binding.roleHavingToddler.setCardBackgroundColor(resources.getColor(android.R.color.white))

        selectedCard.setCardBackgroundColor(resources.getColor(R.color.purple_200))
    }
}