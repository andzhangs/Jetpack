package zs.android.jetpack.app

import android.app.Application

/**
 *
 * @author zhangshuai
 * @date 2024/4/22 13:35
 * @description 自定义类描述
 */
class JetpackApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        System.loadLibrary("jetpack")
    }
}