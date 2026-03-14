package com.example.project.ui

import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val status: String,
    val articles: List<Article>,
    val totalResults: Int,

)
