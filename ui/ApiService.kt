package com.example.project.ui

import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ApiService {
    suspend fun createPost(tittle: String, body: String): CreatePostResponse {
        return ApiClient.client.post("https://jsonplaceholder.typicode.com/posts") {
            contentType(ContentType.Application.Json)
            setBody(
                CreatePostRequest(
                    tittle,
                    body,
                    1)
            )
        }.body()
    }
}