package com.tiuho22bangkit.gizi.ui.medrec.kid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiuho22bangkit.gizi.databinding.FragmentKidMedrecBinding
import com.tiuho22bangkit.gizi.ui.ViewModelFactory
import com.tiuho22bangkit.gizi.ui.medrec.MedrecViewModel


class KidMedrecFragment : Fragment() {

    private val viewModel: MedrecViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var _binding: FragmentKidMedrecBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy {
        KidAHAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKidMedrecBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRVKidAnalysisHistory()

    }

    override fun onResume() {
        super.onResume()
        kidAnalysisHistoryObserver()
    }

    private fun kidAnalysisHistoryObserver() {
        viewModel.loadKidAnalysisHistoryData().observe(viewLifecycleOwner) { kidAnalysisHistory ->
            adapter.submitList(kidAnalysisHistory)
        }
    }

    private fun setupRVKidAnalysisHistory() {
        binding.rvKidAnalysisHistory.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = this@KidMedrecFragment.adapter

            addItemDecoration(
                DividerItemDecoration(
                    context, (layoutManager as LinearLayoutManager).orientation
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}