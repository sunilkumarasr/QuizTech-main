package com.example.quiztech.auth

import android.content.Intent
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quiztech.databinding.ActivityAddProfile2Binding
import com.example.quiztechimport.HomeActivity

class AddProfile2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProfile2Binding
    // In Clean Architecture, a ViewModel would be injected or created here.
    // private lateinit var viewModel: AddProfile2ViewModel
    var addressType = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProfile2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel initialization would happen here, e.g.:
        // viewModel = ViewModelProvider(this).get(AddProfile2ViewModel::class.java)

        binding.radioType.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {

                when(checkedId)
                {
                    binding.radioHome.id ->addressType="Home"
                    binding.radioShop.id ->addressType="Shop"
                    binding.radioOther.id ->addressType="Other"
                }
            }

        })
        setupListeners()
        // Observe ViewModel LiveData for UI updates (e.g., validation errors, navigation events)
    }

    private fun setupListeners() {
        binding.btnPrevious.setOnClickListener {
            // In Clean Arch, navigation might be handled by a Navigator class or via ViewModel events
            // For now, simple finish to go back to AddProfileDetailsActivity
            finish() 
        }

        binding.btnSubmit.setOnClickListener {
            collectAndProcessAddressData()
        }


    }

    private fun collectAndProcessAddressData() {
        val flatHouseNo = binding.etFlatHouseNo.text.toString().trim()
        val floor = binding.etFloor.text.toString().trim() // Optional
        val areaLocality = binding.etAreaLocality.text.toString().trim()
        val landmark = binding.etLandmark.text.toString().trim() // Optional
        val city = binding.etCity.text.toString().trim()
        val country = binding.etCountry.text.toString().trim()
        val state = binding.etState.text.toString().trim()
        val zipCode = binding.etZipCode.text.toString().trim()




        // --- ViewModel Interaction would be primary here in Clean Architecture ---
        // 1. viewModel.submitAddressDetails(
        //       flatHouseNo, floor, areaLocality, landmark, city, country, state, zipCode, addressType
        //    )
        // 2. The ViewModel would then call a UseCase to validate and save the data.
        // 3. The Activity would observe LiveData for results (success/error/navigation).

        // Placeholder validation and action:
        if (flatHouseNo.isNotEmpty() && areaLocality.isNotEmpty() && city.isNotEmpty() && 
            country.isNotEmpty() && state.isNotEmpty() && zipCode.isNotEmpty() && addressType.isNotEmpty()) {
            
            Toast.makeText(this, "Address Submitted (Not Saved Yet)", Toast.LENGTH_LONG).show()
            // Navigate to HomeActivity or another appropriate screen on success
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Please fill all required fields and select address type", Toast.LENGTH_SHORT).show()
        }
    }
}