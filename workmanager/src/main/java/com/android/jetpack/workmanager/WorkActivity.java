package com.android.jetpack.workmanager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.jetpack.workmanager.works.NotificationWorker;
import com.android.jetpack.workmanager.works.UploadWorker;

import java.util.concurrent.Executors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Operation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import io.jetpack.workmanager.R;

/**
 * 链式调用:
 * To create a chain of work, you can use WorkManager.beginWith(OneTimeWorkRequest) or
 * WorkManager.beginWith(List<OneTimeWorkRequest>) ,
 * which return an instance of WorkContinuation.
 * <p>
 * A WorkContinuation can then be used to add dependent OneTimeWorkRequests using
 * WorkContinuation.then(OneTimeWorkRequest)
 * or WorkContinuation.then(List<OneTimeWorkRequest>) .
 * <p>
 * Every invocation of the WorkContinuation.then(...), returns a new instance of WorkContinuation.
 * If you add a List of OneTimeWorkRequests, these requests can potentially run in parallel.
 * <p>
 * Finally, you can use the WorkContinuation.enqueue() method to enqueue() your chain of
 * WorkContinuations.
 * <p>
 * WorkManager.getInstance()
 * // Candidates to run in parallel
 * .beginWith(Arrays.asList(filter1, filter2, filter3))
 * // Dependent work (only runs after all previous work in chain)
 * .then(compress)
 * .then(upload)
 * // Don't forget to enqueue()
 * .enqueue();
 */
public class WorkActivity extends AppCompatActivity {

    private static final String TAG = "print_logs";
    
    private OneTimeWorkRequest oneTimeWorkRequest;
    private String ID_MARK = "First_Work";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        findViewById(R.id.acBtn_startWork).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWork();
            }
        });
        findViewById(R.id.acBtn_cancelWork).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkManager.getInstance()
                        .cancelWorkById(oneTimeWorkRequest.getId());
//                WorkManager.getInstance().cancelAllWorkByTag(ID_MARK);
            }
        });
    }

    @SuppressLint("IdleBatteryChargingConstraints")
    private void startWork() {

        NotificationWorker.start(this.getApplicationContext());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
//                    .setRequiresDeviceIdle(true)
//                    .setRequiresCharging(true)
                    .build();

            Data imageData = new Data.Builder()
                    .putString(UploadWorker.INPUT_URI, "http://lorempixel.com/400/200/")
                    .build();

            oneTimeWorkRequest = new OneTimeWorkRequest
                    .Builder(UploadWorker.class)
                    .setConstraints(constraints)
                    .setInputData(imageData)
//                    .setInitialDelay(10,TimeUnit.SECONDS)
                    .addTag(ID_MARK)
                    .build();

            Operation operation = WorkManager.getInstance(this)
                    .enqueue(oneTimeWorkRequest);
            //工作前
            operation.getState()
                    .observe(this, new Observer<Operation.State>() {
                        @Override
                        public void onChanged(Operation.State state) {
                            Log.i("print_logs", "任务执行前: " + state.toString());
                        }
                    });

            operation.getResult()
                    .addListener(new Runnable() {
                                     @Override
                                     public void run() {
                                         Log.i("print_logs",
                                               "WorkActivity::run: UploadWorker");
                                     }
                                 },
                                 Executors.newSingleThreadExecutor());



            // Observing your work
            // After you enqueue your work, WorkManager allows you to check on its status
            WorkManager.getInstance()
                    .getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                    .observe(this, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(WorkInfo workInfo) {
                            assert workInfo != null;

                            switch (workInfo.getState()) {
                                case ENQUEUED:{
                                    Log.i(TAG, "onChanged: ENQUEUED");
                                    break;
                                }
                                case RUNNING:{
                                    Log.i("print_logs", "RUNNING 当前进度: "+ workInfo.getProgress().getInt("progress", 0));
                                    break;
                                }
                                case SUCCEEDED :{
                                    Log.i(TAG, "onChanged: SUCCEEDED "+ workInfo.getOutputData().getString(UploadWorker.OUTPUT_URI)+", " +workInfo.getOutputData().getInt("progress", -1));
                                    break;
                                }
                                case CANCELLED:{
                                    Log.i(TAG, "onChanged: CANCELLED");
                                    break;
                                }
                                case FAILED:{
                                    Log.i(TAG, "onChanged: FAILED");
                                    break;
                                }
                                case BLOCKED:{
                                    Log.i(TAG, "onChanged: BLOCKED");
                                    break;
                                }
                                default:{
                                    Log.i(TAG, "onChanged: default");
                                }
                            }
                        }
                    });

//            //周期性工作
//            PeriodicWorkRequest periodicWorkRequest=new PeriodicWorkRequest
//                    .Builder(UploadWorker.class,1,TimeUnit.SECONDS)
//                    .setConstraints(constraints)
//                    .build();
//
//            WorkManager.getInstance().getWorkInfoByIdLiveData(periodicWorkRequest.getId()).observe(this, new Observer<WorkInfo>() {
//                @Override
//                public void onChanged(WorkInfo workInfo) {
//                    if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
//                        Log.i(UploadWorker.TAG, "observe: Work is finished!");
//                    }
//                }
//            });
//            WorkManager.getInstance().enqueue(periodicWorkRequest);

        }
    }
}
