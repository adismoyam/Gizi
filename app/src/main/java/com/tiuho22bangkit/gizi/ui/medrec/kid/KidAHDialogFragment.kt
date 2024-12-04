package com.tiuho22bangkit.gizi.ui.medrec.kid

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.data.local.entity.KidAnalysisHistoryEntity
import com.tiuho22bangkit.gizi.utility.calculateMonthDifference

class KidAHDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_kidah_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil data dari arguments
        val kidAHEntity: KidAnalysisHistoryEntity? =
            arguments?.getParcelable(ARG_KID_ANALYSIS_DATA, KidAnalysisHistoryEntity::class.java)

        // Isi TextView dengan data
        view.findViewById<TextView>(R.id.tv_datetime)?.text =
            kidAHEntity?.datetime?.let { datetime ->
                getString(R.string.data_saat_dianalisis, datetime)
            }
        view.findViewById<TextView>(R.id.tv_nama_anak)?.text = kidAHEntity?.name?.let { name ->
            getString(R.string.nama_anak_df, name)
        }
        view.findViewById<TextView>(R.id.tv_gender)?.text = kidAHEntity?.gender?.let { gender ->
            getString(R.string.jenis_kelamin_profile, gender)
        }
        view.findViewById<TextView>(R.id.tv_tanggal_lahir)?.text =
            kidAHEntity?.birthDate?.let { birthDate ->
                getString(R.string.tanggal_lahir_anak_profile, birthDate)
            }

        val usiaSaatDianalisis =
            kidAHEntity?.let { calculateMonthDifference(kidAHEntity.birthDate, it.datetime) }

        view.findViewById<TextView>(R.id.tv_usia)?.text =
            getString(R.string.usia_anak_profile, usiaSaatDianalisis)

        view.findViewById<TextView>(R.id.tv_tinggi)?.text = kidAHEntity?.height?.let { height ->
            getString(R.string.tinggi_anak_profile, height)
        }
        view.findViewById<TextView>(R.id.tv_berat)?.text = kidAHEntity?.weight?.let { weight ->
            getString(R.string.berat_anak_profile, weight)
        }
        view.findViewById<TextView>(R.id.tv_hasil_wasting)?.text =
            kidAHEntity?.wastingRiskResult?.let { wastingRiskResult ->
                getString(R.string.wasting_risk_medrec, wastingRiskResult)
            }
        view.findViewById<TextView>(R.id.tv_hasil_stunting)?.text =
            kidAHEntity?.stuntingRiskResult?.let { stuntingRiskResult ->
                getString(R.string.stunting_risk_medrec, stuntingRiskResult)
            }

        // Tombol tutup
        view.findViewById<Button>(R.id.btn_close)?.setOnClickListener {
            dismiss()
        }

        // Tombol hapus riwayat
        view.findViewById<Button>(R.id.btn_hapus_riwayat)?.setOnClickListener {
            // TODO: Hapus Riwayat
            dismiss()
        }
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