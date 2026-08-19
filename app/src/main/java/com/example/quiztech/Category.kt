package com.example.quiztech

import com.google.gson.annotations.SerializedName

// Using a placeholder for image resource ID or URL string
data class Category(
    @SerializedName("id"    ) var id    : String? = null,
    @SerializedName("name"  ) var name  : String? = null,
    @SerializedName("image" ) var image : String? = null,
    @SerializedName("has_subcategories" ) var has_subcategories : Int=0
)

data class CategoryMainRes (

    @SerializedName("status"     ) var status     : Int?                  = null,
    @SerializedName("message"    ) var message    : String?               = null,
    @SerializedName("categories" ) var categories : ArrayList<Category> = arrayListOf()

)




