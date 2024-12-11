package com.tiuho22bangkit.gizi.ui.analysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.databinding.FragmentKidAnalysisDialogBinding

class KidAnalysisDialogFragment : DialogFragment() {

    private var wastingResult: String? = null
    private var stuntingResult: String? = null
    private var heightInput: String? = null
    private var weightInput: String? = null

    private var _binding: FragmentKidAnalysisDialogBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(wasting: String, stunting: String, height: String, weight: String): KidAnalysisDialogFragment {
            val fragment = KidAnalysisDialogFragment()
            val args = Bundle()
            args.putString("wasting_result", wasting)
            args.putString("stunting_result", stunting)
            args.putString("height_result", height)
            args.putString("weight_result", weight)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            wastingResult = it.getString("wasting_result")
            stuntingResult = it.getString("stunting_result")
            heightInput = it.getString("height_result")
            weightInput = it.getString("weight_result")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        _binding = FragmentKidAnalysisDialogBinding.inflate(inflater, container, false)

        val tvWastingResult = binding.tvWastingResult
        val tvStuntingResult = binding.tvStuntingResult
        val btnClose = binding.btnClose

        tvWastingResult.text = "Weight: $weightInput kg ($wastingResult)"
        tvStuntingResult.text = "Height: $heightInput cm ($stuntingResult)"

        binding.tvStuntingDescription.text = when (stuntingResult) {
            "Normal" -> getString(R.string.stunting_normal)
            "Severely Stunted" -> getString(R.string.stunting_Severely_Stunted)
            "Stunted" -> getString(R.string.stunting_Stunted)
            "Tall" -> getString(R.string.stunting_Tall)
            else -> getString(R.string.condition_unknown)
        }

        btnClose.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),  // 90% dari lebar layar
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
