package io.jetpack.workmanager.demo.works

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import io.jetpack.workmanager.R

/**
 *
 * @author zhangshuai
 * @date 2024/5/24 18:06
 * @description 自定义类描述
 */
object NotifyHelper {

    private val CHANNEL_NAME = "后台任务通知"

    @JvmStatic
    fun createNotification(context: Context,notificationManager: NotificationManager, channelId: String): NotificationCompat.Builder {
//        val mNotificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                //通知类别，在手机设置的应用程序中对应的APP的"通知"中可见
                "${CHANNEL_NAME}_work",
                //方式一 重要性（Android 8.0 及更高版本）
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                //设置渠道描述
                description = "encode media [image or video]"
                //设置提示音
//                setSound()
                //开启指示灯
                enableLights(false)
                //开启震动
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
                //设置锁屏展示
                lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                //设置是否显示角标
                setShowBadge(false)

                //方式二
//                importance = NotificationManager.IMPORTANCE_HIGH  //重要性（Android 8.0 及更高版本）
            }
            notificationManager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(context, channelId)
            //通知栏的左上角小图标
            .setSmallIcon(R.mipmap.ic_launcher_round)
            //通知栏右边内容图标
//            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_foreground))
            .setContentTitle("压缩任务：$channelId") ///$mProgressMax
//            .setContentText("50/100")
            //告知系统该通知应具有的“干扰性”。当发出此类型的通知时，通知会以悬挂的方法显示在屏幕上. 优先级（Android 7.1 及更低版本）
            .setPriority(NotificationCompat.PRIORITY_MAX)
            //点击通知后消失
            .setAutoCancel(false)
            // 除非app死掉或者在代码中取消，否则都不会消失。
            .setOngoing(false)
            // DEFAULT_VIBRATE  添加默认震动提醒  需要VIBRATE permission; DEFAULT_SOUND  添加默认声音提醒; DEFAULT_LIGHTS  添加默认三色灯提醒;  DEFAULT_ALL  添加默认以上3种全部提醒
            .setDefaults(Notification.DEFAULT_ALL)
            //通知是否在锁定屏幕上显示的方法 (setVisibility())，以及指定通知文本的“公开”版本的方法。
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            //设置震动，需要配置权限(android.permission.VIBRATE)
            .setVibrate(longArrayOf(100, 200, 300, 400, 500))
            //设置呼吸灯
//            .setLights(Color.GREEN, 300, 0)
            //通知栏首次出现在通知栏，带上动画效果
            .setTicker("通知到达的时候会在状态栏上方直接显示通知")
            .setAllowSystemGeneratedContextualActions(true)
            //通知栏时间，一般是直接用系统的
            .setWhen(System.currentTimeMillis())
            .setShowWhen(true)
            //通知只会在通知首次出现时打断用户（通过声音、振动或视觉提示），而之后更新则不会再打断用户。
            .setOnlyAlertOnce(true)
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            notificationManagerCompat.notify(notifyId, notificationCompat.build())
//        }
//            .build()
    }

}