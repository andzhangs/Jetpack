package io.dushu.lifcycle.app

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import io.dushu.lifcycle.BuildConfig

/**
 * author: zhangshuai 6/26/21 7:39 PM
 * email: zhangshuai@dushu365.com
 * mark:
 *  1、针对整个应用程序，与Activity的数量无关
 *  2、ON_CREATE：只会被调用一次，
 *  3、ON_DESTROY：永远不会调用
 */
internal class MyApplicationObserver : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MyApplicationObserver::onCreate: ")
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MyApplicationObserver::onStart: ")
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MyApplicationObserver::onResume: ")
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MyApplicationObserver::onPause: ")
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MyApplicationObserver::onStop: ")
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MyApplicationObserver::onDestroy: ")
        }
    }
}