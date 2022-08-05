package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.IPostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

class PostViewModel : ViewModel() {

    private val repository: IPostRepository = PostRepositoryInMemoryImpl()
    val data: LiveData<Post> = repository.get()
    fun like() = repository.like()
    fun share() = repository.share()
}