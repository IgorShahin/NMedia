package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    var content: String,
    val published: String,
    val likedByMe: Boolean,
    val amountLike: Long = 0,
    val amountShare: Long = 0,
    val amountVisible: Long = 0
) {

    fun toDto() = Post(id, author, content, published, likedByMe, amountLike, amountShare)

    companion object {

        fun fromDto(post: Post) = with(post) {
            PostEntity(id, author, content, published, likedByMe, amountLike, amountShare)
        }
    }
}