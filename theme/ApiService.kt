package com.example.project.ui.theme

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ApiService {
    suspend fun getFacts(page: Int): CatsResponse {
        return ApiClient.client.get("https://catfact.ninja/facts") {
            parameter("page", page)

        }.body()
    }
}