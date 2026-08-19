package com.example.quiztech.wallet

import android.app.Activity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quiztech.R
import com.razorpay.Checkout
import com.razorpay.PayloadHelper
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONArray
import org.json.JSONObject

class PaymentScreenActivity : AppCompatActivity(), PaymentResultWithDataListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val android_id: String? = Settings.Secure.getString(
            getContentResolver(),
            Settings.Secure.ANDROID_ID
        )

        Checkout.preload(getApplicationContext());
        val checkout= Checkout()
        checkout.setKeyID("rzp_test_RGa8N3jMtdXGHG");
        callPayment(this,"5000","TX232323","tokemnna","2",1)
    }

    fun setHelper()
    {
        val payloadHelper = PayloadHelper("<currency>", 100, "order_XXXXXXXXX")
        payloadHelper.name = "<name>"
        payloadHelper.description = "Description"
        payloadHelper.prefillEmail = "<email>"
        payloadHelper.prefillContact = "<phone>"
        payloadHelper.prefillCardNum = "<cardNumber>"
        payloadHelper.prefillCardCvv = "111"
        payloadHelper.prefillCardExp = "11/30"
        payloadHelper.prefillMethod = "card"
        payloadHelper.prefillName = "MerchantName"
        payloadHelper.sendSmsHash = true
        payloadHelper.retryMaxCount = 4
        payloadHelper.retryEnabled = true
        payloadHelper.color = "#000000"
        payloadHelper.allowRotation = true
        payloadHelper.rememberCustomer = true
        payloadHelper.timeout = 10
        payloadHelper.redirect = true
        payloadHelper.recurring = "1"
        payloadHelper.subscriptionCardChange = true
        payloadHelper.customerId = "cust_XXXXXXXXXX"
        payloadHelper.callbackUrl = "https://accepts-posts.request"
        payloadHelper.subscriptionId = "sub_XXXXXXXXXX"
        payloadHelper.modalConfirmClose = true
        payloadHelper.backDropColor = "#ffffff"
        payloadHelper.hideTopBar = true
        payloadHelper.notes = JSONObject("{\"remarks\":\"Discount to customer\"}")
        payloadHelper.readOnlyEmail = true
        payloadHelper.readOnlyContact = true
        payloadHelper.readOnlyName = true
        payloadHelper.image = "https://www.razorpay.com"
        // these values are set mandatorily during object initialization. Those values can be overridden like this
        payloadHelper.amount=100
        payloadHelper.currency="<currency>"
        payloadHelper.orderId = "order_XXXXXXXXXXXXXX"

        startPayment()
    }


    var currentOrderId: String = ""
    fun callPayment(
        activity: Activity,
        amount: String?,
        transaction_id: String?,
        token_id: String?,
        user_id: String?,
        payment_type: Int
    ) {
        try {
            val reqJson = JSONObject()
            val jsonFeatures = JSONObject()
            jsonFeatures.put("enableExpressPay", true)
            jsonFeatures.put("enableInstrumentDeRegistration", true)
            jsonFeatures.put("enableAbortResponse", true)
            jsonFeatures.put("enableMerTxnDetails", true)
            reqJson.put("features", jsonFeatures)

            val jsonConsumerData = JSONObject()
            jsonConsumerData.put(
                "deviceId",
                "AndroidSH1"
            ) //possible values "AndroidSH1" or "AndroidSH2"
            jsonConsumerData.put("token", token_id)
            jsonConsumerData.put("paymentMode", "all")
            jsonConsumerData.put(
                "merchantLogoUrl",
                "https://www.paynimo.com/CompanyDocs/company-logo-vertical.png"
            )
            if (payment_type == 0) jsonConsumerData.put(
                "merchantId",
                activity.getString(R.string.marchant_code)
            )
            else if (payment_type == 2) jsonConsumerData.put(
                "merchantId",
                activity.getString(R.string.marchant_code)
            )
            jsonConsumerData.put("currency", "INR")
            jsonConsumerData.put("consumerId", "" + user_id)
            jsonConsumerData.put("txnId", transaction_id)
            val jArrayItems = JSONArray()
            val jsonItem1 = JSONObject()
            jsonItem1.put("itemId", "first")
            jsonItem1.put("amount", amount)
            jsonItem1.put("comAmt", "0")
            // jsonItem1.put("consumerMobileNo", "0");
            //jsonItem1.put("consumerEmailId", "0");
            jArrayItems.put(jsonItem1)
            jsonConsumerData.put("items", jArrayItems)
            val jsonCustomStyle = JSONObject()
            jsonCustomStyle.put("PRIMARY_COLOR_CODE", "#45beaa")
            jsonCustomStyle.put("SECONDARY_COLOR_CODE", "#ffffff")
            jsonCustomStyle.put("BUTTON_COLOR_CODE_1", "#2d8c8c")
            jsonCustomStyle.put("BUTTON_COLOR_CODE_2", "#ffffff")
            jsonConsumerData.put("customStyle", jsonCustomStyle)

            reqJson.put("consumerData", jsonConsumerData)

            Log.e("Payment Process", "Payment Process " + reqJson.toString())
            Checkout().open(activity, reqJson)
            // WLCheckoutActivity.open(activity, reqJson);
        } catch (e: java.lang.Exception) {
            println(e.toString())
            Log.e("Exception calling", "Exception calling " + e.message)
        }
    }

    private fun startPayment() {
        /*
        *  You need to pass the current activity to let Razorpay create CheckoutActivity
        * */
        val activity: Activity = this
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name","Razorpay Corp")
            options.put("description","Demoing Charges")
            //You can omit the image option to fetch the image from the Dashboard
            // options.put("image","http://example.com/image/rzp.jpg")
            options.put("theme.color", "#3399cc");
            options.put("currency","<currency>");
            options.put("order_id", "order_DBJOWzybf0sJbb");
            options.put("amount","50000")//pass amount in currency subunits

            val retryObj =  JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            val prefill = JSONObject()
            //prefill.put("email","<email>")
            // prefill.put("contact","<phone>")

            // options.put("prefill",prefill)
            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }



    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {


    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {

    }

}