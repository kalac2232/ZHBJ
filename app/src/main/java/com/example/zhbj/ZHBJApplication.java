package com.example.zhbj;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Process;

import org.xutils.x;

public class ZHBJApplication extends Application {
    public static int getMainThreadId() {
        return mainThreadId;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static Context getContext() {
        return context;
    }

    private static int mainThreadId;
    private static Handler handler;
    private static Context context;
    private static final String TAG = "ETalkApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        context = getApplicationContext();
        handler = new Handler();
        mainThreadId = Process.myTid();

    }
}
