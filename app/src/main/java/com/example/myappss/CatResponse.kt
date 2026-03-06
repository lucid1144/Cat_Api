package com.example.myappss

import kotlinx.serialization.Serializable

@Serializable
data class CatResponse(
    val data: List<CatFact?>
)