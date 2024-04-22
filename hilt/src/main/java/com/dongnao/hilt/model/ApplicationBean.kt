package com.dongnao.hilt.model

import android.app.Application
import android.util.Log
import javax.inject.Inject

/**
 *
 * @author zhangshuai
 * @date 2023/12/23 17:33
 * @description 直接生成实例对象，每注入一次都是新对象
 */
class ApplicationBean @Inject constructor(
    private val application: Application
) {

    fun showCode() {
        Log.i("print_logs", "ApplicationBean::: ${application.hashCode()}")
    }
}