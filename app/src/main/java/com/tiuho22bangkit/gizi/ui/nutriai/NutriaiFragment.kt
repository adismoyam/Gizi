package com.tiuho22bangkit.gizi.ui.nutriai

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiuho22bangkit.gizi.data.remote.ChatMessage
import com.tiuho22bangkit.gizi.databinding.FragmentNutriaiBinding
import com.tiuho22bangkit.gizi.ui.ViewModelFactory

class NutriaiFragment : Fragment() {

    private lateinit var viewModel: NutriaiViewModel

    private var _binding: FragmentNutriaiBinding? = null
    private val binding get() = _binding!!

    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<ChatMessage>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[NutriaiViewModel::class.java]
        _binding = FragmentNutriaiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        val recyclerView = binding.chatRecyclerView
        val inputField = binding.inputField
        val sendButton = binding.sendButton

        chatAdapter = ChatAdapter(messages)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = chatAdapter

        sendButton.setOnClickListener {
            val userMessage = inputField.text.toString()
            if (userMessage.isNotBlank()) {
                // Tambahkan pesan pengguna ke RecyclerView
                messages.add(ChatMessage(userMessage, true))
                chatAdapter.notifyItemInserted(messages.size - 1)
                recyclerView.scrollToPosition(messages.size - 1)

                binding.pageTitle.visibility = View.GONE
                binding.loadingIndicator.visibility = View.VISIBLE
                startLoadingAnimation()

                viewModel.sendChatToApi(userMessage)
                inputField.text.clear()
            }
        }


        viewModel.response.observe(viewLifecycleOwner) { response ->
            val formattedResponse = response
                .replace("\n", "<br>")
                .replace("##\\s*(.*?)<br>".toRegex(), "<h2>$1</h2>")
                .replace("\\*\\*(.*?)\\*\\*".toRegex(), "<b>$1</b>")
                .replace("\\*(.*?)\\*".toRegex(), "<i>$1</i>")
                .replace("^\\*\\s+(.*?)<br>".toRegex(RegexOption.MULTILINE), "<li>$1</li>")
                .replace("(<li>.*?</li>)".toRegex(RegexOption.DOT_MATCHES_ALL), "<ul>$1</ul>")

            stopLoadingAnimation()
            binding.loadingIndicator.visibility = View.GONE
            binding.pageTitle.visibility = View.VISIBLE

            messages.add(ChatMessage(formattedResponse, false))
            chatAdapter.notifyItemInserted(messages.size - 1)
            recyclerView.scrollToPosition(messages.size - 1)
        }

    }

    private var isLoading = false

    private fun startLoadingAnimation() {
        isLoading = true
        val loadingText = binding.loadingIndicator
        val handler = Handler(Looper.getMainLooper())
        val loadingStates = arrayOf("•", "• •", "• • •")
        var index = 0

        handler.post(object : Runnable {
            override fun run() {
                if (isLoading) {
                    loadingText.text = loadingStates[index]
                    index = (index + 1) % loadingStates.size
                    handler.postDelayed(this, 500)
                } else {
                    loadingText.text = ""
                }
            }
        })
    }

    private fun stopLoadingAnimation() {
        isLoading = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}