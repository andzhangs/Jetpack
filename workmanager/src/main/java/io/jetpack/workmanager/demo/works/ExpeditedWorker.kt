package io.jetpack.workmanager.demo.works

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.*
import io.jetpack.workmanager.MainActivity
import io.jetpack.workmanager.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * @author zhangshuai
 * @date 2022/4/4 星期一
 * @email zhangshuai@dushu365.com
 * @description
 */
open class ExpeditedWorker(private val mContext: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(mContext, workerParameters) {

    companion object {
        @JvmStatic
        val KEY_DATA = "key_data"

        const val Progress = "progress"
        private const val delayDuration = 1L
    }

    /**
     * A suspending method to do your work.
     * <p>
     * To specify which [CoroutineDispatcher] your work should run on, use `withContext()`
     * within `doWork()`.
     * If there is no other dispatcher declared, [Dispatchers.Default] will be used.
     * <p>
     * A CoroutineWorker is given a maximum of ten minutes to finish its execution and return a
     * [ListenableWorker.Result].  After this time has expired, the worker will be signalled to
     * stop.
     *
     * @return The [ListenableWorker.Result] of the result of the background work; note that
     * dependent work will not execute if you return [ListenableWorker.Result.failure]
     */
    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO) {
            //
            //观察工作器的中间进度 -> 更新进度
            val firstUpdate = workDataOf(Progress to 0)
            val lastUpdate = workDataOf(Progress to 100)
            setProgress(firstUpdate)
            delay(delayDuration)
            setProgress(lastUpdate)

            val outPutData = Data.Builder().putString(KEY_DATA, "I am from ExpeditedWorker").build()
            Log.i("print_logs", "ExpeditedWorker::doWork: ${outPutData.getString(KEY_DATA)}")
            try {
                setForeground(getForegroundInfo())
            } catch (e: Exception) {
                //ForegroundServiceStartNotAllowedException
                Log.e("print_logs", "ExpeditedWorker::doWork: $e")
            }
            return@withContext Result.success(outPutData)
        }
        return Result.failure()
    }

    /**
     * 使用CoroutineWorker时必须实现次方法
     */
    override suspend fun getForegroundInfo(): ForegroundInfo {
        val pendingIntent =
            PendingIntent.getActivity(mContext, 0, Intent(mContext, MainActivity::class.java), 0)
        return ForegroundInfo(1, createNotification(pendingIntent))
    }

    private fun createNotification(pendingIntent: PendingIntent): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                mContext.packageName,
                "下载文件通知",  //通知类别，在手机设置的应用程序中对应的APP的"通知"中可见
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "My Notification Channel"
                enableLights(true)
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
                lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                setShowBadge(true) //设置是否显示角标
            }
            val notificationManager = mContext.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        return NotificationCompat.Builder(mContext, mContext.packageName)
            .setSmallIcon(R.drawable.ic_launcher_foreground) //通知栏的左上角小图标
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    mContext.resources,
                    R.drawable.ic_launcher_foreground
                )
            ) //通知栏右边内容图标
            .setContentTitle("下载完成")
            .setContentText("点击安装")
            .setPriority(NotificationCompat.PRIORITY_MAX) //当发出此类型的通知时，通知会以悬挂的方法显示在屏幕上
            .setAutoCancel(true) //点击通知后清除
            .setOngoing(true) // 除非app死掉或者在代码中取消，否则都不会消失。
            .setDefaults(Notification.DEFAULT_ALL)// DEFAULT_VIBRATE  添加默认震动提醒  需要VIBRATE permission; DEFAULT_SOUND  添加默认声音提醒; DEFAULT_LIGHTS  添加默认三色灯提醒;  DEFAULT_ALL  添加默认以上3种全部提醒
            .setVibrate(
                longArrayOf(
                    100,
                    200,
                    300,
                    400,
                    500
                )
            ) //设置震动，需要配置权限(android.permission.VIBRATE)
            .setLights(Color.RED, 300, 0) //设置呼吸灯
            .setTicker("通知到达的时候会在状态栏上方直接显示通知")
            .setAllowSystemGeneratedContextualActions(true)
            .setWhen(System.currentTimeMillis())
            .setShowWhen(true)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("在APP中的开发中会用到提醒，通知类似的功能，之前一篇文章我已经介绍到了[**《Android使用NotificationListenerService监听手机收到的通知》**](https://zhuanlan.zhihu.com/p/62380569)，监听了通知，没有通知的实现")
            )
//            .setStyle(
//                NotificationCompat.InboxStyle()
//                    .addLine("1、在APP中的开发中会用到提醒，通知类似的功能")
//                    .addLine("2、使用NotificationListenerService监听")
//                    .addLine("3、监听了通知，没有通知的实现")
//            )
            //方式一
            .addAction(R.drawable.ic_launcher_foreground, "点击安装", pendingIntent) //设置意图
            //方式二
//                .setFullScreenIntent(pendingIntent,false)  //不建议使用这个方法，华为小米适配有问题
            //方式三
//            .setContentIntent(pendingIntent)
            .build()
    }
}