package com.example.quiztech.profile

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quiztech.R
import com.example.quiztech.databinding.ActivityEditProfileBinding
import com.example.quiztech.model.OTPVerifyResponse
import com.example.quiztech.services.ServiceManager
import com.example.quiztechimport.HomeActivity
import com.prvt.sreezzyuser.common.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    var name=""
    var email=""
    var phone=""
    var gender=""
    var user_id=""
    var address=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user_id= Utils.getData(this@EditProfileActivity,"user_id","").toString()
        name= Utils.getData(this@EditProfileActivity,"name","").toString()
        email= Utils.getData(this@EditProfileActivity,"email","").toString()
        phone= Utils.getData(this@EditProfileActivity,"phone","").toString()
        gender= Utils.getData(this@EditProfileActivity,"gender","").toString()
        address= Utils.getData(this@EditProfileActivity,"address","").toString()


        binding.etEmail.setText(email)
        binding.etFullName.setText(name)
        binding.etPhoneNumber.setText(phone)
        binding.headerToolbar.txtHeader.text = "Edit Profile"
        binding.headerToolbar.imgBack.setOnClickListener {
            onBackPressed()
        }

        setupGenderSpinner()
        setupClickListeners()
    }

    private fun setupGenderSpinner() {
        val genders = arrayOf("Select Gender","Male", "Female", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genders)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGender.adapter = adapter

        binding.spinnerGender.onItemSelectedListener=object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                when(position)
                {
                    0-> gender=""
                    1-> gender="m"
                    2-> gender="f"
                    3-> gender="o"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }



        }

        when(gender)
        {
            ""-> binding.spinnerGender.setSelection(0)
            "m"-> binding.spinnerGender.setSelection(1)
            "f"-> binding.spinnerGender.setSelection(2)
            "o"-> binding.spinnerGender.setSelection(3)
        }
    }

    private fun setupClickListeners() {
        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {
            name = binding.etFullName.text.toString()
            email = binding.etEmail.text.toString()
            phone = binding.etPhoneNumber.text.toString()

            if (name.isEmpty() || gender.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            updateProfile()

        }
    }
    private lateinit var openDialog: ProgressDialog

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
                            Utils.saveData(this@EditProfileActivity, Utils.IS_REGISTERED,"1")
                            Utils.saveData(this@EditProfileActivity,"email",Utils.isNull(email))
                            Utils.saveData(this@EditProfileActivity,"phone",Utils.isNull(phone))
                            Utils.saveData(this@EditProfileActivity,"name",Utils.isNull(name))
                            Utils.saveData(this@EditProfileActivity,"gender",Utils.isNull(gender))


                            finish()
                        }else{
                            println("Failed to verify OTP. ${response.message()}")
                            Utils.showToast(this@EditProfileActivity, body.message!!)
                        }


                    } else {
                        println("Failed to verify OTP. ${response.message()}")
                        Utils.showToast(this@EditProfileActivity,"Failed to send OTP. ${response.message()}")
                    }

                }

                override fun onFailure(call: Call<OTPVerifyResponse>, t: Throwable) {
                    // Handle failure
                    println("Failed to verify OTP. ${t.message}")
                    if (openDialog.isShowing){
                        openDialog.dismiss()
                    }
                    Utils.showToast(this@EditProfileActivity, "Please try again")

                }
            }


            dataManager.updateProfile(otpCallback,user_id,name,address,phone,gender)


        }catch (e:Exception){
            if (openDialog.isShowing){
                openDialog.dismiss()
            }
            Log.e("LoginAPI ",e.message.toString())
        }
    }

}