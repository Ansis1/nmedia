package ru.netology.nmedia.dto

import com.google.gson.annotations.SerializedName

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: Long = 0,
    val likedByMe: Boolean = false,
    val likes: Long = 0,
    val sharedCnt: Long = 0,
    val lookedCnt: Long = 0,
    @SerializedName("title") private var _title: String? = "",
    @SerializedName("video") private var _video: String? = ""
) {
    val title
        get() = _title ?: ""
    val video
        get() = _video ?: ""

    override fun toString(): String {
        return "Post(id=$id, \nauthor='$author', \ncontent='$content', \npublished=$published, \nlikedByMe=$likedByMe, \nlikes=$likes, \nsharedCnt=$sharedCnt, \nlookedCnt=$lookedCnt, \n_title=$_title, \n_video=$_video, \ntitle='$title', \nvideo='$video')"
    }


}