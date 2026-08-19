package com.example.quiztech.quiz

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quiztech.databinding.ActivityLayoutQuizresultBinding
import com.example.quiztech.services.ServiceManager
import com.google.android.material.tabs.TabLayout
import com.google.gson.annotations.SerializedName
import com.prvt.sreezzyuser.common.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList


data class ScoreCardMainRes (

    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : ScoreCard?    = ScoreCard()

)

data class ScoreCard(@SerializedName("leaderboard" ) var leaderboard : ArrayList<Leaderboard> = arrayListOf(),
                     @SerializedName("my_score"    ) var myScore     : MyScore?               = MyScore())

data class Leaderboard (

    @SerializedName("full_name"       ) var fullName       : String? = null,
    @SerializedName("email"           ) var email          : String? = null,
    @SerializedName("user_id"         ) var userId         : String? = null,
    @SerializedName("correct_answers" ) var correctAnswers : String? = null,
    @SerializedName("total_questions" ) var totalQuestions : String? = null,
    @SerializedName("attempted_date"  ) var attemptedDate  : String? = null,
    @SerializedName("user_rank"       ) var userRank       : String? = null,
    @SerializedName("prize"           ) var prize          : String? = null

)
data class MyScore (

    @SerializedName("full_name"       ) var fullName       : String? = null,
    @SerializedName("email"           ) var email          : String? = null,
    @SerializedName("user_id"         ) var userId         : String? = null,
    @SerializedName("correct_answers" ) var correctAnswers : String? = null,
    @SerializedName("total_questions" ) var totalQuestions : String? = null,
    @SerializedName("attempted_date"  ) var attemptedDate  : String? = null,
    @SerializedName("user_rank"       ) var userRank       : String? = null,
    @SerializedName("prize"           ) var prize          : String? = null

)

class QuizResultActivity : ComponentActivity() {

    private lateinit var binding: ActivityLayoutQuizresultBinding

    var user_id=""
    var product_id=""
    var leaderboard=ArrayList<Leaderboard>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayoutQuizresultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        product_id = intent.getStringExtra("product_id").toString()
        user_id = intent.getStringExtra("user_id").toString()
        binding.toolbarHeader.imgBack.setOnClickListener {
            finish()
        }

        setupTabs()
        setupRecyclerViews()
        getScoreCard()
    }
    private lateinit var openDialog: ProgressDialog

    private fun getScoreCard() {
        try {
            openDialog = Utils.openDialog(this@QuizResultActivity)
            val dataManager = ServiceManager.getDataManager()
            val otpCallback = object : Callback<ScoreCardMainRes> {
                override fun onResponse(call: Call<ScoreCardMainRes>, response: Response<ScoreCardMainRes>) {
                    if (openDialog.isShowing) {
                        openDialog.dismiss()
                    }
                    if (response.isSuccessful) {
                        val body = response.body()
                        leaderboard.clear()
                        leaderboard.addAll(body!!.data!!.leaderboard)

                        var correct=body.data!!.myScore!!.totalQuestions!!.toInt()
                        var total=body.data!!.myScore!!.totalQuestions!!.toInt()
                        binding.tvDateTimeValue.text="${body.data!!.myScore!!.attemptedDate}"
                        binding.txtTotalQtns.text="Total Questions : ${body.data!!.myScore!!.totalQuestions}"
                        binding.txtCorrectAns.text="Correct Answers : ${correct}"
                        binding.txtIncorrectAns.text="Wrong Answers : ${ total- correct}"
                        binding.txtYourRank.text="Your Rank : ${ body.data!!.myScore!!.userRank}"
                    } else {
                        Log.e("QuizActivity", "Failed to get test details: ${'$'}{response.message()}")
                    }
                }

                override fun onFailure(call: Call<ScoreCardMainRes>, t: Throwable) {
                    if (openDialog.isShowing) {
                        openDialog.dismiss()
                    }
                    Log.e("QuizActivity", "Error: ${'$'}{t.message}")
                }
            }
            dataManager.getScoreCard(otpCallback, user_id,product_id)
        } catch (e: Exception) {
            if (::openDialog.isInitialized && openDialog.isShowing) {
                openDialog.dismiss()
            }
            Log.e("QuizActivity", e.message.toString())
        }
    }
    private fun setupTabs() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        binding.resultsLayout.visibility = View.VISIBLE
                        binding.leaderboardRecyclerView.visibility = View.GONE
                        binding.winningsRecyclerView.visibility = View.GONE
                    }
                    1 -> {
                        binding.resultsLayout.visibility = View.GONE
                        binding.leaderboardRecyclerView.visibility = View.VISIBLE
                        binding.winningsRecyclerView.visibility = View.GONE
                        loadLeaderBoardData()
                    }
                    2 -> {
                        binding.resultsLayout.visibility = View.GONE
                        binding.leaderboardRecyclerView.visibility = View.GONE
                        binding.winningsRecyclerView.visibility = View.VISIBLE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupRecyclerViews() {
        binding.leaderboardRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.winningsRecyclerView.layoutManager = LinearLayoutManager(this)

        // You will need to create these adapters and provide data to them
       

         binding.leaderboardRecyclerView.adapter = LeaderboardAdapter(leaderboard as ArrayList<Leaderboard>)
        // binding.winningsRecyclerView.adapter = WinningsAdapter(winningsData)
    }
    
    private fun loadLeaderBoardData(){
       
        binding.leaderboardRecyclerView.adapter = LeaderboardAdapter(leaderboard)
    }
}