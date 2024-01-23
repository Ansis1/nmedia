package ru.netology.nmedia.ui

import android.view.View
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
    private val onUrlOpenListener: onUrlOpenListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {

        binding.apply {

            tvTitlepost.text = post.author
            tvDatepost.text = post.published
            tvTextpost.text = post.content

            tvLookCnt.text = reloadCntCounters(post.lookedCnt)
            ibShared.text = reloadCntCounters(post.sharedCnt)
            ibLiked.text = reloadCntCounters(post.likedCnt)
            ibLiked.isChecked = post.likedByMe //сделали для селектора Checkbox'a)
            if (post.video.isBlank()) {
                ivVideoPrew.visibility = View.GONE
                ivVideoPlay.visibility = View.GONE
            }
            ibShared.setOnClickListener {
                onShareListener(post)
            }
            ibLiked.setOnClickListener {
                onLikeListener(post)
            }

            ivVideoPrew.setOnClickListener {
                onUrlOpenListener(post)
            }

            ivVideoPlay.setOnClickListener {
                onUrlOpenListener(post)
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
