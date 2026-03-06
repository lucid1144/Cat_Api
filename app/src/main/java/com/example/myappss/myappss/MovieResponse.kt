package com.example.myappss

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
    @SerialName("Search") val search: List<Movie>? = null,
    @SerialName("Response") val response: String,
    @SerialName("Error") val errorMessage: String? = null
)



