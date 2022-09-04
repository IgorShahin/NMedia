package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.activity.FeedFragment.Companion.idArg
import ru.netology.nmedia.databinding.FragmentPostHandlerBinding
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class PostHandlerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostHandlerBinding.inflate(inflater, container, false)

        arguments?.textArg?.let { binding.content.setText(it) }

        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

        val postId = arguments?.idArg ?: -1
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == postId } ?: return@observe
            with(binding) {
                content.setText(post.content)
                viewModel.edit(post)
            }
        }

        binding.save.setOnClickListener {
            val content = binding.content.text.toString()

            if (content.isNotBlank()) {
                viewModel.changeContent(content)
                viewModel.save()
                findNavController().navigateUp()
            }
        }

        return binding.root
    }

    companion object {
        var Bundle.textArg: String? by StringArg
    }
}