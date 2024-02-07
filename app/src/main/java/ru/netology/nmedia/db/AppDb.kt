package ru.netology.nmedia.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.dto.PostDao
import ru.netology.nmedia.repository.PostDaoImpl

class AppDb private constructor(db: SQLiteDatabase) {
    val postDao: PostDao = PostDaoImpl(db)

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: AppDb(
                    buildDatabase(context, PostDaoImpl.DDL)
                ).also { instance = it }

            }
        }

        private fun buildDatabase(context: Context, DDLs: kotlin.Array<String>) = DBHelper(
            context, 1, "app.db", DDLs,
        ).writableDatabase


    }
}