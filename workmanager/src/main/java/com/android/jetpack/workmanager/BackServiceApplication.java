package com.android.jetpack.workmanager;

import android.app.Application;
import android.util.Log;

import com.android.jetpack.workmanager.jobs.DemoJobCreator;
import com.evernote.android.job.JobManager;


/**
 * @author zhangshuai
 */
public class BackServiceApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        JobManager.create(this).addJobCreator(new DemoJobCreator());

    }
}
