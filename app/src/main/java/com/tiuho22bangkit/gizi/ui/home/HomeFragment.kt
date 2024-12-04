package com.tiuho22bangkit.gizi.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.databinding.FragmentHomeBinding
import com.tiuho22bangkit.gizi.databinding.FragmentProfileBinding
import com.tiuho22bangkit.gizi.ui.ViewModelFactory
import com.tiuho22bangkit.gizi.ui.analysis.MomAnalysisActivity
import com.tiuho22bangkit.gizi.ui.profile.IsiDataIbuActivity
import com.tiuho22bangkit.gizi.ui.profile.KidProfileAdapter
import com.tiuho22bangkit.gizi.ui.profile.ProfileViewModel

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy {
        KidProfileAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRVKidProfile()
        binding.add.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_isiDataAnakFragment)
        }
    }

    private fun setupRVKidProfile() {
        binding.rvKids.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = this@HomeFragment.adapter
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadKidData().observe(viewLifecycleOwner) { kidList ->
            Log.d("ProfileFragment", "Kid list updated: $kidList")
            adapter.submitList(kidList)
        }
        viewModel.checkMomData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}