package com.example.quiztech.quiz

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quiztech.databinding.ActivitySuccessPageBinding
import com.example.quiztechimport.HomeActivity

class SuccessPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySuccessPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackToHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("NAVIGATE_TO", "MY_QUIZ")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
