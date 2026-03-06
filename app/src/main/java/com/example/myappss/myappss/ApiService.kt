package com.example.myappss

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ApiService {
    suspend fun searchMovies(query: String): MovieResponse {
        return ApiClient.client.get("https://www.omdbapi.com/") {
            parameter("apikey", "thewdb")
            parameter("s", query)
        }.body()
    }
}