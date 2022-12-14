package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositoryFileImpl(val context: Context) : IPostRepository {

    private var nextId = 1L
    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val fileName = "posts.json"
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        val file = context.filesDir.resolve(fileName)
        if (file.exists()) {
            context.openFileInput(fileName).bufferedReader().use {
                posts = gson.fromJson(it, type)
                data.value = posts
            }
        } else {
            sync()
        }
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                amountLike = if (it.likedByMe) it.amountLike - 1 else it.amountLike + 1
            )
        }
        data.value = posts
        sync()
    }

    override fun shareById(id: Long) {
        posts = posts.map { if (it.id != id) it else it.copy(amountShare = it.amountShare + 1) }
        data.value = posts
        sync()
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
        sync()
    }

    override fun save(post: Post) {
        posts = if (post.id == 0L) {
            listOf(post.copy(id = nextId++, author = "Me", published = "Now")) + posts
        } else {
            posts.map { if (it.id != post.id) it else it.copy(content = post.content) }
        }
        data.value = posts
        sync()
    }

    private fun sync() {
        context.openFileOutput(fileName, Context.MODE_PRIVATE).bufferedWriter()
            .use { it.write(gson.toJson(posts)) }
    }
}