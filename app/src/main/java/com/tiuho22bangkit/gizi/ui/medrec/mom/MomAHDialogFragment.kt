package com.tiuho22bangkit.gizi.ui.medrec.mom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.data.local.entity.MomAnalysisHistoryEntity

class MomAHDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_momah_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val momAHEntity: MomAnalysisHistoryEntity? =
            arguments?.getParcelable(ARG_MOM_ANALYSIS_DATA, MomAnalysisHistoryEntity::class.java)
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