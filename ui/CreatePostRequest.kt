package com.example.project.ui

import kotlinx.serialization.Serializable

@Serializable
data class CreatePostRequest(
    val title: String,
    val body: String,
    val userId: Int
)