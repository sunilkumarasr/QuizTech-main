package com.example.quiztech.auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quiztech.RegisterActivity
import com.example.quiztech.databinding.ActivityLoginBinding
import com.example.quiztech.model.LoginResponse
import com.example.quiztech.model.UserCommonJson
import com.example.quiztech.services.ServiceManager
import com.prvt.sreezzyuser.common.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var openDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLoginContinue.setOnClickListener {
            val email = binding.etLoginEmail.text.toString()
            if (email.isNotEmpty()) {
                // TODO: Implement actual login logic
              //  Toast.makeText(this, "Login attempt with: " + email, Toast.LENGTH_SHORT).show()
                // For now, let's navigate to HomeActivity after a dummy login

                loginUser(email)
            } else {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvRegisterNow.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
    private fun loginUser(email:String){
        try {
            openDialog=Utils.openDialog(this)
            val dataManager = ServiceManager.getDataManager()
            val otpCallback = object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (openDialog.isShowing){
                        openDialog.dismiss()
                    }
                    if (response.isSuccessful) {
                        val body=response.body()
                        if (body!!.status==1){
                           // Utils.saveData(this@LoginActivity,"id",Utils.isNull(body.!!.id))
                            val intent=Intent(this@LoginActivity, VerifyOtpActivity::class.java)
                            intent.putExtra("screen","login")
                            intent.putExtra("email",email)

                            intent.putExtra("user_id", body.userId)
                            startActivity(intent)
                            finish()
                            Log.e("Response", "response" + response.body().toString())

                        }else{
                            println("Failed to send OTP. ${response.message()}")
                            Utils.showToast(this@LoginActivity, body.message!!)
                        }


                    } else {
                        println("Failed to send OTP. ${response.message()}")
                      //  Utils.showToast(this@LoginActivity,"Failed to send OTP. ${response.message()}")
                    }

                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    // Handle failure
                    println("Failed to send OTP. ${t.message}")
                    if (openDialog.isShowing){
                        openDialog.dismiss()
                    }
                    Utils.showToast(this@LoginActivity, "Please try again")

                }
            }

            dataManager.loginUser(otpCallback,email )


        }catch (e:Exception){
            if (openDialog.isShowing){
                openDialog.dismiss()
            }
            Log.e("LoginAPI ",e.message.toString())
        }
    }
}