package com.example.quiztech.exam

import com.google.gson.annotations.SerializedName

data class Exam(
    val examName: String,
    val examType: String,
    val attempts: String,
    val questionsCount: String,
    val maxMarks: String,
    val time: String
)


data class TestDetailsMain (

    @SerializedName("status"    ) var status   : Int?                = null,
    @SerializedName("message"   ) var message  : String?             = null,
    @SerializedName("mock_data" ) var mockData : TestDetails?           = TestDetails(),
    @SerializedName("rank_data" ) var rankData : ArrayList<RankData> = arrayListOf()

)

data class  TestDetails (

    @SerializedName("id"                 ) var id                : String? = null,
    @SerializedName("product_id"         ) var productId         : String? = null,
    @SerializedName("title"              ) var title             : String? = null,
    @SerializedName("category_id"        ) var categoryId        : String? = null,
    @SerializedName("sub_category_name"  ) var subCategoryName   : String? = null,
    @SerializedName("sub_category_id"    ) var subCategoryId     : String? = null,
    @SerializedName("p_date"             ) var pDate             : String? = null,
    @SerializedName("p_duration"         ) var pDuration         : String? = null,
    @SerializedName("p_time"             ) var pTime             : String? = null,
    @SerializedName("questions"          ) var questions         : String? = null,
    @SerializedName("marks"              ) var marks             : String? = null,
    @SerializedName("short_descriptions" ) var shortDescriptions : String? = null,
    @SerializedName("descriptions"       ) var descriptions      : String? = null,
    @SerializedName("available_for"      ) var availableFor      : String? = null,
    @SerializedName("is_popular"         ) var isPopular         : String? = null,
    @SerializedName("max_members_list"   ) var maxMembersList    : String? = null,
    @SerializedName("left_count"   ) var left_count    : String? = null,
    @SerializedName("rewards"            ) var rewards           : String? = null,
    @SerializedName("test_type"          ) var testType          : String? = null,
    @SerializedName("is_enrolled"             ) var isEnrolled            : Int?    = null,
    @SerializedName("time_difference_minutes" ) var timeDifferenceMinutes : Int?    = null

)

data class RankData (
    @SerializedName("rank"  ) var rank  : String? = null,
    @SerializedName("prize" ) var prize : String? = null
)
