package com.tiuho22bangkit.gizi.ui.medrec.kid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.data.local.entity.KidAnalysisHistoryEntity
import com.tiuho22bangkit.gizi.databinding.KidAnalysisHistoryCardBinding

class KidAHAdapter :
    ListAdapter<KidAnalysisHistoryEntity, KidAHAdapter.MyViewHolder>(
        DIFF_CALLBACK
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            KidAnalysisHistoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val kidAnalysisHistory = getItem(position)
        holder.bind(kidAnalysisHistory)
    }

    class MyViewHolder(private val binding: KidAnalysisHistoryCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(kidAnalysisHistory: KidAnalysisHistoryEntity) {
            binding.apply {
//                if (kid.gender == "Laki-Laki") {
//                    Glide.with(root.context)
//                        .load(kid.uri)
//                        .placeholder(R.drawable.baby_boy)
//                        .into(imgItemPhoto)
//                } else {
//                    Glide.with(root.context)
//                        .load(kid.uri)
//                        .placeholder(R.drawable.baby_girl)
//                        .into(imgItemPhoto)
//                }

                tvNamaAnak.text = kidAnalysisHistory.name
                tvTanggalWaktu.text = kidAnalysisHistory.datetime
                tvHasilWasting.text = root.context.getString(R.string.wasting_risk_medrec, kidAnalysisHistory.wastingRiskResult)
                tvHasilStunting.text = root.context.getString(R.string.stunting_risk_medrec, kidAnalysisHistory.stuntingRiskResult)

                root.setOnClickListener {
                    val context = root.context
                    if (context is AppCompatActivity) {
                        val dialog = KidAHDialogFragment.newInstance(kidAnalysisHistory)
                        dialog.show(context.supportFragmentManager, "KidAHDialogFragment")
                    }
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<KidAnalysisHistoryEntity>() {
            override fun areItemsTheSame(oldItem: KidAnalysisHistoryEntity, newItem: KidAnalysisHistoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: KidAnalysisHistoryEntity,
                newItem: KidAnalysisHistoryEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}