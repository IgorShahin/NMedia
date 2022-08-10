package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.IPostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

val empty = Post(
    id = 0,
    author = "",
    content = "",
    published = "",
    amountLike = 0,
    amountShare = 0,
    amountVisible = 0,
    likedByMe = false
)

class PostViewModel : ViewModel() {

    private val repository: IPostRepository = PostRepositoryInMemoryImpl()
    val data: LiveData<List<Post>> = repository.getAll()
    val edited = MutableLiveData(empty)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun closeEdit() {
        edited.value = empty
    }

    fun changeContent(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content == text) {
                return
            }
            edited.value = edited.value?.copy(content = text)
        }
    }

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long): Boolean {
        var markEdit = false
        if (edited.value?.id == id) {
            closeEdit()
            markEdit = true
        }
        repository.removeById(id)
        return markEdit
    }
}