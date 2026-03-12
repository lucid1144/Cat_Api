package com.example.project.ui

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ApiService {
    suspend fun getCoins(): List<Coin> {
        return ApiClient.client.get("https://api.coingecko.com/api/v3/coins/markets") {
            parameter("vs_currency", "usd")
            parameter("order", "market_cap_desc")
            parameter("per_page", 20)
            parameter("page", 1)
        }.body()
    }
}