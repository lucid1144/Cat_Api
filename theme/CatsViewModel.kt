package com.example.project.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CatsViewModel : ViewModel() {
    private val apiService = ApiService()

    var facts by mutableStateOf<List<CatFact>>(emptyList())
        private set

    var current_page by mutableIntStateOf(1)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessages by mutableStateOf<String?>(null)
        private set

    fun getFacts() {
        isLoading = true
        errorMessages = null

        viewModelScope.launch {
            try {
                val response = apiService.getFacts(page = current_page)
                facts = facts + response.data
                current_page = current_page + 1
            } catch (e: Exception) {
                errorMessages = e.message ?: "Unknown error"
            } finally {
                isLoading = false
            }
        }
    }
}