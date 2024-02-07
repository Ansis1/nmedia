package ru.netology.nmedia.repository

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.PostDao
import ru.netology.nmedia.utils.getHumanDate

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {

    object PostColumns {
        const val COLUMN_SHARED = "shared"
        const val TABLE = "posts"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_PUBLISHED = "published"
        const val COLUMN_LIKED_BY_ME = "likedByMe"
        const val COLUMN_LIKES = "likes"

        const val DLL = "likes"

        val ALL_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_AUTHOR,
            COLUMN_CONTENT,
            COLUMN_PUBLISHED,
            COLUMN_LIKED_BY_ME,
            COLUMN_LIKES,
            COLUMN_SHARED
        )
    }

    override fun getAll(): List<Post> {
        val posts = mutableListOf<Post>()
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "${PostColumns.COLUMN_ID} DESC"

        ).use {
            while (it.moveToNext()) {

                posts.add(map(it))
            }
        }
        return posts
    }

    override fun likeById(id: Long) {
        db.execSQL(
            """
           UPDATE ${PostColumns.TABLE} SET
           ${PostColumns.COLUMN_LIKES} = ${PostColumns.TABLE} + CASE WHEN 
           ${PostColumns.COLUMN_LIKED_BY_ME} THEN -1 ELSE 1 END,
           ${PostColumns.COLUMN_LIKED_BY_ME} = CASE WHEN ${PostColumns.COLUMN_LIKED_BY_ME} 
           THEN 0 ELSE 1 END
           WHERE ${PostColumns.COLUMN_ID} = ?
       """.trimIndent(), arrayOf(id)
        )
    }


    override fun save(post: Post): Post {

        val values = ContentValues().apply {
            val isNewPost = post.id != 0L
            if (isNewPost) {
                put(PostColumns.COLUMN_ID, post.id)
            }
            put(
                PostColumns.COLUMN_PUBLISHED,
                if (isNewPost) getHumanDate(System.currentTimeMillis()) else post.published
            )
            put(PostColumns.COLUMN_AUTHOR, "Ansis")
            put(PostColumns.COLUMN_CONTENT, post.content)
            put(PostColumns.COLUMN_LIKES, post.likedCnt)
            put(PostColumns.COLUMN_LIKED_BY_ME, if (post.likedByMe) 1 else 0)
            put(PostColumns.COLUMN_SHARED, post.sharedCnt)

        }

        val id = db.replace(PostColumns.TABLE, null, values)
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        ).use {
            it.moveToNext()
            return map(it)
        }
    }

    override fun removeById(id: Long) {
        db.delete(
            PostColumns.TABLE,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    override fun getById(id: Long): Post {
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        ).use {
            it.moveToNext()
            return map(it)
        }
    }

    private fun map(cursor: Cursor): Post { //Конвертация строки БД в пост.

        with(cursor) {
            return Post(
                id = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
                author = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
                title = "Title", //TODO
                content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
                published = getString(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED)),
                likedByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKED_BY_ME)) != 0,
                likedCnt = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES)),
                sharedCnt = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_SHARED)),
            )
        }


    }

    companion object { //Запросы на создание таблиц в БД
        var DDL = arrayOf(
            "CREATE TABLE ${PostColumns.TABLE} (" +
                    "${PostColumns.COLUMN_ID} INTEGER NOT NULL PRIMARY KEY," +
                    "${PostColumns.COLUMN_AUTHOR} TEXT," +
                    "${PostColumns.COLUMN_CONTENT} TEXT," +
                    "${PostColumns.COLUMN_PUBLISHED} TEXT," +
                    "${PostColumns.COLUMN_LIKES} INTEGER," +
                    "${PostColumns.COLUMN_SHARED} INTEGER," +
                    "${PostColumns.COLUMN_LIKED_BY_ME} INTEGER" +
                    ");"
        )

    }

}