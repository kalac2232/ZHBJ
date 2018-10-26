package com.example.zhbj.Base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.example.zhbj.Base.BasePager;

public class SettingPager extends BasePager {
    public SettingPager(Activity activity) {
        super(activity);
    }
    public void initData(){
        TextView textView = new TextView(mActivity);
        textView.setText("设置");
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);
        flContent.addView(textView);
        tvTitle.setText("设置");
    }
}
