package com.example.quiztech.model

import com.google.gson.annotations.SerializedName

data class SubscriptionMainRes(
    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("wallet_amount" ) var wallet_amount : Int=0,
    @SerializedName("data"    ) var data    : ArrayList<SubscriptionPlan> = arrayListOf()
)

data class SubscriptionPlan(

    @SerializedName("id"             ) var id            : String?  = null,
    @SerializedName("plan_title"     ) var planTitle     : String?  = null,
    @SerializedName("plan_desc"      ) var planDesc      : String?  = null,
    @SerializedName("duration_days"  ) var durationDays  : String?  = null,
    @SerializedName("price"          ) var price         : String?  = null,
    @SerializedName("paid_qnty"      ) var paidQnty      : String?  = null,
    @SerializedName("free_qnty"      ) var freeQnty      : String?  = null,
    @SerializedName("start_date"     ) var startDate     : String?  = null,
    @SerializedName("end_date"       ) var endDate       : String?  = null,
    @SerializedName("is_subscribed"  ) var isSubscribed  : Boolean? = null,
    @SerializedName("remaining_days" ) var remainingDays : String?  = null
)
