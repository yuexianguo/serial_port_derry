package com.derry.serialport;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.derry.serialportlibrary.T;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = T.TAG;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON //保持屏幕常亮

                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED //在锁屏情况下也可以显示屏幕

                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); //启动Activity的时候点亮屏幕
        setContentView(R.layout.activity_main);
        Button bt_onClick_Schedule = (Button) findViewById(R.id.bt_onClick_Schedule);
        Button bt_onClick_Finished = (Button) findViewById(R.id.bt_onClick_Finished);
        Button bt_onClick_Cancel = (Button) findViewById(R.id.bt_onClick_Cancel);
        Button bt_onClick_Enqueue = (Button) findViewById(R.id.bt_onClick_Enqueue);
        Button bt_onClick_to_serial_page = (Button) findViewById(R.id.bt_onClick_to_serial_page);

        bt_onClick_Schedule.setOnClickListener(view -> {
            Log.w(TAG, "MainActivity onClick_Schedule()");
            Helpers.schedule(this);
        });
        bt_onClick_Finished.setOnClickListener(view -> {
            Log.w(TAG, "MainActivity onClick_Finished()");
            Helpers.jobFinished();
        });
        bt_onClick_Cancel.setOnClickListener(view -> {
            Log.w(TAG, "MainActivity onClick_Cancel()");
            Helpers.cancelJob(this);
        });
        bt_onClick_Enqueue.setOnClickListener(view -> {
            Log.w(TAG, "MainActivity onClick_Enqueue()");
            Helpers.enqueueJob();
        });
        bt_onClick_to_serial_page.setOnClickListener(view -> {
            Log.w(TAG, "MainActivity onClick_Enqueue()");
            startActivity(new Intent(this,SelectSerialPortActivity.class));
        });

        extracted();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void extracted() {
        boolean isIgnoreBatteryOptimize = checkIsIgnoreBatteryOptimize();
        Log.d(T.TAG,"isIgnoreBatteryOptimize="+isIgnoreBatteryOptimize);
        try {
            if (!isIgnoreBatteryOptimize) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:"+getPackageName()));
                startActivity(intent);
            }
        } catch (Exception e){

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkIsIgnoreBatteryOptimize() {
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            boolean ignoringBatteryOptimizations = powerManager.isIgnoringBatteryOptimizations(getPackageName());
            return ignoringBatteryOptimizations;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "MainActivity onDestroy()");
    }

}