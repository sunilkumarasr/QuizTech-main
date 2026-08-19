package com.example.quiztech.wallet

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quiztech.databinding.DialogAddMoneyBinding
import com.example.quiztech.databinding.FragmentWalletBinding
import com.example.quiztech.services.ServiceManager
import com.prvt.sreezzyuser.common.Utils
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.prefs.Preferences

class WalletFragment : Fragment() , PaymentResultListener {

    private var _binding: FragmentWalletBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    lateinit var walletTransactionAdapter: WalletTransactionAdapter

    var user_id=""
    var addedAmount = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize UI elements and set up listeners here
        // For example: binding.textViewWallet.text = "Wallet"
        user_id = Utils.getData(requireActivity(), "user_id", "").toString()
        //getTransactionList()
        binding.rvTransactions.layoutManager= LinearLayoutManager(requireContext())
        walletTransactionAdapter = WalletTransactionAdapter(ArrayList<Transactions>()) { transaction ->
            val intent = Intent(requireContext(), TransactionDetailsActivity::class.java)
            intent.putExtra("transaction_id", transaction.id)
            startActivity(intent)

        }
        binding.rvTransactions.adapter=walletTransactionAdapter
        walletDetails()

        binding.btnAddMoney.setOnClickListener {
            showAddMoneyDialog()
        }

        binding.btnWithdraw.setOnClickListener {
            startActivity(Intent(requireContext(), WithdrawActivity::class.java))
        }

        binding.tvViewAll.setOnClickListener {
            startActivity(Intent(requireContext(), TransactionHistoryActivity::class.java))
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private lateinit var openDialog: ProgressDialog

    /*private fun getTransactionList() {
        try {
            openDialog = Utils.openDialog(requireActivity())
            val dataManager = ServiceManager.getDataManager()
            val otpCallback = object : Callback<WalletDetails> {
                override fun onResponse(call: Call<WalletDetails>, response: Response<WalletDetails>) {
                    if (openDialog.isShowing) openDialog.dismiss()
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.data != null && body.data.isNotEmpty()) {


                        } else {
                            showNoData()
                        }
                    } else {
                        showNoData()
                    }
                }

                override fun onFailure(call: Call<WalletDetails>, t: Throwable) {
                    if (openDialog.isShowing) openDialog.dismiss()
                    showNoData()
                }
            }
            dataManager.transactionLIst(otpCallback, user_id)
        } catch (e: Exception) {
            if (::openDialog.isInitialized && openDialog.isShowing) openDialog.dismiss()
            showNoData()
        }
    } */
    private fun walletDetails() {
        try {
            openDialog = Utils.openDialog(requireActivity())
            val dataManager = ServiceManager.getDataManager()
            val otpCallback = object : Callback<WalletDetails> {
                override fun onResponse(call: Call<WalletDetails>, response: Response<WalletDetails>) {
                    if (openDialog.isShowing) openDialog.dismiss()
                    if (response.isSuccessful) {
                        val body = response.body()
                        binding.txtAmount.text="${Utils.RUPEE} ${body?.walletBalance}"
                        if (body?.recentTransactions != null && body.recentTransactions.isNotEmpty()) {

                            walletTransactionAdapter.mockTests!!.clear()
                            body?.recentTransactions?.let { walletTransactionAdapter.mockTests!!.addAll(it) }
                            walletTransactionAdapter.notifyDataSetChanged()
                        } else {
                            showNoData()
                        }
                    } else {
                        showNoData()
                    }
                }

                override fun onFailure(call: Call<WalletDetails>, t: Throwable) {
                    if (openDialog.isShowing) openDialog.dismiss()
                    showNoData()
                }
            }
            dataManager.walletDetails(otpCallback, user_id)
        } catch (e: Exception) {
            if (::openDialog.isInitialized && openDialog.isShowing) openDialog.dismiss()
            showNoData()
        }
    }
    private fun showNoData() {
      //  myQuizAdapter.addTest(ArrayList())
      /*  binding.rvQuiz.visibility = View.GONE
        binding.tvNoData.visibility = View.VISIBLE*/
    }

    private fun showAddMoneyDialog() {
        val dialogBinding = DialogAddMoneyBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogBinding.root)
        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogBinding.btnProceed.setOnClickListener {
            val amount = dialogBinding.etAmount.text.toString().trim()
            if (amount.isEmpty()) {
                dialogBinding.tilAmount.error = "Please enter amount"
                return@setOnClickListener
            }
            if (amount.toDouble() <= 0) {
                dialogBinding.tilAmount.error = "Please enter valid amount"
                return@setOnClickListener
            }
            addedAmount = amount
            startPayment(amount)
            alertDialog.dismiss()
        }

        dialogBinding.btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun startPayment(amount: String) {
        /*addMoney("PAY-00001", addedAmount)
        if(true)  return*/

        val checkout = Checkout()
        checkout.setKeyID("rzp_live_S9y3xwcgpWFu3v") //Test
        //checkout.setKeyID("rzp_live_RGZtOh3ydFTGHQ") //Live
        try {
            val phone = Utils.getData(requireActivity(), "mobile", "").toString()
            val email = Utils.getData(requireActivity(), "email", "").toString()
            val options = JSONObject()
            val amt = (amount.toDouble() * 100).toLong()
            options.put("name", "Quiz Tech")
            options.put("description", "Add Money to Wallet")
            // options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("currency", "INR")
            options.put("amount", amt)
            options.put("prefill.email", Utils.isNull(email))
            options.put("prefill.contact", Utils.isNull(phone))

            checkout.open(requireActivity(), options)
        } catch (e: Exception) {
            Toast.makeText(requireActivity(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        try {
            Log.e("Payment Error","Payment Success ${razorpayPaymentID}")
            //Toast.makeText(requireActivity(), "✅ Payment Successful! ID: $razorpayPaymentID", Toast.LENGTH_LONG).show()
            if (razorpayPaymentID != null) {
                addMoney(razorpayPaymentID, addedAmount)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Payment Error","Payment Error ${e.message}")

           // Toast.makeText(requireActivity(), "❌ Payment Failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onPaymentError(p0: Int, description: String?) {
        Log.e("Payment Error","Payment Error ${description}")
        Toast.makeText(requireActivity(), "❌ Payment Failed: $description", Toast.LENGTH_LONG).show()
    }

    private fun addMoney(razorpay_payment_id: String, amount: String) {
        try {
            openDialog = Utils.openDialog(requireActivity())
            val dataManager = ServiceManager.getDataManager()
            val otpCallback = object : Callback<WalletDetails> {
                override fun onResponse(call: Call<WalletDetails>, response: Response<WalletDetails>) {
                    if (openDialog.isShowing) openDialog.dismiss()
                    if (response.isSuccessful) {
                        val body = response.body()
                        binding.txtAmount.text="${Utils.RUPEE} ${body?.walletBalance}"
                        walletDetails()
                        if (body?.recentTransactions != null && body.recentTransactions.isNotEmpty()) {

                            walletTransactionAdapter.mockTests!!.clear()
                            body?.recentTransactions?.let { walletTransactionAdapter.mockTests!!.addAll(it) }
                            walletTransactionAdapter.notifyDataSetChanged()

                        } else {
                            showNoData()
                        }
                    } else {
                        showNoData()
                    }
                }

                override fun onFailure(call: Call<WalletDetails>, t: Throwable) {
                    if (openDialog.isShowing) openDialog.dismiss()
                    showNoData()
                }
            }
            dataManager.addMoney(otpCallback, user_id,amount,razorpay_payment_id,"")
        } catch (e: Exception) {
            if (::openDialog.isInitialized && openDialog.isShowing) openDialog.dismiss()
            showNoData()
        }
    }
}