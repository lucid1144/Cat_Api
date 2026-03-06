package com.example.myappss

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    @SerialName("Title") val title: String,
    @SerialName("Year") val year: String,
    @SerialName("imdbID") val imdbID: String
)