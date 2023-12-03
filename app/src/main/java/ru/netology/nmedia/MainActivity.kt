package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tvTitlePost: TextView = findViewById(R.id.tv_titlepost)
        val tvDatePost: TextView = findViewById(R.id.tv_datepost)
        val tvTextPost: TextView = findViewById(R.id.tv_textpost)
        val tvLikesCnt: TextView = findViewById(R.id.tv_likes_cnt)
        val tvLookCnt: TextView = findViewById(R.id.tv_look_cnt)
        val tvShareCnt: TextView = findViewById(R.id.tv_share_cnt)

        tvTitlePost.setText("Очень длинный заголовок, много - много слов и букв. Совсем.")
        tvDatePost.setText("01 мая в 18:00")
        tvTextPost.setText(
            "AndroidX libraries are moving to a default minimum supported Android API " +
                    "level 19 (previously 14) starting with releases in October, 2023. According to Play " +
                    "Store check-in data, nearly all Android users have devices on API 19 or newer, so it" +
                    "’s no longer necessary to support legacy versions. This change will help AndroidX " +
                    "libraries maximize the potential number of users for app developers and aligns with " +
                    "Google Play Services and Android NDK."
        )
        tvLikesCnt.setText("123")
        tvLookCnt.setText("1K")
        tvShareCnt.setText("4K")

    }
}