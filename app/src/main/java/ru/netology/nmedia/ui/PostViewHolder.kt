package ru.netology.nmedia.ui

import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.reloadCntCounters

class PostViewHolder(

    private val binding: PostCardBinding,
    private val onLikeListener: OnLikeListener,
    private val onShareListener: OnShareListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {

        binding.apply {

            tvTitlepost.text = post.title
            tvDatepost.text = post.published
            tvTextpost.text = post.content

            tvLikesCnt.text = reloadCntCounters(post.counterMap.get("liked") ?: 0)
            tvLookCnt.text = reloadCntCounters(post.counterMap.get("looked") ?: 0)
            tvShareCnt.text = reloadCntCounters(post.counterMap.get("shared") ?: 0)

            ibLiked.setImageResource(if (post.likedByMe) R.drawable.liked else R.drawable.like)

            ibShared.setOnClickListener {
                onShareListener(post)
            }
            ibLiked.setOnClickListener {
                onLikeListener(post)
            }

        }

    }


}
