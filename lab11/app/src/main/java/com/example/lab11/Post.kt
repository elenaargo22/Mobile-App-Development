package com.example.lab11

data class Post(
    val id: Int,
    val title: String,
    val body: String
)

data class Comment(
    val postId: Int,
    val id: Int,
    val name: String,
    val email: String,
    val body: String
)
