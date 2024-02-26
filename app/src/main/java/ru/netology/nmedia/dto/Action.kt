package ru.netology.nmedia.dto

enum class Action {
    LIKE,
    NEWPOST,
}

data class Like(
    val userId: Long,
    val userName: String,
    val postId: Long,
    val postAuthor: String,

    )