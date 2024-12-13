package com.tiuho22bangkit.gizi.ui.medrec.mom

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.data.local.entity.MomAnalysisHistoryEntity
import com.tiuho22bangkit.gizi.databinding.FragmentIsiDataAnakBinding
import com.tiuho22bangkit.gizi.databinding.FragmentMomahDialogBinding
import com.tiuho22bangkit.gizi.ui.ViewModelFactory
import com.tiuho22bangkit.gizi.ui.medrec.MedrecViewModel
import com.tiuho22bangkit.gizi.utility.calculateMonthDifference
import com.tiuho22bangkit.gizi.utility.calculateYearAge
import com.tiuho22bangkit.gizi.utility.calculateYearDifference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MomAHDialogFragment : DialogFragment() {

    private val viewModel: MedrecViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var _binding: FragmentMomahDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMomahDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val momAHEntity: MomAnalysisHistoryEntity? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getParcelable(ARG_MOM_ANALYSIS_DATA, MomAnalysisHistoryEntity::class.java)
            } else {
                @Suppress("DEPRECATION")
                arguments?.getParcelable(ARG_MOM_ANALYSIS_DATA)
            }

        binding.apply {
            momAHEntity?.let {
                tvDatetime.text = getString(R.string.data_saat_dianalisis, momAHEntity.datetime)
                tvHphtValue.text = getString(R.string.hpht_value, momAHEntity.lastMenstrualPeriod)
                tvHplValue.text = getString(R.string.hpl_value, momAHEntity.estimatedDeliveryDate)
                tvTanggalLahirValue.text = getString(R.string.tanggal_lahir_value, momAHEntity.birthDate)

                val usiaSaatDianalisis =
                    calculateYearDifference(
                        momAHEntity.birthDate,
                        momAHEntity.datetime
                    )
                tvUsiaValue.text = getString(R.string.usia_value, usiaSaatDianalisis)
                tvTekananDarahValue.text = getString(R.string.tekanan_darah_value, momAHEntity.systolicBloodPressure.toInt(), momAHEntity.diastolicBloodPressure.toInt())
                tvKadarGulaDarahValue.text = getString(R.string.kadar_gula_darah_value, momAHEntity.bloodSugarLevel)
                tvSuhuTubuhValue.text = getString(R.string.suhu_tubuh_value, momAHEntity.bodyTemperature)
                tvDetakJantungValue.text = getString(R.string.detak_jantung_value, momAHEntity.heartRate.toInt())
                tvHasil.text = getString(R.string.pregnancy_risk_medrec, momAHEntity.result)

                btnHapusRiwayat.setOnClickListener {
                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            viewModel.deleteMomAnalysisHistory(momAHEntity)
                            withContext(Dispatchers.Main) {
                                showToast("Data Berhasil di Hapus!")
                                dismiss()
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                showToast("Terjadi kesalahan, coba lagi")
                            }
                        }
                    }
                }
            }

            // Tombol tutup
            btnClose.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 1),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    companion object {
        private const val ARG_MOM_ANALYSIS_DATA = "mom_analysis_data"
        fun newInstance(momAnalysisHistoryEntity: MomAnalysisHistoryEntity): MomAHDialogFragment {
            val fragment = MomAHDialogFragment()
            val args = Bundle()
            args.putParcelable(
                ARG_MOM_ANALYSIS_DATA,
                momAnalysisHistoryEntity
            )
            fragment.arguments = args
            return fragment
        }
    }
}