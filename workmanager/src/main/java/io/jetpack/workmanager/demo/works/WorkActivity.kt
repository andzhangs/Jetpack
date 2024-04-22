package io.jetpack.workmanager.demo.works

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.work.*
import io.jetpack.workmanager.databinding.ActivityWorkBinding
import io.jetpack.workmanager.utils.viewBinding
import java.util.concurrent.TimeUnit

/**
 * 链式调用:
 * To create a chain of work, you can use WorkManager.beginWith(OneTimeWorkRequest) or WorkManager.beginWith(List<OneTimeWorkRequest>) ,
 * which return an instance of WorkContinuation.
 *
 * A WorkContinuation can then be used to add dependent OneTimeWorkRequests using WorkContinuation.then(OneTimeWorkRequest)
 * or WorkContinuation.then(List<OneTimeWorkRequest>) .
 *
 * Every invocation of the WorkContinuation.then(...), returns a new instance of WorkContinuation.
 * If you add a List of OneTimeWorkRequests, these requests can potentially run in parallel.
 *
 * Finally, you can use the WorkContinuation.enqueue() method to enqueue() your chain of WorkContinuations.
 *
 * WorkManager.getInstance()
 *     // Candidates to run in parallel
 *     .beginWith(Arrays.asList(filter1, filter2, filter3))
 *     // Dependent work (only runs after all previous work in chain)
 *     .then(compress)
 *     .then(upload)
 *     // Don't forget to enqueue()
 *     .enqueue();
 *
 */
class WorkActivity : AppCompatActivity() {

    private val mBinding by viewBinding(ActivityWorkBinding::inflate)
    private lateinit var mOneTimeWorkRequest: OneTimeWorkRequest
    private val ID_MARK = "first_work"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding.acBtnStartWork.setOnClickListener {
            starUploadWork()
//            startExpeditedWorker()
        }

        mBinding.acBtnCancelWork.setOnClickListener {
            WorkManager.getInstance(this).cancelWorkById(mOneTimeWorkRequest.id)
            // or
            // WorkManager.getInstance().cancelAllWorkByTag(ID_MARK)
        }
    }

    @SuppressLint("IdleBatteryChargingConstraints")
    private fun starUploadWork() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //约束
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true) // 低电量是不运行工作
                .setRequiresStorageNotLow(true) // 存储空间不足时 不运行工作
                .setRequiredNetworkType(NetworkType.CONNECTED)
//                .setRequiresDeviceIdle(true) //设备必须在空闲状态
//                .setRequiresCharging(true) //设备充电时运行
                .build()

            val dataBuilder = Data.Builder()
                .putString(
                    DemoUploadWorker.INPUT_URI,
                    "I am from WorkActivity"
                )
                .build()

            //一次性工作
            //方式一
            mOneTimeWorkRequest = OneTimeWorkRequest.Builder(DemoUploadWorker::class.java)
                .setConstraints(constraints)
                .setInputData(dataBuilder)
                .addTag(ID_MARK)
                .build()
            //方式二
//            val uploadWorkRequest= OneTimeWorkRequestBuilder<DemoUploadWorker>()
//                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST) //加急作业
//                .setInitialDelay(3,TimeUnit.SECONDS) //加入队列后至少经过2秒钟后再运行
//                .setConstraints(constraints)
//                .setInputData(dataBuilder)
//                .addTag(ID_MARK)
//                .build()

            val operation = WorkManager.getInstance(this).enqueue(mOneTimeWorkRequest)


            operation.state.observe(this) {
                Log.i("print_logs", "WorkActivity::onChanged: $it")
            }

            // Observing your work
            // After you enqueue your work, WorkManager allows you to check on its status
            WorkManager.getInstance(this).getWorkInfoByIdLiveData(mOneTimeWorkRequest.id)
                .observe(this) {
                    if (it.state == WorkInfo.State.SUCCEEDED) {
                        val data = it.outputData.getString(DemoUploadWorker.OUTPUT_URI)
                        Log.i(
                            "print_logs",
                            "WorkActivity::starWork::mOneTimeWorkRequest work is finished, $data"
                        )
                        Toast.makeText(this@WorkActivity, data, Toast.LENGTH_SHORT).show()
                    }
                }

            //周期性工作
//            val periodicWorkRequest =
//                PeriodicWorkRequest.Builder(DemoUploadWorker::class.java, 1, TimeUnit.SECONDS)
//                    .setConstraints(constraints)
//                    .setInputData(dataBuilder)
//                    .addTag(ID_MARK)
//                    .build()
//
//            val operation2=WorkManager.getInstance().enqueue(periodicWorkRequest)
//
//            operation2.state.observe(this){
//                Log.i("print_logs", "WorkActivity::starWork::operation2 $it")
//            }
//
//            WorkManager.getInstance().getWorkInfoByIdLiveData(periodicWorkRequest.id).observe(this){
//                if (it.state == WorkInfo.State.SUCCEEDED) {
//                    Log.i("print_logs", "WorkActivity::starWork::periodicWorkRequest work is finished")
//                }
//            }
        }
    }


    /**
     * 杂项，不可运行
     */
    private fun startExpeditedWorker() {
        //约束
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            constraints.setRequiresDeviceIdle(true)
        }
        constraints.build()

        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<ExpeditedWorker>()
             //输入合并器
            .setInputMerger(ArrayCreatingInputMerger::class) // 会尝试合并输入，并在必要时创建数组。
            .setInputMerger(OverwritingInputMerger::class) // 会尝试将所有输入中的所有键添加到输出中。如果发生冲突，它会覆盖先前设置的键
            .build()

        //以下是可在每小时的最后 15 分钟内运行的定期工作的示例
        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<ExpeditedWorker>(1, TimeUnit.HOURS, 15, TimeUnit.SECONDS)
                .setBackoffCriteria(  //重试和退避政策
                    BackoffPolicy.LINEAR, // 退避政策定义了在后续重试过程中，退避延迟时间随时间以怎样的方式增长 LINEAR 或 EXPONENTIAL
                    WorkRequest.MIN_BACKOFF_MILLIS, // 退避延迟时间指定了首次尝试后重试工作前的最短等待时间。此值不能超过 10 秒（或 MIN_BACKOFF_MILLIS）
                    TimeUnit.MILLISECONDS
                )
                .addTag(ID_MARK) // 标记
                .setInputData(workDataOf("image_uri" to "I am from WorkActivity"))
                .build()


        val configuration = Configuration.Builder().build()
        WorkManager.initialize(this, configuration)

        //计入队列
        WorkManager.getInstance(this).enqueue(periodicWorkRequest)


        /**
         * ExistingWorkPolicy:
         * REPLACE：用新工作替换现有工作。此选项将取消现有工作。
         * KEEP：保留现有工作，并忽略新工作。
         * APPEND：将新工作附加到现有工作的末尾。此政策将导致您的新工作链接到现有工作，在现有工作完成后运行。
         */
        //管理工作
        WorkManager.getInstance(this).enqueueUniqueWork(
            ID_MARK,
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            oneTimeWorkRequest
        ) //用户一次性工作
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            ID_MARK,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        ) //用于定期工作

        //复杂的工作查询
        val build = WorkQuery.Builder
            .fromTags(listOf("syncTag"))
            .addStates(listOf(WorkInfo.State.FAILED, WorkInfo.State.CANCELLED))
            .addUniqueWorkNames(listOf("sync,preProcess"))
            .build()

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(periodicWorkRequest.id)
            .observe(this) {
                val progress=it.progress
                val value=progress.getInt(ExpeditedWorker.Progress,0)
            }

    }

}