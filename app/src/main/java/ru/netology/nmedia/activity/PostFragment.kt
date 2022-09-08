package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.FeedFragment.Companion.idArg
import ru.netology.nmedia.activity.PostHandlerFragment.Companion.textArg
import ru.netology.nmedia.adapter.conversion
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import java.lang.reflect.Method

class PostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentPostBinding.inflate(inflater, container, false)

        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

        val postId = arguments?.idArg ?: -1
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == postId } ?: return@observe
            with(binding.postContent) {
                author.text = post.author
                published.text = post.published
                contentPosts.text = post.content
                like.text = post.amountLike.conversion()
                share.text = post.amountShare.conversion()
                countView.text = post.amountVisible.conversion()

                like.isChecked = post.likedByMe

                like.setOnClickListener { viewModel.likeById(post.id) }
                share.setOnClickListener {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }
                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)

                    viewModel.shareById(post.id)
                }
                video.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)
                }
                play.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)
                }

                if (post.video != null) groupVideo.visibility = View.VISIBLE
                else groupVideo.visibility = View.GONE

                menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.menu_post)

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            setForceShowIcon(true)
                        }

                        setOnMenuItemClickListener { menuItem ->
                            when (menuItem.itemId) {
                                R.id.remove -> {
                                    viewModel.removeById(post.id)
                                    findNavController().navigateUp()
                                }
                                R.id.edit -> findNavController().navigate(R.id.action_postFragment_to_postHandlerFragment,
                                    Bundle().apply {
                                        textArg = post.content
                                    }
                                )
                            }
                            true
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            setForceShowIcon(true)
                        } else {
                            try {
                                val fields = javaClass.declaredFields
                                for (field in fields) {
                                    if ("mPopup" == field.name) {
                                        field.isAccessible = true
                                        val menuPopupHelper = field[this]
                                        val classPopupHelper =
                                            Class.forName(menuPopupHelper.javaClass.name)
                                        val setForceIcons: Method = classPopupHelper.getMethod(
                                            "setForceShowIcon",
                                            Boolean::class.javaPrimitiveType
                                        )
                                        setForceIcons.invoke(menuPopupHelper, true)
                                        break
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }.show()
                }
                viewModel.edit(post)
            }
        }

        return binding.root
    }


}