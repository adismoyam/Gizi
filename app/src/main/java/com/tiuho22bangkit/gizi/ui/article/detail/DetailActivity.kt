package com.tiuho22bangkit.gizi.ui.article.detail

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.data.local.entity.KidEntity
import com.tiuho22bangkit.gizi.data.remote.JumainResponseItem
import com.tiuho22bangkit.gizi.databinding.ActivityDetailBinding
import com.tiuho22bangkit.gizi.ui.analysis.KidAnalysisActivity.Companion.KID_DATA

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val article = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ARTICLE, JumainResponseItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(ARTICLE)
        }

        article?.let {
            binding.apply {
                Glide.with(this@DetailActivity)
                    .load(it.urlToImage)
                    .into(ivPicture)

                tvTitle.text = it.title
                tvAuthor.text = it.author
                tvPublishedAt.text = it.publishedAt
                tvDescription.text = it.description
                tvContent.text = HtmlCompat.fromHtml(
                    it.content,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )

                btnOpenWeb.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                    startActivity(intent)
                }
            }
        } ?: run {
            Toast.makeText(this, "Article not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    companion object {
        const val ARTICLE = "article"
    }
}