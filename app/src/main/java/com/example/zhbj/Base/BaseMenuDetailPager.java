package com.example.zhbj.Base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.zhbj.MainActivity;
import com.example.zhbj.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public abstract class BaseMenuDetailPager {

    public Activity mActivity;
    public FrameLayout flContent;
    public ImageButton btnMenu;
    public TextView tvTitle;
    public final View mRootView;

    public BaseMenuDetailPager(Activity activity){
        mActivity = activity;
        mRootView = initView();
    }

    public abstract View initView();

    public void initData(){

    }
}
