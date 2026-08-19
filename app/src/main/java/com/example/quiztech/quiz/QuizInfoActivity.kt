package com.example.quiztech.quiz

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quiztech.BaseActivity
import com.example.quiztech.R
import com.example.quiztech.databinding.ActivityLayoutQuizinfoBinding
import com.example.quiztech.databinding.ItemRankPrizeBinding
import com.example.quiztech.exam.RankData
import com.example.quiztech.exam.TestDetailsMain
import com.example.quiztech.model.MainResponse
import com.example.quiztech.services.ServiceManager
import com.prvt.sreezzyuser.common.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizInfoActivity :
    BaseActivity<ActivityLayoutQuizinfoBinding>(ActivityLayoutQuizinfoBinding::inflate) {


    var test_id = ""
    var user_id = ""
    var left_count = 0
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        user_id = Utils.getData(this@QuizInfoActivity, "user_id", "").toString()
        test_id = intent.getStringExtra("test_id").toString()

        setupViews()

        loadQuizInfoData()
        //getSubscriptionPlans()
        //getExamQuestions()
    }


    private fun setupViews() {

        // Header back
        binding.layoutHeader.imgBack.setOnClickListener {
            finish()
        }

        // Bottom back
        binding.backButton.setOnClickListener {
            finish()
        }

        // Swipe refresh
        binding.swipeRefresh.setOnRefreshListener {

            loadQuizInfoData()

            binding.swipeRefresh.postDelayed({
                if (!isFinishing && !isDestroyed) {
                    binding.swipeRefresh.isRefreshing = false
                }
            }, 700)
        }
    }

    private fun loadQuizInfoData() {
        getTestDetails()
    }

    private lateinit var openDialog: ProgressDialog

    private fun getTestDetails() {
        try {
            openDialog = Utils.openDialog(this@QuizInfoActivity)
            val dataManager = ServiceManager.Companion.getDataManager()
            val otpCallback = object : Callback<TestDetailsMain> {
                override fun onResponse(
                    call: Call<TestDetailsMain>,
                    response: Response<TestDetailsMain>
                ) {
                    Log.e("response", "response MockList ${response.body().toString()}")
                    if (openDialog.isShowing) {
                        openDialog.dismiss()
                    }
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.status == 1) {
                            val data = body.mockData
                            binding.layoutHeader.txtHeader.text = "Summary"

                            binding.txtTitle.text = "${data!!.title}"
                            binding.txtShort.text = "${data.shortDescriptions}"
                            binding.txtSub.text = "${data.subCategoryName}"

                            if (data.testType?.contains("Free", ignoreCase = true) == true) {
                                binding.tvTag.text = "Free"
                                binding.tvTag.setBackgroundResource(R.drawable.bg_tag_free)
                                binding.tvTag.visibility = View.VISIBLE
                            } else if (data.testType?.contains("Paid", ignoreCase = true) == true) {
                                binding.tvTag.text = "Paid"
                                binding.tvTag.setBackgroundResource(R.drawable.bg_tag_paid)
                                binding.tvTag.visibility = View.VISIBLE
                            } else {
                                binding.tvTag.visibility = View.GONE
                            }

                            binding.textTestType.text = "Test Type: ${data.testType ?: "Free Test"}"
                            binding.textQuestionsCount.text = "Questions Count: ${data.questions}"
                            binding.textMaxMarks.text = "Max Marks: ${data.marks}"
                            binding.textTime.text = "Time: ${data.pDuration} Minutes"
                            binding.textMaxMembers.text = data.maxMembersList ?: "0"
                            binding.textLeftCount.text = data.left_count ?: "0"
                            binding.textRewards.text = data.rewards ?: "0"
                            left_count = data.left_count?.toInt() ?: 0
                            binding.txtDateTime.text = "${data.pDate} : ${data.pTime} "
                            binding.webDescription.loadData(
                                "${data.descriptions}",
                                "text/html",
                                "UTF-8"
                            )


                            binding.lnrRankPrizes.removeAllViews()

                            body.rankData.forEach { rankData ->

                                val itemBinding = ItemRankPrizeBinding.inflate(
                                    layoutInflater,
                                    binding.lnrRankPrizes,
                                    false
                                )

                                itemBinding.tvRankValue.text = rankData.rank
                                itemBinding.tvPrizeValue.text = rankData.prize

                                binding.lnrRankPrizes.addView(itemBinding.root)
                            }


                            if (data.isEnrolled == 0) {
                                binding.startButton.text = "Enroll Now"
                                binding.startButton.setOnClickListener {
                                    if (left_count == 0) {
                                        Utils.showToast(
                                            applicationContext,
                                            "Enrollment limit has been reached"
                                        )
                                        return@setOnClickListener
                                    }
                                    enrollNow()
                                }
                            } else {
                                if (data.timeDifferenceMinutes ?: 0 < 1) {
                                    binding.startButton.text = "Start Now"
                                    binding.startButton.isEnabled = true
                                    binding.startButton.setOnClickListener {
                                        val intent = Intent(
                                            applicationContext,
                                            QuizActivity::class.java
                                        ).apply {
                                            putExtra("test_id", test_id)
                                            putExtra("user_id", user_id)

                                        }
                                        startActivity(intent)
                                        finish()
                                    }
                                } else {
                                    startCountdown(data.timeDifferenceMinutes ?: 0)
                                }
                            }
                        } else {
                            Utils.showToast(this@QuizInfoActivity, "No Mock Tests Available")
                            finish()
                        }
                    } else {
                        Log.e("QuizInfo", "Failed to get test details: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<TestDetailsMain>, t: Throwable) {
                    if (openDialog.isShowing) {
                        openDialog.dismiss()
                    }
                    Log.e("QuizInfo", "Error: ${t.message}")
                }
            }

            dataManager.getTestDetails(otpCallback, test_id, user_id = user_id)

        } catch (e: Exception) {
            if (openDialog.isShowing) {
                openDialog.dismiss()
            }
            Log.e("QuizInfoAPI ", e.message.toString())
        }
    }

    private fun startCountdown(secondss: Int) {
        countDownTimer?.cancel()

        binding.cardTimer.visibility = View.VISIBLE

        val millisInFuture = secondss * 1000L

        countDownTimer = object : CountDownTimer(millisInFuture, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val days = (millisUntilFinished / (1000 * 60 * 60 * 24))
                val hours = (millisUntilFinished / (1000 * 60 * 60)) % 24
                val mins = (millisUntilFinished / (1000 * 60)) % 60
                val secs = (millisUntilFinished / 1000) % 60

                var timeString = ""
                if (days > 0)
                    timeString = String.format("%02d:%02d:%02d:%02d", days, hours, mins, secs)
                else
                    timeString = String.format("%02d:%02d:%02d", hours, mins, secs)

                binding.txtStartTimer.text = timeString

                // Pulsing animation
                val scaleAnim = ScaleAnimation(
                    1.0f,
                    1.1f,
                    1.0f,
                    1.1f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                )
                scaleAnim.duration = 500
                scaleAnim.repeatMode = Animation.REVERSE
                scaleAnim.repeatCount = 0
                binding.txtStartTimer.startAnimation(scaleAnim)

                binding.startButton.isEnabled = false
            }

            override fun onFinish() {
                binding.startButton.text = "Start Now"
                binding.startButton.isEnabled = true
                binding.startButton.setOnClickListener {
                    val intent = Intent(applicationContext, QuizActivity::class.java).apply {
                        putExtra("test_id", test_id)
                        putExtra("user_id", user_id)
                    }
                    startActivity(intent)
                    finish()
                }
            }
        }.start()
    }

    private fun enrollNow() {
        try {
            openDialog = Utils.openDialog(this@QuizInfoActivity)
            val dataManager = ServiceManager.Companion.getDataManager()
            val otpCallback = object : Callback<MainResponse> {
                override fun onResponse(
                    call: Call<MainResponse>,
                    response: Response<MainResponse>
                ) {
                    Log.e("response", "response MockList ${response.body().toString()}")
                    if (openDialog.isShowing) {
                        openDialog.dismiss()
                    }
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.status == 1) {
                            getTestDetails()
                        } else {
                            Utils.showToast(
                                this@QuizInfoActivity,
                                body?.message ?: "Failed to enroll"
                            )
                        }
                    } else {
                        Log.e("QuizInfo", "Failed to get test details: ${response.message()}")
                        Utils.showToast(this@QuizInfoActivity, "Error: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                    if (openDialog.isShowing) {
                        openDialog.dismiss()
                    }
                    Log.e("QuizInfo", "Error: ${t.message}")
                }
            }

            dataManager.enrollProduct(otpCallback, user_id = user_id, test_id)

        } catch (e: Exception) {
            if (openDialog.isShowing) {
                openDialog.dismiss()
            }
            Log.e("QuizInfoAPI ", e.message.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }

}

