package ru.netology.nmedia.service

import android.annotation.SuppressLint
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.R
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Action
import ru.netology.nmedia.dto.Like
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepositorySQLiteRoomImpl
import kotlin.random.Random

class FCMService : FirebaseMessagingService() {

    private val action = "action"
    private val content = "content"
    private val post = "post"
    private val gson = Gson()

    override fun onMessageReceived(message: RemoteMessage) {

        if (!message.data.isEmpty()) {

            message.data[action]?.let {

                when (Action.valueOf(it)) {
                    Action.LIKE -> message.data[content]?.let { it1 -> handleMessage(1, it1) }

                    Action.NEWPOST -> message.data[post]?.let { it1 -> handleMessage(2, it1) }
                }

            }

        }
        Log.println(Log.INFO, "new msg received firebase", Gson().toJson(message))
    }

    private fun handleMessage(type: Int, data: String) {

        try {

            when (type) {
                1 -> {
                    val dataLike = gson.fromJson(
                        data,
                        Like::class.java
                    )
                    val map = mutableMapOf<String, String>()
                    map["author"] = dataLike.postAuthor
                    map["content"] = dataLike.postId.toString()
                    createNotify(1, map)

                }

                2 -> {
                    val post = gson.fromJson(
                        data,
                        Post::class.java
                    )

                    PostRepositorySQLiteRoomImpl(
                        //AppDb.getInstance(application).postDao(),
                        applicationContext
                    ).save(post)

                    val contentLines = post.content.split(".")
                    val linecCnt = contentLines.size

                    val map = mutableMapOf<String, String>()
                    map["author"] = post.author

                    when  {

                        linecCnt >= 2 -> {
                            map["content1"] = contentLines[0]
                            map["content2"] = contentLines[1] + "..."
                        }

                        else -> {
                            map["content1"] = contentLines[0] + "..."
                            map["content2"] = ""

                        }
                    }
                    createNotify(2, map)

                }

            }

        } catch (e: Exception) { // перехват если нет в списке Enum.

            when (e) {
                is NumberFormatException -> {
                    Log.println(
                        Log.ERROR,
                        "onMessageReceived",
                        "will not be processed: bad data ${e.localizedMessage}"
                    )
                }

                is IllegalArgumentException -> {
                    Log.println(
                        Log.ERROR,
                        "onMessageReceived",
                        "get action out of enum types. Will not be processed. ${e.localizedMessage}"
                    )
                }

                is NullPointerException -> {
                    Log.println(
                        Log.ERROR,
                        "onMessageReceived",
                        "will not be processed: not any parameters ${e.localizedMessage}"
                    )

                }

                else -> Log.println(
                    Log.ERROR,
                    "onMessageReceived",
                    "will not be processed: undefined error ${e.suppressed}"
                )

            }

        }
    }

    @SuppressLint("MissingPermission")
    private fun createNotify(type: Int, map: Map<String, String>) {

        when (type) {
            1 -> {
                val notification =
                    NotificationCompat.Builder(this, getString(R.string.channel1Id))
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(
                            getString(
                                R.string.notification_new_like_title
                            )
                        )
                        .setContentText(
                            getString(
                                R.string.notification_new_like,
                                map.get("author"),
                                map.get("content")
                            )

                        )
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .build()

                NotificationManagerCompat.from(this)
                    .notify(Random.nextInt(10_000), notification)
            }

            2 -> {
                val notification =
                    NotificationCompat.Builder(this, getString(R.string.channel1Id))
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(
                            getString(
                                R.string.notification_new_post,
                                map["author"]
                            )
                        )
                        .setContentText(map["content1"])
                        .setStyle(
                            NotificationCompat.InboxStyle()
                                .addLine(map["content1"])
                                .addLine(map["content2"])
                        )
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .build()

                NotificationManagerCompat.from(this)
                    .notify(Random.nextInt(10_000), notification)
            }
        }
    }


    override fun onNewToken(token: String) {
        Log.println(Log.INFO, "new token firebase", token)
    }
}