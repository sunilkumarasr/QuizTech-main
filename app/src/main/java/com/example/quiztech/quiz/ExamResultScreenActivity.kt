package com.example.quiztech.quiz

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quiztech.databinding.ActivityExamResultScreenBinding
import com.example.quiztech.databinding.ItemWinnerRowBinding
import com.example.quiztech.services.ServiceManager
import com.prvt.sreezzyuser.common.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import android.text.Html
import androidx.core.text.HtmlCompat
import com.example.quiztech.databinding.ItemQuestionResultBinding

import com.google.android.material.tabs.TabLayout

class ExamResultScreenActivity : ComponentActivity() {

    private lateinit var binding: ActivityExamResultScreenBinding
    private lateinit var progressDialog: ProgressDialog
    private var testId: String = ""
    private var userId: String = ""
    
    private var questionsList: List<ExamQuestionResult> = arrayListOf()
    private var winnersList: List<Winner> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExamResultScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        testId = intent.getStringExtra("test_id") ?: ""
        userId = intent.getStringExtra("user_id") ?: ""

        binding.imgClose.setOnClickListener { finish() }
        
        setupRecyclerView()
        setupTabs()
        fetchExamDetails()
    }

    private fun setupRecyclerView() {
        binding.rvResults.layoutManager = LinearLayoutManager(this)
    }

    private fun setupTabs() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> showQuestions()
                    1 -> showWinners()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun showQuestions() {
        binding.tvSectionTitle.text = "📝 Question Analysis"
        binding.rvResults.adapter = QuestionResultAdapter(questionsList)
    }

    private fun showWinners() {
        binding.tvSectionTitle.text = "🏆 Winners List"
        binding.rvResults.adapter = WinnersAdapter(winnersList)
    }

    private fun fetchExamDetails() {
        progressDialog = Utils.openDialog(this)
        val dataManager = ServiceManager.getDataManager()
        
        val callback = object : Callback<ExamResultResponse> {
            override fun onResponse(call: Call<ExamResultResponse>, response: Response<ExamResultResponse>) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null && result.status == true) {
                        updateUI(result)
                    }
                } else {
                    Log.e("ExamResult", "API Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ExamResultResponse>, t: Throwable) {
                progressDialog.dismiss()
                Log.e("ExamResult", "Network Error: ${t.message}")
            }
        }

        dataManager.getExamDetails(callback, testId, userId)
    }

    private fun updateUI(response: ExamResultResponse) {
        val data = response.data ?: return
        
        val myScore = data.myScore
        binding.tvUserName.text = myScore?.fullName ?: Utils.getData(this, "full_name", "User").toString()
        
        val summary = data.summary
        binding.tvRank.text = myScore?.userRank ?: "-"
        binding.tvScore.text = myScore?.correctAnswers ?: summary?.correctAnswers ?: "0"
        binding.tvPrize.text = if (myScore?.prize != null && myScore.prize != "0") "₹${myScore.prize}" else "-"
        
        val rank = myScore?.userRank
        binding.tvBadge.text = when (rank) {
            "1" -> "🏅 1st"
            "2" -> "🥈 2nd"
            "3" -> "🥉 3rd"
            null, "" -> "Completed"
            else -> "Rank $rank"
        }

        questionsList = data.questions
        winnersList = data.winners ?: arrayListOf()
        
        // Default to showing questions (first tab)
        showQuestions()
    }

    inner class WinnersAdapter(private val winners: List<Winner>) :
        RecyclerView.Adapter<WinnersAdapter.ViewHolder>() {

        inner class ViewHolder(val binding: ItemWinnerRowBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemWinnerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val winner = winners[position]
            holder.binding.tvWinnerRank.text = winner.userRank
            holder.binding.tvWinnerName.text = winner.fullName
            holder.binding.tvWinnerPrize.text = if (winner.prize != null && winner.prize != "0") "₹${winner.prize}" else "-"
            
            val rank = winner.userRank
            holder.binding.tvWinnerBadge.text = when (rank) {
                "1" -> "🏅 1st"
                "2" -> "🥈 2nd"
                "3" -> "🥉 3rd"
                else -> "Rank $rank"
            }
        }

        override fun getItemCount(): Int = winners.size
    }

    inner class QuestionResultAdapter(private val questions: List<ExamQuestionResult>) :
        RecyclerView.Adapter<QuestionResultAdapter.ViewHolder>() {

        inner class ViewHolder(val binding: ItemQuestionResultBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemQuestionResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = questions[position]
            holder.binding.tvQuestionText.text = HtmlCompat.fromHtml(item.questionText ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY)
            holder.binding.tvGivenAnswer.text = item.givenAnswer
            holder.binding.tvCorrectAnswer.text = item.correctAnswer
            holder.binding.tvStatus.text = item.answerStatus
            
            if (item.answerStatus == "Correct") {
                holder.binding.tvStatus.setBackgroundResource(com.example.quiztech.R.drawable.bg_badge_orange) // Reusing orange for correct
            } else {
                holder.binding.tvStatus.setBackgroundColor(android.graphics.Color.RED)
            }

            if (!item.answer_description.isNullOrEmpty()) {
                holder.binding.layoutDescription.visibility = View.VISIBLE
                holder.binding.tvDescription.text = HtmlCompat.fromHtml(item.answer_description ?: "", HtmlCompat.FROM_HTML_MODE_LEGACY)
            } else {
                holder.binding.layoutDescription.visibility = View.GONE
            }
        }

        override fun getItemCount(): Int = questions.size
    }
}
