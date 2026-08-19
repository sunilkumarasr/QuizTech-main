package com.example.quiztech.enroll

import android.content.Intent
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.quiztech.BaseActivity
import com.example.quiztech.R
import com.example.quiztech.databinding.ActivityAllCategoriesBinding
import com.example.quiztech.databinding.ActivityAllCategoriesBinding.inflate
import com.example.quiztech.databinding.ActivityEnrollBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class EnrollActivity : BaseActivity<ActivityEnrollBinding>(ActivityEnrollBinding::inflate) {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)

        val adapter = EnrollViewPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "About Quiz"
                1 -> "Winnings"
                else -> null
            }
        }.attach()

binding.layoutHeader.imgBack.setOnClickListener {
    finish()
}
        binding.btnEnrollNow.setOnClickListener {
            startActivity(Intent(applicationContext, EnrollPaymentPage::class.java))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
