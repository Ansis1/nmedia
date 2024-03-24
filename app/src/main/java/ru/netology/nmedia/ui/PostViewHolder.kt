package ru.netology.nmedia.ui

import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.reloadCntCounters


class PostViewHolder(
    private val binding: PostCardBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {

        binding.apply { // отрисовка элементов списка

            tvTitlepost.text = post.author
            tvDatepost.text = ru.netology.nmedia.utils.getHumanDate(post.published)
            tvTextpost.text = post.content

            tvLookCnt.text = reloadCntCounters(post.lookedCnt)
            ibShared.text = reloadCntCounters(post.sharedCnt)
            ibLiked.text = reloadCntCounters(post.likes)
            ibLiked.isChecked = post.likedByMe //сделали для селектора Checkbox'a)
            Log.i("post2", post.toString())
            if (post.video.isBlank()) {
                ivVideoPrew.visibility = View.GONE
                ivVideoPlay.visibility = View.GONE
            }
            ibShared.setOnClickListener {
                onInteractionListener.onShare(post)
            }
            ibLiked.setOnClickListener {
                onInteractionListener.onLike(post)
            }

            ivVideoPrew.setOnClickListener {
                onInteractionListener.onUrlOpen(post)
            }

            ivVideoPlay.setOnClickListener {
                onInteractionListener.onUrlOpen(post)
            }

            ibMenu.setOnClickListener {
                PopupMenu(it.context, it).apply { // всплывающее меню через три точки
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.it_remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }

                            R.id.it_edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }


            itemView.setOnClickListener {

                onInteractionListener.onPostClick(post)
            }
        }

    }


}
