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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.databinding.FragmentProfileBinding
import com.tiuho22bangkit.gizi.ui.ViewModelFactory
import com.tiuho22bangkit.gizi.ui.analysis.MomAnalysisActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        // Mengamati perubahan pada LiveData kidData
        viewModel.kidData.observe(viewLifecycleOwner) { kidList ->
            Log.d("ProfileFragment", "Kid list updated: $kidList")
            adapter.submitList(kidList)
        }

        val buttonTambahAnak: ImageButton = view.findViewById(R.id.tambah_anak)
        buttonTambahAnak.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_isiDataAnakFragment)
        }

        // Mengamati apakah data ibu sudah ada
        viewModel.isMomDataAvailable.observe(viewLifecycleOwner) { isMomDataAvailable ->
            val intent = if (isMomDataAvailable) {
                // Jika data ibu sudah ada, navigasi ke MomAnalysisActivity
                Intent(requireContext(), MomAnalysisActivity::class.java)
//                    .putExtra(MomAnalysisActivity.MOM_DATA, mom)
            } else {
                // Jika data ibu belum ada, navigasi ke IsiDataIbuActivity
                Intent(requireContext(), IsiDataIbuActivity::class.java)
            }

            binding.circleImage.setOnClickListener {
                startActivity(intent)
            }
        }

    }




    override fun onResume() {
        super.onResume()
        viewModel.kidData.observe(viewLifecycleOwner) { kidList ->
            adapter.submitList(kidList)
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