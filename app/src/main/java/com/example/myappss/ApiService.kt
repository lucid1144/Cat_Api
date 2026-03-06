package com.example.myappss

import io.ktor.client.call.body
import io.ktor.client.request.get

class ApiService {
    suspend fun getCatFacts(): CatResponse {
        return ApiClient.client.get("https://catfact.ninja/facts").body()
    }
}