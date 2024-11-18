package com.tiuho22bangkit.gizi.ui.article

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tiuho22bangkit.gizi.data.remote.ArticlesItem
import com.tiuho22bangkit.gizi.databinding.ArticleCardBinding

class ArticleAdapter(private val onClick: (ArticlesItem) -> Unit)
    : ListAdapter<ArticlesItem, ArticleAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ArticleCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val article = getItem(position)
        Log.d("ArticleAdapter", "Binding article at position $position: ${article.title}")
        holder.bind(article)
        holder.itemView.setOnClickListener{onClick(article)}
    }

    class MyViewHolder(private val binding: ArticleCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticlesItem) {
            binding.tvArticleTitle.text = article.title
            binding.tvArticleDescription.text = article.description
            Glide.with(binding.root.context)
                .load(article.urlToImage)
                .into(binding.ivArticleImage)

            Log.d("ArticleAdapter", "Binding article: ${article.author} - ${article.title} - ${article.description}")
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(
                oldItem: ArticlesItem,
                newItem: ArticlesItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ArticlesItem,
                newItem: ArticlesItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}