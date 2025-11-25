package com.nilam.moodtrackerapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nilam.moodtrackerapp.data.Quote
import com.nilam.moodtrackerapp.network.QuoteApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class QuoteViewModel : ViewModel() {

    private val _quote = MutableStateFlow<Quote?>(null)
    val quote: StateFlow<Quote?> = _quote

    fun loadQuote() {
        viewModelScope.launch {
            try {
                _quote.value = QuoteApi.service.getRandomQuote()
            } catch (e: Exception) {
                _quote.value = Quote("Unable to load quote.", "System")
            }
        }
    }
}
