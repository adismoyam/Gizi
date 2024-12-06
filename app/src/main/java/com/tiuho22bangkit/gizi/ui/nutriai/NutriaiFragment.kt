package com.tiuho22bangkit.gizi.ui.nutriai

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.tiuho22bangkit.gizi.data.GiziRepository
import com.tiuho22bangkit.gizi.data.remote.ApiConfig
import com.tiuho22bangkit.gizi.data.remote.ChatbotApiService
import com.tiuho22bangkit.gizi.databinding.FragmentNutriaiBinding
import com.tiuho22bangkit.gizi.databinding.FragmentProfileBinding
import com.tiuho22bangkit.gizi.ui.ViewModelFactory
import com.tiuho22bangkit.gizi.ui.profile.ProfileViewModel

class NutriaiFragment : Fragment() {

    private lateinit var viewModel: NutriaiViewModel

    private var _binding: FragmentNutriaiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory).get(NutriaiViewModel::class.java)
        _binding = FragmentNutriaiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val inputField = binding.inputField
        val sendButton = binding.sendButton
        val responseText = binding.responseText

        sendButton.setOnClickListener {
            val message = inputField.text.toString()
            viewModel.sendMessage(message)
        }

        viewModel.response.observe(viewLifecycleOwner) { response ->
            responseText.text = response
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}