package com.example.zhbj.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class TopNewsViewPager extends ViewPager {

    private int startX;
    private int startY;
    private static final String TAG = "TopNewsViewPager";
    public TopNewsViewPager(@NonNull Context context) {
        super(context);
    }

    public TopNewsViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);

        switch(ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int)ev.getX();
                startY = (int)ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int)ev.getX();
                int endY = (int)ev.getY();

                int dx = endX - startX;
                int dy = endY - startY;

                if (Math.abs(dy) < Math.abs(dx)) {
                    int currentItem = getCurrentItem();
                    //左右滑动
                    if (dx > 0) {
                        //向右滑

                        if (currentItem == 0) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    } else {
                        int count = getAdapter().getCount();
                        Log.i(TAG, "dispatchTouchEvent: count: "+count);
                        Log.i(TAG, "dispatchTouchEvent: currentItem: "+currentItem);
                        if (currentItem == count - 1) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                } else {
                    //上下滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
