package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.utils.reloadCntCounters
import ru.netology.nmedia.viewmodel.PostViewModel

class PostCardFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = ru.netology.nmedia.databinding.FragmentPostCardBinding.inflate(
            inflater,
            container,
            false
        )
        val postId = arguments?.getLong("id", Long.MIN_VALUE)
        if (postId?.equals(Long.MIN_VALUE) == true) {
            Snackbar.make(
                binding.root, R.string.error_empty_content,
                BaseTransientBottomBar.LENGTH_INDEFINITE
            ).show()
            findNavController().navigateUp()
        }

        var currPost = viewModel.getById(postId ?: 0)

        with(binding.postCardFragment) {

            tvTextpost.setText(currPost.content)
            tvTitlepost.setText(currPost.author)
            tvDatepost.setText(ru.netology.nmedia.utils.getHumanDate(currPost.published))

            tvLookCnt.text = reloadCntCounters(currPost.lookedCnt)
            ibShared.text = reloadCntCounters(currPost.sharedCnt)
            ibLiked.text = reloadCntCounters(currPost.likes)
            ibLiked.isChecked = currPost.likedByMe

            if (currPost.video.isBlank()) {
                ivVideoPrew.visibility = View.GONE
                ivVideoPlay.visibility = View.GONE
            }
            ibShared.setOnClickListener {
                viewModel.shareById(currPost.id, currPost.content) // поделиться
                currPost = viewModel.getById(postId ?: 0)
                ibShared.text = reloadCntCounters(currPost.sharedCnt)
            }
            ibLiked.setOnClickListener {
                viewModel.likeById(currPost.id, currPost.likedByMe) //лайк
                currPost = viewModel.getById(postId ?: 0)
                ibLiked.text = reloadCntCounters(currPost.likes)
            }

            ivVideoPrew.setOnClickListener {
                viewModel.openInBrowser(currPost.video) //открыть ссылку
            }

            ivVideoPlay.setOnClickListener {
                viewModel.openInBrowser(currPost.video) //открыть ссылку
            }

            ibMenu.setOnClickListener {
                PopupMenu(it.context, it).apply { // всплывающее меню через три точки
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.it_remove -> {
                                viewModel.removeById(currPost.id) //удалить (popup)
                                findNavController().navigateUp()
                                true
                            }

                            R.id.it_edit -> {
                                viewModel.edit(currPost)
                                findNavController().navigate(
                                    R.id.action_postCardFragment_to_editPostFragment,
                                    Bundle().apply {
                                        putString("content", currPost.content)
                                    })
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }


        }
        return binding.root
    }

}