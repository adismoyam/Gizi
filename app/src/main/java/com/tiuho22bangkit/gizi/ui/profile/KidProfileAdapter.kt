package com.tiuho22bangkit.gizi.ui.profile

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tiuho22bangkit.gizi.ui.analysis.KidAnalysisActivity
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.data.local.entity.KidEntity
import com.tiuho22bangkit.gizi.databinding.KidProfileCardBinding

class KidProfileAdapter : ListAdapter<KidEntity, KidProfileAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            KidProfileCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val kid = getItem(position)
        holder.bind(kid)
    }

    class MyViewHolder(private val binding: KidProfileCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(kid: KidEntity) {
            binding.apply {
                Glide.with(root.context)
                    .load(kid.uri)
                    .placeholder(R.drawable.bocil)
                    .into(imgItemPhoto)
//                if (kid.gender == "Laki-Laki") {
//                    Glide.with(root.context)
//                        .load(kid.uri)
//                        .placeholder(R.drawable.bocil)
//                        .into(imgItemPhoto)
//                } else {
//                    Glide.with(root.context)
//                        .load(kid.uri)
//                        .placeholder(R.drawable.baby_girl)
//                        .into(imgItemPhoto)
//                }

                tvItemName.text = kid.name

                root.setOnClickListener {
                    val intent = Intent(root.context, KidAnalysisActivity::class.java)
                    intent.putExtra(KidAnalysisActivity.KID_DATA, kid)
                    root.context.startActivity(intent)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<KidEntity>() {
            override fun areItemsTheSame(oldItem: KidEntity, newItem: KidEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: KidEntity,
                newItem: KidEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}