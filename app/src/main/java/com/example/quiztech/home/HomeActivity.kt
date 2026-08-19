package com.example.quiztechimport

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.quiztech.home.HomeFragment
import com.example.quiztech.quiz.MyQuizFragment
import com.example.quiztech.profile.ProfileFragment
import com.example.quiztech.R
import com.example.quiztech.wallet.WalletFragment
import com.example.quiztech.winning.WinningsFragment
import com.example.quiztech.databinding.ActivityHomeBinding
import com.example.quiztech.winning.SubscriptionsFragment
import com.razorpay.PaymentResultListener

class HomeActivity : AppCompatActivity(), PaymentResultListener {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

       // setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setupToolbarItemClickListeners()
        setupBottomNavigation()

        // Load the default fragment
        if (savedInstanceState == null) {
            if (intent.getStringExtra("NAVIGATE_TO") == "MY_QUIZ") {
                binding.bottomNavigation.selectedItemId = R.id.navigation_my_quiz
            } else {
                binding.bottomNavigation.selectedItemId = R.id.navigation_home
            }
        }
    }

    private fun setupToolbarItemClickListeners() {
        // Listeners for items directly in the Toolbar owned by the Activity
       // binding.ivLogo.setOnClickListener { Toast.makeText(this, "Logo clicked", Toast.LENGTH_SHORT).show() }
        //binding.ivWhatsapp.setOnClickListener { Toast.makeText(this, "WhatsApp clicked", Toast.LENGTH_SHORT) }
        //binding.ivNotifications.setOnClickListener { Toast.makeText(this, "Notifications clicked", Toast.LENGTH_SHORT) }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.homeFragmentContainer, fragment)
            .commit()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener {item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.navigation_home -> HomeFragment()
                R.id.navigation_my_quiz -> MyQuizFragment()
                R.id.navigation_winnings -> SubscriptionsFragment()
                R.id.navigation_wallet -> WalletFragment()
                R.id.navigation_profile -> ProfileFragment()
                else -> HomeFragment() // Default case
            }
            loadFragment(selectedFragment)
            true
        }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        val fragment = supportFragmentManager.findFragmentById(R.id.homeFragmentContainer)
        if (fragment is PaymentResultListener) {
            fragment.onPaymentSuccess(razorpayPaymentID)
        }
    }

    override fun onPaymentError(code: Int, description: String?) {
        val fragment = supportFragmentManager.findFragmentById(R.id.homeFragmentContainer)
        if (fragment is PaymentResultListener) {
            fragment.onPaymentError(code, description)
        }
    }
}




