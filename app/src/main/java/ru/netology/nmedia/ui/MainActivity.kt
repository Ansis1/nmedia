package ru.netology.nmedia.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.utils.reloadCntCounters
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { posts ->

            posts.map { post ->
                PostCardBinding.inflate(layoutInflater, binding.root, true).apply {

                    tvTextpost.text = post.content
                    tvTitlepost.text = post.title
                    tvDatepost.text = post.published
                    tvLikesCnt.text = reloadCntCounters(post.counterMap.get("liked") ?: 0)
                    tvLookCnt.text = reloadCntCounters(post.counterMap.get("looked") ?: 0)
                    tvShareCnt.text = reloadCntCounters(post.counterMap.get("shared") ?: 0)
                    ibLiked.setImageResource(if (post.likedByMe) R.drawable.liked else R.drawable.like)
                    ibShared.setOnClickListener {

                        viewModel.shareById(post.id)
                    }

                    ibLiked.setOnClickListener {

                        viewModel.likeById(post.id)

                    }
                }.root
            }

        }


    }


}

