package com.example.quiztech.home

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quiztech.Category
import com.example.quiztech.CategoryAdapter
import com.example.quiztech.CategoryMainRes
import com.example.quiztech.MockTest
import com.example.quiztech.quiz.MockTestAdapter
import com.example.quiztech.quiz.QuizInfoActivity
import com.example.quiztech.R
import com.example.quiztech.categories.AllCategoriesActivity
import com.example.quiztech.databinding.FragmentHomeBinding
import com.example.quiztech.enroll.EnrollPaymentPage
import com.example.quiztech.exam.ExamListActivity
import com.example.quiztech.model.MockListMainRes
import com.example.quiztech.services.ServiceManager
import com.example.quiztech.subcategories.SubCategoryActivity
import com.prvt.sreezzyuser.common.Utils
import okhttp3.internal.notify
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var mockTestAdapter: MockTestAdapter
    private lateinit var bannerAdapter: BannerAdapter


    var sampleCategories = ArrayList<Category>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        setupCategoryRecyclerView()
        setupMockTestRecyclerView()


        binding.swipeRefresh.setOnRefreshListener {

            loadHomeData()

            binding.swipeRefresh.postDelayed({
                binding.swipeRefresh.isRefreshing = false
            }, 500)
        }

        loadHomeData()

    }

    private fun loadHomeData() {
        getBanners()
        getCategories()
        gtePopularMockTest()
    }

    private fun setupClickListeners() {
        //binding.tvCategoriesViewAll.visibility=View.GONE
        binding.tvMockTestViewAll.visibility=View.GONE
        binding.tvCategoriesViewAll.setOnClickListener {
            // In Clean Architecture, this would typically navigate via a ViewModel or a Navigator interface
            startActivity(Intent(requireContext(), AllCategoriesActivity::class.java))
        }
        binding.tvMockTestViewAll.setOnClickListener {
            startActivity(Intent(requireContext(), ExamListActivity::class.java))
        }
        binding.ivWhatsapp.setOnClickListener {
            // Placeholder for Whatsapp click
            Toast.makeText(requireContext(), "Whatsapp Clicked", Toast.LENGTH_SHORT).show()
        }
        binding.ivNotifications.setOnClickListener {
            // Placeholder for Notifications click
            Toast.makeText(requireContext(), "Notifications Clicked", Toast.LENGTH_SHORT).show()
        }
        binding.ivLogo.setOnClickListener {
            // Placeholder for Logo click
        }
        binding.ivPrevMock.setOnClickListener {
            val current = binding.rvMockTests.currentItem
            if (current > 0) {
                binding.rvMockTests.setCurrentItem(current - 1, true)
            }
        }
        binding.ivNextMock.setOnClickListener {
            val current = binding.rvMockTests.currentItem
            val total = binding.rvMockTests.adapter?.itemCount ?: 0
            if (current < total - 1) {
                binding.rvMockTests.setCurrentItem(current + 1, true)
            }
        }
       /* binding.ivBanner.setOnClickListener {
            Toast.makeText(requireContext(), "Banner clicked", Toast.LENGTH_SHORT).show()
        }*/
        binding.ivSecondBanner.setOnClickListener {
            Toast.makeText(requireContext(), "Second Banner clicked", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setupCategoryRecyclerView() {
        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        // Sample data - In Clean Architecture, this would come from a ViewModel, which gets it from a UseCase

        categoryAdapter = CategoryAdapter(sampleCategories) { category ->
            var intent= Intent(requireContext(), SubCategoryActivity::class.java).apply {
                putExtra("cat_id",category.id)
            }


            startActivity(intent)
        }
        binding.rvCategories.adapter = categoryAdapter
    }

    private fun setupMockTestRecyclerView() {
       // binding.rvMockTests.layoutManager = LinearLayoutManager(requireContext(),or)
        // Sample data - In Clean Architecture, this would come from a ViewModel
        val sampleMockTests = ArrayList<MockTest>()
        mockTestAdapter = MockTestAdapter(sampleMockTests) { mockTest ->
            val intent = Intent(requireContext(), QuizInfoActivity::class.java).apply {
                putExtra("test_id", mockTest.testId)
            }
            startActivity(intent)
        }
        binding.rvMockTests.adapter = mockTestAdapter
    }

    private fun getBanners() {
        try {
            val dataManager = ServiceManager.getDataManager()
            val bannerCallback = object : Callback<com.example.quiztech.model.BannerResponse> {
                override fun onResponse(call: Call<com.example.quiztech.model.BannerResponse>, response: Response<com.example.quiztech.model.BannerResponse>) {
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        if (body.status == 1) {
                            if (_binding != null) {
                                if (body.data.isNotEmpty()) {
                                    binding.ivDefaultBanner.visibility = View.GONE
                                    binding.viewPagerBanners.visibility = View.VISIBLE
                                    binding.dotsIndicator.visibility = View.VISIBLE
                                    
                                    bannerAdapter = BannerAdapter(body.data)
                                    binding.viewPagerBanners.adapter = bannerAdapter
                                    binding.dotsIndicator.attachTo(binding.viewPagerBanners)
                                } else {
                                    binding.ivDefaultBanner.visibility = View.VISIBLE
                                    binding.viewPagerBanners.visibility = View.GONE
                                    binding.dotsIndicator.visibility = View.GONE
                                }
                            }
                        } else {
                            showDefaultBanner()
                        }
                    } else {
                        showDefaultBanner()
                    }
                }

                override fun onFailure(call: Call<com.example.quiztech.model.BannerResponse>, t: Throwable) {
                    Log.e("getBanners", "Failed: ${t.message}")
                    showDefaultBanner()
                }

                private fun showDefaultBanner() {
                    if (_binding != null) {
                        binding.ivDefaultBanner.visibility = View.VISIBLE
                        binding.viewPagerBanners.visibility = View.GONE
                        binding.dotsIndicator.visibility = View.GONE
                    }
                }
            }
            dataManager.getBanners(bannerCallback)
        } catch (e: Exception) {
            Log.e("getBanners", e.message.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private lateinit var openDialog: ProgressDialog
    private fun getCategories() {
        try {
            openDialog = Utils.openDialog(requireActivity())
            val dataManager = ServiceManager.getDataManager()
            val otpCallback = object : Callback<CategoryMainRes> {
                override fun onResponse(call: Call<CategoryMainRes>, response: Response<CategoryMainRes>) {
                    val body = response.body()
                    Log.e("response", "response $body")
                    if (::openDialog.isInitialized && openDialog.isShowing) {
                        openDialog.dismiss()
                    }
                    if (response.isSuccessful && body != null) {
                        if (body.status == 1) {
                            if (_binding != null) {
                                categoryAdapter.addCategories(body.categories)
                            }
                        } else {
                            println("Failed to get categories. ${response.message()}")
                        }
                    } else {
                        println("Failed to get categories. ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<CategoryMainRes>, t: Throwable) {
                    println("Failed to get categories. ${t.message}")
                    if (::openDialog.isInitialized && openDialog.isShowing) {
                        openDialog.dismiss()
                    }
                }
            }

            dataManager.getCategories(otpCallback)

        } catch (e: Exception) {
            if (::openDialog.isInitialized && openDialog.isShowing) {
                openDialog.dismiss()
            }
            Log.e("getCategories", e.message.toString())
        }
    }

    private fun gtePopularMockTest() {
        try {
            val dataManager = ServiceManager.getDataManager()
            val otpCallback = object : Callback<MockListMainRes> {
                override fun onResponse(call: Call<MockListMainRes>, response: Response<MockListMainRes>) {
                    val body = response.body()
                    Log.e("response", "response $body")
                    if (::openDialog.isInitialized && openDialog.isShowing) {
                        openDialog.dismiss()
                    }
                    if (response.isSuccessful && body != null) {
                        if (body.status == 1) {
                            mockTestAdapter = MockTestAdapter(body.mockList) { mockTest ->
                                if (isAdded) {
                                    val intent = Intent(requireContext(), QuizInfoActivity::class.java).apply {
                                        putExtra("test_id", mockTest.testId)
                                    }
                                    startActivity(intent)
                                }
                            }
                            _binding?.rvMockTests?.adapter = mockTestAdapter
                            _binding?.dotsIndicatorMock?.attachTo(binding.rvMockTests)
                        } else {
                            println("Failed to get popular mock tests. ${response.message()}")
                        }
                    } else {
                        println("Failed to get popular mock tests. ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<MockListMainRes>, t: Throwable) {
                    println("Failed to get popular mock tests. ${t.message}")
                    if (::openDialog.isInitialized && openDialog.isShowing) {
                        openDialog.dismiss()
                    }
                }
            }

            dataManager.getPopularMockTests(otpCallback)

        } catch (e: Exception) {
            if (::openDialog.isInitialized && openDialog.isShowing) {
                openDialog.dismiss()
            }
            Log.e("gtePopularMockTest", e.message.toString())
        }
    }
}