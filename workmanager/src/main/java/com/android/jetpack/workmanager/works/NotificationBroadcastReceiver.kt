package com.android.jetpack.workmanager.works

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import io.jetpack.workmanager.R

class NotificationBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val TYPE = "type"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.apply {
            intent?.also {
                val type = it.getIntExtra(TYPE, -1)
                if (type != -1) {
                    NotificationManagerCompat.from(this).cancel(type)
                }
                if (it.action == getString(R.string.action_notification_cancelled)) {
                    Log.i("print_logs", "onReceive: 处理滑动清除和点击删除事件！")
                }
            }
        }
    }
}