package edu.fullerton.bookishh.api

import com.google.gson.annotations.SerializedName

class RegisterResponse {
    @SerializedName("Success")
    lateinit var successMessage: String

    @SerializedName("Bad Request")
    lateinit var errorMessage: String
}