package com.tiuho22bangkit.gizi.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.databinding.FragmentHomeBinding
import com.tiuho22bangkit.gizi.ui.ViewModelFactory
import com.tiuho22bangkit.gizi.ui.article.ArticleAdapter
import com.tiuho22bangkit.gizi.ui.profile.KidProfileAdapter

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy {
        KidProfileAdapter()
    }

    private val articleAdapter by lazy {
        ArticleAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.hide()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRVArticle()

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        articleObserver()

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null && (viewModel.allArticles.value == null || viewModel.allArticles.value!!.isEmpty())) {
                showError(error)
            } else {
                hideError()
            }
        }

        setupRVKidProfile()
        binding.add.setOnClickListener {
            findNavController().navigate(R.id.navigation_role)
        }
        setupCardDescription()

        binding.buttonChatbot.setOnClickListener {
            findNavController().navigate(R.id.navigation_nutriai)
        }
    }

    private fun articleObserver() {
        viewModel.allArticles .observe(viewLifecycleOwner) { article ->
            val limitArticle = article.take(5)
            articleAdapter.submitList(limitArticle)
            binding.articleHome.visibility = View.VISIBLE
        }
    }

    private fun setupRVArticle() {
        binding.articleHome.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = this@HomeFragment.articleAdapter

            addItemDecoration(
                DividerItemDecoration(
                    context, (layoutManager as LinearLayoutManager).orientation
                )
            )
        }
    }

    private fun setupCardDescription() {
        viewModel.lastKidAnalysisHistory.observe(viewLifecycleOwner, Observer { kid ->
            if (kid != null) {
                val stuntingResult = kid.stuntingRiskResult ?: getString(R.string.condition_unknown)
                val wastingResult = kid.wastingRiskResult ?: getString(R.string.condition_unknown)

                binding.tvStuntingDescription.text = when (stuntingResult) {
                    "Normal" -> getString(R.string.stunting_normal)
                    "Severely Stunted" -> getString(R.string.stunting_Severely_Stunted)
                    "Stunted" -> getString(R.string.stunting_Stunted)
                    "Tall" -> getString(R.string.stunting_Tall)
                    else -> getString(R.string.condition_unknown)
                }

                binding.tvWastingDescription.text = when (wastingResult) {
                    "Normal" -> getString(R.string.wasting_normal)
                    "Risk of Overweight" -> getString(R.string.wasting_Risk_of_Overweight)
                    "Severely Underweight" -> getString(R.string.wasting_Severely_Underweight)
                    "Underweight" -> getString(R.string.wasting_Underweight)
                    else -> getString(R.string.condition_unknown)
                }
            } else {
                binding.tvStuntingDescription.text = getString(R.string.condition_unknown)
                binding.tvWastingDescription.text = getString(R.string.condition_unknown)
            }
        })

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun hideError() {
        binding.tvErrorMessage.visibility = View.GONE
    }

    private fun showError(message: String) {
        binding.tvErrorMessage.text = message
        binding.tvErrorMessage.visibility = View.VISIBLE
        if (viewModel.allArticles.value.isNullOrEmpty()) {
            binding.articleHome.visibility = View.INVISIBLE
        }
    }

    private fun getUserToken(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }

    private fun setupRVKidProfile() {
        binding.rvKids.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = this@HomeFragment.adapter
        }

        val userToken = getUserToken()
        if (userToken != null) {
            viewModel.loadKidDataFromFirebase(userToken).observe(viewLifecycleOwner) { kidList ->
                if (kidList.isNotEmpty()) {
                    adapter.submitList(kidList)
                } else {
                    Toast.makeText(requireContext(), "Data Anak Kosong.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "User token not found.", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onResume() {
        super.onResume()
        val token = getUserToken()!!
        viewModel.loadKidDataFromFirebase(token).observe(viewLifecycleOwner) { kidList ->
            Log.d("ProfileFragment", "Kid list updated: $kidList")
            adapter.submitList(kidList)
        }
        viewModel.checkMomData()
        articleObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}