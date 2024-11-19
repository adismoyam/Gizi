package com.tiuho22bangkit.gizi.ui.article.detail

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.tiuho22bangkit.gizi.R
import com.tiuho22bangkit.gizi.databinding.FragmentArticleBinding
import com.tiuho22bangkit.gizi.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels()

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)

    // TODO: Use the ViewModel
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        return inflater.inflate(R.layout.fragment_detail, container, false)
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = DetailFragmentArgs.fromBundle(requireArguments())
        val article = args.article

        binding.apply {
            Glide.with(root.context)
                .load(article.urlToImage)
                .into(ivPicture)
            tvTitle.text = article.title
            tvAuthor.text = article.author
            tvPublishedAt.text = article.publishedAt
            tvDescription.text = article.description
            tvContent.text = HtmlCompat.fromHtml(
                article.content,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            btnOpenWeb.setOnClickListener{
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                startActivity(intent)
            }
        }

    }

}