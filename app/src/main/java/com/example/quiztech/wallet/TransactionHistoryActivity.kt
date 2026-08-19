package com.example.quiztech.wallet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quiztech.R
import com.example.quiztech.databinding.ActivityTransactionHistoryBinding

import android.app.ProgressDialog
import android.content.Intent
import android.util.Log
import com.example.quiztech.services.ServiceManager
import com.prvt.sreezzyuser.common.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionHistoryBinding
    private lateinit var adapter: TransactionHistoryAdapter
    private var allTransactions = ArrayList<Transactions>()
    private var userId = ""
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = Utils.getData(this, "user_id", "").toString()

        binding.toolbar.txtHeader.text = "Transactions History"
        binding.toolbar.imgBack.setOnClickListener {
            onBackPressed()
        }

        setupRecyclerView()
        setupClickListeners()
        
        fetchTransactions()
    }

    private fun setupRecyclerView() {
        adapter = TransactionHistoryAdapter(ArrayList()) { transaction ->
            val intent = Intent(this, TransactionDetailsActivity::class.java).apply {
                putExtra("transaction_id", transaction.id)
            }
            startActivity(intent)
        }
        binding.rvTransactions.layoutManager = LinearLayoutManager(this)
        binding.rvTransactions.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.rg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.btnNewQuiz -> filterTransactions(false)
                R.id.btnCompletedQuiz -> filterTransactions(true)
            }
        }
    }

    private fun fetchTransactions() {
        progressDialog = Utils.openDialog(this)
        val serviceManager = ServiceManager.getDataManager()
        serviceManager.transactionLIst(object : Callback<TransactionsMain> {
            override fun onResponse(call: Call<TransactionsMain>, response: Response<TransactionsMain>) {
                if (progressDialog.isShowing) progressDialog.dismiss()
                if (response.isSuccessful && response.body()?.status == 1) {
                    allTransactions.clear()
                    allTransactions.addAll(response.body()?.data ?: arrayListOf())
                    // Initial filter
                    filterTransactions(binding.btnCompletedQuiz.isChecked)
                }
            }

            override fun onFailure(call: Call<TransactionsMain>, t: Throwable) {
                if (progressDialog.isShowing) progressDialog.dismiss()
                Log.e("TransactionHistory", "Error fetching transactions: ${t.message}")
            }
        }, userId)
    }

    private fun filterTransactions(isWithdrawal: Boolean) {
        val filtered = allTransactions.filter {
            val type = it.type?.lowercase() ?: ""
            if (isWithdrawal) {
                type.contains("with drawal")
            } else {
                !type.contains("with drawal")
            }
        }
        adapter.updateTransactions(ArrayList(filtered))
    }
}
