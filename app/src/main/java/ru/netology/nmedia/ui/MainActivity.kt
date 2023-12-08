package ru.netology.nmedia.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.utils.reloadCntCounters
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->
            with(binding) {
                tvTextpost.setText(post.content)
                tvTitlepost.setText(post.title)
                tvDatepost.setText(post.published)
                tvLikesCnt.setText(reloadCntCounters(post.counterMap.get("liked") ?: 0))
                tvLookCnt.setText(reloadCntCounters(post.counterMap.get("looked") ?: 0))
                tvShareCnt.setText(reloadCntCounters(post.counterMap.get("shared") ?: 0))
                ibLiked.setImageResource(if (post.likedByMe) R.drawable.liked else R.drawable.like)
            }

        }

        binding.ibShared.setOnClickListener {

            viewModel.share()
        }

        binding.ibLiked.setOnClickListener {

            viewModel.like()

        }

    }


}

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val counterMap: MutableMap<String, Long>,
    val title: String
)

