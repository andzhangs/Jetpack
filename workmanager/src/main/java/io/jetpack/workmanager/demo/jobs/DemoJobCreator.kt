package io.jetpack.workmanager.demo.jobs

import android.app.Activity
import android.util.Log
import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author zhangshuai
 * @date 2022/4/2 星期六
 * @email zhangshuai@dushu365.com
 * @description
 */
open class DemoJobCreator : JobCreator {
    companion object {
        private val jobList = arrayListOf<DemoJobStateData>()

        @Synchronized
        @JvmStatic
        fun scheduleHob(activity: Activity) {
            Log.i("print_logs", "DemoJobCreator::scheduleHob::isDebug ")

            var delayTime = 900900L //if (BuildConfig.DEBUG) 900060L else 900900L
            val jobId = JobRequest.Builder(DemoNotificationJob.TAG)
                .apply {
                    setPeriodic(TimeUnit.SECONDS.toMillis(delayTime), TimeUnit.SECONDS.toMillis(900300L))
                    setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                    setUpdateCurrent(true)
                }.build().schedule()

            Log.i("print_logs", "DemoJobCreator::scheduleHob::jobId= $jobId")

            val timer = Timer()
            val timerTask = object : TimerTask() {
                /**
                 * The action to be performed by this timer task.
                 */
                override fun run() {
                    activity.runOnUiThread {
                        Log.i("print_logs", "DemoJobCreator::run：倒计时前：$delayTime >>>>>>>>")
                        Log.i("print_logs", "DemoJobCreator::run::当前线程： ${Thread.currentThread()}")
                        delayTime--
                        Log.i("print_logs", "DemoJobCreator::run: 倒计时后：$delayTime <<<<<<<<")
                    }
                }
            }
            timer.schedule(timerTask, delayTime, 900100L)
            jobList.add(DemoJobStateData(jobId, timer, timerTask))
        }

        /**
         * 根据jobId关闭指定任务
         *
         * @param jobId
         */
        fun cancelByJobId(jobId: Int?) {
            Log.i("print_logs", "DemoJobCreator::cancelByJobId::删除jobId= $jobId")
            jobId?.let {
                if (jobList.size > 0) {
                    jobList.forEach {
                        if (it.jobId == jobId) {
                            JobManager.instance().cancel(jobId)
                            it.timerTask?.cancel()
                            it.timer?.cancel()
                            jobList.remove(it)
                            Log.i("print_logs", "DemoJobCreator::cancelByJobId->: $jobId")
                        }
                    }
                }
            }
        }

        /**
         * 销毁倒计时
         */
        fun destroy() {
            if (jobList.size > 0) {
                JobManager.instance().cancelAll()
                jobList.forEach {
                    it.timerTask?.cancel()
                    it.timer?.cancel()
                    Log.i("print_logs", "DemoJobCreator::destroy: ${it.jobId}")
                }
                jobList.clear()
            }
        }

    }

    /**
     * Map the `tag` to a `Job`. If you return `null`, then other `JobCreator`s
     * get the chance to create a `Job` for this tag. If no job is created at all, then it's assumed
     * that job failed. This method is called on a background thread right before the job runs.
     *
     * @param tag The tag from the [JobRequest] which you passed in the constructor of the
     * [JobRequest.Builder] class.
     * @return A new [Job] instance for this tag. If you return `null`, then the job failed
     * and isn't rescheduled.
     * @see JobRequest.Builder.Builder
     */
    override fun create(tag: String): Job? {
        return when (tag) {
            DemoNotificationJob.TAG -> {
                Log.i("print_logs", "DemoJobCreator::create")
                DemoNotificationJob()
            }
            else -> {
                null
            }
        }
    }

}