package com.dongnao.hilt.model

import android.content.Context
import android.util.Log
import com.dongnao.hilt.MainActivity
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
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
@ActivityScoped
class ActivityContextBean @Inject constructor(
    @ActivityContext private val activityContext: Context
) {

    fun showCode() {
        Log.i("print_logs", "ActivityContextBean::: ${activityContext.hashCode()}")
        (activityContext as MainActivity).printMsg()
    }
}