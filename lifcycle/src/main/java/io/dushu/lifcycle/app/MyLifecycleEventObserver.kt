package io.dushu.lifcycle.app

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.dushu.lifcycle.BuildConfig

/**
 *
 * @author zhangshuai
 * @date 2024/12/11 14:51
 * @description 自定义类描述
 */
class MyLifecycleEventObserver : LifecycleEventObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (BuildConfig.DEBUG) {
            Log.d("print_logs", "MyLifecycleEventObserver::onStateChanged: $event")
        }
    }
}