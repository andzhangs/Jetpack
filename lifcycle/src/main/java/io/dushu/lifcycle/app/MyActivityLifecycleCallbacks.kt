package io.dushu.lifcycle.app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log

/**
 *
 * @author zhangshuai
 * @date 2024/12/11 14:37
 * @description 自定义类描述
 */
class MyActivityLifecycleCallbacks private constructor(): Application.ActivityLifecycleCallbacks {

    companion object{
        @JvmStatic
        fun newInstance(): MyActivityLifecycleCallbacks{
            return MyActivityLifecycleCallbacks()
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.w("print_logs", "MyActivityLifecycleCallbacks::onActivityCreated: ${activity::class.java.simpleName}")
    }

    override fun onActivityStarted(activity: Activity) {
        Log.w("print_logs", "MyActivityLifecycleCallbacks::onActivityStarted: ${activity::class.java.simpleName}")
    }

    override fun onActivityResumed(activity: Activity) {
        Log.w("print_logs", "MyActivityLifecycleCallbacks::onActivityResumed: ${activity::class.java.simpleName}")
    }

    override fun onActivityPaused(activity: Activity) {
        Log.w("print_logs", "MyActivityLifecycleCallbacks::onActivityPaused: ${activity::class.java.simpleName}")
    }

    override fun onActivityStopped(activity: Activity) {
        Log.w("print_logs", "MyActivityLifecycleCallbacks::onActivityStopped: ${activity::class.java.simpleName}")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log.w("print_logs", "MyActivityLifecycleCallbacks::onActivitySaveInstanceState: ${activity::class.java.simpleName}")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.w("print_logs", "MyActivityLifecycleCallbacks::onActivityDestroyed: ${activity::class.java.simpleName}")
    }
}