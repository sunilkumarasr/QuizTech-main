package com.example.quiztech.model


import android.os.Parcelable
import com.example.quiztech.MockTest
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MainResponse (
    @SerializedName("status"  ) var status  : Int?    = null,
    @SerializedName("message" ) var message : String? = null,
    //@SerializedName("data"    ) var data    : Data   = Data(),
//    @SerializedName("profile_image" ) var profile_image :String?  = ""
)



data class Contact (

    @SerializedName("email"  ) var email  : String? = "",
    @SerializedName("landline_no" ) var landline_no : String? = "",
    @SerializedName("phone" ) var phone : String? = "",
    @SerializedName("phone_2" ) var phone_2 : String? = "",
    @SerializedName("address" ) var address : String? = "",
    @SerializedName("logo" ) var logo : String? = "",
    @SerializedName("created_date" ) var created_date : String? = "",
    @SerializedName("created_time" ) var created_time : String? = ""

)

data class FAQsMainResponse (

    @SerializedName("status"  ) var status  : Boolean?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : ArrayList<Faqs>   = arrayListOf()

)
data class ContactResponse (

    @SerializedName("status"  ) var status  : Boolean?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data" ) var contact : ArrayList<Contact> = arrayListOf(),

)
data class OTPVerifyResponse (

    @SerializedName("status"       ) var status      : Int?      = null,
    @SerializedName("message"      ) var message     : String?   = null,
    @SerializedName("user_info"    ) var userInfo    : UserInfo? = null,
    @SerializedName("access_token" ) var accessToken : String?   = null

)
data class PageResponse (

    @SerializedName("status"  ) var status  : Boolean?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data" ) var data : ArrayList<PageData> = arrayListOf(),
    )
data class BannerResponse (

    @SerializedName("status"  ) var status  : Int?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data" ) var data : ArrayList<BannerList> = arrayListOf(),
    )
data class ResendOTPResponse (

    @SerializedName("status"  ) var status  : Int?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("otp" ) var otp : String="",
    )
data class BannerList(
    @SerializedName("image") var image:String?="",
    @SerializedName("id") var id: String? = "",
)
data class PageData(
    @SerializedName("id"  ) var id  : String?    = "",
    @SerializedName("information_title" ) var information_title : String? = "",
    @SerializedName("description" ) var description : String? = "",
)
data class Faqs (

    @SerializedName("id"         ) var id        : String? = null,
    @SerializedName("type_id"    ) var typeId    : String? = null,
    @SerializedName("module_id"  ) var moduleId  : String? = null,
    @SerializedName("question"   ) var question  : String? = null,
    @SerializedName("answer"     ) var answer    : String? = null,
    @SerializedName("created_at" ) var createdAt : String? = null,
    @SerializedName("created_by" ) var createdBy : String? = null,
    @SerializedName("updated_at" ) var updatedAt : String? = null,
    @SerializedName("updated_by" ) var updatedBy : String? = null,
    @SerializedName("status"     ) var status    : String? = null,
    var isExpand:Boolean=false

)
data class SubmitEnquiry(
    @SerializedName("name") var name       : String = "",
    @SerializedName("email") var email       : String = "",
    @SerializedName("phone") var phone       : String = "",
    @SerializedName("subject")var subject : String = "",
    @SerializedName("message") var message   : String = "",

    )

data class  LoginResponse(

    @SerializedName("status"  ) var status  : Int?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("user_id" ) var userId  : String?    = null,
    @SerializedName("email"   ) var email   : String? = null
)
data class UserInfo (

    @SerializedName("id"                           ) var id                        : String? = null,
    @SerializedName("full_name"                    ) var fullName                  : String? = null,
    @SerializedName("address"                      ) var address                   : String? = null,
    @SerializedName("email"                        ) var email                     : String? = null,
    @SerializedName("phone"                        ) var phone                     : String? = null,
    @SerializedName("gender"                       ) var gender                    : String? = null,
    @SerializedName("active_subscription_id"       ) var activeSubscriptionId      : Int?    = null,
    @SerializedName("available_quizes_qnty"        ) var availableQuizesQnty       : Int?    = null,
    @SerializedName("active_subscription_end_date" ) var activeSubscriptionEndDate : String? = null,
    @SerializedName("is_new_user"                  ) var isNewUser                 : Int?    = null

)
data class UserRegisterData(
    @SerializedName("user_id") var user_id:String="",
    @SerializedName("first_name") var first_name:String="",
    @SerializedName("last_name") var last_name:String="",
    @SerializedName("type") var type:String="",
    @SerializedName("house_no") var house_no:String="",
    @SerializedName("floor") var floor:String="",
    @SerializedName("area") var area:String="",
    @SerializedName("landmark") var landmark:String="",
    @SerializedName("city_town") var city_town:String="",
    @SerializedName("zip_code") var zip_code:String="",
    @SerializedName("phone") var phone :String="",
    @SerializedName("email") var email :String="",
):Serializable
data class  UserProfileResponse(
    @SerializedName("status") var status:Boolean=false,
    @SerializedName("message") var message: String="",
    @SerializedName("data") var data: ArrayList<ProfileData> = arrayListOf(),
)
data class ProfileData(
    @SerializedName("full_name") var full_name:String?="",
    @SerializedName("phone") var phone:String?="",
    @SerializedName("email") var email:String?="",
    @SerializedName("users_id") var users_id:String?="",
    @SerializedName("wallet") var wallet:String?="",
    @SerializedName("referred_by") var referred_by:String?="",
    @SerializedName("profile_image") var profile_image:String?="",
)


data class CategoryResponse (

    @SerializedName("status"  ) var status  : Boolean?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data" ) var data : ArrayList<Category> = arrayListOf(),
)

data class Category(
    @SerializedName("id") var id: String="",
    @SerializedName("category") var category: String="",
    @SerializedName("sub_category") var subCategory: String="",
    @SerializedName("category_image") var catImage: String="",
    @SerializedName("sub_category_image") var subCatImage: String="",
): Serializable

data class SubCatResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("video_url") val video_url: String="",
    @SerializedName("data") val data: ArrayList<SubCatData> = arrayListOf()
)
data class SubCatData(
    @SerializedName("id") var id: String="",
    @SerializedName("product_id") var productId: String="",
    @SerializedName("category_id") var categoryId: String="",
    @SerializedName("sub_category_id") var subCategoryId: String="",
    @SerializedName("category_id_name") var categoryIdName: String="",
    @SerializedName("sub_category_id_name") var subCategoryIdName: String="",
    @SerializedName("title") var title: String="",
    @SerializedName("mrp_price") var mrpPrice: String="",
    @SerializedName("market_price") var marketPrice: String="",
    @SerializedName("our_price") var ourPrice: String="",
    @SerializedName("price_off") var priceOff: String="",
    @SerializedName("gst") var gst: String="",
    @SerializedName("short_descriptions") var shortDescriptions: String="",
    @SerializedName("descriptions") var descriptions: String="",
    @SerializedName("specifications") var specifications: String="",
    @SerializedName("image") var image: String="",
    @SerializedName("broucher") var broucher: String="",
    @SerializedName("attributes") var attributes: List<Any>,
    @SerializedName("attribute_id") var attributeId: String="",
    @SerializedName("weight") var weight: String="",
    @SerializedName("prices") var prices: String="",
    @SerializedName("discount_prices") var discountPrices: String="",
    @SerializedName("weight_class_id") var weightClassId: String="",
    @SerializedName("cart_id") var cartId: String="",
    @SerializedName("quantity") var quantity: String="",
   // @SerializedName("video_url") var video_url: String="",
    @SerializedName("user_rating") var userRating: Int=0,
    var addCount: Int=0,
    var isNewSub: Boolean=false
):Serializable


data class UserCommonJson(
    @SerializedName("api_key") var api_key: String?="",
    @SerializedName("user_id") var user_id: String?="",
    @SerializedName("email") var email: String?="",
    @SerializedName("otp") var otp: String?="",
    @SerializedName("id") var id: String?="",
    @SerializedName("bank_id") var bank_id: String?="",
    @SerializedName("vehicle_id") var vehicle_id: String?="",
    @SerializedName("status") var status: String?="",
    @SerializedName("hublist_id") var hublist_id: String?="",
    @SerializedName("page_name") var page_name: String?=""
)



data class MockListMainRes (

    @SerializedName("status"    ) var status   : Int?                = null,
    @SerializedName("message"   ) var message  : String?             = null,
    @SerializedName("mock_list" ) var mockList : ArrayList<MockTest> = ArrayList()

)

data class MockList (

    @SerializedName("id"              ) var id            : String? = null,
    @SerializedName("product_id"      ) var productId     : String? = null,
    @SerializedName("title"           ) var title         : String? = null,
    @SerializedName("category_id"     ) var categoryId    : String? = null,
    @SerializedName("sub_category_id" ) var subCategoryId : String? = null,
    @SerializedName("our_price"       ) var ourPrice      : String? = null,
    @SerializedName("available_for"   ) var availableFor  : String? = null,
    @SerializedName("is_popular"      ) var isPopular     : String? = null,
    @SerializedName("test_type"       ) var testType      : String? = null

)


data class ContactUsMain (

    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<ContactUs> = arrayListOf()

)

data class ContactUs (

    @SerializedName("address" ) var address : String? = null,
    @SerializedName("phone"   ) var phone   : String? = null,
    @SerializedName("phone_2" ) var phone2  : String? = null,
    @SerializedName("email"   ) var email   : String? = null

)

data class SubScriptionMain (

    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("data"    ) var data    : SubScriptionPlanSubscribe?    = SubScriptionPlanSubscribe()

)
data class SubScriptionPlanSubscribe (

    @SerializedName("subscription_id" ) var subscriptionId : Int?    = null,
    @SerializedName("start_date"      ) var startDate      : String? = null,
    @SerializedName("end_date"        ) var endDate        : String? = null,
    @SerializedName("valid_days"      ) var validDays      : String? = null

)