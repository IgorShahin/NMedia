package ru.netology.nmedia.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(
    val id: Long,
    val author: String,
    var content: String,
    val published: String,
    val amountLike: Long,
    val amountShare: Long,
    val amountVisible: Long,
    val likedByMe: Boolean,
    val video: String? = null
): Parcelable
