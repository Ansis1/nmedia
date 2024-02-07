package ru.netology.nmedia.dto

interface PostDao {
    fun getAll(): List<Post> // получить список Post
    fun likeById(id: Long) //нажать понравилось
    fun save(post: Post): Post
    fun removeById(id: Long) // удалить пост (скрыть из списка)
    fun getById(id: Long): Post
}