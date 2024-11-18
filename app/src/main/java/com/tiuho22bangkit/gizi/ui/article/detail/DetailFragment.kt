package com.tiuho22bangkit.gizi.ui.article.detail

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.tiuho22bangkit.gizi.R

class DetailFragment : Fragment() {

    private lateinit var ivPicture: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvAuthor: TextView
    private lateinit var tvPublishedAt: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvContent: TextView
    private lateinit var btnOpenWeb: Button

    companion object {
        const val EXTRA_ARTICLE_URL = "extra_article_url"
    }

    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        tvCategoryName = view.findViewById(R.id.tv_category_name)
//        tvCategoryDescription = view.findViewById(R.id.tv_category_description)
//        btnProfile = view.findViewById(R.id.btn_profile)
//        btnShowDialog = view.findViewById(R.id.btn_show_dialog)

        /*  Jika pada activity anda dapat langsung memanggil findViewById tanpa kode view. di
        depannya, namun pada fragment anda harus membutuhkan kode tersebut.
        Hal ini karena pada fragment dalam inisialisasi layout kita menggunaan inflater yang
         kemudian pada method onCreateView masuk pada variabel view.    */
//        if (savedInstanceState != null) {
//            val descFromBundle = savedInstanceState.getString(EXTRA_DESCRIPTION)
//            description = descFromBundle
//        }

//        if (arguments != null) {
            // Mengambil data yang dikirimkan melalui obyek bundle
//            val categoryName = arguments?.getString(EXTRA_NAME)
//            tvCategoryName.text = categoryName

            // Mengambil data yang dikirimkan melalui metode Getter dari Setter
//            tvCategoryDescription.text = description
//        }// Kelas Bundle merupakan kelas map data string untuk obyek-obyek parcelable.
        //  Di sini kita bisa menginput lebih dari satu parameter/variabel ke dalam obyek Bundle.



        btnOpenWeb.setOnClickListener{
//            val intent = Intent(requireActivity(), ProfileActivity::class.java)
//            startActivity(intent)
        }
    }

}