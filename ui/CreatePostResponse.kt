package com.example.project.ui

import kotlinx.serialization.Serializable

@Serializable
data class CreatePostResponse(
    val id: Int,
    val title: String,
    val body: String,
    val userId: Int
)