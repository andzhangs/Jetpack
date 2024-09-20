package io.jetpack.workmanager

/**
 * @author zhangshuai@attrsense.com
 * @date 2023/6/25 19:06
 * @description 函数式接口：fun interface, 中只能包含一个抽象方法。
 */
class Api {

    fun interface APiService {
        fun getUser(userName: String): String
    }


}