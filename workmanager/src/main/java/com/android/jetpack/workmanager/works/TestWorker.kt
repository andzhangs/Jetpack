package com.android.jetpack.workmanager.works

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import io.jetpack.workmanager.BuildConfig
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

/**
 * @author zhangshuai@attrsense.com
 * @date 2023/7/3 15:32
 * @description
 */
class TestWorker(val context: Context, val workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    companion object {

        private val encodeRequest by lazy {
            //添加约束
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)   //若为 true，那么当设备处于“电量不足模式”时，工作不会运行。
                .setRequiresStorageNotLow(true)   //若为 true，那么当设备上的存储空间不足时，工作不会运行。
                .build()

            //构建任务
            OneTimeWorkRequest.Builder(TestWorker::class.java)
                .setConstraints(constraints)
                .addTag(TestWorker::class.java.simpleName)
                .build()
        }

        private val request by lazy {
            PeriodicWorkRequest.Builder(
                TestWorker::class.java, 15, TimeUnit.MINUTES
            ).addTag(TestWorker::class.java.simpleName)
                .build()
        }

        @JvmStatic
        fun start(context: Context) {

//            //添加约束
//            val constraints = Constraints.Builder()
//                .setRequiresBatteryNotLow(true)   //若为 true，那么当设备处于“电量不足模式”时，工作不会运行。
//                .setRequiresStorageNotLow(true)   //若为 true，那么当设备上的存储空间不足时，工作不会运行。
//                .build()
//
//            //构建任务
//            val encodeRequest=OneTimeWorkRequest.Builder(TestWorker::class.java)
//                .setConstraints(constraints)
//                .addTag(TestWorker::class.java.simpleName)
//                .build()


            Log.i("print_logs", "TestWorker::start: ")
//            WorkManager.getInstance(context).enqueueUniqueWork(
//                TestWorker::class.java.simpleName,
//                ExistingWorkPolicy.KEEP,
//                encodeRequest
//            )

            WorkManager.getInstance(context).beginUniqueWork(
                TestWorker::class.java.simpleName,
                ExistingWorkPolicy.KEEP,
                encodeRequest
            ).enqueue()
        }

        @JvmStatic
        fun setWorkListener(activity: AppCompatActivity) {
            WorkManager.getInstance(activity)
                .getWorkInfosByTagLiveData(TestWorker::class.java.simpleName).observe(activity) {
                if (it.isNotEmpty()) {
                    it.forEach { workInfo ->
                        if (BuildConfig.DEBUG) {
                            Log.i(
                                "print_logs",
                                "TestWorker::setWorkListener: ${it.size}, ${workInfo.state}"
                            )
                        }
                    }
                }
            }
        }

        @JvmStatic
        fun stop(activity: AppCompatActivity) {
            if (BuildConfig.DEBUG) {
                Log.i("print_logs", "MainActivity::onCreate: 调用停止")
            }
            WorkManager.getInstance(activity).cancelAllWorkByTag(TestWorker::class.java.simpleName)
        }
    }

    override suspend fun doWork(): Result {
        Log.d("print_logs", "NotificationWorker::doWork: ${workerParameters.id}")
        doSomeThing()
        Log.i("print_logs", "NotificationWorker::doWork: end")
        return Result.success()
    }

    private suspend fun doSomeThing() {
        for (i in 0..3) {
            if (BuildConfig.DEBUG) {
                Log.i("print_logs", "TestWorker::doSomeThing: ${System.currentTimeMillis()}")
            }
            delay(1000L)
        }
    }


}