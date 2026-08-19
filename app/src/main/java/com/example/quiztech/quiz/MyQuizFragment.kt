package com.example.quiztech.quiz

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quiztech.databinding.FragmentMyQuizBinding
import com.example.quiztech.services.ServiceManager
import com.google.gson.annotations.SerializedName
import com.prvt.sreezzyuser.common.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class MyQuizMainRes(
    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<MyQuiz> = arrayListOf()
)

data class MyQuiz(
    @SerializedName("id") var id: String? = null,
    @SerializedName("user_id") var userId: String? = null,
    @SerializedName("product_id") var productId: String? = null,
    @SerializedName("enrolled_at") var enrolledAt: String? = null,
    @SerializedName("completed_at") var completedAt: String? = null,
    @SerializedName("subscription_plan_id") var subscriptionPlanId: String? = null,
    @SerializedName("available_for") var availableFor: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("p_date") var pDate: String? = null,
    @SerializedName("p_time") var pTime: String? = null,
    @SerializedName("p_duration") var pDuration: String? = null,
    @SerializedName("is_completed") var isCompleted: Int? = null,
    @SerializedName("test_type") var test_type: String? = null
)

class MyQuizFragment : Fragment() {

    private var _binding: FragmentMyQuizBinding? = null
    private val binding get() = _binding!!
    private lateinit var myQuizAdapter: MyQuizAdapter
    private var type = "0"
    private var user_id = ""
    private lateinit var openDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user_id = Utils.getData(requireActivity(), "user_id", "").toString()

        setupRecyclerView()

        binding.rg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.btnNewQuiz.id -> type = "0"
                binding.btnCompletedQuiz.id -> type = "1"
            }
            getQuiz()
        }

        getQuiz()
    }

    private fun setupRecyclerView() {
        myQuizAdapter = MyQuizAdapter(ArrayList()) { myQuiz ->
            if (type == "1") {
                val intent = Intent(requireActivity(), ExamResultScreenActivity::class.java).apply {
                    putExtra("test_id", myQuiz.productId)
                    putExtra("user_id", user_id)
                }
                startActivity(intent)
            } else {
                val intent = Intent(requireActivity(), QuizInfoActivity::class.java).apply {
                    putExtra("test_id", myQuiz.productId)
                }
                startActivity(intent)
            }
        }
        binding.rvQuiz.layoutManager = LinearLayoutManager(requireContext())
        binding.rvQuiz.adapter = myQuizAdapter
    }

    private fun getQuiz() {
        try {
            openDialog = Utils.openDialog(requireActivity())
            val dataManager = ServiceManager.getDataManager()
            val otpCallback = object : Callback<MyQuizMainRes> {
                override fun onResponse(call: Call<MyQuizMainRes>, response: Response<MyQuizMainRes>) {
                    if (openDialog.isShowing) openDialog.dismiss()
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.data != null && body.data.isNotEmpty()) {
                            binding.rvQuiz.visibility = View.VISIBLE
                            binding.tvNoData.visibility = View.GONE
                            myQuizAdapter.addTest(body.data)
                        } else {
                            showNoData()
                        }
                    } else {
                        showNoData()
                    }
                }

                override fun onFailure(call: Call<MyQuizMainRes>, t: Throwable) {
                    if (openDialog.isShowing) openDialog.dismiss()
                    showNoData()
                }
            }
            dataManager.myQuiz(otpCallback, user_id, type)
        } catch (e: Exception) {
            if (::openDialog.isInitialized && openDialog.isShowing) openDialog.dismiss()
            showNoData()
        }
    }

    private fun showNoData() {
        myQuizAdapter.addTest(ArrayList())
        binding.rvQuiz.visibility = View.GONE
        binding.tvNoData.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
