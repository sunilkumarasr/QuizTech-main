package com.example.quiztech.wallet

import com.google.gson.annotations.SerializedName

data class Transactions(
    @SerializedName("id"                  ) var id                : String? = null,
    @SerializedName("user_id"             ) var userId            : String? = null,
    @SerializedName("product_id"          ) var productId         : String? = null,
    @SerializedName("amount"              ) var amount            : String? = null,
    @SerializedName("razorpay_payment_id" ) var razorpayPaymentId : String? = null,
    @SerializedName("razorpay_order_id"   ) var razorpayOrderId   : String? = null,
    @SerializedName("type"                ) var type              : String? = null,
    @SerializedName("status"              ) var status            : String? = null,
    @SerializedName("bank_name"           ) var bankName          : String? = null,
    @SerializedName("account_number"      ) var accountNumber     : String? = null,
    @SerializedName("ifsc_code"           ) var ifscCode          : String? = null,
    @SerializedName("upi_id"              ) var upiId             : String? = null,
    @SerializedName("created_at"          ) var createdAt         : String? = null

)

data class TransactionsMain (

    @SerializedName("status"        ) var status       : Int?            = null,
    @SerializedName("message"       ) var message      : String?         = null,
    @SerializedName("total_records" ) var totalRecords : Int?            = null,
    @SerializedName("data"          ) var data         : ArrayList<Transactions> = arrayListOf()

)
data class TransactionDetailMain(

    @SerializedName("status") var status: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("total_records") var totalRecords: Int? = null,
    @SerializedName("data") var data: Transactions? = null

)
data class WalletDetails (


    @SerializedName("status"              ) var status             : Int?                          = null,
    @SerializedName("message"             ) var message            : String?                       = null,
    @SerializedName("wallet_balance"      ) var walletBalance      : Int?                          = null,
    @SerializedName("total_credit"        ) var totalCredit        : Int?                          = null,
    @SerializedName("total_withdraw"      ) var totalWithdraw      : Int?                          = null,
    @SerializedName("recent_transactions" ) var recentTransactions : ArrayList<Transactions> = arrayListOf()

)


