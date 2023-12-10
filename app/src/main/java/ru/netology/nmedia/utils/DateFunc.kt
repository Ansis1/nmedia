package ru.netology.nmedia.utils

import android.os.Build
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun getHumanDate(timeInMs: Long): String { // перевод даты в ЧПУ-вид
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