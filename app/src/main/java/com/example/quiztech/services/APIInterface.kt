package com.prvt.sreezzyuser.services


import com.example.quiztech.CategoryMainRes
import com.example.quiztech.exam.TestDetailsMain
import com.example.quiztech.model.BannerResponse
import com.example.quiztech.model.ContactUsMain
import com.example.quiztech.model.FAQsMainResponse
import com.example.quiztech.model.LoginResponse
import com.example.quiztech.model.MainResponse
import com.example.quiztech.model.MockListMainRes
import com.example.quiztech.model.OTPVerifyResponse
import com.example.quiztech.model.PageResponse
import com.example.quiztech.model.ResendOTPResponse
import com.example.quiztech.model.SubCategoryMainRes
import com.example.quiztech.model.SubScriptionMain
import com.example.quiztech.model.SubscriptionMainRes
import com.example.quiztech.model.TopicMainRes
import com.example.quiztech.quiz.ExamResultResponse
import com.example.quiztech.quiz.MyQuizMainRes
import com.example.quiztech.quiz.QuestionMinRes
import com.example.quiztech.quiz.ScoreCardMainRes
import com.example.quiztech.quiz.SubmitExamRequest
import com.example.quiztech.quiz.SubmitExamResponse
import com.example.quiztech.services.ServiceManager.Companion.ROOT_URL_SUB
import com.example.quiztech.wallet.TransactionDetailMain
import com.example.quiztech.wallet.TransactionsMain
import com.example.quiztech.wallet.WalletDetails
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface APIInterface {


    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "register")
    fun loginUser(
        @Field("api_key") apiKey: String,
        @Field("email") email: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "verify_otp")
    fun verifyOtp(
        @Field("api_key") apiKey: String,
        @Field("user_id") user_id: String,
        @Field("otp") otp: String,
    ): Call<OTPVerifyResponse>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "resend_otp")
    fun resendOtp(
        @Field("api_key") apiKey: String,
        @Field("user_id") user_id: String,
    ): Call<ResendOTPResponse>


    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "update_profile")
    fun updateProfile(
        @Header("Authorization") authHeader: String,
        @Field("user_id") user_id: String,
        @Field("full_name") full_name: String,
        @Field("address") address: String,
        @Field("phone") phone: String,
        @Field("gender") gender: String,
    ): Call<OTPVerifyResponse>


    @POST(ROOT_URL_SUB + "get_categories")
    fun getCategories(
        @Header("Authorization") authHeader: String,
    ): Call<CategoryMainRes>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "get_sub_categories")
    fun getSubCategories(
        @Header("Authorization") authHeader: String,
        @Field("cat_id") cat_id: String,
    ): Call<SubCategoryMainRes>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "get_topics")
    fun getTopicBySib(
        @Header("Authorization") authHeader: String,
        @Field("cat_id") cat_id: String,
        @Field("sub_cat_id") sub_cat_id: String,
    ): Call<TopicMainRes>


    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "get_sub_topics")
    fun getSubTopicBySib(
        @Header("Authorization") authHeader: String,
        @Field("topic_id") topic_id: String,
    ): Call<MainResponse>


    @POST(ROOT_URL_SUB + "get_popular_mock_list")
    fun getPopularMockTests(  @Header("Authorization") authHeader: String): Call<MockListMainRes>



    //Test Apis
    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "get_mock_list_by_category")
    fun getMockTestByCategory(  @Header("Authorization") authHeader: String,
                                @Field("category_id") category_ic: String,): Call<MockListMainRes>


    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "get_mock_list_by_sub_category")
    fun getMockTestBySubCategory(  @Header("Authorization") authHeader: String,
        @Field("sub_category_id") sub_category_ic: String): Call<MockListMainRes>



    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "get_mock_list_by_topic")
    fun getMockTestByTopic(  @Header("Authorization") authHeader: String,
                                   @Field("topic_id") topic_ic: String): Call<MockListMainRes>


    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "get_mock_list_by_sub_category")
    fun getMockTestBySubTopic(  @Header("Authorization") authHeader: String,
                                @Field("sub_topic_id") sub_topic_id: String): Call<MockListMainRes>






    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "get_test_details")
    fun getTestDetails(  @Header("Authorization") authHeader: String,
                         @Field("test_id") test_id: String,
                         @Field("user_id") user_id: String): Call<TestDetailsMain>


    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "exam_result")
    fun getExamDetails(  @Header("Authorization") authHeader: String,
                         @Field("product_id") test_id: String,
                         @Field("user_id") user_id: String): Call<ExamResultResponse>



    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "get_exam_questions")
    fun getExamQuestions(  @Header("Authorization") authHeader: String,
                         @Field("test_id") test_id: String): Call<QuestionMinRes>


    /*@POST(ROOT_URL_SUB + "get_exam_questions")
    fun submitExam(  @Header("Authorization") authHeader: String,
                           @Query("test_id") test_id: String): Call<MockListMainRes>*/


    //Subscription

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "get_subscription_plans")
    fun getSubscriptionPlans(@Query("api_key") apiKey: String,
                             @Field("user_id") user_id: String): Call<SubscriptionMainRes>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "get_user_active_subscription")
    fun getUserActiveSubscriptions(  @Header("Authorization") authHeader: String,
                                     @Field("user_id") user_id: String): Call<SubscriptionMainRes>
    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "subscribe")
    fun subscribePlan(  @Header("Authorization") authHeader: String,
                                     @Field("user_id") user_id: String,
                           @Field("plan_id") plan_id: String,  @Field("razorpay_payment_id") razorpay_payment_id: String,
                        @Field("razorpay_order_id") razorpay_order_id: String,@Field("pay_with_wallet") pay_with_wallet: String,): Call<SubScriptionMain>
 @FormUrlEncoded
    @POST(ROOT_URL_SUB + "enroll_product")
    fun enrollProduct(  @Header("Authorization") authHeader: String,
                                     @Field("user_id") user_id: String,
                           @Field("product_id") plan_id: String): Call<MainResponse>


    @POST(ROOT_URL_SUB + "submit_exam")
    fun submitExam(
        @Header("Authorization") authHeader: String,
        @Body request: SubmitExamRequest
    ): Call<SubmitExamResponse>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "get_scorecard")
    fun getScoreCard( @Header("Authorization") authHeader: String,
                      @Field("user_id") user_id: String,
                      @Field("product_id") product_id: String
    ): Call<ScoreCardMainRes>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "my_quiz")
    fun myQuiz( @Header("Authorization") authHeader: String,
                      @Field("user_id") user_id: String,
                      @Field("completed") type: String
    ): Call<MyQuizMainRes>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "wallet_details")
    fun walletDetails( @Header("Authorization") authHeader: String,
                      @Field("user_id") user_id: String

    ): Call<WalletDetails>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "transaction_detail")
    fun transactionDetail( @Header("Authorization") authHeader: String,
                      @Field("id") transaction_id: String

    ): Call<TransactionDetailMain>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "wallet_transactions_list")
    fun transactionLIst( @Header("Authorization") authHeader: String,
                      @Field("user_id") user_id: String

    ): Call<TransactionsMain>


    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "add_money")
    fun addMoney( @Header("Authorization") authHeader: String,
                      @Field("user_id") user_id: String,
                      @Field("amount") amount: String,
                      @Field("razorpay_payment_id") razorpay_payment_id: String,
                      @Field("razorpay_order_id") razorpay_order_id: String,


    ): Call<WalletDetails>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "withdraw")
    fun withdraw( @Header("Authorization") authHeader: String,
                      @Field("user_id") user_id: String,
                      @Field("amount") amount: String,
                      @Field("bank_name") bank_name: String,
                      @Field("account_number") account_number: String,
                      @Field("ifsc_code") ifsc_code: String,
                      @Field("upi_id") upi_id: String,


    ): Call<WalletDetails>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "faqs_list")
    fun faqs(@Header("Authorization") authHeader: String,@Field("api_key") apiKey: String): Call<FAQsMainResponse>


    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "contact_details")
    fun contactDetails(@Header("Authorization") authHeader: String,@Field("api_key") apiKey: String): Call<ContactUsMain>

    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "enquiry_form")
    fun enquireForm(@Header("Authorization") authHeader: String,
        @Field("api_key") apiKey: String,
                    @Field("name") name: String,
                    @Field("phone") phone: String,
                    @Field("subject") subject: String,
                    @Field("message") message: String,
                    @Field("user_id") user_id: String,
    ): Call<FAQsMainResponse>



    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "pages_list")
    fun getPageList(
        @Field("api_key") apiKey: String,
        @Field("page_name") pageName: String
    ): Call<PageResponse>
    @FormUrlEncoded
    @POST(ROOT_URL_SUB + "banners_list")
    fun getBanners(
        @Field("api_key") apiKey: String,
    ): Call<BannerResponse>
}
