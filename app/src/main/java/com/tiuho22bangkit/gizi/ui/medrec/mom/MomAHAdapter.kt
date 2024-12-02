package com.tiuho22bangkit.gizi.ui.medrec.mom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.data.local.entity.MomAnalysisHistoryEntity
import com.tiuho22bangkit.gizi.databinding.MomAnalysisHistoryCardBinding
import com.tiuho22bangkit.gizi.ui.medrec.kid.KidAHDialogFragment

class MomAHAdapter :
    ListAdapter<MomAnalysisHistoryEntity, MomAHAdapter.MyViewHolder>(
        DIFF_CALLBACK
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            MomAnalysisHistoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val momAnalysisHistory = getItem(position)
        holder.bind(momAnalysisHistory)
    }

    class MyViewHolder(private val binding: MomAnalysisHistoryCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(momAnalysisHistory: MomAnalysisHistoryEntity) {
            binding.apply {

                tvTanggalWaktu.text = momAnalysisHistory.datetime
                tvPregnancyRisk.text = root.context.getString(R.string.pregnancy_risk_medrec, momAnalysisHistory.result)

                root.setOnClickListener {
                    val context = root.context
                    if (context is AppCompatActivity) {
                        val dialog = MomAHDialogFragment.newInstance(momAnalysisHistory)
                        dialog.show(context.supportFragmentManager, "KidAHDialogFragment")
                    }
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MomAnalysisHistoryEntity>() {
            override fun areItemsTheSame(oldItem: MomAnalysisHistoryEntity, newItem: MomAnalysisHistoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: MomAnalysisHistoryEntity,
                newItem: MomAnalysisHistoryEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}