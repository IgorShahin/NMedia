package ru.netology.nmedia.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.dto.Post
import java.lang.reflect.Method
import java.math.RoundingMode
import java.text.DecimalFormat

interface AdapterCallback {
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onRemove(post: Post)
    fun onEdit(post: Post)
    fun onVideo(post: Post)
    fun onPost(post: Post)
}

class PostsAdapter(
    private val callback: AdapterCallback
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, callback)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

}

class PostViewHolder(
    private val binding: PostCardBinding,
    private val callback: AdapterCallback
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            contentPosts.text = post.content
            like.text = post.amountLike.conversion()
            share.text = post.amountShare.conversion()
            countView.text = post.amountVisible.conversion()

            like.isChecked = post.likedByMe

            like.setOnClickListener { callback.onLike(post) }
            share.setOnClickListener { callback.onShare(post) }
            video.setOnClickListener { callback.onVideo(post) }
            play.setOnClickListener { callback.onVideo(post) }

            root.setOnClickListener { callback.onPost(post) }
            contentPosts.setOnClickListener { callback.onPost(post) }

            if (post.video != null) groupVideo.visibility = View.VISIBLE
            else groupVideo.visibility = View.GONE

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_post)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        setForceShowIcon(true)
                    }

                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.remove -> callback.onRemove(post)
                            R.id.edit -> callback.onEdit(post)
                        }
                        true
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        setForceShowIcon(true)
                    } else {
                        try {
                            val fields = javaClass.declaredFields
                            for (field in fields) {
                                if ("mPopup" == field.name) {
                                    field.isAccessible = true
                                    val menuPopupHelper = field[this]
                                    val classPopupHelper =
                                        Class.forName(menuPopupHelper.javaClass.name)
                                    val setForceIcons: Method = classPopupHelper.getMethod(
                                        "setForceShowIcon",
                                        Boolean::class.javaPrimitiveType
                                    )
                                    setForceIcons.invoke(menuPopupHelper, true)
                                    break
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }.show()
            }
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