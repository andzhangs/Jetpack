package com.android.jetpack.workmanager.jobs;

import android.app.Activity;
import android.content.Context;
import android.print.PrinterId;
import android.util.Log;

import com.evernote.android.job.BuildConfig;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class DemoJobCreator implements JobCreator {

    private static final int DEFAULT_STATE = 0;
    private static List<JobStateData> jobList = new ArrayList<>();
    private static Timer timer;
    private static Activity mActivity;

    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            case ShowNotificationJob.TAG: {
                Log.i(ShowNotificationJob.TAG, "DemoJobCreator is created: ");
                return new ShowNotificationJob();
            }
            default:
                return null;
        }
    }

    private static int jobId;
    private static long delayTime = 0L;

    public static synchronized void scheduleJob(final Activity activity) {
        mActivity = activity;
        if (BuildConfig.DEBUG) {
            // debug模式下，api<24 真机上才能调试
            Log.i(ShowNotificationJob.TAG, "debug环境: ");
            delayTime = 60L;
            jobId = new JobRequest.Builder(ShowNotificationJob.TAG)
                    .setPeriodic(TimeUnit.SECONDS.toMillis(delayTime), TimeUnit.SECONDS.toMillis(30L))
                    .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                    .setUpdateCurrent(true)
                    .build()
                    .schedule();
        } else {
            Log.i(ShowNotificationJob.TAG, "release环境: ");
            delayTime = 900L;
            jobId = new JobRequest.Builder(ShowNotificationJob.TAG)
                    .setPeriodic(TimeUnit.SECONDS.toMillis(delayTime), TimeUnit.SECONDS.toMillis(300L))
                    .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                    .setUpdateCurrent(true)
                    .build()
                    .schedule();
        }
        Log.i(ShowNotificationJob.TAG, "Start scheduleJob：" + jobId);

        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.w(ShowNotificationJob.TAG, "倒计时: " + delayTime);
                        delayTime--;
                    }
                });
            }
        };
        timer.schedule(timerTask, delayTime, 1000);

        jobList.add(new JobStateData(jobId, timer, timerTask));
        jobId = 0;
    }

    /**
     * 根据jobId关闭指定任务
     *
     * @param jobId
     */
    public static void cancelByJobId(int jobId) {
        Log.i(ShowNotificationJob.TAG, "cancel : " + jobId);
        if (jobList != null && jobList.size() > 0) {
            for (JobStateData jobStateData : jobList) {
                if (jobStateData.getJobId()==jobId) {
                    jobStateData.getTimerTask().cancel();
                    jobStateData.getTimer().cancel();
                    jobList.remove(jobStateData);
                    Log.i(ShowNotificationJob.TAG, "取消: "+jobStateData.getJobId());
                }
            }
        }
    }

    /**
     * 关闭所有后台任务
     */
    public static void cancelAll() {
        Log.i(ShowNotificationJob.TAG, "cancel All: ");
        JobManager.instance().cancelAll();
        destroy();
    }

    //销毁计时
    public static void destroy() {
        if (jobList != null && jobList.size() > 0) {
            for (JobStateData jobStateData : jobList) {
                jobStateData.getTimerTask().cancel();
                jobStateData.getTimer().cancel();
                Log.i(ShowNotificationJob.TAG, "移除: "+jobStateData.getJobId());
            }
            jobList.clear();
        }
    }
}
