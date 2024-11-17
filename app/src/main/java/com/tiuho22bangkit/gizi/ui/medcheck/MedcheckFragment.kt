package com.tiuho22bangkit.gizi.ui.medcheck

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.databinding.FragmentMedcheckBinding
import com.tiuho22bangkit.gizi.databinding.FragmentNutriaiBinding
import com.tiuho22bangkit.gizi.ui.nutriai.NutriaiViewModel

class MedcheckFragment : Fragment() {

    private var _binding: FragmentMedcheckBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val medcheckViewModel =
            ViewModelProvider(this).get(MedcheckViewModel::class.java)

        _binding = FragmentMedcheckBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textMedcheck
        medcheckViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}