package com.example.quiztech

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quiztech.auth.LoginActivity
import com.example.quiztech.auth.VerifyOtpActivity
import com.example.quiztech.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegisterContinue.setOnClickListener {
            val email = binding.etRegisterEmail.text.toString().trim()
            if (email.isNotEmpty()) {
                // TODO: Implement actual registration logic (e.g., call API to send OTP)
                Toast.makeText(this, "OTP sent to: " + email, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, VerifyOtpActivity::class.java)
                intent.putExtra("USER_EMAIL", email) // Pass email to VerifyOtpActivity
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvLoginNow.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }
}