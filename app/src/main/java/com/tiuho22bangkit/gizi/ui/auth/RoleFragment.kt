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
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.databinding.FragmentLoginBinding
import com.tiuho22bangkit.gizi.databinding.FragmentRoleBinding
import com.tiuho22bangkit.gizi.ui.ViewModelFactory
import com.tiuho22bangkit.gizi.ui.analysis.MomAnalysisActivity
import com.tiuho22bangkit.gizi.ui.profile.IsiDataIbuActivity
import com.tiuho22bangkit.gizi.ui.profile.ProfileViewModel
import java.util.UUID

class RoleFragment : Fragment() {
    private val viewModel: ProfileViewModel by viewModels {
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

        binding.rolePregnant.setOnClickListener {
            viewModel.isMomDataAvailable.observe(viewLifecycleOwner) { isMomDataAvailable ->
                if (isMomDataAvailable) {
                    viewModel.loadMomData().observe(viewLifecycleOwner) { mom ->
                        val intent = Intent(requireContext(), MomAnalysisActivity::class.java)
                            .putExtra(MomAnalysisActivity.MOM_DATA, mom)
                        startActivity(intent)
                    }
                } else {
                    val intent = Intent(requireContext(), IsiDataIbuActivity::class.java)
                    startActivity(intent)
                }
            }
            highlightSelectedRole(binding.rolePregnant)
        }

        binding.roleHavingToddler.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_role_to_isiDataAnakFragment)
            highlightSelectedRole(binding.roleHavingToddler)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun highlightSelectedRole(selectedCard: CardView) {
        binding.rolePregnant.setCardBackgroundColor(resources.getColor(android.R.color.white))
        binding.roleHavingToddler.setCardBackgroundColor(resources.getColor(android.R.color.white))

        selectedCard.setCardBackgroundColor(resources.getColor(R.color.purple_200))
    }
}