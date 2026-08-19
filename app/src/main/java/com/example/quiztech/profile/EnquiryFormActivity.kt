package com.example.quiztech.profile

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quiztech.databinding.ActivityEnquiryFormBinding
import com.example.quiztech.model.FAQsMainResponse
import com.example.quiztech.services.ServiceManager
import com.prvt.sreezzyuser.common.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EnquiryFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEnquiryFormBinding
    var name=""
    var phone=""
    var message=""
    var subject=""
    var user_id=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnquiryFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        name= Utils.getData(applicationContext, "email","").toString()
        phone= Utils.getData(applicationContext, "phone","").toString()
        user_id = Utils.getData(applicationContext, "user_id", "").toString()

        binding.headerToolbar.txtHeader.text = "Enquiry Form"
        binding.headerToolbar.imgBack.setOnClickListener {
            onBackPressed()
        }
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnSend.setOnClickListener {
           subject = binding.etSubject.text.toString()
            message= binding.etMessage.text.toString()

            if (subject.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Handle send logic here
            submitForm()
        }
    }
    private lateinit var openDialog: ProgressDialog

    private fun submitForm(){
        try {
              openDialog=Utils.openDialog(this@EnquiryFormActivity)
            val dataManager = ServiceManager.getDataManager()
            val otpCallback = object : Callback<FAQsMainResponse> {
                override fun onResponse(call: Call<FAQsMainResponse>, response: Response<FAQsMainResponse>) {
                    Log.e("response","response ${response.body().toString()}")
                    if (openDialog.isShowing){
                        openDialog.dismiss()
                    }
                    if (response.isSuccessful) {
                        val body=response.body()

                        Utils.showToast(this@EnquiryFormActivity,body!!.message!!)

finish()
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

            dataManager.enquiryForm(otpCallback,name,phone,subject,message,user_id )


        }catch (e:Exception){
            if (openDialog.isShowing){
                openDialog.dismiss()
            }
            Log.e("LoginAPI ",e.message.toString())
        }
    }
}