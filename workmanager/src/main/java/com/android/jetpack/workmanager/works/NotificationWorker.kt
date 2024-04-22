package com.android.jetpack.workmanager.works

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.await
import io.jetpack.workmanager.R
import io.jetpack.workmanager.base.BaseActivity
import kotlin.random.Random

/**
 * @author zhangshuai@attrsense.com
 * @date 2023/7/3 15:32
 * @description
 */
class NotificationWorker(val context: Context, val workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    companion object {

        private val encodeRequest: OneTimeWorkRequest by lazy {
            //添加约束
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)   //若为 true，那么当设备处于“电量不足模式”时，工作不会运行。
                .setRequiresStorageNotLow(true)   //若为 true，那么当设备上的存储空间不足时，工作不会运行。
                .build()

            //构建任务
            OneTimeWorkRequest.Builder(NotificationWorker::class.java)
                .setConstraints(constraints)
                .addTag(NotificationWorker::class.java.simpleName)
                .build()
        }

        @JvmStatic
        fun start(context: Context) {
            val notificationManagerCompat = NotificationManagerCompat.from(context)
            if (notificationManagerCompat.areNotificationsEnabled()) {
                //添加约束
                val constraints = Constraints.Builder()
                    .setRequiresBatteryNotLow(true)   //若为 true，那么当设备处于“电量不足模式”时，工作不会运行。
                    .setRequiresStorageNotLow(true)   //若为 true，那么当设备上的存储空间不足时，工作不会运行。
                    .build()

                //构建任务
               val request= OneTimeWorkRequest.Builder(NotificationWorker::class.java)
                    .setConstraints(constraints)
                    .addTag(NotificationWorker::class.java.simpleName)
                    .build()

                WorkManager.getInstance(context).enqueue(request)
            } else {
                Intent().apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    try {
                        action = Settings.ACTION_APP_NOTIFICATION_SETTINGS

                        //8.0及以后版本使用这两个extra.  >=API 26
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        putExtra(Settings.EXTRA_CHANNEL_ID, context.applicationInfo.uid)

                        //5.0-7.1 使用这两个extra.  <= API 25, >=API 21
                        putExtra("app_package", context.packageName)
                        putExtra("app_uid", context.applicationInfo.uid)

                        context.startActivity(this)
                    } catch (e: Exception) {
                        e.printStackTrace()

                        //其他低版本或者异常情况，走该节点。进入APP设置界面
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        putExtra("package", context.packageName)

                        //val uri = Uri.fromParts("package", packageName, null)
                        //data = uri
                        context.startActivity(this)
                    }
                }
            }
        }

        @JvmStatic
        fun stop(context: Context) {
            WorkManager.getInstance(context)
                .cancelAllWorkByTag(NotificationWorker::class.java.simpleName)
        }

        @JvmStatic
        fun setWorkListener(activity: BaseActivity, block: (WorkInfo) -> Unit) {
            WorkManager.getInstance(activity)
                .getWorkInfosByTagLiveData(NotificationWorker::class.java.simpleName)
                .observe(activity) {
                    if (it.isNotEmpty()) {
                        it[0]?.also { workInfo ->
                            block(workInfo)
                        }
                    }
                }
        }
    }

    override suspend fun doWork(): Result {
        Log.i("print_logs", "NotificationWorker::doWork: ")
        foregroundInfoAsync.await()

        //生成Data返回数据
//        workDataOf("" to "")
        return Result.success()
    }

    private var mNotificationManagerCompat: NotificationManagerCompat? = null
    private var mNotification: NotificationCompat.Builder? = null
    private val mNotifyId: Int = 100
    private var mProgressMax: Int = 0
    private var mCurrentProgress = 0

    override suspend fun getForegroundInfo(): ForegroundInfo {

        mNotificationManagerCompat = NotificationManagerCompat.from(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                context.packageName,
                //通知类别，在手机设置的应用程序中对应的APP的"通知"中可见
                "压缩图片通知",
                //方式一 重要性（Android 8.0 及更高版本）
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                //设置渠道描述
                description = "encode to .anf"
                //设置提示音
//                setSound()
                //开启指示灯
                enableLights(true)
                //开启震动
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
                //设置锁屏展示
                lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                //设置是否显示角标
                setShowBadge(true)

                //方式二
//                importance = NotificationManager.IMPORTANCE_HIGH  //重要性（Android 8.0 及更高版本）

                context.getSystemService(NotificationManager::class.java)
                    .createNotificationChannel(this)
            }
        }

        mNotification = NotificationCompat.Builder(context, context.packageName)
            //通知栏的左上角小图标
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            //通知栏右边内容图标
//            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_foreground))
            .setContentTitle("新消息")
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
            .setLights(Color.GREEN, 300, 0)
            //通知栏首次出现在通知栏，带上动画效果
            .setTicker("通知到达的时候会在状态栏上方直接显示通知")
            .setAllowSystemGeneratedContextualActions(true)
            //通知栏时间，一般是直接用系统的
            .setWhen(System.currentTimeMillis())
            .setShowWhen(true)
            //通知只会在通知首次出现时打断用户（通过声音、振动或视觉提示），而之后更新则不会再打断用户。
            .setOnlyAlertOnce(true)
            .setStyle(
                NotificationCompat.InboxStyle()
                    .addLine("1、在APP中的开发中会用到提醒，通知类似的功能")
                    .addLine("2、使用NotificationListenerService监听")
                    .addLine("3、监听了通知，没有通知的实现")
            )

        val notification = mNotification!!.build()

        val notifyId=Random.nextInt()
        mNotificationManagerCompat?.notify(notifyId, notification)

        return ForegroundInfo(notifyId, notification)
    }

    /**
     * 实时更新进度
     */
    fun updateProgress() {
        mNotification?.apply {
            ++mCurrentProgress
            if (mCurrentProgress < mProgressMax) {
                setRunning()
            } else if (mCurrentProgress == mProgressMax) {
                setOngoing(false)
                setAutoCancel(true)
                setContentTitle("压缩完成：${mProgressMax}张")
                setProgress(mProgressMax, mCurrentProgress, false)
                mNotificationManagerCompat?.notify(mNotifyId, build())
            }
        }
    }

    /**
     * 更新累加最大数量
     * @param max Int
     */
    fun updateProgressMax(max: Int) {
        //当前批次压缩完成了，重置进度
        if (this.mCurrentProgress == this.mProgressMax) {
            resetProgress(max)
        } else { //当前批次进行中，累加最大数量
            this.mProgressMax += max
            setRunning()
        }
    }

    /**
     * 重置最大数量
     * @param reMax Int
     */
    private fun resetProgress(reMax: Int) {
        this.mCurrentProgress = 0
        this.mProgressMax = reMax
        setRunning()
    }

    /**
     * 设置压缩中
     */
    private fun setRunning() {
        mNotification?.apply {
            setOngoing(true)
            setAutoCancel(false)
            setContentTitle("压缩中：$mCurrentProgress/$mProgressMax")
            setProgress(mProgressMax, mCurrentProgress, false)
            mNotificationManagerCompat?.notify(mNotifyId, build())
        }
    }

}