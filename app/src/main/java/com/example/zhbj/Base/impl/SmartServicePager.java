package com.example.zhbj.Base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.zhbj.Base.BasePager;

public class SmartServicePager extends BasePager {
    public SmartServicePager(Activity activity) {
        super(activity);
    }
    public void initData(){
        TextView textView = new TextView(mActivity);
        textView.setText("服务");
        tvTitle.setText("服务");
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        btnMenu.setVisibility(View.VISIBLE);
        textView.setGravity(Gravity.CENTER);
        flContent.addView(textView);
    }
}
