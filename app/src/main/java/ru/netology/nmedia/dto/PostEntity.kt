package ru.netology.nmedia.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PostEntity(
    //Таблица SQL
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val likedByMe: Boolean,
    val likes: Long,
    val sharedCnt: Long,
    val lookedCnt: Long,
    val title: String,
    val video: String,
    val published: Long,

    ) {

    fun toDto() = Post(
        id,
        author,
        content,
        published,
        likedByMe,
        likes,
        sharedCnt,
        lookedCnt,
        title,
        video
    )

    companion object {
        fun fromDto(post: Post) = PostEntity(
            post.id,
            post.author,
            post.content,
            post.likedByMe,
            post.likes,
            post.sharedCnt,
            post.lookedCnt,
            post.title ?: "",
            post.video ?: "",
            post.published
        )
    }

}