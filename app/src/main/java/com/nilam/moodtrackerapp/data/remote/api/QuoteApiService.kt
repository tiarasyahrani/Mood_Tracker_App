package com.nilam.moodtrackerapp.data.remote.api

import com.nilam.moodtrackerapp.data.remote.model.QuoteResponse
import retrofit2.http.GET

interface QuoteApiService {

    @GET("random")
    suspend fun getRandomQuote(): QuoteResponse
}
