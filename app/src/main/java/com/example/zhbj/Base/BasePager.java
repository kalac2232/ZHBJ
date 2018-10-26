package com.example.zhbj.Base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.zhbj.MainActivity;
import com.example.zhbj.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * 五个标签页的基类
 */
public class BasePager {

    public Activity mActivity;
    public FrameLayout flContent;
    public ImageButton btnMenu;
    public TextView tvTitle;
    public ImageButton btnPhoto;
    public final View mRootView;

    public BasePager(Activity activity){
        mActivity = activity;
        mRootView = initView();
    }

    public View initView() {
        View view = View.inflate(mActivity, R.layout.base_pager, null);
        tvTitle = view.findViewById(R.id.tv_title);
        btnMenu = view.findViewById(R.id.btn_menu);
        btnMenu.setVisibility(View.GONE);
        flContent = view.findViewById(R.id.fl_content);
        btnPhoto = view.findViewById(R.id.btn_photo);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) mActivity;
                SlidingMenu slidingMenu = activity.getSlidingMenu();
                slidingMenu.toggle();
            }
        });
        return view;
    }
    public void initData(){

    }
}
