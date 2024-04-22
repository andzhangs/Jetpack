package io.jetpack.workmanager

import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * @author zhangshuai
 * @date 2022/4/5 星期二
 * @email zhangshuai@dushu365.com
 * @description
 */
class MyUniqueWork(
    @NonNull
    private val context: Context,
    @NonNull
    private val workerParameters: WorkerParameters
) :
    Worker(context, workerParameters) {

    companion object {

        private val mRequest by lazy {
            OneTimeWorkRequest.Builder(MyUniqueWork::class.java)
                .addTag(MyUniqueWork::class.java.simpleName)
                .build()

        }

        @JvmStatic
        fun start(context: Context) {
            WorkManager.getInstance(context.applicationContext).enqueueUniqueWork(
                MyUniqueWork::class.java.simpleName,
                ExistingWorkPolicy.KEEP,
                mRequest
            )
        }
    }

    override fun doWork(): Result {
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "TestWork::doWork: ${workerParameters.id}")
        }

        if (true){
            return stopSelf()
        }

        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "MyUniqueWork::doWork: ")
        }
        return Result.success()
    }

    /**
     * 停止自己
     */
    private fun stopSelf(): Result {
        WorkManager.getInstance(applicationContext).cancelWorkById(id)
        return Result.success()
    }
}