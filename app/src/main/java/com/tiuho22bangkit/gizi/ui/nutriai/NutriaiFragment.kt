package com.tiuho22bangkit.gizi.ui.nutriai

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
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
            // Helper function untuk memproses markdown-like text menjadi HTML
            val formattedResponse = response
                .replace("\n", "<br>") // Ganti \n dengan <br> untuk baris baru
                .replace("## (.*?)<br>".toRegex(), "<h2>$1</h2>") // Ganti ## heading dengan <h2>
                .replace("\\*\\*(.*?)\\*\\*".toRegex(), "<b>$1</b>") // Ganti **teks** dengan <b>teks</b>
                .replace("\\*(.*?)\\*".toRegex(), "<i>$1</i>") // Ganti *teks* dengan <i>teks</i>
                .replace("^\\* (.*?)<br>".toRegex(RegexOption.MULTILINE), "<li>$1</li>") // Ganti * item dengan <li>item</li>
                .replace("<li>(.*?)</li>".toRegex(), "<ul><li>$1</li></ul>") // Bungkus semua <li> dalam <ul>

            // Gunakan HtmlCompat.fromHtml untuk menampilkan teks dengan format HTML
            responseText.text = HtmlCompat.fromHtml(
                formattedResponse,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}