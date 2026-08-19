package com.example.quiztech.winning

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quiztech.databinding.FragmentSubscriptionsBinding
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

class SubscriptionsFragment : Fragment(), PaymentResultListener {

    private var _binding: FragmentSubscriptionsBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private lateinit var openDialog: ProgressDialog
    var plan_id: String? = ""
    var user_id = ""
    var wallet_amount = 0
    private lateinit var planAdapter: SubscriptionPlanAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSubscriptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize UI elements and set up listeners here
        // For example: binding.textViewWinnings.text = "Winnings"
        user_id = Utils.getData(requireActivity(), "user_id", "").toString()

        setupRecyclerView()
        getSubscriptionPlans()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        planAdapter = SubscriptionPlanAdapter(mutableListOf()) { plan ->
            // Handle plan subscription
            plan_id = plan.id
            if (plan.price != null && plan.price!!.isNotEmpty()) {
                if (wallet_amount > Integer.parseInt(plan.price))
                    subScribePLan("", "1")
                else
                    startPayment(plan.price.toString())
            }

            //subScribePLan()
            //  Utils.showToast(requireActivity(), "Subscribing to ${plan.planTitle}")
        }
        binding.rvSubscriptions.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvSubscriptions.adapter = planAdapter
    }

    private fun getSubscriptionPlans() {
        plan_id = ""
        try {
            openDialog = Utils.openDialog(requireActivity())
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
                            wallet_amount = body.wallet_amount
                            planAdapter.updatePlans(body!!.data)
                        } else {
                            Utils.showToast(
                                requireActivity(),
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
            dataManager.getSubscriptionPlans(callback, user_id)
        } catch (e: Exception) {
            if (openDialog.isShowing) openDialog.dismiss()
            Log.e("Subscription", "Exception: ${e.message}")
        }
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
            //4591 9731 00953079
            checkout.open(requireActivity(), options)
        } catch (e: Exception) {
            Toast.makeText(requireActivity(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun subScribePLan(razorpayPaymentID: String, pay_with_wallet: String) {
        try {
            openDialog = Utils.openDialog(requireActivity())
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
                        Utils.showToast(requireActivity(), body!!.message!!)
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
        Toast.makeText(requireActivity(), "❌ Payment Failed: $description", Toast.LENGTH_LONG)
            .show()
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
            if (plan.isSubscribed == true) {
                binding.btnSubscribe.visibility = View.GONE
            } else {
                binding.btnSubscribe.visibility = View.VISIBLE
            }
            binding.btnSubscribe.setOnClickListener {
                onSubscribeClick(plan)
            }
        }
    }
}