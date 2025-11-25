package com.nilam.moodtrackerapp.data.remote.repository

import com.nilam.moodtrackerapp.data.remote.api.RetrofitClient

class QuoteRepository {

    private val api = RetrofitClient.api

    suspend fun getQuote() = api.getRandomQuote()
}
