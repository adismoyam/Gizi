package com.tiuho22bangkit.gizi.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.databinding.FragmentProfileBinding
import com.tiuho22bangkit.gizi.ui.ViewModelFactory
import com.tiuho22bangkit.gizi.ui.analysis.MomAnalysisActivity

class ProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy {
        KidProfileAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRVKidProfile()

        val buttonTambahAnak: ImageButton = view.findViewById(R.id.tambah_anak)
        buttonTambahAnak.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_isiDataAnakFragment)
        }

        binding.circleImage.setOnClickListener {
            val intent = Intent(requireContext(), IsiDataIbuActivity::class.java)
            startActivity(intent)
        }
        momObserver()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadKidData().observe(viewLifecycleOwner) { kidList ->
            Log.d("ProfileFragment", "Kid list updated: $kidList")
            adapter.submitList(kidList)
        }
        viewModel.checkMomData()
    }

    private fun momObserver() {
        viewModel.isMomDataAvailable.observe(viewLifecycleOwner) { isMomDataAvailable ->
            if (isMomDataAvailable) {
                viewModel.loadMomData().observe(viewLifecycleOwner) { mom ->
                    binding.tvName.text = mom.name
                    Glide.with(this)
                        .load(mom.uri)
                        .placeholder(R.drawable.mother)
                        .into(binding.circleImage)

                    binding.circleImage.setOnClickListener {
                        val intent = Intent(requireContext(), MomAnalysisActivity::class.java)
                            .putExtra(MomAnalysisActivity.MOM_DATA, mom)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun setupRVKidProfile() {
        binding.rvProfilAnak.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = this@ProfileFragment.adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}