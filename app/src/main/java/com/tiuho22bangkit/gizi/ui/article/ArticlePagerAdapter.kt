package com.tiuho22bangkit.gizi.ui.article

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tiuho22bangkit.gizi.ui.article.kehamilan.KehamilanFragment
import com.tiuho22bangkit.gizi.ui.article.nutrisi.NutrisiFragment
import com.tiuho22bangkit.gizi.ui.article.parenting.ParentingFragment
import com.tiuho22bangkit.gizi.ui.article.rekomendasi.RekomendasiFragment

class ArticlePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RekomendasiFragment()
            1 -> KehamilanFragment()
            2 -> NutrisiFragment()
            3 -> ParentingFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }

    override fun getItemCount(): Int = 4
}
