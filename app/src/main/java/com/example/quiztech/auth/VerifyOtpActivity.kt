package com.example.quiztech.auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quiztech.RegisterActivity
import com.example.quiztech.auth.AddProfileDetailsActivity
import com.example.quiztech.databinding.ActivityVerifyOtpBinding
import com.example.quiztech.model.OTPVerifyResponse
import com.example.quiztech.model.ResendOTPResponse
import com.example.quiztech.services.ServiceManager
import com.example.quiztechimport.HomeActivity
import com.prvt.sreezzyuser.common.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerifyOtpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifyOtpBinding
    var mainOTP=""
    var is_new_user=true
    var user_id: String =""
    private lateinit var openDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userEmail = intent.getStringExtra("email")
         user_id = intent.getStringExtra("user_id").toString()
        binding.tvUserEmail.text = userEmail ?: "-"

        setupOtpEditTextListeners()

        binding.tvUserEmail.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        binding.btnVerifyContinue.setOnClickListener {
            val otp = "${binding.etOtp1.text}${binding.etOtp2.text}${binding.etOtp3.text}${binding.etOtp4.text}"
            if (otp.length == 4) {
                // TODO: Implement actual OTP verification logic
                /*startActivity(Intent(this, AddProfileDetailsActivity::class.java))
                finishAffinity()*/
                verifyOTP(otp)
            } else {
                Toast.makeText(this, "Please enter the complete OTP", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvResendCode.setOnClickListener {
            resendOTP(user_id)
            //Toast.makeText(this, "Resend OTP requested for: " + binding.tvUserEmail.text, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupOtpEditTextListeners() {
        val otpFields = listOf(binding.etOtp1, binding.etOtp2, binding.etOtp3, binding.etOtp4)

        for (i in otpFields.indices) {
            otpFields[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1 && i < otpFields.size - 1) {
                        otpFields[i+1].requestFocus()
                    }
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            otpFields[i].setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                    if (otpFields[i].text.isEmpty() && i > 0) {
                        otpFields[i-1].requestFocus()
                        otpFields[i-1].setText("") // Clear previous field as well for better UX
                    }
                }
                false
            })
        }
    }

    private fun resendOTP(phone:String){
        try {
            openDialog=Utils.openDialog(this)
            val dataManager = ServiceManager.getDataManager()
            val otpCallback = object : Callback<ResendOTPResponse> {
                override fun onResponse(call: Call<ResendOTPResponse>, response: Response<ResendOTPResponse>) {
                    if (openDialog.isShowing){
                        openDialog.dismiss()
                    }
                    if (response.isSuccessful) {
                        val body=response.body()
                        if (body!!.status == 1 ){
                            Utils.showToast(this@VerifyOtpActivity, body.message!!)

                            mainOTP=Utils.isNull(body.otp)
                            //tv_dummy_otp.text = "OTP: "+mainOTP


                            Log.e("Response", "response" + response.body().toString())

                        }else{
                            println("Failed to send OTP. ${response.message()}")
                            Utils.showToast(this@VerifyOtpActivity, body.message!!)
                        }


                    } else {
                        println("Failed to send OTP. ${response.message()}")
                        Utils.showToast(this@VerifyOtpActivity,"Failed to send OTP. ${response.message()}")
                    }

                }

                override fun onFailure(call: Call<ResendOTPResponse>, t: Throwable) {
                    // Handle failure
                    println("Failed to send OTP. ${t.message}")
                    if (openDialog.isShowing){
                        openDialog.dismiss()
                    }
                    Utils.showToast(this@VerifyOtpActivity, "Please try again")

                }
            }

            dataManager.resendOTP(otpCallback,phone )


        }catch (e:Exception){
            if (openDialog.isShowing){
                openDialog.dismiss()
            }
            Log.e("LoginAPI ",e.message.toString())
        }
    }

    private fun verifyOTP(otp:String){
        try {
            openDialog=Utils.openDialog(this)
            val dataManager = ServiceManager.getDataManager()
            val otpCallback = object : Callback<OTPVerifyResponse> {
                override fun onResponse(call: Call<OTPVerifyResponse>, response: Response<OTPVerifyResponse>) {
                    if (openDialog.isShowing){
                        openDialog.dismiss()
                    }
                    if (response.isSuccessful) {
                        val body=response.body()
                        if (body!!.status == 1 ){

                            is_new_user=response.body()!!.userInfo!!.isNewUser==1
                            Log.e("Response", "response" + response.body().toString())
                            Utils.showToast(this@VerifyOtpActivity, body.message!!)

                            var intent=Intent(this@VerifyOtpActivity, HomeActivity::class.java)
                            Utils.saveData(this@VerifyOtpActivity,"user_id",Utils.isNull(body.userInfo!!.id))
                            Utils.saveData(this@VerifyOtpActivity,"access_token",Utils.isNull(body.accessToken))
                            Utils.access_token=Utils.isNull(body.accessToken)
                            if(is_new_user) {
                                    intent = Intent(
                                        this@VerifyOtpActivity,
                                        AddProfileDetailsActivity::class.java
                                    )

                                    Utils.saveData(this@VerifyOtpActivity, Utils.IS_REGISTERED,"0")
                                }else
                                {
                                    Utils.saveData(this@VerifyOtpActivity, Utils.IS_REGISTERED,"1")
                                }

                                intent.putExtra("screen","login")
                                intent.putExtra("email",body.userInfo!!.email)
                            Utils.saveData(this@VerifyOtpActivity,"email",Utils.isNull(body.userInfo!!.email))
                            Utils.saveData(this@VerifyOtpActivity,"phone",Utils.isNull(body.userInfo!!.phone))
                            Utils.saveData(this@VerifyOtpActivity,"name",Utils.isNull(body.userInfo!!.fullName))
                            Utils.saveData(this@VerifyOtpActivity,"gender",Utils.isNull(body.userInfo!!.gender))
                            Utils.saveData(this@VerifyOtpActivity,"address",Utils.isNull(body.userInfo!!.address))

                              //  intent.putExtra("userData",body.userData)
                               /* Utils.saveData(this@VerifyOtpActivity,"id",Utils.isNull(body.userData!!.id))
                                Utils.saveData(this@VerifyOtpActivity,"user_id",Utils.isNull(body.userData!!.usersId))
                                Utils.saveData(this@VerifyOtpActivity,"name",Utils.isNull(body.userData!!.firstName)+" "+Utils.isNull(body.userData!!.lastName))
                                Utils.saveData(this@VerifyOtpActivity,"email",Utils.isNull(body.userData!!.email))
                                Utils.saveData(this@VerifyOtpActivity,"phone",Utils.isNull(body.userData!!.phone))
                              */  startActivity(intent)
                                finish()




                        }else{
                            println("Failed to verify OTP. ${response.message()}")
                            Utils.showToast(this@VerifyOtpActivity, body.message!!)
                        }


                    } else {
                        println("Failed to verify OTP. ${response.message()}")
                        Utils.showToast(this@VerifyOtpActivity,"Failed to send OTP. ${response.message()}")
                    }

                }

                override fun onFailure(call: Call<OTPVerifyResponse>, t: Throwable) {
                    // Handle failure
                    println("Failed to verify OTP. ${t.message}")
                    if (openDialog.isShowing){
                        openDialog.dismiss()
                    }
                    Utils.showToast(this@VerifyOtpActivity, "Please try again")

                }
            }


            dataManager.verifyOTP(otpCallback,user_id,otp )


        }catch (e:Exception){
            if (openDialog.isShowing){
                openDialog.dismiss()
            }
            Log.e("LoginAPI ",e.message.toString())
        }
    }
}