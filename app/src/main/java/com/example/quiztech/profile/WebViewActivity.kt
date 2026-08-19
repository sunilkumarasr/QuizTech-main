package com.example.quiztech.profile

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.quiztech.databinding.ActivityWebViewBinding
import com.example.quiztech.model.FAQsMainResponse
import com.example.quiztech.model.PageResponse
import com.example.quiztech.services.ServiceManager
import com.prvt.sreezzyuser.common.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding
    private lateinit var openDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title") ?: "QuizTech"
        val pageName = intent.getStringExtra("url") ?: ""
        val isUrl = intent.getBooleanExtra("isUrl", false)

        binding.headerToolbar.txtHeader.text = title
        binding.headerToolbar.imgBack.setOnClickListener {
            onBackPressed()
        }

        if (isUrl && pageName.isNotEmpty()) {
            fetchPageContent(pageName)
        } else {
            val content = intent.getStringExtra("content") ?: ""
            loadHtmlContent(content)
        }
    }

    private fun fetchPageContent(pageName: String) {
        try {
            openDialog = Utils.openDialog(this)
            val serviceManager = ServiceManager.getDataManager()
            serviceManager.getPageList(pageName, object : Callback<PageResponse> {
                override fun onResponse(call: Call<PageResponse>, response: Response<PageResponse>) {
                    if (openDialog.isShowing) openDialog.dismiss()
                    if (response.isSuccessful && response.body()?.status == true) {
                        val data = response.body()?.data
                        if (!data.isNullOrEmpty()) {
                            loadHtmlContent(data[0].description ?: "")
                        }
                    } else {
                        Log.e("WebViewActivity", "Failed to fetch page: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<PageResponse>, t: Throwable) {
                    if (openDialog.isShowing) openDialog.dismiss()
                    Log.e("WebViewActivity", "Error fetching page", t)
                }
            })
        } catch (e: Exception) {
            if (::openDialog.isInitialized && openDialog.isShowing) openDialog.dismiss()
            Log.e("WebViewActivity", "Exception in fetchPageContent", e)
        }
    }

    private fun loadHtmlContent(content: String) {
        binding.webView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadDataWithBaseURL(null, content, "text/html", "UTF-8", null)
        }
    }

    private fun contactUs(){
        try {
            openDialog=Utils.openDialog(this@WebViewActivity)
            val dataManager = ServiceManager.getDataManager()
            val otpCallback = object : Callback<FAQsMainResponse> {
                override fun onResponse(call: Call<FAQsMainResponse>, response: Response<FAQsMainResponse>) {
                    Log.e("response","response ${response.body().toString()}")
                    if (openDialog.isShowing){
                        openDialog.dismiss()
                    }
                    if (response.isSuccessful) {
                        val body=response.body()




                    } else {
                        println("Failed to send OTP. ${response.message()}")
                        //  Utils.showToast(this@LoginActivity,"Failed to send OTP. ${response.message()}")
                    }

                }

                override fun onFailure(call: Call<FAQsMainResponse>, t: Throwable) {
                    // Handle failure
                    println("Failed to send OTP. ${t.message}")
                    if (openDialog.isShowing){
                        openDialog.dismiss()
                    }

                }
            }

            //dataManager.contactUs(otpCallback )


        }catch (e:Exception){
            if (openDialog.isShowing){
                openDialog.dismiss()
            }
            Log.e("LoginAPI ",e.message.toString())
        }
    }
}
