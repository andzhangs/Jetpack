package com.android.jetpack.workmanager.works;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


/**
 * @author zhangshuai
 */
public class UploadWorker extends Worker {

    private static final String TAG = "print_logs";
    
    public static final String INPUT_URI = "";
    public static final String OUTPUT_URI = "";

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(TAG, "doWork: ");
        try {
            Data.Builder dataBuilder = new Data.Builder();
            for (int i = 1; i <= 5; i++) {
                if (!isStopped()) {
                    Log.i(TAG, "doWork: "+ i);
                    setProgressAsync(dataBuilder.putInt("progress",i).build());
                    Thread.sleep(1000L);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if (isStopped()) {
            return Result.failure();
        }else{
            // Get the input
            String imageUriInput = getInputData().getString(INPUT_URI);
            Log.i(TAG, "UploadWorker doWork(): 接收的图片地址：" + imageUriInput);

            //Create the output of the work
            Data dataOutput = new Data.Builder().putString(OUTPUT_URI, "http://lorempixel.com/g/300/200/").putInt("progress",200).build();

            Log.i(TAG, "UploadWorker doWork(): 发送的图片："+dataOutput.getString(OUTPUT_URI) + ", "+dataOutput.getInt("progress",-1));
            return Result.success(dataOutput);
        }
    }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.i(TAG, "onStopped: ");
    }
    
    
}
