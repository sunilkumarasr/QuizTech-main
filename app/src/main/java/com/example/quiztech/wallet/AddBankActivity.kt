package com.example.quiztech.wallet

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quiztech.databinding.ActivityAddBankBinding

class AddBankActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBankBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBankBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.headerToolbar.txtHeader.text="Add bank account"

        binding.headerToolbar.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnSave.setOnClickListener {
            val bankName = binding.etBankName.text.toString()
            val accountNumber = binding.etAccountNumber.text.toString()
            val confirmAccountNumber = binding.etConfirmAccountNumber.text.toString()
            val accountHolderName = "John Doe" // Assuming a default value for now
            val ifscCode = binding.etIfscCode.text.toString()

            if (bankName.isEmpty() || accountNumber.isEmpty() || confirmAccountNumber.isEmpty() || ifscCode.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (accountNumber != confirmAccountNumber) {
                Toast.makeText(this, "Account numbers do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val resultIntent = Intent()
            resultIntent.putExtra("bankName", bankName)
            resultIntent.putExtra("accountNumber", accountNumber)
            resultIntent.putExtra("accountHolderName", accountHolderName)
            resultIntent.putExtra("ifscCode", ifscCode)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}