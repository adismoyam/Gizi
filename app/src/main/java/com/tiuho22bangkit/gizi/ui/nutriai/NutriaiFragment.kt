package com.tiuho22bangkit.gizi.ui.nutriai

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.tiuho22bangkit.gizi.R
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

        val sharedPreferences = requireContext().getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("auth_token", null)

        chatAdapter = ChatAdapter(messages)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = chatAdapter

        val imageView = binding.loadingGif
        Glide.with(this)
            .asGif()
            .load(R.drawable.bot) // ganti dengan GIF di folder drawable
            .into(imageView)

        sendButton.setOnClickListener {
            val userMessage = inputField.text.toString()
            if (userMessage.isEmpty()) {
                Toast.makeText(requireContext(), "Kolom Input Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show()
            } else {
                // Tambahkan pesan pengguna ke RecyclerView
                messages.add(ChatMessage(userMessage, true))
                chatAdapter.notifyItemInserted(messages.size - 1)
                recyclerView.scrollToPosition(messages.size - 1)

                binding.apply {
                    pageTitle.visibility = View.GONE
                    loadingIndicator.visibility = View.VISIBLE
                    titleInfo.visibility = View.GONE
                    titleDescription.visibility = View.GONE
                }
                startLoadingAnimation()

                viewModel.sendChatToApi(id!!, userMessage)
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
        binding.loadingGif.visibility = View.VISIBLE
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
        binding.loadingGif.visibility = View.GONE
        isLoading = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}