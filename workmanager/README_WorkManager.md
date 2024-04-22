if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Constraints constraints = new Constraints.Builder()
                    .setRequiresDeviceIdle(true)
                    //Recurring work
                    .setRequiresCharging(true)
                    .build();

            //Recurring work 定期重复性调用
            PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest
                    .Builder(SaveImageFileWorker.class, 1, TimeUnit.HOURS)
                    .setConstraints(constraints)
                    .build();
            WorkManager.getInstance().enqueue(periodicWorkRequest);


            Data imageData = new Data.Builder().putString(DATA_KEY_ONE, "http://lorempixel.com/g/400/200/").build();

            //一次性调度
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest
                    .Builder(UploadWorker.class)
                    //Work constraints
                    .setConstraints(constraints)
                    // Initial Delays
                    .setInitialDelay(10, TimeUnit.MINUTES)
                    //Retries and Backoff Policy
                    .setBackoffCriteria(
                            BackoffPolicy.LINEAR,
                            OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                            TimeUnit.MILLISECONDS)
                    //Defining input/output for your task
                    .setInputData(imageData)
                    //Tagging work
                    .addTag("cleanUp")
                    //Input Mergers
                    .setInputMerger(ArrayCreatingInputMerger.class)
                    .build();
            WorkManager.getInstance().enqueue(workRequest);

            //Work States and observing work
            WorkManager.getInstance().getWorkInfoByIdLiveData(workRequest.getId())
                    .observe(this, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(WorkInfo workInfo) {
                            if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
//                                displayMessage("Work finished!")
                            }
                        }
                    });
            WorkManager.getInstance().beginWith(Arrays.asList(workRequest)).then(workRequest).then(workRequest).enqueue();

            //Cancelling and stopping work
            WorkManager.getInstance().cancelWorkById(workRequest.getId());
        }
    }

    class SaveImageFileWorker extends ListenableWorker {

        /**
         * @param appContext   The application {@link Context}
         * @param workerParams Parameters to setup the internal state of this worker
         */
        public SaveImageFileWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
            super(appContext, workerParams);
        }

        @NonNull
        @Override
        public ListenableFuture<Result> startWork() {
            return null;
        }
    }
    
    public class UploadWorker extends Worker {
    
        private static final String TAG = "MUploadWorker";
    
        public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
            super(context, workerParams);
        }
    
        @NonNull
        @Override
        public Result doWork() {
                // Do the work here--in this case, upload the images.
                Log.i(TAG, "当前线程: "+Thread.currentThread());
                //Get the Input
            String imageUriInput=getInputData().getString(MainActivity.DATA_KEY_ONE);
            //Do the work
    
    
            Data outputData=new Data.Builder().putString(MainActivity.DATA_KEY_ONE,"http://lorempixel.com/g/400/200/").build();
    
    
            return Result.success(outputData);
        }
    }