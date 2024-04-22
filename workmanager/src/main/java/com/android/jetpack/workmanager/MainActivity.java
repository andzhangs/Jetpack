package com.android.jetpack.workmanager;

import android.os.Bundle;

import com.android.jetpack.workmanager.jobs.DemoJobCreator;

import androidx.appcompat.app.AppCompatActivity;
import io.jetpack.workmanager.R;

/**
 * Android-Job
 *
 * @author zhangshuai
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_job);

        findViewById(R.id.acBtnStart_Job).setOnClickListener(v -> DemoJobCreator.scheduleJob(
                MainActivity.this));
        findViewById(R.id.acBtnCancel_Job).setOnClickListener(v -> DemoJobCreator.cancelByJobId(2));
    }
}
