package ru.netology.nmedia.repository

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.getHumanDate

class PostRepositoryImpl : PostRepository {
    var posts = listOf( // Хранилище постов
        Post(
            1234,
            "Ansis",
            "This is content of Post1.",
            getHumanDate(System.currentTimeMillis()),
            false,
            lookedCnt = 90L,
            title = "Test title",
            video = "https://youtu.be/DA2S9UEGF7c"
        ),
        Post(
            2234,
            "Ansis2",
            "",
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
            title = "Test title3",
            video = "https://youtu.be/fTdZGgrY7aE?si=UxOZsGe9nOgb3rA9"
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

    private val data = MutableLiveData(posts) //контроллируемое хранилище
    override fun getAll(): LiveData<List<Post>> = data
    override fun likeById(id: Long) {
        var currPost = posts.last { it.id == id }
        currPost = currPost.copy(likedByMe = !currPost.likedByMe)
        val cnt = if (currPost.likedByMe) 1L else {
            if (currPost.likedCnt > 0) -1L else 0
        }
        changeCounters(id, "liked", cnt, currPost)

    }

    override fun shareById(id: Long, ctx: Context) {

        changeCounters(id, "shared", 10, null)
        onShare(id, ctx)
    }

    override fun save(post: Post) {
        posts = if (post.id == 0L) {

            listOf(

                post.copy(
                    id = posts.maxOf { it.id } + 1,
                    author = "Me",
                    likedByMe = false,
                    published = getHumanDate(System.currentTimeMillis()))

            ) + posts
        } else {
            posts.map {
                if (it.id != post.id) it else post.copy()
            }
        }
        data.value = posts

    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun onShare(id: Long, ctx: Context) { //поделиться
        val currPost = posts.last { it.id == id }
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, currPost.content)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, ctx.getString(R.string.chooser_share_post))
        shareIntent.setFlags(FLAG_ACTIVITY_NEW_TASK)
        ctx.startActivity(shareIntent)
    }

    override fun openInBrowser(urlVideo: String, ctx: Context) { //открыть ссылку в браузере
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlVideo))
        val shareIntent = Intent.createChooser(intent, ctx.getString(R.string.chooser_share_post))
        shareIntent.setFlags(FLAG_ACTIVITY_NEW_TASK)
        ctx.startActivity(shareIntent)
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