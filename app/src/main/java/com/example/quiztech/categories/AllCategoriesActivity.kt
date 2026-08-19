package com.example.quiztech.categories

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.quiztech.BaseActivity
import com.example.quiztech.CategoryMainRes
import com.example.quiztech.R
import com.example.quiztech.databinding.ActivityAllCategoriesBinding
import com.example.quiztech.services.ServiceManager
import com.prvt.sreezzyuser.common.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllCategoriesActivity : BaseActivity<ActivityAllCategoriesBinding>(ActivityAllCategoriesBinding::inflate) {

    private lateinit var categoryAdapter: AllCategoryAdapter

    // In Clean Architecture, a ViewModel would be injected or created here.
    // private lateinit var viewModel: AllCategoriesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // ViewModel initialization would go here:
        // viewModel = ViewModelProvider(this).get(AllCategoriesViewModel::class.java)

        setupToolbar()
        setupRecyclerView()
        loadCategories() // In Clean Arch, this would trigger a ViewModel call to fetch data
    }

    private fun setupToolbar() {

        binding.layoutHeader.imgBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Standard back button behavior
        }
    }

    private fun setupRecyclerView() {
        // Remove app:layoutManager and app:spanCount from XML if they exist
        // to ensure this programmatic setting takes effect.
        binding.rvAllCategories.layoutManager = 
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        
        // Initialize adapter with an empty list initially
        categoryAdapter = AllCategoryAdapter(this, ArrayList())
        binding.rvAllCategories.adapter = categoryAdapter
    }

    private fun loadCategories() {
        getCategories()
    }

    private lateinit var openDialog: ProgressDialog
    private fun getCategories(){
        try {
            openDialog=Utils.openDialog(this@AllCategoriesActivity)
            val dataManager = ServiceManager.getDataManager()
            val otpCallback = object : Callback<CategoryMainRes> {
                override fun onResponse(call: Call<CategoryMainRes>, response: Response<CategoryMainRes>) {
                    Log.e("response","response ${response.body().toString()}")
                    if (openDialog.isShowing){
                        openDialog.dismiss()
                    }
                    if (response.isSuccessful) {
                        val body=response.body()
                        if (body!!.status==1){

                            //  sampleCategories.addAll(body.categories)
                            categoryAdapter.addCategories(body.categories)
                        }else{
                            println("Failed to send OTP. ${response.message()}")
                        }


                    } else {
                        println("Failed to send OTP. ${response.message()}")
                        //  Utils.showToast(this@LoginActivity,"Failed to send OTP. ${response.message()}")
                    }

                }

                override fun onFailure(call: Call<CategoryMainRes>, t: Throwable) {
                    // Handle failure
                    println("Failed to send OTP. ${t.message}")
                    if (openDialog.isShowing){
                        openDialog.dismiss()
                    }

                }
            }

            dataManager.getCategories(otpCallback )


        }catch (e:Exception){
            if (openDialog.isShowing){
                openDialog.dismiss()
            }
            Log.e("LoginAPI ",e.message.toString())
        }
    }
}
