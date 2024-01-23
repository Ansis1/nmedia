package ru.netology.nmedia.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = "",
    title = "",
)

class PostViewModel : ViewModel() {

    private var repository: PostRepository = PostRepositoryImpl()
    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    fun save() { //сохранение
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun setEditedValue(edPost: Post) { // запись измененного значения
        edited.value = edPost
    }

    fun changeContent(content: String) { // изменение текста

        edited.value?.let {

            val text = content.trim()
            if (it.content == text) {
                return
            }
            repository.save(it.copy(content = text))
            edited.value = empty

        }

    }

    fun cancelEditing() { //отмена редактирования
        edited.value = empty
    }

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long, ctx: Context) = repository.shareById(id, ctx)
    fun openInBrowser(urlVideo: String, ctx: Context) = repository.openInBrowser(urlVideo, ctx)
    fun removeById(id: Long) = repository.removeById(id)
}

