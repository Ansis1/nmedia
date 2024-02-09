package ru.netology.nmedia.repository

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import androidx.lifecycle.map
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.PostDao
import ru.netology.nmedia.dto.PostEntity

class PostRepositorySQLiteRoomImpl(
    private val dao: PostDao,
    private val context: Context
) : PostRepository {

    override fun getAll() = dao.getAll().map { list ->
        list.map {
            it.toDto()
        }
    }


    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun getById(id: Long): Post {
        return dao.getById(id)
    }


    override fun shareById(id: Long) { // Действие поделиться
        dao.shareById(id)

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getById(id).content)
            type = "text/plain"
        }
        val shareIntent =
            Intent.createChooser(intent, context.getString(R.string.chooser_share_post))
        shareIntent.setFlags(FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    override fun save(post: Post) { //сохранить
        dao.save(PostEntity.fromDto(post))

    }

    override fun removeById(id: Long) { // Скрыть с экрана
        dao.removeById(id)
    }

    override fun openInBrowser(urlVideo: String) { //открыть ссылку в браузере
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlVideo))
        val shareIntent =
            Intent.createChooser(intent, context.getString(R.string.chooser_share_post))
        shareIntent.setFlags(FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }
}