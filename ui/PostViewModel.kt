package com.example.project.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {

    private val api = ApiService()

    var result by mutableStateOf<CreatePostResponse?>(null)
        private set

    var loading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun createPost(title: String, body: String) {

        loading = true
        error = null
        result = null

        viewModelScope.launch {

            try {

                val response = api.createPost(title, body)

                result = response

            } catch (e: Exception) {

                error = e.message ?: "Unknown error"

            } finally {

                loading = false

            }

        }
    }
}