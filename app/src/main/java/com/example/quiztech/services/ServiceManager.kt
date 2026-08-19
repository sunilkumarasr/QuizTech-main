package com.example.quiztech.services

import com.example.quiztech.CategoryMainRes
import com.example.quiztech.exam.TestDetailsMain
import com.example.quiztech.model.ContactUsMain
import com.example.quiztech.model.FAQsMainResponse
import com.example.quiztech.model.LoginResponse
import com.example.quiztech.model.MainResponse
import com.example.quiztech.model.MockListMainRes
import com.example.quiztech.model.OTPVerifyResponse
import com.example.quiztech.model.ResendOTPResponse
import com.example.quiztech.model.SubCategoryMainRes
import com.example.quiztech.model.SubscriptionMainRes
import com.example.quiztech.model.TopicMainRes
import com.example.quiztech.model.PageResponse
import com.example.quiztech.model.SubScriptionMain
import com.example.quiztech.quiz.ExamResultResponse
import com.example.quiztech.quiz.MyQuizMainRes
import com.example.quiztech.quiz.QuestionMinRes
import com.example.quiztech.quiz.ScoreCardMainRes
import com.example.quiztech.quiz.SubmitExamRequest
import com.example.quiztech.quiz.SubmitExamResponse
import com.example.quiztech.wallet.TransactionDetailMain
import com.example.quiztech.wallet.TransactionsMain
import com.example.quiztech.wallet.WalletDetails
import com.google.gson.GsonBuilder
import com.prvt.sreezzyuser.common.Utils

import com.prvt.sreezzyuser.services.APIInterface
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import java.io.File
import java.util.concurrent.TimeUnit

class ServiceManager {
    val ROOT_URL = "http://quiztech.in/"

    companion object {
        private var dataManager: ServiceManager? = null
        const val  ROOT_URL_SUB = "api/"
        private val apiKey="the_quiz_company_7s736V2J2iB549214s40i3Lz77I0297L"
        const val SUB_ROOT_URL = ""
        @JvmStatic
        fun getDataManager(): ServiceManager {
            if (dataManager == null) {
                dataManager = ServiceManager()
            }
            return dataManager as ServiceManager
        }
    }

    private val retrofit: Retrofit

    init {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.callTimeout(5, TimeUnit.MINUTES)
        httpClient.readTimeout(5, TimeUnit.MINUTES)
        httpClient.addInterceptor(logging)
        val gson = GsonBuilder()
            .setLenient()
            .create()

        retrofit = Retrofit.Builder().baseUrl(ROOT_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
    }


    fun loginUser(cb: Callback<LoginResponse>, email:String){
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.loginUser(apiKey,email)
        call.enqueue(cb)
    }

    fun verifyOTP(cb: Callback<OTPVerifyResponse>, user_id:String, otp:String){
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.verifyOtp(apiKey,user_id,otp)
        call.enqueue(cb)
    }
    fun resendOTP(cb: Callback<ResendOTPResponse>, user_id:String){
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.resendOtp(apiKey,user_id)
        call.enqueue(cb)
    }
    fun updateProfile(cb: Callback<OTPVerifyResponse>,user_id:String,fullname:String, address:String, phone:String, gender:String){
    val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.updateProfile("Bearer $token", user_id,fullname,address,phone,gender)
        call.enqueue(cb)
    }

    fun getCategories(cb: Callback<CategoryMainRes>){
    val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getCategories("Bearer $token")
        call.enqueue(cb)
    }
    fun getSubCategories(cb: Callback<SubCategoryMainRes>, cat_id: String){
    val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getSubCategories("Bearer $token",cat_id)
        call.enqueue(cb,)
    }

    fun getTopic(cb: Callback<TopicMainRes>,cat_id: String,sub_cat: String){
    val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getTopicBySib("Bearer $token",cat_id,sub_cat)
        call.enqueue(cb,)
    }

    fun getSubTopic(cb: Callback<MainResponse>,topic_id: String){
    val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getSubTopicBySib("Bearer $token",topic_id)
        call.enqueue(cb,)
    }

    fun getPopularMockTests(cb: Callback<MockListMainRes>){
    val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getPopularMockTests("Bearer $token")
        call.enqueue(cb,)
    }
    fun getMockTestByCategory(cb: Callback<MockListMainRes>,cat_id:String){
    val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getMockTestByCategory("Bearer $token",cat_id)
        call.enqueue(cb,)
    }

    fun getMockTestBySubCategory(cb: Callback<MockListMainRes>,sub_cat_id:String){
        val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getMockTestBySubCategory("Bearer $token",sub_cat_id)
        call.enqueue(cb,)
    }
    fun getMockTestByTopic(cb: Callback<MockListMainRes>,topic_id:String){
        val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getMockTestByTopic("Bearer $token",topic_id)
        call.enqueue(cb,)
    }
    fun getMockTestBySubTopic(cb: Callback<MockListMainRes>,sub_topic_id:String){
        val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getMockTestBySubTopic("Bearer $token",sub_topic_id)
        call.enqueue(cb,)
    }
    fun getTestDetails(cb: Callback<TestDetailsMain>, test_id:String, user_id:String){
        val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getTestDetails("Bearer $token",test_id,user_id)
        call.enqueue(cb,)
    }
    fun getExamDetails(cb: Callback<ExamResultResponse>, product_id:String, user_id:String){
        val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getExamDetails("Bearer $token",product_id,user_id)
        call.enqueue(cb,)
    }

    fun getSubscriptionPlans(cb: Callback<SubscriptionMainRes>, user_id: String){
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getSubscriptionPlans(apiKey,user_id)
        call.enqueue(cb)
    }
    fun getUserActiveSubscriptions(cb: Callback<SubscriptionMainRes>,user_id: String){
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getUserActiveSubscriptions(apiKey,user_id)
        call.enqueue(cb)
    }
    fun subscribePlan(cb: Callback<SubScriptionMain>, user_id: String, plan_id: String, razorpay_payment_id: String, razorpay_order_id: String, pay_with_wallet: String,){
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.subscribePlan(apiKey,user_id, plan_id , razorpay_payment_id, razorpay_order_id,pay_with_wallet )
        call.enqueue(cb)
    }
    fun enrollProduct(cb: Callback<MainResponse>,user_id: String,product_id: String){
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.enrollProduct(apiKey,user_id, product_id  )
        call.enqueue(cb)
    }

    fun getExamQuestions(cb: Callback<QuestionMinRes>, test_id:String){
        val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getExamQuestions("Bearer $token",test_id)
        call.enqueue(cb,)
    }

    fun submitExam(cb: Callback<SubmitExamResponse>, request: SubmitExamRequest) {
        val token = Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.submitExam("Bearer $token", request)
        call.enqueue(cb)
    }


    fun getScoreCard(cb: Callback<ScoreCardMainRes>,user_id: String,product_id: String){
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getScoreCard(apiKey,user_id, product_id  )
        call.enqueue(cb)
    }
    fun myQuiz(cb: Callback<MyQuizMainRes>, user_id: String, type: String){
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.myQuiz(apiKey,user_id, type  )
        call.enqueue(cb)
    }
    fun walletDetails(cb: Callback<WalletDetails>, user_id: String, ){
        val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.walletDetails("Bearer $token",user_id  )
        call.enqueue(cb)
    }
    fun transactionDetail(cb: Callback<TransactionDetailMain>, transaction_id: String, ){
        val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.transactionDetail("Bearer $token",transaction_id  )
        call.enqueue(cb)
    }
    fun transactionLIst(cb: Callback<TransactionsMain>, user_id: String, ){
        val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.transactionLIst("Bearer $token",user_id  )
        call.enqueue(cb)
    }
    fun addMoney(cb: Callback<WalletDetails>, user_id: String, amount: String,
                 razorpay_payment_id: String, razorpay_order_id: String,){
        val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.addMoney("Bearer $token",  user_id, amount ,razorpay_payment_id,razorpay_order_id)
        call.enqueue(cb)
    }
    fun withdraw(cb: Callback<WalletDetails>, user_id: String, amount: String,
                 bank_name: String, account_number: String, ifsc_code: String, upi_id: String,){
        val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.withdraw("Bearer $token",  user_id, amount ,bank_name,account_number,ifsc_code,upi_id)
        call.enqueue(cb)
    }
    fun enquiryForm(cb: Callback<FAQsMainResponse>,name:String,phone:String
                            ,subject:String,message:String,user_id:String){
    val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.enquireForm("Bearer $token",apiKey,name,phone,subject,message,user_id)
        call.enqueue(cb,)
    }


    fun faqs(cb: Callback<FAQsMainResponse>){
        val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.faqs("Bearer $token",apiKey)
        call.enqueue(cb,)
    }
    fun contactUs(cb: Callback<ContactUsMain>){
        val token= Utils.access_token
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.contactDetails("Bearer $token",apiKey)
        call.enqueue(cb,)
    }

    fun getPageList(pageName: String, cb: Callback<PageResponse>) {
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getPageList(apiKey, pageName)
        call.enqueue(cb)
    }

    fun getBanners(cb: Callback<com.example.quiztech.model.BannerResponse>) {
        val apiService = retrofit.create(APIInterface::class.java)
        val call = apiService.getBanners(apiKey)
        call.enqueue(cb)
    }

    fun File.toImageRequestBody(partName: String): MultipartBody.Part {
        val mimeType = when (this.extension.lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "gif" -> "image/gif"
            else -> "image/*"
        }

        val requestFile = this.asRequestBody(mimeType.toMediaTypeOrNull())

        return MultipartBody.Part.createFormData(partName, this.name, requestFile)
    }

}
