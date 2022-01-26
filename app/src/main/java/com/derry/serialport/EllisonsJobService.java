package com.derry.serialport;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.derry.serialportlibrary.T;

/**
 * description :
 * author : Andy.Guo
 * email : Andy.Guo@waclightiong.com.cn
 * data : 2022/1/26
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class EllisonsJobService extends JobService {
    public static final int ELLISONS_JOB_ID = 0;
    public static final int ELLISONS_JOB_OVERDIDE_DEADLINE = 1000;
    private static final String TAG = T.TAG;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();
        Log.w(TAG, "EllisonsJobService onCreate()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "EllisonsJobService destroyed.");
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.w(TAG, "EllisonsJobService onStartJob()");
        Helpers.doHardWork(this, params);
        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(mRunnable, 5000L);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.w(TAG, "EllisonsJobService stopped & wait to restart params:" + params);
        return true;
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            Log.w(TAG, "doHardWork mHandler.postDelayed " + currentTime);
            mHandler.removeCallbacksAndMessages(null);
            if (currentTime - 1643186421279L > 6 * 60 * 1000L) {
                Intent intent = new Intent(EllisonsJobService.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                mHandler.postDelayed(mRunnable, 5000L);
            }


        }
    };
}
