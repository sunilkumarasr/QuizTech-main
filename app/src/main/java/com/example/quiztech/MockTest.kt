package com.example.quiztech

import com.google.gson.annotations.SerializedName

data class MockTest(


    @SerializedName("test_id"            ) var testId            : String? = null,
    @SerializedName("product_id"         ) var productId         : String? = null,
    @SerializedName("title"              ) var title             : String? = null,
    @SerializedName("category_id"        ) var categoryId        : String? = null,
    @SerializedName("sub_category_id"    ) var subCategoryId     : String? = null,
    @SerializedName("sub_category_name"  ) var subCategoryName   : String? = null,
    @SerializedName("topic_id"           ) var topicId           : String? = null,
    @SerializedName("p_date"             ) var pDate             : String? = null,
    @SerializedName("p_duration"         ) var pDuration         : String? = null,
    @SerializedName("p_time"             ) var pTime             : String? = null,
    @SerializedName("questions"          ) var questions         : String? = null,
    @SerializedName("marks"              ) var marks             : String? = null,
    @SerializedName("short_descriptions" ) var shortDescriptions : String? = null,
    @SerializedName("available_for"      ) var availableFor      : String? = null,
    @SerializedName("is_popular"         ) var isPopular         : String? = null,
    @SerializedName("max_members_list"   ) var maxMembersList    : String? = null,
    @SerializedName("test_type"          ) var testType          : String? = null
    // You could add an ID for navigation or API calls: val id: String
)
