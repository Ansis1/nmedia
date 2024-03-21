package ru.netology.nmedia.repository

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post
import java.util.concurrent.TimeUnit

class PostRepositorySQLiteRoomImpl(
    private val context: Context
) : PostRepository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeListPost = object : TypeToken<List<Post>>() {}
    private val typePost = object : TypeToken<Post>() {}

    companion object {

        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAll(): List<Post> {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()
        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, typeListPost.type)
            }

    }


    override fun likeById(id: Long, isLiked: Boolean): Post {

        val request: Request = Request.Builder().apply {
            if (isLiked) {
                delete()
            } else {
                post("".toRequestBody())
            }
            url("${BASE_URL}/api/slow/posts/$id/likes")

        }.build()
        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, typePost.type)

            }

    }

    override fun save(post: Post): Post { //сохранить
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, typePost)
            }
    }

    override fun removeById(id: Long) { // Скрыть с экрана
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }

    override fun getById(id: Long): Post {

        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()
        Log.i("post", "url ${BASE_URL}/api/slow/posts/$id")

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, typePost.type)
            }

    }


    override fun shareById(id: Long, content: String) { // поделиться

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, content)
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
}

