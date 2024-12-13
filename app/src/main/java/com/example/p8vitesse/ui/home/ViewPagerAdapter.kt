package com.example.p8vitesse.ui.home


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.p8vitesse.ui.home.all.AllFragment
import com.example.p8vitesse.ui.home.favoris.FavorisFragment

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2 // Number of tabs

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllFragment()
            1 -> FavorisFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
