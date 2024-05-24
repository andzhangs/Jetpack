package io.jetpack.workmanager.demo.works

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author zhangshuai
 * @date 2022/4/4 星期一
 * @email zhangshuai@dushu365.com
 * @description
 */
open class ExpeditedWorker( private val mContext: Context, private val workerParameters: WorkerParameters) :
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
        if (NotificationManagerCompat.from(mContext).areNotificationsEnabled()) {
            //切换到前台服务状态
            setForeground(getForegroundInfo())

            Log.i("print_logs", "ExpeditedWorker::doWork: 显示通知")

        }else{
            Log.e("print_logs", "ExpeditedWorker::doWork: 启动通知失败")
        }

        withContext(Dispatchers.IO) {
            //观察工作器的中间进度 -> 更新进度
//            val firstUpdate = workDataOf(Progress to 0)
//            val lastUpdate = workDataOf(Progress to 100)
//            setProgress(firstUpdate)
//            delay(delayDuration)
//            setProgress(lastUpdate)

            val outPutData = Data.Builder().putString(KEY_DATA, "I am from ExpeditedWorker").build()
            Log.i("print_logs", "ExpeditedWorker::doWork: ${outPutData.getString(KEY_DATA)}")

            return@withContext Result.success(outPutData)
        }
        return Result.success()
    }

    /**
     * 使用CoroutineWorker时必须实现次方法
     */
    override suspend fun getForegroundInfo(): ForegroundInfo {
        Log.i("print_logs", "ExpeditedWorker::getForegroundInfo: ")

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            val notify= NotifyHelper.createNotification(mContext,"1002")
            NotificationManagerCompat.from(mContext).notify(1,notify)
        }

        return ForegroundInfo(2, NotifyHelper.createNotification(mContext,"1001"))
    }

}