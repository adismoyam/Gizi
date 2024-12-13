package com.tiuho22bangkit.gizi.ui.medrec.kid

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.data.local.entity.KidAnalysisHistoryEntity
import com.tiuho22bangkit.gizi.databinding.FragmentKidahDialogBinding
import com.tiuho22bangkit.gizi.ui.ViewModelFactory
import com.tiuho22bangkit.gizi.ui.medrec.MedrecViewModel
import com.tiuho22bangkit.gizi.utility.calculateMonthDifference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KidAHDialogFragment : DialogFragment() {

    private val viewModel: MedrecViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var _binding: FragmentKidahDialogBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKidahDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil data dari   arguments
        val kidAHEntity: KidAnalysisHistoryEntity? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getParcelable(ARG_KID_ANALYSIS_DATA, KidAnalysisHistoryEntity::class.java)
            } else {
                @Suppress("DEPRECATION")
                arguments?.getParcelable(ARG_KID_ANALYSIS_DATA)
            }

        binding.apply {
            kidAHEntity?.let {
                // Isi TextView dengan data
                tvDatetime.text = getString(R.string.data_saat_dianalisis, kidAHEntity.datetime)
                tvNamaAnakValue.text = getString(R.string.nama_value, kidAHEntity.name)
                tvJenisKelaminValue.text = getString(R.string.jenis_kelamin_value, kidAHEntity.gender)
                tvTanggalLahirValue.text = getString(
                    R.string.tanggal_lahir_value,
                    kidAHEntity.birthDate
                )

                val usiaSaatDianalisis =
                    calculateMonthDifference(
                        kidAHEntity.birthDate,
                        kidAHEntity.datetime
                    )

                tvUsiaAnakValue.text = getString(R.string.usia_anak_value, usiaSaatDianalisis)
                tvTinggiAnakValue.text = getString(R.string.tinggi_value, kidAHEntity.height)
                tvBeratAnakValue.text = getString(R.string.berat_value, kidAHEntity.weight)
                tvHasilWasting.text = getString(
                    R.string.wasting_risk_medrec,
                    kidAHEntity.wastingRiskResult
                )
                tvHasilStunting.text = getString(
                    R.string.stunting_risk_medrec,
                    kidAHEntity.stuntingRiskResult
                )


                // Tombol hapus riwayat
                btnHapusRiwayat.setOnClickListener {
                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            viewModel.deleteKidAnalysisHistory(kidAHEntity)
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

    override fun onStart() {
        super.onStart()
        // Mengatur ukuran
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.8).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val ARG_KID_ANALYSIS_DATA = "kid_analysis_data"
        fun newInstance(kidAnalysisHistoryEntity: KidAnalysisHistoryEntity): KidAHDialogFragment {
            val fragment = KidAHDialogFragment()
            val args = Bundle()
            args.putParcelable(
                ARG_KID_ANALYSIS_DATA,
                kidAnalysisHistoryEntity
            ) // Pastikan KidEntity implement Parcelable
            fragment.arguments = args
            return fragment
        }
    }
}