package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostLayoutBinding
import ru.netology.nmedia.dto.Post
import java.math.RoundingMode
import java.text.DecimalFormat

typealias LikeCallback = (Post) -> Unit
typealias ShareCallback = (Post) -> Unit

class PostsAdapter(
    private val likeCallback: LikeCallback,
    private val shareCallback: ShareCallback
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, likeCallback, shareCallback)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

}

class PostViewHolder(
    private val binding: PostLayoutBinding,
    private val likeCallback: LikeCallback,
    private val shareCallback: ShareCallback
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            contentPosts.text = post.content
            countLike.text = post.amountLike.conversion()
            countShare.text = post.amountShare.conversion()
            countView.text = post.amountVisible.conversion()

            like.setImageResource(if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24)

            like.setOnClickListener { likeCallback(post) }
            share.setOnClickListener { shareCallback(post) }
        }

    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}

fun Long.conversion(): String = when {
    this in 1000..9999 -> DecimalFormat("#.#K").apply { roundingMode = RoundingMode.FLOOR }
        .format(this / 1000.0)
    this in 10000..999999 -> String.format("%dK", this / 1000)
    this >= 1000000 -> DecimalFormat("#.#M").apply { roundingMode = RoundingMode.FLOOR }
        .format(this / 1000000.0)
    else -> {
        this.toString()
    }
}