package com.example.myappss

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CatViewModel : ViewModel() {
    private val apiService = ApiService()

    var facts by mutableStateOf<List<CatFact?>>(emptyList())
    private set

    var loading by mutableStateOf(false)
    private set

    var error by mutableStateOf<String?>(null)
    private set

    fun getFacts() {
        loading = true
        error = null

        viewModelScope.launch {
            try {
                val response = apiService.getCatFacts()
                facts = response.data
            }
            catch (e: Exception) {
                error = e.message ?: "Unknown error"
            }
            finally {
                loading = false
            }
        }
    }
}
