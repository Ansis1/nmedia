package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>> // получить список Post
    fun likeById(id: Long) //нажать понравилось
    fun shareById(id: Long) //поделиться
}