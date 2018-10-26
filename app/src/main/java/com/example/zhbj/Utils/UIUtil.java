package com.example.zhbj.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Process;
import android.view.View;

import com.example.zhbj.ZHBJApplication;;

public class UIUtil {
    /**
     * 获取上下文
     * @return
     */
    public static Context getContext () {
        return ZHBJApplication.getContext();
    }
    public static Handler getHandler () {
        return ZHBJApplication.getHandler();
    }

    /**
     * 获取主线程id
     * @return
     */
    public static int getMainThreadId () {
        return ZHBJApplication.getMainThreadId();
    }
    public static String getString (int id) {
        return getContext().getResources().getString(id);
    }
    public static String[] getStringArray (int id) {
        return getContext().getResources().getStringArray(id);
    }
    public static int getColor (int id) {
        return getContext().getResources().getColor(id);
    }
    public static Drawable getDrawable (int id) {
        return getContext().getResources().getDrawable(id);
    }
    public static int getDimension (int id) {
        return getContext().getResources().getDimensionPixelSize(id);
    }
    public static int dip2px(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density +0.5f);
    }
    public static float px2dip(int px) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return px/density;
    }
    public static View inflate(int id) {
        return View.inflate(getContext(), id, null);
    }

    /**
     * 判断当前线程是否为主线程
     * @return true 是主线程
     */
    public static boolean isRunOnUIThread() {
        //获取当前线程id，如果当前线程id和主线程id相同，那么当前线程就是主线程
        int myTid = Process.myTid();
        if (myTid == getMainThreadId()) {
            return  true;
        }
        return false;
    }

    /**
     * 使线程运行在主线程(UI线程)
     * @param runnable
     */
    public static void runOnUIThread(Runnable runnable) {
        if (isRunOnUIThread()) {
            //已经是主线程了 直接运行
            runnable.run();
        } else {
            //如果是子线程,借助handler让其运行在主线程
            getHandler().post(runnable);
        }
    }
}
