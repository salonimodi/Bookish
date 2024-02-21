package edu.fullerton.bookishh.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import edu.fullerton.bookishh.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JobsExecuter {
    companion object {

        private var gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val api: JobsAPI = retrofit.create(JobsAPI::class.java)
    }
}