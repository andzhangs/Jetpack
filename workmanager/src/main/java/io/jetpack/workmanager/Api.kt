package io.jetpack.workmanager

/**
 * @author zhangshuai@attrsense.com
 * @date 2023/6/25 19:06
 * @description
 */
class Api {

    fun interface APiService {
        fun getUser(userName: String): String
    }


}