package com.example.p8vitesse.ui


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

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
