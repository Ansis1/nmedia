package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likedCnt: Long = 0,
    val sharedCnt: Long = 0,
    val lookedCnt: Long = 0,
    val title: String,
    val video: String = "",
)