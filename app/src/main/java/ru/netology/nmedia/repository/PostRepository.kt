package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): List<Post> // получить список
    fun likeById(id: Long, isLiked: Boolean): Post// понравилось
    fun shareById(id: Long) //поделиться
    fun save(post: Post)
    fun removeById(id: Long) // удалить пост
    fun openInBrowser(urlVideo: String) //Открыть видео ссылку в браузере
    fun getById(id: Long): Post
}