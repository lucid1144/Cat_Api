package com.example.myappss

import kotlinx.serialization.Serializable

@Serializable

data class CatFact(
    val fact: String,
    val length: Int
) {

}