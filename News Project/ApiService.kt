package com.example.project.ui

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ApiService {
    suspend fun getNews(): NewsResponse {
        return ApiClient.client.get("https://newsapi.org/v2/top-headlines?"){
            parameter("country", "us")
            parameter("apiKey", "8201d3f906b946d4a1bd9e5afe38733d")

        }.body()
    }
}