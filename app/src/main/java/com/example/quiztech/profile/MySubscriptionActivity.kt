package com.example.quiztech.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.quiztech.databinding.ActivityMySubscriptionBinding
import com.example.quiztech.model.SubscriptionMainRes
import com.example.quiztech.services.ServiceManager
import com.prvt.sreezzyuser.common.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MySubscriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMySubscriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMySubscriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.layoutHeader.txtHeader.text = "My Subscriptions"
        binding.layoutHeader.imgBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        getMySubscriptions()
    }

    private fun getMySubscriptions() {
        val userId = Utils.getData(this, "user_id", "").toString()
        val openDialog = Utils.openDialog(this)
        
        ServiceManager.getDataManager().getUserActiveSubscriptions(object : Callback<SubscriptionMainRes> {
            override fun onResponse(call: Call<SubscriptionMainRes>, response: Response<SubscriptionMainRes>) {
                if (openDialog.isShowing) openDialog.dismiss()
                
                if (response.isSuccessful && response.body()?.status!!) {
                    val subscriptions = response.body()?.data ?: arrayListOf()
                    if (subscriptions.isNotEmpty()) {
                        binding.rvSubscriptions.adapter = MySubscriptionAdapter(subscriptions)
                        binding.rvSubscriptions.visibility = View.VISIBLE
                        binding.tvNoData.visibility = View.GONE
                    } else {
                        binding.rvSubscriptions.visibility = View.GONE
                        binding.tvNoData.visibility = View.VISIBLE
                    }
                } else {
                    binding.rvSubscriptions.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<SubscriptionMainRes>, t: Throwable) {
                if (openDialog.isShowing) openDialog.dismiss()
                Log.e("MySubscriptionActivity", "Error: ${t.message}")
                binding.rvSubscriptions.visibility = View.GONE
                binding.tvNoData.visibility = View.VISIBLE
            }
        }, userId)
    }
}
