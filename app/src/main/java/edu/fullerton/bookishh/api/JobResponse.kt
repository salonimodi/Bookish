package edu.fullerton.bookishh.api

import com.google.gson.annotations.SerializedName

data class JobResponse(
    @SerializedName("Success!")
    val jobs: List<Job>
)
