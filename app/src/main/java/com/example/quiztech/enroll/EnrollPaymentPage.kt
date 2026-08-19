package com.example.quiztech.enroll

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.core.view.isVisible
import com.example.quiztech.databinding.ActivityEnrollPaymentBinding
import com.example.quiztech.quiz.QuizInfoActivity

class EnrollPaymentPage : ComponentActivity() {

    private lateinit var binding: ActivityEnrollPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnrollPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // You can now access the views using the binding object
        binding.layoutHeader.imgBack.setOnClickListener {

            finish()
        }

        binding.addCashButton.setOnClickListener {
            if(binding.lnrAddCash.visibility== View.VISIBLE) {
               finish()
            }
            else {
                binding.lnrSuccess.root.visibility = View.GONE
                binding.lnrAddCash.visibility = View.VISIBLE
            }
        }

        binding.enrollNowButton.setOnClickListener {

            binding.lnrSuccess.root.visibility = View.VISIBLE
            binding.lnrAddCash.visibility = View.GONE
        }


        binding.lnrSuccess.doneButton.setOnClickListener {

            finish()
        }
        binding.lnrSuccess.startNowButton.setOnClickListener {
            startActivity(Intent(applicationContext, QuizInfoActivity::class.java))
            finish()
            //startActivity()
        }
    }
}