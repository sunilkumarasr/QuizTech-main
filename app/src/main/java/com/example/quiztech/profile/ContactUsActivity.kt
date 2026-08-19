package com.example.quiztech.profile

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.quiztech.databinding.ActivityContactUsBinding
import com.example.quiztech.model.ContactUsMain
import com.example.quiztech.services.ServiceManager
import com.prvt.sreezzyuser.common.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactUsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.headerToolbar.txtHeader.text  = "Contact US"
        binding.headerToolbar.imgBack.setOnClickListener {
            onBackPressed()
        }

        setupClickListeners()
        contactUs()
    }

    private fun setupClickListeners() {
        binding.tvLandline.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:040-123456")
            startActivity(intent)
        }

        binding.tvMobile1.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:+919123456789")
            startActivity(intent)
        }

        binding.tvMobile2.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:+919123456789")
            startActivity(intent)
        }

        binding.tvEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:Quiztech@gmail.com")
            startActivity(intent)
        }
    }

    private lateinit var openDialog: ProgressDialog

    private fun contactUs(){
        try {
            openDialog=Utils.openDialog(this@ContactUsActivity)
            val dataManager = ServiceManager.getDataManager()
            val otpCallback = object : Callback<ContactUsMain> {
                override fun onResponse(call: Call<ContactUsMain>, response: Response<ContactUsMain>) {
                    Log.e("response","response ${response.body().toString()}")
                    if (openDialog.isShowing){
                        openDialog.dismiss()
                    }
                    if (response.isSuccessful) {
                        val body=response.body()
                        body!!.data.forEach {
                            binding.txtAddress.text=it.address
                            binding.tvMobile1.text="Mobile : ${it.phone}"
                            binding.tvMobile2.text="${it.phone2}"
                            binding.tvEmail.text="${it.email}"
                        }




                    } else {
                        println("Failed to send OTP. ${response.message()}")
                        //  Utils.showToast(this@LoginActivity,"Failed to send OTP. ${response.message()}")
                    }

                }

                override fun onFailure(call: Call<ContactUsMain>, t: Throwable) {
                    // Handle failure
                    println("Failed to send OTP. ${t.message}")
                    if (openDialog.isShowing){
                        openDialog.dismiss()
                    }

                }
            }

            dataManager.contactUs(otpCallback )


        }catch (e:Exception){
            if (openDialog.isShowing){
                openDialog.dismiss()
            }
            Log.e("LoginAPI ",e.message.toString())
        }
    }
}