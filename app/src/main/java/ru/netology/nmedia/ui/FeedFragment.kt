package ru.netology.nmedia.ui

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
    ): View? {

        val binding = ru.netology.nmedia.databinding.FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )



        val adapter = PostsAdapter({
            viewModel.likeById(it.id) //лайк
        }, {
            viewModel.shareById(it.id) // поделиться
        }, {
            viewModel.removeById(it.id) //удалить (popup)
        }, {
            viewModel.setEditedValue(it) //изменить (popup)
        }, {
            viewModel.openInBrowser(it.video) //открыть ссылку
        }, {
            openPostCard(it.id) //открыть карточку поста
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

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
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

    private fun openPostCard(post_id: Long) {

        findNavController().navigate(R.id.action_feedFragment_to_postCardFragment, Bundle().apply {
            putLong("id", post_id)
        })

    }


}





