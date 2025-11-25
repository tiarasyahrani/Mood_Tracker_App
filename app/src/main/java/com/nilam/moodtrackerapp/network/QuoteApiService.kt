package com.nilam.moodtrackerapp.network

import com.nilam.moodtrackerapp.data.Quote
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface QuoteApiService {
    @GET("random")
    suspend fun getRandomQuote(): Quote
}
object QuoteApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.quotable.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val service: QuoteApiService = retrofit.create(QuoteApiService::class.java)
}