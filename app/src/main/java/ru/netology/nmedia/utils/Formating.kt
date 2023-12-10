package ru.netology.nmedia.utils

fun reloadCntCounters(count: Long): String {
    val stringNumb = count.toString()
    return when {

        count > 9_99_999 -> {

            "${stringNumb[0]}${
                if (stringNumb[1] != '0') {
                    "." + stringNumb[1]
                } else {
                    ""
                }
            }M"
        }

        count > 9_999 -> {
            "${stringNumb[0]}${stringNumb[1]}${stringNumb[2]}K"

        }

        count > 999 -> {

            "${stringNumb[0]}${
                if (stringNumb[1] != '0') {
                    "." + stringNumb[1]
                } else {
                    ""
                }
            }K"

        }

        else -> stringNumb
    }

}