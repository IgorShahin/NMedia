package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val amountLike: Long,
    val amountShare: Long,
    val amountVisible: Long,
    val likedByMe: Boolean
)
