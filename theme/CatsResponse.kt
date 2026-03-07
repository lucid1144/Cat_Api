package com.example.project.ui.theme

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatsResponse(
    @SerialName("data")val data: List<CatFact>,
    @SerialName("current_page") val currentPage: Int,
)
