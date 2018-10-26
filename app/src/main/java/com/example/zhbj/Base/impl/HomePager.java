package com.example.zhbj.Base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.example.zhbj.Base.BasePager;

public class HomePager extends BasePager {
    public HomePager(Activity activity) {
        super(activity);
    }
    public void initData(){
        TextView textView = new TextView(mActivity);
        textView.setText("首页");
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);
        flContent.addView(textView);
        tvTitle.setText("智慧北京");
    }
}
