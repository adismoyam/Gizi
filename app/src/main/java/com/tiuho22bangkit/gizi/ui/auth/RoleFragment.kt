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
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.databinding.FragmentLoginBinding
import com.tiuho22bangkit.gizi.databinding.FragmentRoleBinding
import com.tiuho22bangkit.gizi.ui.ViewModelFactory

class RoleFragment : Fragment() {
    private val userViewModel: UserViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private var _binding: FragmentRoleBinding? = null
    private val binding get() = _binding!!
    private lateinit var selectedRole: String

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

        val username = arguments?.getString("username")
        val email = arguments?.getString("email")
        val password = arguments?.getString("password")

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
                userViewModel.registerUser(username!!, email!!, password!!, selectedRole)
            } else {
                Toast.makeText(requireContext(), "Pilih Tipe Pengguna terlebih dahulu!", Toast.LENGTH_SHORT).show()
            }
        }

        observeViewModel()
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

    private fun highlightSelectedRole(selectedCard: CardView) {
        binding.rolePregnant.setCardBackgroundColor(resources.getColor(android.R.color.white))
        binding.roleBreastfeeding.setCardBackgroundColor(resources.getColor(android.R.color.white))
        binding.roleHavingToddler.setCardBackgroundColor(resources.getColor(android.R.color.white))

        selectedCard.setCardBackgroundColor(resources.getColor(R.color.purple_200))
    }
}