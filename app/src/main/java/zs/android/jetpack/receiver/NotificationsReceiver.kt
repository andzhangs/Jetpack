package zs.android.jetpack.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat

class NotificationsReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_CANCEL = "action_notification_cancelled"
        const val KEY_NOTIFY_ID = "key_notify_id"
        const val KEY_NOTIFY_MESSAGE = "key_notify_message"
    }

    override fun onReceive(context: Context, intent: Intent) {

        val notifyId = intent.getIntExtra(KEY_NOTIFY_ID, -1)
        if (notifyId != -1) {
            //停止通知，没有通知服务
            NotificationManagerCompat.from(context).cancel(notifyId)
        }

        val msg = intent.getStringExtra(KEY_NOTIFY_MESSAGE)
        Log.i("print_logs", "接收消息: $msg")


        if (intent.action == ACTION_CANCEL) {
            Log.i("print_logs", "onReceive: 处理滑动清除和点击删除事件!")
        }
    }
}