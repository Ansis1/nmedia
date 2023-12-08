package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val counterMap = mutableMapOf<String, Long>()
    val counterStringsMap = mutableMapOf(Pair("looked", "1K"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
        tvLikesCnt.setText(counterStringsMap.get("liked") ?: "0")
        tvShareCnt.setText(counterStringsMap.get("shared") ?: "0")
        tvLookCnt.setText(counterStringsMap.get("looked") ?: "0")
    }
        binding.ibShared.setOnClickListener {

            reloadCntCounters("shared", 10,binding.tvShareCnt);
        }

        var isLiked = false
        val lkeIb =  binding.ibLiked
        lkeIb.setOnClickListener {

            if (!isLiked) {
                isLiked = true
                lkeIb.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.liked))
                reloadCntCounters("liked", tvForChange = binding.tvLikesCnt);
            } else {
                if ((counterMap.get("liked") ?: 0) > 0) {
                    reloadCntCounters("liked", -1, binding.tvLikesCnt);
                }
                lkeIb.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.like))
                isLiked = false
            }

        }

        with(binding) {
            tvTitlepost.setText(getString(R.string.post_title))
            tvDatepost.setText(getString(R.string.post_date))
            tvTextpost.setText(getString(R.string.post_txt))
        }
    }

    private fun reloadCntCounters(type: String, summ: Long = 1, tvForChange: TextView) {

        val prevCnt = counterMap.get(type) ?: 0
        val currCnt = prevCnt + summ
        counterMap.put(type, currCnt)
        val stringNumb = currCnt.toString()

        when {

            currCnt > 9_99_999 -> {

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
                val finishString = "${stringNumb[0]}${stringNumb[1]}${stringNumb[2]}K"
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