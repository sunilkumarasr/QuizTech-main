package com.example.quiztech.subcategories.topics

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quiztech.databinding.ActivityTopicBinding
import com.example.quiztech.exam.ExamListActivity
import com.example.quiztech.model.Topic
import com.example.quiztech.model.TopicMainRes
import com.example.quiztech.services.ServiceManager
import com.prvt.sreezzyuser.common.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TopicActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTopicBinding

    var catId=""
    var subCatId=""
    var has_items=1
   lateinit var topics: ArrayList<Topic>
   lateinit var topicAdapter: TopicAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subCatId=intent.getStringExtra("sub_cat_id").toString()
        catId=intent.getStringExtra("cat_id").toString()
        has_items=intent.getIntExtra("has_items",1)
        setupToolbar()
        topics= ArrayList()
        topicAdapter = TopicAdapter()
        binding.rvTopics.layoutManager = GridLayoutManager(this@TopicActivity, 2)
        binding.rvTopics.adapter = topicAdapter


        if(has_items==0)
        {
            val intent= Intent(applicationContext, ExamListActivity::class.java).apply {
                putExtra("cat_id",catId)
                putExtra("sub_cat_id",subCatId)
                putExtra("topic_id","-1")
                putExtra("sub_topic_id","-1")
            }
            startActivity(intent)
            finish()
        }else
        getTopics()
    }
    private fun setupToolbar() {

        binding.layoutHeader.imgBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Standard back button behavior
        }
    }
    private lateinit var openDialog: ProgressDialog

    private fun getTopics(){
        try {
            openDialog= Utils.openDialog(this@TopicActivity)
            val dataManager = ServiceManager.Companion.getDataManager()
            val otpCallback = object : Callback<TopicMainRes> {
                override fun onResponse(call: Call<TopicMainRes>, response: Response<TopicMainRes>) {
                    Log.e("response","response ${response.body().toString()}")
                    if (openDialog.isShowing){
                        openDialog.dismiss()
                    }
                    if (response.isSuccessful) {
                        val body=response.body()
                        if (body?.status==1){
                            if(body.topics.isEmpty()){
                                val intent= Intent(applicationContext, ExamListActivity::class.java).apply {
                                    putExtra("cat_id",catId)
                                    putExtra("sub_cat_id",subCatId)
                                    putExtra("topic_id","-1")
                                    putExtra("sub_topic_id","-1")
                                }
                                startActivity(intent)
                                finish()
                            } else {
                                topicAdapter.addTopics(body.topics)
                            }

                        }else{
                            val intent= Intent(applicationContext, ExamListActivity::class.java).apply {
                                putExtra("cat_id",catId)
                                putExtra("sub_cat_id",subCatId)
                                putExtra("topic_id","-1")
                                putExtra("sub_topic_id","-1")
                            }
                            startActivity(intent)
                            finish()
                            println("Failed to get topics. ${response.message()}")
                        }


                    } else {
                        println("Failed to get topics. ${response.message()}")
                        //  Utils.showToast(this@LoginActivity,"Failed to send OTP. ${response.message()}")
                    }

                }

                override fun onFailure(call: Call<TopicMainRes>, t: Throwable) {
                    // Handle failure
                    println("Failed to get topics. ${t.message}")
                    if (openDialog.isShowing){
                        openDialog.dismiss()
                    }

                }
            }

            dataManager.getTopic(otpCallback,catId,subCatId )


        }catch (e:Exception){
            if (openDialog.isShowing){
                openDialog.dismiss()
            }
            Log.e("LoginAPI ",e.message.toString())
        }
    }
}
