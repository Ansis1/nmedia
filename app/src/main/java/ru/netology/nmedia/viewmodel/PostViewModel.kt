package ru.netology.nmedia.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositorySQLiteRoomImpl
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    content = "",
    author = "Me", //TODO unhardcore
    likedByMe = false,
    likes = 0,
    published = 0,
    title = "",
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: PostRepository =
        PostRepositorySQLiteRoomImpl(application)
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data

    val edited = MutableLiveData(empty)

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() {

        thread {

            _data.postValue(FeedModel(loading = true))
            try {

                val posts = repository.getAll()

                FeedModel(posts = posts, empty = posts.isEmpty())

            } catch (e: IOException) {

                FeedModel(error = true)
            }.also(_data::postValue)

        }


    }

    fun save() {

        edited.value?.let {
            thread {

                repository.save(it)
                _postCreated.postValue(Unit)

            }

        }
        edited.value = empty
    }

    fun edit(edPost: Post) { // запись значения перед редактированием
        Log.i("e", "" + edPost.title)
        edited.value = edPost
    }

    fun changeContent(content: String) { // изменение текста
        val text = content.trim()

        edited.value?.let {


            if (it.content == text) {
                return
            }
            edited.value = it.copy(content = text)

        }

    }

    fun likeById(id: Long, isLiked: Boolean) {
        thread {
            val lkdPost = repository.likeById(id, isLiked)
            _data.postValue(
                _data.value?.copy(posts = _data.value?.posts.orEmpty()
                    .filter { it.id != lkdPost.id } + lkdPost
                ))
            repository.save(lkdPost)

        }
    }

    fun removeById(id: Long) {

        thread {
            // Оптимистичная модель
            val old = _data.value?.posts.orEmpty()
            _data.postValue(
                _data.value?.copy(posts = _data.value?.posts.orEmpty()
                    .filter { it.id != id }
                )
            )
            try {
                repository.removeById(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }
    }


    fun cancelEditing() { //отмена редактирования
        edited.value = empty
    }


    fun shareById(id: Long, content: String) = repository.shareById(id, content)
    fun openInBrowser(urlVideo: String) = repository.openInBrowser(urlVideo)

    fun getById(id: Long): Post =
        repository.getById(id)
}

