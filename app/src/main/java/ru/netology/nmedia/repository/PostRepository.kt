package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>> // получить список Post
    fun likeById(id: Long) //нажать понравилось
    fun shareById(id: Long, ctx: Context) //поделиться
    fun save(post: Post)
    fun removeById(id: Long) // удалить пост (скрыть из списка)
    fun onShare(id: Long, ctx: Context)
}