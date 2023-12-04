package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources

class MainActivity : AppCompatActivity() {

    val counterMap = mutableMapOf<String, Long>()
    val counterStringsMap = mutableMapOf(Pair("looked", "1K"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvTitlePost: TextView = findViewById(R.id.tv_titlepost)
        val tvDatePost: TextView = findViewById(R.id.tv_datepost)
        val tvTextPost: TextView = findViewById(R.id.tv_textpost)
        val tvLikesCnt: TextView = findViewById(R.id.tv_likes_cnt)
        val tvLookCnt: TextView = findViewById(R.id.tv_look_cnt)
        val tvShareCnt: TextView = findViewById(R.id.tv_share_cnt)
        val lkeIb: ImageButton = findViewById(R.id.ib_liked)
        val shareIb: ImageButton = findViewById(R.id.ib_shared)
        tvLikesCnt.setText(counterStringsMap.get("liked") ?: "0")
        tvShareCnt.setText(counterStringsMap.get("shared") ?: "0")
        tvLookCnt.setText(counterStringsMap.get("looked") ?: "0")

        shareIb.setOnClickListener {

            reloadCntCounters("shared", 10, tvShareCnt);
        }

        var isLiked = false
        lkeIb.setOnClickListener {

            if (!isLiked) {
                isLiked = true
                lkeIb.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.liked))
                reloadCntCounters("liked", tvForChange = tvLikesCnt);
            } else {
                if ((counterMap.get("liked") ?: 0) > 0) {
                    reloadCntCounters("liked", -1, tvLikesCnt);
                }
                lkeIb.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.like))
                isLiked = false
            }

        }
        tvTitlePost.setText(getString(R.string.post_title))
        tvDatePost.setText(getString(R.string.post_date))
        tvTextPost.setText(getString(R.string.post_txt))

    }

    private fun reloadCntCounters(type: String, summ: Long = 1, tvForChange: TextView) {

        val prevCnt = counterMap.get(type) ?: 0
        val currCnt = prevCnt + summ
        counterMap.put(type, currCnt)
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
                counterStringsMap.put(type, finishString)
                tvForChange.text = finishString
            }

            currCnt > 9_999 -> {
                val finishString = "${stringNumb[0]}${stringNumb[1]}K"
                counterStringsMap.put(type, finishString)
                tvForChange.text = finishString

            }

            currCnt > 999 -> {

                val finishString = "${stringNumb[0]}${
                    if (stringNumb[1] != '0') {
                        "." + stringNumb[1]
                    } else {
                        ""
                    }
                }K"
                counterStringsMap.put(type, finishString)
                tvForChange.text = finishString

            }


            else -> {
                tvForChange.setText(stringNumb)
            }

        }

    }
}