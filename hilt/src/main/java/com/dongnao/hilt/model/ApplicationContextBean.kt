package com.dongnao.hilt.model

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 *
 * @author zhangshuai
 * @date 2023/12/23 17:33
 * @description 直接生成实例对象，每注入一次都是新对象
 */
class ApplicationContextBean @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) {

    fun showCode() {
        Log.i("print_logs", "ApplicationContextBean::: ${applicationContext.hashCode()}")
    }

    fun printLog(): String {
        return "ApplicationContextBean::printLog"
    }
}