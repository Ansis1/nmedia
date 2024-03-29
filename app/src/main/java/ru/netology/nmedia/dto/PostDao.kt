package ru.netology.nmedia.dto

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nmedia.utils.getHumanDate

@Dao
interface PostDao {

    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>> // получить список Post

    @Insert
    fun insert(post: PostEntity)

    @Query("UPDATE PostEntity SET content = :content WHERE id = :id")
    fun updateContentById(id: Long, content: String)

    fun save(post: PostEntity) =
        if (post.id == 0L) insert(
            post.copy(
                author = "Ansis",
                published = getHumanDate(System.currentTimeMillis())
            )
        ) else updateContentById(post.id, post.content)

    @Query(
        """ UPDATE PostEntity SET 
        likedCnt = likedCnt + CASE WHEN likedByMe THEN -1 ELSE 1 END,
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id
        """
    )
    fun likeById(id: Long) //нажать понравилось

    @Query(
        """ UPDATE PostEntity SET 
        sharedCnt = sharedCnt + 10
        WHERE id = :id
        """
    )
    fun shareById(id: Long) //нажать понравилось

    @Query("DELETE FROM PostEntity WHERE id = :id")
    fun removeById(id: Long) // удалить пост (скрыть из списка)

    @Query("SELECT * FROM PostEntity WHERE id = :id")
    fun getById(id: Long): Post
}