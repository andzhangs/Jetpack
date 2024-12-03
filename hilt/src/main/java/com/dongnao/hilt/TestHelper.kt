package com.dongnao.hilt

import android.util.Log
import com.dongnao.hilt.di.AnalyticsService
import javax.inject.Inject
import javax.inject.Singleton

/**
 *
 * @author zhangshuai
 * @date 2024/12/3 11:14
 * @description 自定义类描述
 */
@Singleton
class TestHelper @Inject constructor(
    private val mAnalyticsService: AnalyticsService
) {

    fun loadPrint() {
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "TestHelper::>>>>> ${this.hashCode()} <<<<<< ")
            mAnalyticsService.load()
        }
    }

}