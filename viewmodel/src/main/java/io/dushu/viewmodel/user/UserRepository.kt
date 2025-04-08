package io.dushu.viewmodel.user

/**
 *
 * @author zhangshuai
 * @date 2025/4/8 10:13
 * @description 自定义类描述
 */
class UserRepository {
    fun getUserId() = "userId:${System.currentTimeMillis()}"
}