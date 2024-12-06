package com.tiuho22bangkit.gizi.ui.article.rekomendasi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.databinding.FragmentRekomendasiBinding
import com.tiuho22bangkit.gizi.ui.article.ArticleAdapter
import com.tiuho22bangkit.gizi.ui.article.RemakeArticleViewModel

class RekomendasiFragment : Fragment() {

    private val viewModel: RemakeArticleViewModel by viewModels()

    private var _binding: FragmentRekomendasiBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { ArticleAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRekomendasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRVRekomendasiArticle()

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        rekomendasiArticleObserver()

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null && (viewModel.rekomendasiArticles.value == null || viewModel.rekomendasiArticles.value!!.isEmpty())) {
                showError(error)
            } else {
                hideError()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        rekomendasiArticleObserver()
    }

    private fun rekomendasiArticleObserver() {
        viewModel.rekomendasiArticles.observe(viewLifecycleOwner) { rekomendasiArticle ->
            adapter.submitList(rekomendasiArticle)
            binding.rvRekomendasiArticle.visibility = View.VISIBLE
        }
    }

    private fun setupRVRekomendasiArticle() {
        binding.rvRekomendasiArticle.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = this@RekomendasiFragment.adapter

            addItemDecoration(
                DividerItemDecoration(
                    context, (layoutManager as LinearLayoutManager).orientation
                )
            )
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun showError(message: String) {
        binding.tvErrorMessage.text = message
        binding.tvErrorMessage.visibility = View.VISIBLE
        if (viewModel.rekomendasiArticles.value.isNullOrEmpty()) {
            binding.rvRekomendasiArticle.visibility = View.INVISIBLE
        }
    }

    private fun hideError() {
        binding.tvErrorMessage.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}