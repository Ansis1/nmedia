package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositorySQLiteRoomImpl

private val empty = Post(
    id = 0,
    content = "",
    author = "Me", //TODO unhardcore
    likedByMe = false,
    published = "",
    title = "",
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: PostRepository =
        PostRepositorySQLiteRoomImpl(AppDb.getInstance(application).postDao(), application)
    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    fun setEditedValue(edPost: Post) { // запись значения перед редактированием
        edited.value = edPost
    }

    fun changeContent(content: String) { // изменение текста
        edited.value?.let {

            val text = content.trim()
            if (it.content != text) {
                repository.save(it.copy(content = text))
            }
            edited.value = empty

        }

    }

    fun cancelEditing() { //отмена редактирования
        edited.value = empty
    }

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun openInBrowser(urlVideo: String) = repository.openInBrowser(urlVideo)
    fun removeById(id: Long) = repository.removeById(id)
    fun getById(id: Long) = repository.getById(id)
}

