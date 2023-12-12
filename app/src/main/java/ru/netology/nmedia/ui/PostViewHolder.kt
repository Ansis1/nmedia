package ru.netology.nmedia.ui

import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.reloadCntCounters

class PostViewHolder(

    private val binding: PostCardBinding,
    private val onLikeListener: OnLikeListener,
    private val onShareListener: OnShareListener,
    private val onRemoveListener: OnRemoveListener,
    private val onEditListener: OnEditListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {

        binding.apply {

            tvTitlepost.text = post.author
            tvDatepost.text = post.published
            tvTextpost.text = post.content

            tvLikesCnt.text = reloadCntCounters(post.likedCnt)
            tvLookCnt.text = reloadCntCounters(post.lookedCnt)
            tvShareCnt.text = reloadCntCounters(post.sharedCnt)

            ibLiked.setImageResource(if (post.likedByMe) R.drawable.liked else R.drawable.like)

            ibShared.setOnClickListener {
                onShareListener(post)
            }
            ibLiked.setOnClickListener {
                onLikeListener(post)
            }


            ibMenu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.it_remove -> {
                                onRemoveListener(post)
                                true
                            }

                            R.id.it_edit -> {
                                onEditListener(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }

        }

    }


}
