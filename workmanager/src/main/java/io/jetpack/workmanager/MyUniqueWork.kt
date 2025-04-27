package io.jetpack.workmanager

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * @author zhangshuai
 * @date 2022/4/5 星期二
 * @email zhangshuai@dushu365.com
 * @description
 */
class MyUniqueWork(
    @NonNull
    private val mContext: Context,
    @NonNull
    private val workerParameters: WorkerParameters
) : CoroutineWorker(mContext, workerParameters) {

    companion object {

        private val mRequest by lazy {
            OneTimeWorkRequest.Builder(MyUniqueWork::class.java)
                .addTag(MyUniqueWork::class.java.simpleName)
                .build()

        }

        private val mPeriodicRequest by lazy {
            PeriodicWorkRequest.Builder(MyUniqueWork::class.java,15,TimeUnit.MINUTES)
                .addTag(MyUniqueWork::class.java.simpleName)
                .build()

        }

        @JvmStatic
        fun start(context: Context) {
            //此处2秒内只会执行一次
            //它允许你创建一个一次性执行的任务，确保每次只有一个实例在运行，即使先前的工作请求尚未完成
            WorkManager.getInstance(context.applicationContext).enqueueUniqueWork(
                MyUniqueWork::class.java.simpleName,
                ExistingWorkPolicy.KEEP,
                mRequest
            )

            //它允许你创建一个周期性执行的任务，确保每次只有一个实例在运行，即使先前的工作请求尚未完成
//            WorkManager.getInstance(context.applicationContext).enqueueUniquePeriodicWork(
//                MyUniqueWork::class.java.simpleName,
//                ExistingPeriodicWorkPolicy.KEEP,
//                mPeriodicRequest
//            )
        }
    }


    private var isRepeat=true
    private var mTimes=0
    private val NOTIFY_ID=10002


    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MyUniqueWork::doWork: ${workerParameters.id}")
        }

//        if (true){
//            return stopSelf()
//        }

        //增加耗时操作后，可以防止后续任务重复进入
//        delay(2000L)

        while (isRepeat){

            ++mTimes

            if (BuildConfig.DEBUG) {
                Log.i("print_logs", "计时: $mTimes")
            }
            delay(1000)
            mNotification.apply {
                setOngoing(true)
                setAutoCancel(false)
                if (mTimes == 10) {
                    setContentTitle("上传完成")
                    mNotificationManagerCompat.cancel(NOTIFY_ID)
                    isRepeat=false
                } else {
                    setContentTitle("计时：$mTimes")
                    mNotificationManagerCompat.notify(NOTIFY_ID,this.build())
                }
            }
        }

//        try {
//            withContext(Dispatchers.IO) {
//                val simpleDateFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA)
//                val date=simpleDateFormat.format(Date(System.currentTimeMillis()))
//
//                val file=File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),"$date.txt")
//                FileOutputStream(file).use {
//                    it.write("$date -> 我是写入的文件!".toByteArray())
//                }
//            }
//        }catch (e:Exception){
//            e.printStackTrace()
//        }

        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MyUniqueWork::doWork: end")
        }

        return Result.success()
    }

    //获取通知ID、渠道名

    private val mNotificationManagerCompat: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(
            mContext
        )
    }
    private val mNotification: NotificationCompat.Builder by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                mContext.applicationContext.packageName,
                //通知类别，在手机设置的应用程序中对应的APP的"通知"中可见
                "周期任务",
                //方式一 重要性（Android 8.0 及更高版本）
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                //设置渠道描述
                description = "上传媒体文件"
                //设置提示音
                setSound(null, null)
                //开启指示灯
                enableLights(false)
                //开启震动
                enableVibration(false)
                vibrationPattern = longArrayOf(0) //静音模式  //100, 200, 300, 400, 500
                //设置锁屏展示
                lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                //设置是否显示角标
                setShowBadge(true)
                mNotificationManagerCompat.createNotificationChannel(this)
            }
        }

        NotificationCompat.Builder(
            mContext.applicationContext,
            mContext.applicationContext.packageName
        )
            .setDefaults(Notification.DEFAULT_ALL)  // 请求用户授权
            //通知栏的左上角小图标
            .setSmallIcon(R.mipmap.ic_launcher_round)
            //通知栏右边内容图标
            .setContentTitle("计时：0") ///$mProgressMax
            //告知系统该通知应具有的“干扰性”。当发出此类型的通知时，通知会以悬挂的方法显示在屏幕上. 优先级（Android 7.1 及更低版本）
            .setPriority(NotificationCompat.PRIORITY_MAX)
            //点击通知后消失
            .setAutoCancel(false)
            // 除非app死掉或者在代码中取消，否则都不会消失。
            .setOngoing(true)
            //通知是否在锁定屏幕上显示的方法 (setVisibility())，以及指定通知文本的“公开”版本的方法。
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            //禁止声音
            .setSound(null)
            //关闭震动
            .setVibrate(longArrayOf(0))
            //设置呼吸灯
            .setLights(Color.GREEN, 300, 0)
            .setAllowSystemGeneratedContextualActions(true)
            //通知栏时间，一般是直接用系统的
            .setWhen(System.currentTimeMillis())
            //通知只会在通知首次出现时打断用户（通过声音、振动或视觉提示），而之后更新则不会再打断用户。
            .setOnlyAlertOnce(true)
//            .setContentIntent(pendingIntent)
    }


    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(NOTIFY_ID, mNotification.build())
    }

    /**
     * 停止自己
     */
    private fun stopSelf(): Result {
        WorkManager.getInstance(applicationContext).cancelWorkById(id)
        return Result.success()
    }

}