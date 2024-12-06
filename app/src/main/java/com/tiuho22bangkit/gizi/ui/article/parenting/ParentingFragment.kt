package com.tiuho22bangkit.gizi.ui.article.parenting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiuho22bangkit.gizi.databinding.FragmentParentingBinding
import com.tiuho22bangkit.gizi.ui.article.ArticleAdapter
import com.tiuho22bangkit.gizi.ui.article.RemakeArticleViewModel

class ParentingFragment : Fragment() {

    private val viewModel: RemakeArticleViewModel by viewModels()

    private var _binding: FragmentParentingBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy { ArticleAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentParentingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRVParentingArticle()

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        parentingArticleObserver()

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null && (viewModel.parentingArticles.value == null || viewModel.parentingArticles.value!!.isEmpty())) {
                showError(error)
            } else {
                hideError()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        parentingArticleObserver()
    }

    private fun parentingArticleObserver() {
        viewModel.parentingArticles.observe(viewLifecycleOwner) { parentingArticle ->
            adapter.submitList(parentingArticle)
            binding.rvParentingArticle.visibility = View.VISIBLE
        }
    }

    private fun setupRVParentingArticle() {
        binding.rvParentingArticle.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = this@ParentingFragment.adapter

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
        if (viewModel.parentingArticles.value.isNullOrEmpty()) {
            binding.rvParentingArticle.visibility = View.INVISIBLE
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