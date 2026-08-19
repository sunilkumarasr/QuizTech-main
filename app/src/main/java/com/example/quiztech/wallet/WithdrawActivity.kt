package com.example.quiztech.wallet

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quiztech.databinding.ActivityWithdrawBinding
import com.example.quiztech.services.ServiceManager
import com.prvt.sreezzyuser.common.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WithdrawActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWithdrawBinding
    private lateinit var bankAccountAdapter: BankAccountAdapter
    private val bankAccounts = mutableListOf<BankAccount>()

    var user_id=""
    var amount=""
    var  bankName=""
    var accountNumber=""
    var  confirmAccountNumber=""
    var   ifscCode=""
    var   upi_id=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWithdrawBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user_id = Utils.getData(applicationContext, "user_id", "").toString()

        binding.headerToolbar.txtHeader.text = "Withdraw"
        binding.headerToolbar.imgBack.setOnClickListener {
            onBackPressed()
        }

       // setupRecyclerView()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        bankAccountAdapter = BankAccountAdapter(bankAccounts)
        binding.rvBankAccounts.layoutManager = LinearLayoutManager(this)
        binding.rvBankAccounts.adapter = bankAccountAdapter
    }

    private fun setupClickListeners() {
        binding.btnWithdraw.setOnClickListener {
             amount = binding.etAmount.text.toString()
            if (amount.isEmpty()) {
                Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (amount.toInt()<100) {
                Toast.makeText(this, "Minimum withdraw amount should 100", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
             bankName = binding.etBankName.text.toString()
             accountNumber = binding.etAccountNumber.text.toString()
             confirmAccountNumber = binding.etConfirmAccountNumber.text.toString()
            upi_id=binding.etupiid.text.toString()
             ifscCode = binding.etIfscCode.text.toString()

            if (bankName.isEmpty() || accountNumber.isEmpty() || confirmAccountNumber.isEmpty() || ifscCode.isEmpty()|| upi_id.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (accountNumber != confirmAccountNumber) {
                Toast.makeText(this, "Account numbers do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            
            withDrawMoney()
            // Handle withdrawal logic here
        }

        binding.btnAddBankAccount.setOnClickListener {
            addBankAccountLauncher.launch(Intent(this, AddBankActivity::class.java))
        }
    }

    private val addBankAccountLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val bankName = data?.getStringExtra("bankName")
                val accountNumber = data?.getStringExtra("accountNumber")
                val accountHolderName = data?.getStringExtra("accountHolderName")
                val ifscCode = data?.getStringExtra("ifscCode")

                if (bankName != null && accountNumber != null && accountHolderName != null && ifscCode != null) {
                    val newAccount =
                        BankAccount(bankName, accountNumber, accountHolderName, ifscCode)
                    bankAccounts.add(newAccount)
                    bankAccountAdapter.notifyItemInserted(bankAccounts.size - 1)
                }
            }
        }


    private lateinit var openDialog: ProgressDialog

    private fun withDrawMoney() {
        try {
            openDialog = Utils.openDialog(this@WithdrawActivity)
            val dataManager = ServiceManager.getDataManager()
            val otpCallback = object : Callback<WalletDetails> {
                override fun onResponse(call: Call<WalletDetails>, response: Response<WalletDetails>) {
                    if (openDialog.isShowing) openDialog.dismiss()
                    if (response.isSuccessful) {
                        val body = response.body()
                        Toast.makeText(this@WithdrawActivity, "Withdrawal request submitted successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@WithdrawActivity, "Failed to submit withdrawal request", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<WalletDetails>, t: Throwable) {
                    if (openDialog.isShowing) openDialog.dismiss()
                    Toast.makeText(this@WithdrawActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            }
            dataManager.withdraw(otpCallback, user_id, amount,bankName,accountNumber,ifscCode,upi_id )
        } catch (e: Exception) {
            if (::openDialog.isInitialized && openDialog.isShowing) openDialog.dismiss()

        }
    }
}
