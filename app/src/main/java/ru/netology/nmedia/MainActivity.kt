package ru.netology.nmedia

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.databinding.PostLayoutBinding
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: PostLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = PostLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по " +
                    "онлайн маркетингу. Затем появились курсы по дизайну, разработке, аналитике и" +
                    " управлению. Мы растем сами и помогаем расти студентам: от новичков до уверенных " +
                    "профессиналов. Но самое важное остается с нами: мы верим, что в каждом уже есть " +
                    "сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша " +
                    "миссия - помочь встать на путь роста и начать цепочку перемен - http://netolo.gy/fyb</string>",
            published = "21 мая в 18:36",
            amountLike = 999,
            amountShare = 9999,
            amountVisible = 100000
        )

        with(binding) {
            author.text = post.author
            published.text = post.published
            contentPosts.text = post.content
            countLike.text = convertetionAmount(post.amountLike)
            countShare.text = convertetionAmount(post.amountShare)
            countView.text = convertetionAmount(post.amountVisible)

            like.let {
                fun ImageButton.setLiked(liked: Boolean) {
                    val likeIconResId = if (liked)
                        R.drawable.ic_liked_24 else R.drawable.ic_like_24
                    val likeCount = if (!liked)
                        post.amountLike++ else post.amountLike--
                    countLike.text = convertetionAmount(likeCount)
                    setImageResource(likeIconResId)
                }

                it.setLiked(post.likedByMe)

                it.setOnClickListener {
                    post.likedByMe = !post.likedByMe
                    like.setLiked(post.likedByMe)
                }
            }

            share.setOnClickListener { countShare.text = convertetionAmount(post.amountShare++) }
        }

    }

}

fun convertetionAmount(number: Long): String = when {
    number in 1000..9999 -> DecimalFormat("#.#K").apply { roundingMode = RoundingMode.FLOOR }
        .format(number / 1000.0)
    number in 10000..999999 -> String.format("%dK", number / 1000)
    number >= 1000000 -> DecimalFormat("#.#M").apply { roundingMode = RoundingMode.FLOOR }
        .format(number / 1000000.0)
    else -> {
        number.toString()
    }
}
