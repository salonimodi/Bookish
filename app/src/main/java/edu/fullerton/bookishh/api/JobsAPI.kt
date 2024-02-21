package edu.fullerton.bookishh.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface JobsAPI {
    @GET("/job_listings")
    fun getJobs(
        @QueryMap parameters: Map<String, String>?
    ): Call<JobResponse>
}