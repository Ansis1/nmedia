package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val counterMap = mutableMapOf<String, Long>()
    val counterStringsMap = mutableMapOf(Pair("looked", "1K"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var curPost = Post(
            1234,
            "Ansis",
            "AndroidX libraries are moving to a default minimum supported Android API level " +
                    "19 (previously 14) starting with releases in October, 2023. According to Play " +
                    "Store check-in data, nearly all Android users have devices on API 19 or newer, " +
                    "so it’s no longer necessary to support legacy versions. This change will help " +
                    "AndroidX libraries maximize the potential number of users for app developers " +
                    "and aligns with Google Play Services and Android NDK.",
            "01 мая в 18:00",
            false,
            mutableMapOf(Pair("looked", 1_000)),
            "Очень длинный заголовок, много - много слов и букв. Совсем."

        )
        with(binding) {
            tvLikesCnt.setText(getStringCounter(curPost.counterMap.get("liked") ?: 0))
            tvShareCnt.setText(getStringCounter(curPost.counterMap.get("shared") ?: 0))
            tvLookCnt.setText(getStringCounter(curPost.counterMap.get("looked") ?: 0))
        }
        binding.ibShared.setOnClickListener {
            val currSharedCnt = curPost.counterMap.get("shared") ?: 0
            curPost.counterMap.put("shared", 10 + currSharedCnt)
            binding.tvShareCnt.setText(getStringCounter(currSharedCnt + 10))
        }

        val lkeIb = binding.ibLiked
        lkeIb.setOnClickListener {

            curPost = curPost.copy(likedByMe = !curPost.likedByMe)
            lkeIb.setImageResource(if (curPost.likedByMe) R.drawable.liked else R.drawable.like)
            val currCntLike = curPost.counterMap.get("liked") ?: 0
            val cnt = currCntLike + if (curPost.likedByMe) 1L else {
                if (currCntLike > 0) -1L else 0
            }
            curPost.counterMap.put("liked", cnt)
            binding.tvLikesCnt.setText(getStringCounter(curPost.counterMap.get("liked") ?: 0))
        }


        with(binding) {
            tvTitlepost.setText(curPost.title)
            tvDatepost.setText(curPost.published)
            tvTextpost.setText(curPost.content)
        }
    }

    private fun getStringCounter(currCnt: Long): String {

        val stringNumb = currCnt.toString()

        return when {

            currCnt > 9_99_999 -> {

                "${stringNumb[0]}${
                    if (stringNumb[1] != '0') {
                        "." + stringNumb[1]
                    } else {
                        ""
                    }
                }M"
            }

            currCnt > 9_999 -> {
                "${stringNumb[0]}${stringNumb[1]}${stringNumb[2]}K"


            }

            currCnt > 999 -> {

                "${stringNumb[0]}${
                    if (stringNumb[1] != '0') {
                        "." + stringNumb[1]
                    } else {
                        ""
                    }
                }K"

            }

            else -> {
                stringNumb
            }

        }

    }
}