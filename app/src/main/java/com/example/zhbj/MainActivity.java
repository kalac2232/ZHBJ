package com.example.zhbj;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.example.zhbj.Fragment.ContentFragment;
import com.example.zhbj.Fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

    private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
    private static final String TAG_CONTENT = "TAG_CONTENT";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        setBehindContentView(R.layout.left_menu);
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//全屏触摸
        slidingMenu.setBehindOffset(400);//屏幕预留400像素
        initFragment();
    }
    private void initFragment(){
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.replace(R.id.fl_left_menu,new LeftMenuFragment(),TAG_LEFT_MENU);//替换
        transaction.replace(R.id.fl_main,new ContentFragment(),TAG_CONTENT);
        transaction.commit();
    }
    public LeftMenuFragment getLeftMenuFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        LeftMenuFragment fragment = (LeftMenuFragment) supportFragmentManager.findFragmentByTag(TAG_LEFT_MENU);
        return fragment;
    }
    public ContentFragment getContentMenuFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment) supportFragmentManager.findFragmentByTag(TAG_CONTENT);
        return fragment;
    }
}
