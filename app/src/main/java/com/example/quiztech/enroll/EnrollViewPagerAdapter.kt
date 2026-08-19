package com.example.quiztech.enroll

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class EnrollViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2 // We have two tabs: About Quiz and Winnings
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AboutQuizFragment() // Placeholder, will create this next
            1 -> EnrollPrizeFragment()  // Placeholder, will create this next
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
