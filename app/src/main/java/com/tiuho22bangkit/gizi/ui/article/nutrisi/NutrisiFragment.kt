package com.tiuho22bangkit.gizi.ui.article.nutrisi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiuho22bangkit.gizi.databinding.FragmentNutrisiBinding
import com.tiuho22bangkit.gizi.ui.article.ArticleAdapter
import com.tiuho22bangkit.gizi.ui.article.RemakeArticleViewModel

class NutrisiFragment : Fragment() {

    private val viewModel: RemakeArticleViewModel by viewModels()

    private var _binding: FragmentNutrisiBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { ArticleAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNutrisiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRVNutrisiArticle()

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        nutrisiArticleObserver()

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null && (viewModel.nutrisiArticles.value == null || viewModel.nutrisiArticles.value!!.isEmpty())) {
                showError(error)
            } else {
                hideError()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        nutrisiArticleObserver()
    }

    private fun nutrisiArticleObserver() {
        viewModel.nutrisiArticles.observe(viewLifecycleOwner) { nutrisiArticle ->
            adapter.submitList(nutrisiArticle)
            binding.rvNutrisiArticle.visibility = View.VISIBLE
        }
    }

    private fun setupRVNutrisiArticle() {
        binding.rvNutrisiArticle.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = this@NutrisiFragment.adapter

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
        if (viewModel.nutrisiArticles.value.isNullOrEmpty()) {
            binding.rvNutrisiArticle.visibility = View.INVISIBLE
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