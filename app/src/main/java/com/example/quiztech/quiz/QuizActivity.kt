package com.example.quiztech.quiz

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import com.example.quiztech.R
import com.example.quiztech.databinding.ActivityLayoutQuizBinding
import com.example.quiztech.services.ServiceManager
import com.google.gson.annotations.SerializedName
import com.prvt.sreezzyuser.common.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class QuestionMinRes(
    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("duration_left") var duration_left: Int? = 5,
    @SerializedName("data") var data: ArrayList<Question> = arrayListOf()
)

data class Question(
    @SerializedName("id") var id: String? = null,
    @SerializedName("product_id") var productId: String? = null,
    @SerializedName("question_text") var questionText: String? = null,
    @SerializedName("section_id") var sectionId: String? = null,
    @SerializedName("section_name") var sectionName: String? = null,
    @SerializedName("options") var options: ArrayList<Options> = arrayListOf(),
    val correctAnswerIndex: Int
)

data class Options(
    @SerializedName("id") var id: String? = null,
    @SerializedName("question_id") var questionId: String? = null,
    @SerializedName("option_text") var optionText: String? = null
)

data class SubmitExamRequest(
    @SerializedName("user_id") val userId: String,
    @SerializedName("product_id") val productId: String,
    @SerializedName("answers") val answers: List<Answer>
)

data class Answer(
    @SerializedName("question_id") val questionId: String,
    @SerializedName("selected_option_id") val selectedOptionId: String
)

data class SubmitExamResponse(
    @SerializedName("status") val status: Boolean?,
    @SerializedName("message") val message: String?
)

class QuizActivity : ComponentActivity() {

    private lateinit var binding: ActivityLayoutQuizBinding
    private var countDownTimer: CountDownTimer? = null
    private var currentQuestionIndex = 0
    private var currentQuestion: Question? = null
    var test_id = ""
    var user_id = ""
    var duration_left: Int? = 5

    private var questions = arrayListOf<Question>()
    private val selectedAnswers = mutableMapOf<Int, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLayoutQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        test_id = intent.getStringExtra("test_id").toString()
        user_id = intent.getStringExtra("user_id").toString()
        binding.toolbarHeader.imgBack.setOnClickListener {
            showExitConfirmationDialog()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmationDialog()
            }
        })

        getExamQuestions()

        updateButtonVisibility()

        binding.nextButton.setOnClickListener {
            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                loadQuestion(currentQuestionIndex)
                updateButtonVisibility()
            } else {
                submitExam()
            }
        }

        binding.previousButton.setOnClickListener {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--
                loadQuestion(currentQuestionIndex)
                updateButtonVisibility()
            }
        }

        binding.lnrOption1.setOnClickListener { selectOption(0) }
        binding.lnrOption2.setOnClickListener { selectOption(1) }
        binding.lnrOption3.setOnClickListener { selectOption(2) }
        binding.lnrOption4.setOnClickListener { selectOption(3) }
    }

    private fun loadQuestion(questionIndex: Int) {
        val question = questions[questionIndex]
        currentQuestion = question

        binding.txtQuestion.loadData(question.questionText ?: "", "text/html", "utf-8")
        binding.tvQuestionCount.text = "${questionIndex + 1}/${questions.size}"
        updateProgressBar()

        val optionWebViewMap = mapOf(
            binding.option1 to binding.lnrOption1,
            binding.option2 to binding.lnrOption2,
            binding.option3 to binding.lnrOption3,
            binding.option4 to binding.lnrOption4
        )

        val webViews = listOf(binding.option1, binding.option2, binding.option3, binding.option4)
        for (i in webViews.indices) {
            val webView = webViews[i]
            if (i < question.options.size) {
                webView.visibility = View.VISIBLE
                webView.loadData(question.options[i].optionText ?: "", "text/html", "utf-8")
                webView.setBackgroundColor(0) // Transparent background

                // Fix: WebView consuming click events. Forward touch events to parent LinearLayout.
                webView.setOnTouchListener { v, event ->
                    if (event.action == MotionEvent.ACTION_UP) {
                        optionWebViewMap[v]?.performClick()
                    }
                    true
                }
            } else {
                webView.visibility = View.GONE
            }
        }

        updateOptionsUI()
    }

    private fun selectOption(optionIndex: Int) {
        if (selectedAnswers[currentQuestionIndex] == optionIndex) {
            selectedAnswers.remove(currentQuestionIndex)
        } else {
            selectedAnswers[currentQuestionIndex] = optionIndex
        }
        updateOptionsUI()
    }

    private fun updateOptionsUI() {
        val selectedIndex = selectedAnswers[currentQuestionIndex]

        binding.lnrOption1.setBackgroundResource(if (selectedIndex == 0) R.drawable.round_selected else R.drawable.round_unselected)
        binding.lnrOption2.setBackgroundResource(if (selectedIndex == 1) R.drawable.round_selected else R.drawable.round_unselected)
        binding.lnrOption3.setBackgroundResource(if (selectedIndex == 2) R.drawable.round_selected else R.drawable.round_unselected)
        binding.lnrOption4.setBackgroundResource(if (selectedIndex == 3) R.drawable.round_selected else R.drawable.round_unselected)
    }

    private fun updateProgressBar() {
        if (questions.isNotEmpty()) {
            val progress = ((currentQuestionIndex + 1).toFloat() / questions.size * 100).toInt()
            binding.progressBar.progress = progress
        }
    }

    private fun updateButtonVisibility() {
        binding.previousButton.visibility = if (currentQuestionIndex == 0) View.INVISIBLE else View.VISIBLE
        binding.nextButton.text = if (currentQuestionIndex == questions.size - 1) "Finish" else "Next"
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer((1000 * 60 * duration_left!!).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                binding.tvTimer.text = String.format("%02d:%02d Min", minutes, seconds)
            }

            override fun onFinish() {
                binding.tvTimer.text = "00:00 Min"
                submitExam()
            }
        }.start()
    }

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Quiz Not completed yet")
        builder.setMessage("Are you sure you want to submit the Exam?")
        builder.setPositiveButton("Yes") { _, _ ->
            finish()
        }
        builder.setNegativeButton("No", null)
        builder.show()
    }

    private fun submitExam() {
        if (questions.isEmpty()) {
            finish()
            return
        }

        val productId = questions.first().productId ?: ""
        val answers = selectedAnswers.mapNotNull { (questionIndex, optionIndex) ->
            val question = questions.getOrNull(questionIndex)
            val option = question?.options?.getOrNull(optionIndex)
            if (question?.id != null && option?.id != null) {
                Answer(question.id!!, option.id!!)
            } else {
                null
            }
        }

        val request = SubmitExamRequest(
            userId = user_id,
            productId = productId,
            answers = answers
        )

        openDialog = Utils.openDialog(this)
        val dataManager = ServiceManager.getDataManager()
        val callback = object : Callback<SubmitExamResponse> {
            override fun onResponse(call: Call<SubmitExamResponse>, response: Response<SubmitExamResponse>) {
                if (openDialog.isShowing) {
                    openDialog.dismiss()
                }
                if (response.isSuccessful) {
                    val intent = Intent(this@QuizActivity, SuccessPageActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("QuizActivity", "Failed to submit exam: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SubmitExamResponse>, t: Throwable) {
                if (openDialog.isShowing) {
                    openDialog.dismiss()
                }
                Log.e("QuizActivity", "Error submitting exam: ${t.message}")
            }
        }
        dataManager.submitExam(callback, request)
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }

    private lateinit var openDialog: ProgressDialog

    private fun getExamQuestions() {
        try {
            openDialog = Utils.openDialog(this@QuizActivity)
            val dataManager = ServiceManager.getDataManager()
            val otpCallback = object : Callback<QuestionMinRes> {
                override fun onResponse(call: Call<QuestionMinRes>, response: Response<QuestionMinRes>) {
                    if (openDialog.isShowing) {
                        openDialog.dismiss()
                    }
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.status == true) {
                            duration_left=body?.duration_left
                            questions.clear()
                            questions.addAll(body.data)
                            loadQuestion(currentQuestionIndex)
                            startTimer()
                        }else
                        {
                            Utils.showToast(applicationContext,body!!.message!!)
                            finish()
                        }
                    } else {
                        Log.e("QuizActivity", "Failed to get test details: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<QuestionMinRes>, t: Throwable) {
                    if (openDialog.isShowing) {
                        openDialog.dismiss()
                    }
                    Log.e("QuizActivity", "Error: ${t.message}")
                }
            }
            dataManager.getExamQuestions(otpCallback, test_id)
        } catch (e: Exception) {
            if (::openDialog.isInitialized && openDialog.isShowing) {
                openDialog.dismiss()
            }
            Log.e("QuizActivity", e.message.toString())
        }
    }
}
