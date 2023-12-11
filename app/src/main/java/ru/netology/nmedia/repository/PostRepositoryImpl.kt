package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.getHumanDate

class PostRepositoryImpl : PostRepository {


    var posts = listOf(
        Post(
            1234,
            "Ansis",
            "This is content of Post1.",
            getHumanDate(System.currentTimeMillis()),
            false,
            lookedCnt = 90L,
            title = "Test title"
        ),
        Post(
            2234,
            "Ansis",
            "This is content of Post2.",
            getHumanDate(System.currentTimeMillis()),
            false,
            lookedCnt = 120L,
            title = "Test title2"
        ),
        Post(
            22341,
            "Ansis",
            "This is content of Post3.",
            getHumanDate(System.currentTimeMillis()),
            false,
            lookedCnt = 1200L,
            title = "Test title3"
        ),
        Post(
            22342,
            "Ansis",
            "This is content of Post4.",
            getHumanDate(System.currentTimeMillis()),
            false,
            lookedCnt = 10300L,
            title = "Test title4"
        ),
        Post(
            22343,
            "Ansis",
            "This is content of Post5.",
            getHumanDate(System.currentTimeMillis()),
            false,
            lookedCnt = 100500,
            title = "Test title5"
        ),
        Post(
            22344,
            "Ansis",
            "This is content of Post6.",
            getHumanDate(System.currentTimeMillis()),
            false,
            lookedCnt = 200_000,
            title = "Test title6"
        ),
        Post(
            223424,
            "Ansis",
            "This is content of Post7.",
            getHumanDate(System.currentTimeMillis()),
            false,
            lookedCnt = 2000_000,
            title = "Test title7"
        ),

        )

    private val data = MutableLiveData(posts)
    override fun getAll(): LiveData<List<Post>> = data
    override fun likeById(id: Long) {
        var currPost = posts.last { it.id == id }
        currPost = currPost.copy(likedByMe = !currPost.likedByMe)
        val cnt = if (currPost.likedByMe) 1L else {
            if (currPost.likedCnt > 0) -1L else 0
        }
        changeCounters(id, "liked", cnt, currPost)

    }

    override fun shareById(id: Long) {

        changeCounters(id, "shared", 10, null)
    }

    private fun changeCounters(id: Long, type: String, summ: Long, thisPost: Post?) {
        var currPost = thisPost ?: posts.last { it.id == id }.copy()
        val finalSumm = if (summ == 0L) 1 else summ
        currPost = when (type) {
            "liked" -> {
                currPost.copy(likedCnt = currPost.likedCnt + finalSumm)
            }

            "looked" -> {
                currPost.copy(lookedCnt = currPost.lookedCnt + finalSumm)
            }

            else -> {
                currPost.copy(sharedCnt = currPost.sharedCnt + finalSumm)
            }
        }
        posts = posts.map {
            if (it.id != id) it else currPost
        }
        data.value = posts
    }


}