package com.tiuho22bangkit.gizi.ui.article

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tiuho22bangkit.gizi.ui.article.kehamilan.KehamilanFragment
import com.tiuho22bangkit.gizi.ui.article.nutrisi.NutrisiFragment
import com.tiuho22bangkit.gizi.ui.article.parenting.ParentingFragment

class ArticlePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> KehamilanFragment()
            1 -> NutrisiFragment()
            2 -> ParentingFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
    override fun getItemCount(): Int = 3
}
