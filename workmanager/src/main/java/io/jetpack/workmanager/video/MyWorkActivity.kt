package io.jetpack.workmanager.video

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import io.jetpack.workmanager.R
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit

class MyWorkActivity : AppCompatActivity() {

    companion object {
        val KEY_INPUT = "data_input"
    }

    fun getCurrentTime() = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_work)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addWork(view: View) {

        Log.i("print_logs", "MyWork::doWork-create: ${getCurrentTime()}")

        val tag = "work_request_1"

        //触发条件
        val constraints = Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        //传入数据
        val inputData = Data.Builder()
            .putString(KEY_INPUT, "I am from MyWorkActivity!")
            .build()

        //配置任务
        //一次性执行的任务
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(MyWork::class.java)
            .setConstraints(constraints)  //满足约束条件
//            .setBackoffCriteria(BackoffPolicy.LINEAR,Duration.ofSeconds(2)) //退避策略
            .setInitialDelay(2, TimeUnit.SECONDS) //延时执行
            .addTag(tag)  //设置标签
            .setInputData(inputData)
            .build()

        //周期性任务
        val periodicWorkRequest = PeriodicWorkRequest.Builder(MyWork::class.java, Duration.ofMinutes(15)).build()


        //任务提交给WorkManager
        val workManager = WorkManager.getInstance(this.applicationContext)
        workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.id).observe(this) {
            Log.i("print_logs", "MyWorkActivity::addWork::State ${it.state}")

            if (it.state == WorkInfo.State.SUCCEEDED) {
                Log.i("print_logs", "MyWorkActivity::addWork::outputData: ${it.outputData.getString(MyWork.KEY_OUTPUT)}")
            }
        }


        val operation = workManager.enqueue(oneTimeWorkRequest)
        operation.state.observe(this) {
            Log.i("print_logs", "MyWorkActivity::addWork::Operation $it")
        }

        //取消任务
//        Timer().schedule(object : TimerTask() {
//            override fun run() {
//                Log.e("print_logs", "MyWork::doWork-cancel: ${getCurrentTime()}")
//                workManager.cancelWorkById(oneTimeWorkRequest.id)
//            }
//
//        }, 2000)
    }

    @SuppressLint("EnqueueWork")
    fun addWorkChain(view: View) {

        fun setData(msg:String)=Data.Builder().putString(KEY_INPUT,msg).build()

        val oneTimeWorkRequest1= OneTimeWorkRequestBuilder<MyWork2>().setInputData(setData("I am from oneTimeWorkRequest1 !")).build()
        val oneTimeWorkRequest2= OneTimeWorkRequestBuilder<MyWork2>().setInputData(setData("I am from oneTimeWorkRequest2 !!")).build()
        val oneTimeWorkRequest3= OneTimeWorkRequestBuilder<MyWork2>().setInputData(setData("I am from oneTimeWorkRequest3 !!!")).build()
        val oneTimeWorkRequest4= OneTimeWorkRequestBuilder<MyWork2>().setInputData(setData("I am from oneTimeWorkRequest4 !!!!")).build()
        val oneTimeWorkRequest5= OneTimeWorkRequestBuilder<MyWork2>().setInputData(setData("I am from oneTimeWorkRequest5 !!!!!")).build()

        val workManager=WorkManager.getInstance(this.applicationContext)
        //工作链
//        workManager.beginWith(oneTimeWorkRequest1)
//            .then(oneTimeWorkRequest2)
//            .then(oneTimeWorkRequest3)
//            .enqueue()

        //任务组合：1->2,3->4,->5
        val workContinuation1=workManager.beginWith(oneTimeWorkRequest1).then(oneTimeWorkRequest2)
        val workContinuation2=workManager.beginWith(oneTimeWorkRequest3).then(oneTimeWorkRequest4)
        val listWorkContinuations= mutableListOf(workContinuation1,workContinuation2)
        //跳转到 WorkContinuation查看使用
        WorkContinuation.combine(listWorkContinuations).then(oneTimeWorkRequest5).enqueue()



        workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest1.id).observe(this){
            Log.w("print_logs", "MyWorkActivity::addWorkChain::oneTimeWorkRequest1 ${it.state}")
        }
        workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest2.id).observe(this){
            Log.w("print_logs", "MyWorkActivity::addWorkChain::oneTimeWorkRequest2 ${it.state}")
        }
        workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest3.id).observe(this){
            Log.w("print_logs", "MyWorkActivity::addWorkChain::oneTimeWorkRequest3 ${it.state}")
        }
        workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest4.id).observe(this){
            Log.w("print_logs", "MyWorkActivity::addWorkChain::oneTimeWorkRequest4 ${it.state}")
        }
        workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest5.id).observe(this){
            Log.w("print_logs", "MyWorkActivity::addWorkChain::oneTimeWorkRequest5 ${it.state}")
        }
    }
}