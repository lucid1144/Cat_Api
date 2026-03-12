package com.example.project.ui

import android.util.EventLogTags
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Coin(
    val name: String,
    val symbol: String,
    @SerialName("current_price")
    val price: Float,
    val image: String,
    val price_change_percentage_24h: Float,
    val market_cap: Long,
    @SerialName("total_volume")
    val totalVolume: Long,
    @SerialName("high_24h")
    val high24h: Float,
    @SerialName("low_24h")
    val low24h: Float,
)
