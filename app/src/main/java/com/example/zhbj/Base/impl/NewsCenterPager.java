package com.example.zhbj.Base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhbj.Base.BaseMenuDetailPager;
import com.example.zhbj.Base.BasePager;
import com.example.zhbj.Base.impl.menu.InteractMenuDetailPager;
import com.example.zhbj.Base.impl.menu.NewsMenuDetailPager;
import com.example.zhbj.Base.impl.menu.PhotosMenuDetailPager;
import com.example.zhbj.Base.impl.menu.TopicMenuDetailPager;
import com.example.zhbj.Fragment.LeftMenuFragment;
import com.example.zhbj.MainActivity;
import com.example.zhbj.Utils.CacheUtils;
import com.example.zhbj.Utils.UIUtil;
import com.example.zhbj.domain.NewsMenu;
import com.example.zhbj.globle.GlobleConstants;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

public class NewsCenterPager extends BasePager {
    private static final String TAG="NewsCenterPager";
    private ArrayList<BaseMenuDetailPager> mMenuDetailPager;
    private NewsMenu mNewsMenuJson;

    public NewsCenterPager(Activity activity) {
        super(activity);
    }
    public void initData(){
//        TextView textView = new TextView(mActivity);
//        textView.setText("新闻");
//        textView.setTextColor(Color.RED);
//        textView.setTextSize(22);
//        textView.setGravity(Gravity.CENTER);
//        flContent.addView(textView);
        btnMenu.setVisibility(View.VISIBLE);
        tvTitle.setText("新闻中心");
        //判断有没有缓存
        String cache = CacheUtils.getCache(GlobleConstants.CATEGORY_URL, mActivity);
        Log.i(TAG, "initData: cache"+cache);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache);
        }
        getDataFromServer();
    }
    private void getDataFromServer(){

        RequestParams params = new RequestParams(GlobleConstants.CATEGORY_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result: "+result);
                processData(result);
                //写缓存
                CacheUtils.setCache(GlobleConstants.CATEGORY_URL,result,mActivity);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                String message = ex.getMessage();
                Log.i(TAG, "onError: message"+message);
                //Toast.makeText(UIUtil.getContext(),message,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    public void setCurrentDetailPager(int position) {
        BaseMenuDetailPager baseMenuDetailPager = mMenuDetailPager.get(position);
        View view = baseMenuDetailPager.mRootView;
        //清楚之前的布局
        flContent.removeAllViews();
        //给帧布局添加
        flContent.addView(view);
        baseMenuDetailPager.initData();
        String title = mNewsMenuJson.getData().get(position).getTitle();
        tvTitle.setText(title);
        if (baseMenuDetailPager instanceof PhotosMenuDetailPager) {
            btnPhoto.setVisibility(View.VISIBLE);
        } else {
            btnPhoto.setVisibility(View.GONE);
        }
    }
    private void processData(String json) {
        Gson gson = new Gson();
        mNewsMenuJson = gson.fromJson(json, NewsMenu.class);
        MainActivity activity  = (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = activity.getLeftMenuFragment();
        leftMenuFragment.setMenuData(mNewsMenuJson.getData());
        //初始化四个菜单详情页
        mMenuDetailPager = new ArrayList<>();
        mMenuDetailPager.add(new NewsMenuDetailPager(mActivity,mNewsMenuJson.getData().get(0).getChildren()));
        //mMenuDetailPager.add(new NewsMenuDetailPager(mActivity));
        mMenuDetailPager.add(new TopicMenuDetailPager(mActivity));
        mMenuDetailPager.add(new PhotosMenuDetailPager(mActivity,btnPhoto));
        mMenuDetailPager.add(new InteractMenuDetailPager(mActivity));
        Log.i(TAG, "processData: mMenuDetailPager.size"+mMenuDetailPager.size());
        //设置默认的页面
        setCurrentDetailPager(0);
    }
}
