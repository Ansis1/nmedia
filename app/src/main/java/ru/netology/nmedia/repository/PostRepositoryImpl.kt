package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.getHumanDate

class PostRepositoryImpl : PostRepository {

    var nPost = Post(
        1234,
        "Ansis",
        "This is content of Post.",
        getHumanDate(System.currentTimeMillis()),
        false,
        mutableMapOf(Pair("looked", 1_000)),
        "Test title"
    )

    private val data = MutableLiveData(nPost)
    override fun get(): LiveData<Post> = data
    override fun like() {
        nPost = nPost.copy(likedByMe = !nPost.likedByMe) //Меняем зн. для смены иконки
        val cnt = if (nPost.likedByMe) 1L else {
            if ((nPost.counterMap.get("liked") ?: 0) > 0) -1L else 0
        }
        changeCounters("liked", cnt)

    }

    override fun share() {
        changeCounters("shared", 10)
    }

    private fun changeCounters(type: String, summ: Long) {
        val prevCnt = nPost.counterMap.get(type) ?: 0
        val currCnt = prevCnt + (if (summ == 0L) 1 else summ)
        val nwPost = nPost.copy()
        nwPost.counterMap.put(type, currCnt)
        data.value = nwPost
    }


}