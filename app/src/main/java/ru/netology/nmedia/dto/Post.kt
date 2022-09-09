package ru.netology.nmedia.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(
    val id: Long,
    val author: String,
    var content: String,
    val published: String,
    val likedByMe: Boolean,
    val amountLike: Long = 0,
    val amountShare: Long = 0,
    val amountVisible: Long = 0
): Parcelable
