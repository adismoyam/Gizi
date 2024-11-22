package com.tiuho22bangkit.gizi.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.databinding.FragmentRegisterBinding
import com.tiuho22bangkit.gizi.databinding.FragmentRoleBinding
import com.tiuho22bangkit.gizi.ui.ViewModelFactory

class RoleFragment : Fragment() {
    private val userViewModel: UserViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private var _binding: FragmentRoleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButton.setOnClickListener {
            val username = arguments?.getString("username")
            val email = arguments?.getString("email")
            val password = arguments?.getString("password")

            if (username!!.isEmpty() || email!!.isEmpty() || password!!.isEmpty()) {
                Toast.makeText(requireContext(), "Semua bidang harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userViewModel.registerUser(username, email, password, "user")
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        userViewModel.registerResult.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                // Redirect ke login atau halaman lain
            }.onFailure {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

}