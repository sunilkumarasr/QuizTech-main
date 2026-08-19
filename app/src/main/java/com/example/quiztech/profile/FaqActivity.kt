package com.example.quiztech.profile

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quiztech.databinding.ActivityFaqBinding
import com.example.quiztech.model.FAQsMainResponse
import com.example.quiztech.services.ServiceManager
import com.prvt.sreezzyuser.common.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FaqActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaqBinding
    private lateinit var adapter: FaqAdapter
    private val faqs = mutableListOf<Faq>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.headerToolbar.txtHeader.text= "FAQ"
        binding.headerToolbar.imgBack.setOnClickListener {
            onBackPressed()
        }

        setupRecyclerView()
       // addDummyData()
        faqs()
    }

    private fun setupRecyclerView() {
        adapter = FaqAdapter(faqs)
        binding.rvFaqs.layoutManager = LinearLayoutManager(this)
        binding.rvFaqs.adapter = adapter
    }

    private fun addDummyData() {
        faqs.add(
            Faq(
                "Terms & Conditions",
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s."
            )
        )
        faqs.add(
            Faq(
                "Terms & Conditions",
                "Product of The Rice Company Pvt. Ltd."
            )
        )
        faqs.add(
            Faq(
                "Terms & Conditions",
                "Product of The Rice Company Pvt. Ltd."
            )
        )
        adapter.notifyDataSetChanged()
    }

    private lateinit var openDialog: ProgressDialog

    private fun faqs(){
        faqs.clear()

        try {
            openDialog=Utils.openDialog(this@FaqActivity)
            val dataManager = ServiceManager.getDataManager()
            val otpCallback = object : Callback<FAQsMainResponse> {
                override fun onResponse(call: Call<FAQsMainResponse>, response: Response<FAQsMainResponse>) {
                    Log.e("response","response ${response.body().toString()}")
                    if (openDialog.isShowing){
                        openDialog.dismiss()
                    }
                    if (response.isSuccessful) {
                        val body=response.body()

                        body!!.data.forEach {
                            faqs.add(Faq(it.question!!,it.answer!!))
                            adapter.notifyDataSetChanged()
                        }



                    } else {
                        println("Failed to send OTP. ${response.message()}")
                        //  Utils.showToast(this@LoginActivity,"Failed to send OTP. ${response.message()}")
                    }

                }

                override fun onFailure(call: Call<FAQsMainResponse>, t: Throwable) {
                    // Handle failure
                    println("Failed to send OTP. ${t.message}")
                    if (openDialog.isShowing){
                        openDialog.dismiss()
                    }

                }
            }

            dataManager.faqs(otpCallback )


        }catch (e:Exception){
            if (openDialog.isShowing){
                openDialog.dismiss()
            }
            Log.e("LoginAPI ",e.message.toString())
        }
    }
}