package io.jetpack.workmanager.video

import android.content.Context
import android.os.SystemClock
import android.util.Log
import androidx.annotation.NonNull
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author zhangshuai
 * @date 2022/4/5 星期二
 * @email zhangshuai@dushu365.com
 * @description
 */
class MyWork2(@NonNull context: Context, @NonNull workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    companion object{
        val KEY_OUTPUT="data_output"
    }

    override fun doWork(): Result {
        val inputData=inputData.getString(MyWorkActivity.KEY_INPUT)
        Log.i("print_logs", "MyWork2::doWork: ${inputData}")

//        val currentTime=SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(Date())
//        Log.i("print_logs", "MyWork2::doWork: $currentTime，${MyWork2::class.simpleName}")
        
        val outputData= Data.Builder()
            .putString(KEY_OUTPUT,"I am from MyWork-2 !")
            .build()
        return Result.success(outputData)
    }
}