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
 * 使用 @ActivityScoped 将 AnalyticsAdapter 的作用域限定为 ActivityComponent，
 * Hilt 会在相应 activity 的整个生命周期内提供 AnalyticsAdapter 的同一实例
 */
class ActivityBean @Inject constructor(
    private val activity: FragmentActivity
) {

    fun showCode() {
        Log.i("print_logs", "ActivityBean::: ${activity.hashCode()}")
    }
}