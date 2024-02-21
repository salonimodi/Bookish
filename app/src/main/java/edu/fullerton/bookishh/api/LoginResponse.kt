package edu.fullerton.bookishh.api

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("token")
    var token: String = ""
)