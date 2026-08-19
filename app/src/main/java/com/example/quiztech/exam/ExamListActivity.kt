package com.example.quiztech.exam

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quiztech.MockTest
import com.example.quiztech.quiz.MockTestAdapter
import com.example.quiztech.databinding.ActivityExamListBinding
import com.example.quiztech.enroll.EnrollActivity
import com.example.quiztech.model.MainResponse
import com.example.quiztech.model.MockListMainRes
import com.example.quiztech.quiz.QuizInfoActivity
import com.example.quiztech.services.ServiceManager
import com.prvt.sreezzyuser.common.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class ExamListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExamListBinding
    private lateinit var examListAdapter: MockTestAdapter

    var cat_id=""
    var sub_cat_id=""
    var topic_id=""
    var sub_topic_id=""
    var sampleMockTests = ArrayList<MockTest>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExamListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cat_id=intent.getStringExtra("cat_id").toString()
        sub_cat_id=intent.getStringExtra("sub_cat_id").toString()
        topic_id=intent.getStringExtra("topic_id").toString()
        sub_topic_id=intent.getStringExtra("sub_topic_id").toString()
        setupToolbar()
        setupRecyclerView()
        getMockList()
    }

    private fun setupToolbar() {

        binding.layoutHeader.imgBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        // Sample data - replace with actual data fetching logic


        examListAdapter  = MockTestAdapter(sampleMockTests) { mockTest ->

            startActivity(Intent(this, QuizInfoActivity::class.java).apply {
                putExtra("test_id",mockTest.testId)
            })
        }
        binding.recyclerViewExams.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewExams.adapter = examListAdapter
    }


    private lateinit var openDialog: ProgressDialog

    private fun getMockList(){
        try {
            openDialog= Utils.openDialog(this@ExamListActivity)
            val dataManager = ServiceManager.Companion.getDataManager()
            val otpCallback = object : Callback<MockListMainRes> {
                override fun onResponse(call: Call<MockListMainRes>, response: Response<MockListMainRes>) {
                    Log.e("response","response MockList ${response.body().toString()}")
                    if (openDialog.isShowing){
                        openDialog.dismiss()
                    }
                    if (response.isSuccessful) {
                        val body=response.body()
                        if (body!!.status==1){

                            sampleMockTests.clear()
                            sampleMockTests.addAll(body.mockList)
                            examListAdapter.addTest(body.mockList)

                        }else{
                            println("Failed to send OTP. ${response.message()}")
                        }

                        if(body.mockList.isEmpty()){
                            binding.recyclerViewExams.visibility = View.GONE
                            binding.tvNoData.visibility = View.VISIBLE
                        } else {
                            binding.recyclerViewExams.visibility = View.VISIBLE
                            binding.tvNoData.visibility = View.GONE
                        }

                    } else {
                        binding.recyclerViewExams.visibility = View.GONE
                        binding.tvNoData.visibility = View.VISIBLE
                        println("Failed to send OTP. ${response.message()}")
                        //  Utils.showToast(this@LoginActivity,"Failed to send OTP. ${response.message()}")
                    }

                }

                override fun onFailure(call: Call<MockListMainRes>, t: Throwable) {
                    // Handle failure
                    println("Failed to send OTP. ${t.message}")
                    if (openDialog.isShowing){
                        openDialog.dismiss()
                    }
                    binding.recyclerViewExams.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                }
            }

            if(topic_id!="-1")
            dataManager.getMockTestByTopic(otpCallback,topic_id, )
          else if(sub_cat_id!="-1")
            dataManager.getMockTestBySubCategory(otpCallback,sub_cat_id, )
            else
            dataManager.getMockTestByCategory(otpCallback,cat_id, )


        }catch (e:Exception){
            if (openDialog.isShowing){
                openDialog.dismiss()
            }
            Log.e("LoginAPI ",e.message.toString())
        }
    }
}