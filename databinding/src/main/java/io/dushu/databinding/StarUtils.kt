package io.dushu.databinding

/**
 * author: zhangshuai 6/26/21 11:47 PM
 * email: zhangshuai@dushu365.com
 * mark:
 */
object StarUtils {

    fun getStar(level: Int): String = when (level) {
        1 -> "一星"
        2 -> "二星"
        3 -> "三星"
        4 -> "四星"
        5 -> "五星"
        else -> "差评"
    }

}