package ru.netology.nmedia.repository

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.getHumanDate

class PostRepositoryFileImpl(
    private val context: Context,
) : PostRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val filename = "posts.json"
    var posts = emptyList<Post>()
    private val data = MutableLiveData(posts) //контроллируемое хранилище

    init {
        val file = context.filesDir.resolve(filename)
        if (file.exists()) {
            try {
                context.openFileInput(filename).bufferedReader().use {
                    posts = gson.fromJson(it, type)
                }
            } catch (e: Exception) {

                sync()
            }
        } else {

            sync()
        }


    }

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
        onShare(id)
    }

    override fun save(post: Post) {
        posts = if (post.id == 0L) {

            listOf(
                post.copy(
                    id = if (posts.isEmpty()) {
                        1
                    } else posts.maxOf { it.id } + 1,
                    author = "Me",
                    likedByMe = false,
                    published = getHumanDate(System.currentTimeMillis()))

            ) + posts
        } else {
            posts.map {
                if (it.id != post.id) it else post.copy()
            }
        }
        sync()
        data.value = posts

    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        sync()
        data.value = posts
    }

    override fun onShare(id: Long) { //поделиться
        val currPost = posts.last { it.id == id }
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, currPost.content)
            type = "text/plain"
        }
        val shareIntent =
            Intent.createChooser(intent, context.getString(R.string.chooser_share_post))
        shareIntent.setFlags(FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    override fun openInBrowser(urlVideo: String) { //открыть ссылку в браузере
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlVideo))
        val shareIntent =
            Intent.createChooser(intent, context.getString(R.string.chooser_share_post))
        shareIntent.setFlags(FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    override fun sync() {
        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
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
        sync()
    }


}