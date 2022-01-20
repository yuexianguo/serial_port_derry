package com.derry.serialport;

import android.app.Application;
import com.squareup.leakcanary.LeakCanary;

/**
 * 同学们：这个只是用 LeakCanary 去关联整个APP而已 InitApplication
 */
public class InitApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
        // Normal app init code...
    }
}
