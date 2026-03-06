package com.example.myappss

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    private val apiService = ApiService()

    var movies by mutableStateOf<List<Movie>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun search(query: String) {
        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            try {
                val response = apiService.searchMovies(query)
                if (response.response == "True") {
                    movies = response.search ?: emptyList()
                } else {
                    errorMessage = response.errorMessage ?: "No movie found"
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Unknown error"
            } finally {
                isLoading = false
            }
        }
    }
}
