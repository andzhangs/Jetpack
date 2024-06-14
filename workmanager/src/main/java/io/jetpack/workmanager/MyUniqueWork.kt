package io.jetpack.workmanager

import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

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
) : CoroutineWorker(context, workerParameters) {

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
            WorkManager.getInstance(context.applicationContext).enqueueUniqueWork(
                MyUniqueWork::class.java.simpleName,
                ExistingWorkPolicy.KEEP,
                mRequest
            )

            //周期性
//            WorkManager.getInstance(context.applicationContext).enqueueUniquePeriodicWork(
//                MyUniqueWork::class.java.simpleName,
//                ExistingPeriodicWorkPolicy.KEEP,
//                mPeriodicRequest
//            )
        }
    }



    override suspend fun doWork(): Result {
        if (BuildConfig.DEBUG) {
            Log.i("print_logs", "TestWork::doWork: ${workerParameters.id}")
        }

//        if (true){
//            return stopSelf()
//        }

        //增加耗时操作后，可以防止后续任务重复进入
        delay(2000L)

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