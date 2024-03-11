package ru.netology.nmedia.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }
        callback.isEnabled = true
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = ru.netology.nmedia.databinding.FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )


        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onUrlOpen(post: Post) {
                viewModel.openInBrowser(post.video)
            }

            override fun onPostClick(post: Post) {
                openPostCard(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id, post.likedByMe)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {

                viewModel.shareById(post.id)
            }
        })


        viewModel.edited.observe(viewLifecycleOwner) {
            if (it.id == 0L) {
                binding.editedPrevGroup.visibility = View.GONE
                return@observe
            }

            findNavController().navigate(
                R.id.action_feedFragment_to_editPostFragment,
                Bundle().apply {
                    putString("content", it.content)
                })
        }

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { state ->
            adapter.submitList(state.posts)
            binding.progress.isVisible = state.loading
            binding.errorGroup.isVisible = state.error
            binding.emptyText.isVisible = state.empty
        }


        //Добавление или изменение (кнопка).
        binding.ibChangeOrAdd.setOnClickListener {
            with(binding.etNewComment) {

                if (text.isNullOrBlank()) {

                    Toast.makeText(
                        this.context,
                        context.getString(R.string.not_empty_msg),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                viewModel.changeContent(text.toString())
                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
            }
        }

        //Отмена редактирования
        binding.ibCancelEditing.setOnClickListener {
            with(binding) {
                editedPrevGroup.visibility = View.GONE
                etNewComment.setText("")
            }
            viewModel.cancelEditing()
        }

        return binding.root
    }

    binding.retryButton.setOnClickListener
    {
        viewModel.loadPosts()
    }

    binding.fab.setOnClickListener
    {
        findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
    }

    private fun openPostCard(post_id: Long) {

        findNavController().navigate(R.id.action_feedFragment_to_postCardFragment, Bundle().apply {
            putLong("id", post_id)
        })

    }


}





