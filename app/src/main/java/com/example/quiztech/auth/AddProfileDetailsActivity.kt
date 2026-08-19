package com.example.quiztech.auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quiztech.MainActivity
import com.example.quiztech.R
import com.example.quiztech.databinding.ActivityAddProfileDetailsBinding
import com.example.quiztech.model.OTPVerifyResponse
import com.example.quiztech.services.ServiceManager
import com.example.quiztechimport.HomeActivity
import com.prvt.sreezzyuser.common.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddProfileDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProfileDetailsBinding
var email=""
    var userId=""
    var gender="m"
    var fullName=""
    var address=""
    var phoneNumber=""
    private lateinit var openDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProfileDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email=intent.getStringExtra("email").toString()
        userId= Utils.getData(this,"user_id","").toString()

        binding.etEmail.setText("${email}")
        setupGenderSpinner()
        setupClickListeners()
    }

    private fun setupGenderSpinner() {
        // In a real app, gender options might come from resources or a configuration file
        val genders = arrayOf("Male", "Female", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genders)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGender.adapter = adapter
        binding.spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                gender = parent.getItemAtPosition(position).toString()
                when(position)
                {
                    0-> gender="m"
                    1-> gender="f"
                    2-> gender="o"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                gender=""
            }
        }

    }

    private fun setupClickListeners() {
        binding.btnNext.setOnClickListener {
            // Collect data from fields
             fullName = binding.etFullName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
             phoneNumber = binding.etPhoneNumber.text.toString().trim()
             address = binding.etAddress.text.toString().trim()

            // --- ViewModel Interaction would happen here in Clean Architecture ---
            // 1. Validate input (e.g., using a ViewModel method)
            // 2. If valid, call ViewModel to save profile details (which interacts with UseCases -> Repository)
            // 3. Observe LiveData/StateFlow from ViewModel for success/failure
            // For now, we'll do a simple check and navigate

            if (fullName.isEmpty() || address.isEmpty() || phoneNumber.isEmpty()||gender.isEmpty()) {
                Utils.showToast(applicationContext,"Fill all details")
                return@setOnClickListener
            }
            updateProfile()

            /*if (fullName.isNotEmpty() && email.isNotEmpty() && phoneNumber.isNotEmpty()) {
                Toast.makeText(this, "Profile details captured (Not saved yet)", Toast.LENGTH_LONG).show()
                // Navigate to HomeActivity or another appropriate screen
                // For this example, let's assume successful profile creation navigates to Home
                val intent = Intent(this, AddProfile2Activity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            }*/
        }
    }
    private fun updateProfile(){
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
                            Utils.saveData(this@AddProfileDetailsActivity, Utils.IS_REGISTERED,"1")
                            Utils.saveData(this@AddProfileDetailsActivity,"email",Utils.isNull(body.userInfo!!.email))
                            Utils.saveData(this@AddProfileDetailsActivity,"phone",Utils.isNull(body.userInfo!!.phone))
                            Utils.saveData(this@AddProfileDetailsActivity,"name",Utils.isNull(body.userInfo!!.fullName))
                            Utils.saveData(this@AddProfileDetailsActivity,"gender",Utils.isNull(body.userInfo!!.gender))
                            Utils.saveData(this@AddProfileDetailsActivity,"address",Utils.isNull(body.userInfo!!.address))

                            startActivity(Intent(applicationContext, HomeActivity::class.java))
                            finish()
                        }else{
                            println("Failed to verify OTP. ${response.message()}")
                            Utils.showToast(this@AddProfileDetailsActivity, body.message!!)
                        }


                    } else {
                        println("Failed to verify OTP. ${response.message()}")
                        Utils.showToast(this@AddProfileDetailsActivity,"Failed to send OTP. ${response.message()}")
                    }

                }

                override fun onFailure(call: Call<OTPVerifyResponse>, t: Throwable) {
                    // Handle failure
                    println("Failed to verify OTP. ${t.message}")
                    if (openDialog.isShowing){
                        openDialog.dismiss()
                    }
                    Utils.showToast(this@AddProfileDetailsActivity, "Please try again")

                }
            }


            dataManager.updateProfile(otpCallback,userId,fullName,address,phoneNumber,gender)


        }catch (e:Exception){
            if (openDialog.isShowing){
                openDialog.dismiss()
            }
            Log.e("LoginAPI ",e.message.toString())
        }
    }
}