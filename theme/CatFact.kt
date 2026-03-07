package com.example.project.ui.theme

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatFact(
    @SerialName("fact") val fact: String,
    @SerialName("length") val length: Int
)