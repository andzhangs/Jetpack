package io.jetpack.workmanager.demo.works

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

/**
 * @author zhangshuai
 * @date 2022/4/4 星期一
 * @email zhangshuai@dushu365.com
 * @description
 */
open class ExpeditedWorker(
    private val mContext: Context,
    private val workerParameters: WorkerParameters
) :
    CoroutineWorker(mContext, workerParameters) {

    companion object {
        @JvmStatic
        val KEY_DATA = "key_data"

        const val Progress = "progress"
        private const val delayDuration = 1000L
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

    private val mNotifyManager by lazy { mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        if (NotificationManagerCompat.from(mContext.applicationContext).areNotificationsEnabled()) {
            Log.i("print_logs", "显示通知:")
            //切换到前台服务状态
            val info = getForegroundInfo()
//            mNotifyManager.notify(info.notificationId,info.notification)
            setForeground(info)

        } else {
            Log.e("print_logs", "显示通知失败!!!")
        }

        repeat(10) {
            mNotifyManager.notify(2,
                mNotificationCompat.apply {
                    setContentTitle("当前进度：$it")
                    setTicker("哈哈哈：$it")
                }.build()
            )
            delay(1000L)
        }

        //观察工作器的中间进度 -> 更新进度
        val firstUpdate = workDataOf(Progress to 0)
        val lastUpdate = workDataOf(Progress to 100)
        setProgress(firstUpdate)
        delay(delayDuration)
        setProgress(lastUpdate)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val notifyList = mNotifyManager.activeNotifications

            Log.i("print_logs", "notifyList: ${notifyList.size}")

            if (notifyList.isNotEmpty()) {
                notifyList.forEachIndexed { index, item ->
                    Log.i("print_logs", "notifyList: $index, ${item.id}")
                }
            } else {

            }
        } else {

        }

        val outPutData = Data.Builder().putString(KEY_DATA, "I am from ExpeditedWorker").build()


        return Result.success(outPutData)
    }

    private val mNotificationCompat by lazy {
        NotifyHelper.createNotification(
            mContext.applicationContext,
            mNotifyManager,
            "10086"
        )
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            2,
            mNotificationCompat.build()
        )
    }
}