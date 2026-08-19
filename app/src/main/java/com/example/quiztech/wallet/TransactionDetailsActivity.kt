package com.example.quiztech.wallet

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.quiztech.databinding.ActivityTransactionDetailsBinding
import com.example.quiztech.services.ServiceManager
import com.prvt.sreezzyuser.common.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionDetailsBinding
    private lateinit var openDialog: ProgressDialog
    var user_id=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user_id = Utils.getData(applicationContext, "user_id", "").toString()
        val transactionId = intent.getStringExtra("transaction_id") ?: ""

        binding.headerToolbar.txtHeader.text = "Transaction Details"

        binding.headerToolbar.imgBack.setOnClickListener {
            finish()
        }
        walletDetails(transactionId)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun walletDetails(transactionId: String) {
        try {
            openDialog = Utils.openDialog(this@TransactionDetailsActivity)
            val dataManager = ServiceManager.getDataManager()
            val otpCallback = object : Callback<TransactionDetailMain> {
                override fun onResponse(call: Call<TransactionDetailMain>, response: Response<TransactionDetailMain>) {
                    if (openDialog.isShowing) openDialog.dismiss()
                    val body = response.body()
                    if (response.isSuccessful && body != null && body.status == 1) {
                        binding.scrollView.visibility = View.VISIBLE
                        binding.tvNoData.visibility = View.GONE
                        updateUI(body.data)
                    } else {
                        binding.scrollView.visibility = View.GONE
                        binding.tvNoData.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<TransactionDetailMain>, t: Throwable) {
                    if (openDialog.isShowing) openDialog.dismiss()
                    binding.scrollView.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                    Toast.makeText(this@TransactionDetailsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            }
            // Use transactionDetail API with transactionId
            dataManager.transactionDetail(otpCallback, transactionId)
        } catch (e: Exception) {
            if (::openDialog.isInitialized && openDialog.isShowing) openDialog.dismiss()
        }
    }

    private fun updateUI(transaction: Transactions?) {
        transaction?.let {
            binding.apply {
                // Success message and icon based on status
                if (it.status?.lowercase() == "success") {
                    tvStatus.text = "Transfer Successful"
                    tvStatus.setTextColor(resources.getColor(com.example.quiztech.R.color.success_color))
                    ivStatusIcon.setImageResource(com.example.quiztech.R.drawable.ic_checkmark_circle_green)
                } else {
                    tvStatus.text = "Transfer ${it.status ?: "Failed"}"
                    tvStatus.setTextColor(resources.getColor(android.R.color.holo_red_dark))
                    // ivStatusIcon.setImageResource(...)
                }

                tvTransactionAmountHeader.text = "${Utils.RUPEE} ${it.amount}"
                tvPaymentAmount.text = "${Utils.RUPEE} ${it.amount}"

                // Parse date and time
                val createdAt = it.createdAt // format: "2026-04-21 21:37:36"
                if (!createdAt.isNullOrEmpty()) {
                    val parts = createdAt.split(" ")
                    if (parts.size == 2) {
                        tvDate.text = parts[0]
                        tvTime.text = parts[1]
                    }
                }

                tvReferenceNumber.text = it.razorpayPaymentId ?: "N/A"
                
                // Bank Details
                if (!it.bankName.isNullOrEmpty()) {
                    layoutBankDetails.visibility = View.VISIBLE
                    tvBankName.text = it.bankName
                    tvAccountNumber.text = "**** - **** - ${it.accountNumber?.takeLast(4) ?: ""}"
                } else if (!it.upiId.isNullOrEmpty()) {
                    layoutBankDetails.visibility = View.VISIBLE
                    tvBankName.text = "UPI"
                    tvAccountNumber.text = it.upiId
                } else {
                    layoutBankDetails.visibility = View.GONE
                }
            }
        }
    }
}