package com.example.zhbj.Base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.zhbj.Base.BasePager;

public class GovAffairsPager extends BasePager {
    public GovAffairsPager(Activity activity) {
        super(activity);
    }
    public void initData(){
        TextView textView = new TextView(mActivity);
        textView.setText("政务");
        tvTitle.setText("政务");
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);
        btnMenu.setVisibility(View.VISIBLE);
        flContent.addView(textView);
    }
}
