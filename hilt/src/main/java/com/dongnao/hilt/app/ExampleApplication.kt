package com.dongnao.hilt.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 *
 * @author zhangshuai
 * @date 2023/12/23 15:29
 * @description 自定义类描述
 */
@HiltAndroidApp
class ExampleApplication : Application() { //, Configuration.Provider {

//    override fun getWorkManagerConfiguration(): Configuration {
//        return Configuration.Builder()
//            .setMinimumLoggingLevel(Log.INFO)
//            .build()
//    }

    override fun onCreate() {
        super.onCreate()
    }
}