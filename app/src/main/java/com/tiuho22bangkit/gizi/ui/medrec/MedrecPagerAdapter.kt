package com.tiuho22bangkit.gizi.ui.medrec

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tiuho22bangkit.gizi.ui.medrec.kid.KidMedrecFragment
import com.tiuho22bangkit.gizi.ui.medrec.mom.MomMedrecFragment

class MedrecPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MomMedrecFragment()
            1 -> KidMedrecFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }

    override fun getItemCount(): Int = 2
}
