package ru.netology.nmedia.data

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var amountLike: Long,
    var amountShare: Long,
    var amountVisible: Long,
    var likedByMe: Boolean = false
)
