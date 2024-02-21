package edu.fullerton.bookishh.api
import com.google.gson.annotations.SerializedName

data class Job (
    @SerializedName("id")
    val id: Int,
    val employerId: Int,
    @SerializedName("company_name")
    val companyName : String,
    @SerializedName("company_description")
    val companyDescription : String,
    @SerializedName("title")
    val role: String,
    @SerializedName("title_description")
    val desc: String,
    @SerializedName("location")
    val location : String,
    @SerializedName("salary")
    val salary : Int
)