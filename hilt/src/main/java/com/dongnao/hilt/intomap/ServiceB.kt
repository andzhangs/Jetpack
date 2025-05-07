package com.dongnao.hilt.intomap

import android.util.Log
import javax.inject.Inject

/**
 *
 * @author zhangshuai
 * @date 2025/5/7 10:25
 * @description 自定义类描述
 */
class ServiceB @Inject constructor() : Service {
    override fun perform(): String {
        return "ServiceB::perform."
    }
}