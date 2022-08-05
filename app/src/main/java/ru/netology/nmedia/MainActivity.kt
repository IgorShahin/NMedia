package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.PostLayoutBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: PostLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = PostLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                contentPosts.text = post.content
                countLike.text = post.amountLike.conversion()
                countShare.text = post.amountShare.conversion()
                countView.text = post.amountVisible.conversion()

                if (post.likedByMe) like.setImageResource(R.drawable.ic_liked_24) else like.setImageResource(R.drawable.ic_like_24)

                like.setOnClickListener {
                    viewModel.like()
                }

                share.setOnClickListener {
                    viewModel.share()
                }
            }
        }

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
