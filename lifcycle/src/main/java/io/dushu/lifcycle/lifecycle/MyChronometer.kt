package io.dushu.lifcycle.lifecycle

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.widget.Chronometer
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * author: zhangshuai 6/26/21 5:59 PM
 * email: zhangshuai@dushu365.com
 * mark: 计时器
 *
 * SystemClock.elapsedRealtime() 开机之后的时间
 */
open class MyChronometer(context: Context?, attrs: AttributeSet?) : Chronometer(context, attrs), DefaultLifecycleObserver{

    private var elapsedTime: Long = 0L

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        base = SystemClock.elapsedRealtime() - elapsedTime
        start()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        elapsedTime = SystemClock.elapsedRealtime() - base
        stop()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
    }
}