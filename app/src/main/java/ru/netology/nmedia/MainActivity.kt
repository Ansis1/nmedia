package ru.netology.nmedia

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->
            with(binding) {
                tvTextpost.setText(post.content)
                tvTitlepost.setText(post.title)
                tvDatepost.setText(post.published.toString())
                tvLikesCnt.setText(post.counterStringsMap.get("liked") ?: "0")
                tvLookCnt.setText(post.counterStringsMap.get("looked") ?: "0")
                tvShareCnt.setText(post.counterStringsMap.get("shared") ?: "0")
                ibLiked.setImageResource(if (post.likedByMe) R.drawable.liked else R.drawable.like)
            }

        }

        binding.ibShared.setOnClickListener {

            viewModel.share()
        }

        binding.ibLiked.setOnClickListener {

            viewModel.like()

        }

    }


}

class PostRepositoryImpl : PostRepository {

    var nPost = Post(
        1234,
        "Ansis",
        "This is content of Post.",
        getHumanDate(System.currentTimeMillis()),
        false,
        mutableMapOf(Pair("1", 0L)),
        mutableMapOf(Pair("looked", "1K")),
        "Test title"
    )

    private fun getHumanDate(timeInMs: Long): String { // перевод даты в ЧПУ-вид
        val pattern = "dd MMM HH:mm"
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formatter = DateTimeFormatter.ofPattern(pattern);
            formatter.format(
                LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(timeInMs),
                    ZoneId.systemDefault()
                )
            )
        } else {
                SimpleDateFormat(pattern).format(timeInMs).toString()
        }
    }

    private val data = MutableLiveData(nPost)
    override fun get(): LiveData<Post> = data
    override fun like() {
        nPost = nPost.copy(likedByMe = !nPost.likedByMe) //Меняем зн. для смены иконки
        val cnt = if (nPost.likedByMe) 1L else {
            if ((nPost.counterMap.get("liked") ?: 0) > 0) -1L else 0
        }
        reloadCntCounters("liked", cnt)
    }

    override fun share() {
        reloadCntCounters("shared", 10);
    }

    override fun reloadCntCounters(type: String, summ: Long) {
        val currPost: Post = nPost.copy()
        val prevCnt = currPost.counterMap.get(type) ?: 0
        val currCnt = prevCnt + (if (summ == 0L) 1 else summ)
        currPost.counterMap.put(type, currCnt)
        val stringNumb = currCnt.toString()
        when {

            currCnt > 999_000 -> {

                val finishString = "${stringNumb[0]}${
                    if (stringNumb[1] != '0') {
                        "." + stringNumb[1]
                    } else {
                        ""
                    }
                }M"
                currPost.counterStringsMap.put(type, finishString)
            }

            currCnt > 9_999 -> {
                val finishString = "${stringNumb[0]}${stringNumb[1]}K"
                currPost.counterStringsMap.put(type, finishString)

            }

            currCnt > 999 -> {

                val finishString = "${stringNumb[0]}${
                    if (stringNumb[1] != '0') {
                        "." + stringNumb[1]
                    } else {
                        ""
                    }
                }K"
                currPost.counterStringsMap.put(type, finishString)
            }

            else -> currPost.counterStringsMap.put(type, stringNumb)
        }

        data.value = currPost
    }
}

interface PostRepository {
    fun get(): LiveData<Post> // получить объект Post
    fun like() //нажать понравилось
    fun share() //поделиться
    fun reloadCntCounters(type: String, summ: Long) //перезалить счетчики
}

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val counterMap: MutableMap<String, Long>,
    val counterStringsMap: MutableMap<String, String>,
    val title: String
)

class PostViewModel : ViewModel() {

    private var repository: PostRepository = PostRepositoryImpl()
    val data = repository.get()
    fun like() = repository.like()
    fun share() = repository.share()
}