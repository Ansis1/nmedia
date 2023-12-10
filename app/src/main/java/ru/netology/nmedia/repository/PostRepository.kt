package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun get(): LiveData<Post> // получить объект Post
    fun like() //нажать понравилось
    fun share() //поделиться
}