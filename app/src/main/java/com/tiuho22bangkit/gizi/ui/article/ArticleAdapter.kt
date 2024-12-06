package com.tiuho22bangkit.gizi.ui.article

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tiuho22bangkit.gizi.data.remote.JumainResponseItem
import com.tiuho22bangkit.gizi.databinding.ArticleCardBinding
import com.tiuho22bangkit.gizi.ui.article.detail.DetailActivity

class ArticleAdapter :
    ListAdapter<JumainResponseItem, ArticleAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ArticleCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val article = getItem(position)
        Log.d("ArticleAdapter", "Binding article at position $position: ${article.title}")
        holder.bind(article)
    }

    class MyViewHolder(private val binding: ArticleCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: JumainResponseItem) {
            binding.apply {
                tvArticleTitle.text = article.title
                tvArticleDescription.text = article.description
                Glide.with(root.context)
                    .load(article.urlToImage)
                    .into(ivArticleImage)

                root.setOnClickListener {
                    val intent = Intent(root.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.ARTICLE, article)
                    root.context.startActivity(intent)
                }
            }
            Log.d(
                "ArticleAdapter",
                "Binding article: ${article.author} - ${article.title} - ${article.description}"
            )
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<JumainResponseItem>() {
            override fun areItemsTheSame(
                oldItem: JumainResponseItem,
                newItem: JumainResponseItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: JumainResponseItem,
                newItem: JumainResponseItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}