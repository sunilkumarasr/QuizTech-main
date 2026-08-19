package com.example.quiztech.subscription

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quiztech.BaseActivity
import com.example.quiztech.databinding.ActivitySubscriptionBinding
import com.example.quiztech.databinding.ItemSubscriptionPlanBinding
import com.example.quiztech.model.SubScriptionMain
import com.example.quiztech.model.SubscriptionMainRes
import com.example.quiztech.model.SubscriptionPlan
import com.example.quiztech.services.ServiceManager
import com.prvt.sreezzyuser.common.Utils
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubscriptionActivity :
    BaseActivity<ActivitySubscriptionBinding>(ActivitySubscriptionBinding::inflate),
    PaymentResultListener {

    private lateinit var openDialog: ProgressDialog
    private lateinit var planAdapter: SubscriptionPlanAdapter
    var user_id = ""
    var wallet_amount = 0
    var plan_id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        user_id = Utils.getData(applicationContext, "user_id", "").toString()

        setupToolbar()
        setupRecyclerView()
        getSubscriptionPlans()
    }

    private fun setupToolbar() {
        binding.layoutHeader.txtHeader.text = "Subscription Plans"
        binding.layoutHeader.imgBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        planAdapter = SubscriptionPlanAdapter(mutableListOf()) { plan ->
            // Handle plan subscription


            // Handle plan subscription
            plan_id = plan.id!!
            if (plan.price != null && plan.price!!.isNotEmpty()) {
                if (wallet_amount > Integer.parseInt(plan.price))
                    subScribePLan("", "1")
                else
                    startPayment(plan.price.toString())
            }
            //  Utils.showToast(this, "Subscribing to ${plan.planTitle}")
        }
        binding.rvSubscriptions.layoutManager = LinearLayoutManager(this)
        binding.rvSubscriptions.adapter = planAdapter
    }

    private fun startPayment(amount: String) {
        /*addMoney("PAY-00001", addedAmount)
        if(true)  return*/

        val checkout = Checkout()
        checkout.setKeyID("rzp_live_S9y3xwcgpWFu3v") //Test
        //checkout.setKeyID("rzp_live_RGZtOh3ydFTGHQ") //Live
        try {
            val phone = Utils.getData(applicationContext, "mobile", "").toString()
            val email = Utils.getData(applicationContext, "email", "").toString()
            val options = JSONObject()
            val amt = (amount.toDouble() * 100).toLong()
            options.put("name", "Quiz Tech")
            options.put("description", "Subscribe Plan")
            // options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("currency", "INR")
            options.put("amount", amt)
            options.put("prefill.email", Utils.isNull(email))
            options.put("prefill.contact", Utils.isNull(phone))
            //4591 9731 00953079
            //checkout.setImage(R.drawable.ic_launcher_round)
            checkout.open(this@SubscriptionActivity, options)
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun subScribePLan(razorpayPaymentID: String, pay_with_wallet: String) {
        try {
            openDialog = Utils.openDialog(this@SubscriptionActivity)
            val dataManager = ServiceManager.Companion.getDataManager()
            val otpCallback = object : Callback<SubScriptionMain> {
                override fun onResponse(
                    call: Call<SubScriptionMain>,
                    response: Response<SubScriptionMain>
                ) {
                    Log.e("response", "response MockList ${response.body().toString()}")
                    if (openDialog.isShowing) {
                        openDialog.dismiss()
                    }
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.status == true) {

                            getSubscriptionPlans()
                        } else {
                            // Utils.showToast(requireActivity(), "No Mock Tests Available")

                        }
                        Utils.showToast(applicationContext, body!!.message!!)
                    } else {
                        Log.e("QuizInfo", "Failed to get test details: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<SubScriptionMain>, t: Throwable) {
                    if (openDialog.isShowing) {
                        openDialog.dismiss()
                    }
                    Log.e("QuizInfo", "Error: ${t.message}")
                }
            }

            dataManager.subscribePlan(
                otpCallback,
                user_id = user_id,
                plan_id!!,
                razorpayPaymentID,
                "",
                pay_with_wallet
            )

        } catch (e: Exception) {
            if (openDialog.isShowing) {
                openDialog.dismiss()
            }
            Log.e("QuizInfoAPI ", e.message.toString())
        }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        try {
            Log.e("Payment Error", "Payment Success ${razorpayPaymentID}")
            //Toast.makeText(requireActivity(), "✅ Payment Successful! ID: $razorpayPaymentID", Toast.LENGTH_LONG).show()
            if (razorpayPaymentID != null) {
                subScribePLan(razorpayPaymentID, "0")

            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Payment Error", "Payment Error ${e.message}")

            // Toast.makeText(requireActivity(), "❌ Payment Failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onPaymentError(p0: Int, description: String?) {
        Log.e("Payment Error", "Payment Error ${description}")
        Toast.makeText(applicationContext, "❌ Payment Failed: $description", Toast.LENGTH_LONG)
            .show()
    }

    private fun getSubscriptionPlans() {
        try {
            openDialog = Utils.openDialog(this)
            val dataManager = ServiceManager.getDataManager()
            val callback = object : Callback<SubscriptionMainRes> {
                override fun onResponse(
                    call: Call<SubscriptionMainRes>,
                    response: Response<SubscriptionMainRes>
                ) {
                    if (openDialog.isShowing) openDialog.dismiss()

                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body!!.status!!) {
                            wallet_amount = response.body()!!.wallet_amount

                            planAdapter.updatePlans(body!!.data)
                        } else {
                            Utils.showToast(
                                this@SubscriptionActivity,
                                body?.message ?: "Failed to load plans"
                            )
                        }
                    } else {
                        Log.e("Subscription", "Failed: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<SubscriptionMainRes>, t: Throwable) {
                    if (openDialog.isShowing) openDialog.dismiss()
                    Log.e("Subscription", "Error: ${t.message}")
                }
            }
            dataManager.getSubscriptionPlans(callback, user_id = user_id)
        } catch (e: Exception) {
            if (openDialog.isShowing) openDialog.dismiss()
            Log.e("Subscription", "Exception: ${e.message}")
        }
    }
}

class SubscriptionPlanAdapter(
    private val plans: MutableList<SubscriptionPlan>,
    private val onSubscribeClick: (SubscriptionPlan) -> Unit
) : RecyclerView.Adapter<SubscriptionPlanAdapter.PlanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val binding =
            ItemSubscriptionPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        holder.bind(plans[position])
    }

    override fun getItemCount(): Int = plans.size

    fun updatePlans(newPlans: List<SubscriptionPlan>) {
        plans.clear()
        plans.addAll(newPlans)
        notifyDataSetChanged()
    }

    inner class PlanViewHolder(private val binding: ItemSubscriptionPlanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(plan: SubscriptionPlan) {
            binding.tvPlanTitle.text = plan.planTitle
            binding.tvPlanDesc.text = plan.planDesc
            binding.tvDuration.text = "${plan.durationDays} Days"
//            binding.tvQuizzes.text = "${plan.paidQnty} Paid + ${plan.freeQnty} Free"
            binding.tvQuizzes.text = "${plan.paidQnty} Paid"
            binding.tvPrice.text = "₹ ${plan.price}"

            binding.btnSubscribe.setOnClickListener {
                onSubscribeClick(plan)
            }
        }
    }
}
