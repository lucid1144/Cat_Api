package com.example.project.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NewsViewModel: ViewModel() {
    private val apiService = ApiService()

    var articles by mutableStateOf<List<Article>>(emptyList())
    private set

    var totalResults by mutableIntStateOf(0)
    private set

    var searchQuery by mutableStateOf("")

    val filteredArticles: List<Article>
        get() = if (searchQuery.isBlank()) {
            articles
        } else {
            articles.filter { it.title.contains(searchQuery, ignoreCase = true) }
        }





    var loading by mutableStateOf(false)
    private set

    var error by mutableStateOf<String?>(null)
    private set

    fun getNews() {
        loading = true
        error = null
        viewModelScope.launch {
            try {
                val response = apiService.getNews()
                if (response.status == "ok") {
                articles = response.articles
                totalResults = response.totalResults
                }
            } catch (e: Exception) {
                error = e.message ?: "Unknown error"
            } finally {
                loading = false
            }
            }

    }

}