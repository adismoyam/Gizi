package com.tiuho22bangkit.gizi.ui.medrec.mom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiuho22bangkit.gizi.databinding.FragmentMomMedrecBinding
import com.tiuho22bangkit.gizi.ui.ViewModelFactory
import com.tiuho22bangkit.gizi.ui.medrec.MedrecViewModel


class MomMedrecFragment : Fragment() {

    private val viewModel: MedrecViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var _binding: FragmentMomMedrecBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy {
        MomAHAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMomMedrecBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRVMomAnalysisHistory()
    }

    override fun onResume() {
        super.onResume()
        momAnalysisHistoryObserver()
    }

    private fun momAnalysisHistoryObserver() {
        viewModel.loadMomAnalysisHistoryData().observe(viewLifecycleOwner) { momAnalysisHistory ->
            adapter.submitList(momAnalysisHistory)
        }
    }

    private fun setupRVMomAnalysisHistory() {
        binding.rvMomAnalysisHistory.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = this@MomMedrecFragment.adapter

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