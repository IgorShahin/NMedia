package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl : IPostRepository {

    private var post = Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по " +
                "онлайн маркетингу. Затем появились курсы по дизайну, разработке, аналитике и" +
                " управлению. Мы растем сами и помогаем расти студентам: от новичков до уверенных " +
                "профессиналов. Но самое важное остается с нами: мы верим, что в каждом уже есть " +
                "сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша " +
                "миссия - помочь встать на путь роста и начать цепочку перемен - http://netolo.gy/fyb",
        published = "21 мая в 18:36",
        amountLike = 999,
        amountShare = 990,
        amountVisible = 100000,
        likedByMe = false
    )
    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data

    override fun like() {
        post = post.copy(
            likedByMe = !post.likedByMe,
            amountLike = if (post.likedByMe) post.amountLike - 1 else post.amountLike + 1
        )
        data.value = post
    }

    override fun share() {
        post = post.copy(amountShare = post.amountShare + 1)
        data.value = post
    }

}