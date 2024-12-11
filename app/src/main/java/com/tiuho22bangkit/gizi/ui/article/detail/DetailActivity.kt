package com.tiuho22bangkit.gizi.ui.article.detail

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.data.remote.JumainResponseItem
import com.tiuho22bangkit.gizi.databinding.ActivityDetailBinding
import java.util.UUID

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        supportActionBar?.hide()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBack.setOnClickListener {
            finish()
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
                tvArticleSauce.text =  String.format(getString(R.string.sumber),it.source.name)
                tvPublishedAt.text = String.format(getString(R.string.published_at),it.publishedAt)
                tvDescription.text = it.description
                tvContent.text = HtmlCompat.fromHtml(
                    it.content,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )

                btnOpenWeb.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                    startActivity(intent)
                }
                var isMarkedAsRead = false

                main.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                    val view = main.getChildAt(0)
                    val diff = (view.bottom - (main.height + scrollY))

                    if (diff == 0 && !isMarkedAsRead) {
                        isMarkedAsRead = true
                        markArticleAsRead(it.id)
                    }
                }
            }
        } ?: run {
            Toast.makeText(this, "Article not found", Toast.LENGTH_SHORT).show()
            finish()
        }
        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://capstone-bangkit-2024-default-rtdb.firebaseio.com/")
    }

    private fun markArticleAsRead(articleId: String) {
        val id = UUID.randomUUID().toString()
        database = FirebaseDatabase.getInstance().getReference("achievement")
        val currentUser = FirebaseAuth.getInstance().currentUser

        currentUser?.let { user ->
            val uid = user.uid
            database.child(id).child("articleId").setValue(articleId)
            database.child(id).child("userId").setValue(uid)
            Toast.makeText(this, "Artikel telah ditandai sebagai dibaca!", Toast.LENGTH_SHORT).show()

        } ?: Log.e("Firebase", "Pengguna tidak terautentikasi")
    }


    companion object {
        const val ARTICLE = "article"
    }
}