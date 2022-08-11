package ru.netology.nmedia

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.adapter.AdapterCallback
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        val adapter = PostsAdapter(object : AdapterCallback {
            override fun onLike(post: Post) = viewModel.likeById(post.id)
            override fun onShare(post: Post) = viewModel.shareById(post.id)

            override fun onRemove(post: Post) {
                if (viewModel.removeById(post.id)) {
                    with(binding) {
                        group.visibility = View.GONE
                        content.setText("")
                        content.clearFocus()
                        AndroidUtils.hideKeyboard(content)
                    }
                }
            }

            override fun onEdit(post: Post) = viewModel.edit(post)

        })
        binding.container.adapter = adapter
        viewModel.data.observe(this) { posts ->
            val newPost = adapter.itemCount < posts.size
            adapter.submitList(posts) {
                if (newPost) binding.container.smoothScrollToPosition(0)
            }
        }
        viewModel.edited.observe(this) {
            if (it.id != 0L) {
                binding.content.setText(it.content)
                binding.content.requestFocus()
                binding.editMessage.text = it.content
                binding.group.visibility = View.VISIBLE
            }
        }

        binding.closeEdit.setOnClickListener {
            with(binding) {
                viewModel.closeEdit()
                group.visibility = View.GONE
                content.setText("")
                content.clearFocus()
                AndroidUtils.hideKeyboard(it)
            }
        }

        binding.save.setOnClickListener {
            with(binding.content) {
                val content = text.toString()
                if (content.isBlank()) {
                    Toast.makeText(it.context, R.string.error_empty_content, Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                viewModel.changeContent(content)
                viewModel.save()

                binding.group.visibility = View.GONE
                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(it)
            }
        }
    }

}
