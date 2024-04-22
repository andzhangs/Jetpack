package com.android.jetpack.workmanager.jobs;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public class JobStateData implements Serializable {
    private int jobId;
    private Timer timer;
    private TimerTask timerTask;

    public JobStateData(int jobId, Timer timer, TimerTask timerTask) {
        this.jobId = jobId;
        this.timer = timer;
        this.timerTask = timerTask;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public TimerTask getTimerTask() {
        return timerTask;
    }

    public void setTimerTask(TimerTask timerTask) {
        this.timerTask = timerTask;
    }
}
