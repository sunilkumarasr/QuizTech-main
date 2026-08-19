package com.example.quiztech.subcategories

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quiztech.databinding.ActivitySubcategoryBinding
import com.example.quiztech.exam.ExamListActivity
import com.example.quiztech.model.SubCategoryMainRes
import com.example.quiztech.model.SubCategory
import com.example.quiztech.services.ServiceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.prvt.sreezzyuser.common.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubcategoryBinding

    var cat_id=""
    var has_items=1
   lateinit var subcategories: ArrayList<SubCategory>
   lateinit var subCategoryAdapter: SubCategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubcategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        cat_id=intent.getStringExtra("cat_id").toString()
        has_items=intent.getIntExtra("has_items",1)
       /* val subcategoriesJson = intent.getStringExtra("subcategories")
        val subcategories: List<SubCategory> = if (subcategoriesJson != null) {
            val type = object : TypeToken<List<SubCategory>>() {}.type
            Gson().fromJson(subcategoriesJson, type)
        } else {
            emptyList()
        }*/
        subcategories= ArrayList()

        subCategoryAdapter=SubCategoryAdapter(subcategories,cat_id)
        binding.rvSubcategories.layoutManager = GridLayoutManager(this, 2)
        binding.rvSubcategories.adapter = subCategoryAdapter
        if(has_items==0)
        {
            val intent= Intent(applicationContext, ExamListActivity::class.java).apply {
                putExtra("cat_id",cat_id)
                putExtra("sub_cat_id","-1")
            }
            startActivity(intent)
            finish()
        }
        getCategories()
    }

    private fun setupToolbar() {

        binding.layoutHeader.imgBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Standard back button behavior
        }
    }
    private lateinit var openDialog: ProgressDialog

    private fun getCategories(){
        try {
            openDialog=Utils.openDialog(this@SubCategoryActivity)
            val dataManager = ServiceManager.getDataManager()
            val otpCallback = object : Callback<SubCategoryMainRes> {
                override fun onResponse(call: Call<SubCategoryMainRes>, response: Response<SubCategoryMainRes>) {
                    Log.e("response","response ${response.body().toString()}")
                    if (openDialog.isShowing){
                        openDialog.dismiss()
                    }
                    if (response.isSuccessful) {
                        val body=response.body()
                        if (body!!.status==1){

                            //  sampleCategories.addAll(body.categories)
                            subCategoryAdapter.addCategories(body.subCategories)
                        }else{
                            println("Failed to send OTP. ${response.message()}")
                        }


                    } else {
                        println("Failed to send OTP. ${response.message()}")
                        //  Utils.showToast(this@LoginActivity,"Failed to send OTP. ${response.message()}")
                    }

                }

                override fun onFailure(call: Call<SubCategoryMainRes>, t: Throwable) {
                    // Handle failure
                    println("Failed to send OTP. ${t.message}")
                    if (openDialog.isShowing){
                        openDialog.dismiss()
                    }

                }
            }

            dataManager.getSubCategories(otpCallback,cat_id )


        }catch (e:Exception){
            if (openDialog.isShowing){
                openDialog.dismiss()
            }
            Log.e("LoginAPI ",e.message.toString())
        }
    }
}