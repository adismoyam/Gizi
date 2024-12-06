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
        _binding = FragmentNutriaiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[NutriaiViewModel::class.java]

        val sendButton = binding.sendButton
        val inputMessage = binding.inputMessage
        val chatContainer = binding.chatContainer

        sendButton.setOnClickListener {
            val message = inputMessage.text.toString()
            if (message.isNotBlank()) {
                viewModel.sendMessage(message)
                inputMessage.text.clear()
            }
        }

        viewModel.chatResponse.observe(viewLifecycleOwner) { response ->
            val textView = TextView(requireContext())
            textView.text = response
            chatContainer.addView(textView)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}