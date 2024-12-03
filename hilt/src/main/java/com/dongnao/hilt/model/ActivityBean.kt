package com.dongnao.hilt.model

import android.util.Log
import androidx.fragment.app.FragmentActivity
import javax.inject.Inject

/**
 *
 * @author zhangshuai
 * @date 2023/12/23 17:33
 * @description 直接生成实例对象，每注入一次都是新对象
 *
 */
class ActivityBean @Inject constructor(
    private val activity: FragmentActivity
) {

    fun showCode() {
        Log.i("print_logs", "ActivityBean::: ${activity.hashCode()}")
    }
}