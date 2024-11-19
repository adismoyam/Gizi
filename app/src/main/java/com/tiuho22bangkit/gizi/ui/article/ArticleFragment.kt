package com.tiuho22bangkit.gizi.ui.article

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.data.remote.ArticlesItem
import com.tiuho22bangkit.gizi.databinding.FragmentArticleBinding
import com.tiuho22bangkit.gizi.ui.article.detail.DetailFragment

class ArticleFragment : Fragment() {

    private val viewModel: ArticleViewModel by viewModels()
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvArticle.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvArticle.addItemDecoration(itemDecoration)

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.articleData.observe(viewLifecycleOwner) { articleList ->
            if (articleList != null && articleList.isNotEmpty()) {
                setArticleData(articleList)
                hideError()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null && (viewModel.articleData.value == null || viewModel.articleData.value!!.isEmpty())) {
                showError(error)
            } else {
                hideError()
            }
        }

        viewModel.findArticles()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun setArticleData(articlesList: List<ArticlesItem>) {

        // safe args
        val adapter = ArticleAdapter { article ->
            val action = ArticleFragmentDirections.actionToDetailFragment(article)
            findNavController().navigate(action)
        }

        adapter.submitList(articlesList)
        binding.rvArticle.adapter = adapter
        binding.rvArticle.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        binding.tvErrorMessage.text = message
        binding.tvErrorMessage.visibility = View.VISIBLE
        if (viewModel.articleData.value.isNullOrEmpty()) {
            binding.rvArticle.visibility = View.INVISIBLE
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