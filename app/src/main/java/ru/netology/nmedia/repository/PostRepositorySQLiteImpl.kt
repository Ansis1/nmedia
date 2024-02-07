package ru.netology.nmedia.repository

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.PostDao

class PostRepositorySQLiteImpl(
    private val dao: PostDao,
    private val context: Context
) : PostRepository {

    var posts = emptyList<Post>()
    private val data = MutableLiveData(posts) //контроллируемое хранилище

    init {
        posts = dao.getAll()
        data.value = posts
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

    override fun getById(id: Long): Post {
        return posts.last { it.id == id }
    }

    override fun shareById(id: Long) { // Действие поделиться

        changeCounters(id, "shared", 10, null)
        onShare(id)
    }

    override fun save(post: Post) { //сохранить
        val id = post.id
        val saved = dao.save(post)

        posts = if (id == 0L) {

            listOf(saved) + posts
        } else {
            posts.map {
                if (it.id != post.id) it else saved
            }
        }

        data.value = posts

    }

    override fun removeById(id: Long) { // Скрыть с экрана
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun onShare(id: Long) { //поделиться (действие)
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
        //TODO not need
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

        dao.save(currPost)
        posts = posts.map {
            if (it.id != id) it else currPost
        }
        data.value = posts
    }


}