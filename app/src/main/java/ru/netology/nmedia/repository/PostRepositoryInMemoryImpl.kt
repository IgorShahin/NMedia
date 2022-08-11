package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl : IPostRepository {

    private var nextId = 1L
    private var posts = listOf(
        Post(
            id = nextId++,
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
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по " +
                    "онлайн маркетингу. Затем появились курсы по дизайну, разработке, аналитике и" +
                    " управлению. Мы растем сами и помогаем расти студентам: от новичков до уверенных " +
                    "профессиналов. Но самое важное остается с нами: мы верим, что в каждом уже есть " +
                    "сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша " +
                    "миссия - помочь встать на путь роста и начать цепочку перемен - http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            amountLike = 142,
            amountShare = 3421,
            amountVisible = 32425,
            likedByMe = false
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по " +
                    "онлайн маркетингу. Затем появились курсы по дизайну, разработке, аналитике и" +
                    " управлению. Мы растем сами и помогаем расти студентам: от новичков до уверенных " +
                    "профессиналов. Но самое важное остается с нами: мы верим, что в каждом уже есть " +
                    "сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша " +
                    "миссия - помочь встать на путь роста и начать цепочку перемен - http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            amountLike = 98967,
            amountShare = 56756,
            amountVisible = 10567,
            likedByMe = false
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по " +
                    "онлайн маркетингу. Затем появились курсы по дизайну, разработке, аналитике и" +
                    " управлению. Мы растем сами и помогаем расти студентам: от новичков до уверенных " +
                    "профессиналов. Но самое важное остается с нами: мы верим, что в каждом уже есть " +
                    "сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша " +
                    "миссия - помочь встать на путь роста и начать цепочку перемен - http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            amountLike = 345,
            amountShare = 5672,
            amountVisible = 231,
            likedByMe = false
        )
    ).reversed()

    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                amountLike = if (it.likedByMe) it.amountLike - 1 else it.amountLike + 1
            )
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map { if (it.id != id) it else it.copy(amountShare = it.amountShare + 1) }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        posts = if (post.id == 0L) {
            listOf(post.copy(id = nextId++, author = "Me", published = "Now")) + posts
        } else {
            posts.map { if (it.id != post.id) it else it.copy(content = post.content) }
        }
        data.value = posts
    }

}