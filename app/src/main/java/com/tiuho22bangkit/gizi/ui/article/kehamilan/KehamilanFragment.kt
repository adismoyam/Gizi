package com.tiuho22bangkit.gizi.ui.article.kehamilan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiuho22bangkit.gizi.databinding.FragmentKehamilanBinding
import com.tiuho22bangkit.gizi.ui.article.ArticleAdapter
import com.tiuho22bangkit.gizi.ui.article.RemakeArticleViewModel

class KehamilanFragment : Fragment() {

    private val viewModel: RemakeArticleViewModel by viewModels()

    private var _binding: FragmentKehamilanBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { ArticleAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentKehamilanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRVKehamilanArticle()

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        kehamilanArticleObserver()

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null && (viewModel.kehamilanArticles.value == null || viewModel.kehamilanArticles.value!!.isEmpty())) {
                showError(error)
            } else {
                hideError()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        kehamilanArticleObserver()
    }

    private fun kehamilanArticleObserver() {
        viewModel.kehamilanArticles.observe(viewLifecycleOwner) { kehamilanArticle ->
            adapter.submitList(kehamilanArticle)
            binding.rvKehamilanArticle.visibility = View.VISIBLE
        }
    }

    private fun setupRVKehamilanArticle() {
        binding.rvKehamilanArticle.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = this@KehamilanFragment.adapter

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
        if (viewModel.kehamilanArticles.value.isNullOrEmpty()) {
            binding.rvKehamilanArticle.visibility = View.INVISIBLE
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