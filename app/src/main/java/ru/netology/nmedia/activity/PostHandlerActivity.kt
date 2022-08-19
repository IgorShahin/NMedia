package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityPostHandlerBinding
import ru.netology.nmedia.dto.Post

class PostHandlerActivity : AppCompatActivity() {

    private val emptyPost = Post(
        id = 0,
        author = "",
        content = "",
        published = "",
        amountLike = 0,
        amountShare = 0,
        amountVisible = 0,
        likedByMe = false
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPostHandlerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val post = intent?.getParcelableExtra(PostContract.EXTRA_POST) as? Post ?: emptyPost

        if (post.content.isBlank()) {
            supportActionBar?.title = getString(R.string.create_post)
        } else {
            supportActionBar?.title = getString(R.string.edit_post)
            binding.content.setText(post.content)
        }

        binding.save.setOnClickListener {
            post.content = binding.content.text.toString()
            if (post.content.isBlank()) {
                setResult(RESULT_CANCELED)
            } else {
                val intent = Intent().apply { putExtra(PostContract.EXTRA_POST, post) }
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}