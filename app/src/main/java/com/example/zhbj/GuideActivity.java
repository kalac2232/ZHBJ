package com.example.zhbj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.zhbj.Utils.SharePreferenceUtil;
import com.example.zhbj.Utils.UIUtil;

import java.util.ArrayList;

/**
 * 新手引导页面
 */
public class GuideActivity extends Activity {

    private ViewPager mViewPager;
    private int[] mImageIds = new int[] {R.mipmap.guide_1,R.mipmap.guide_2,R.mipmap.guide_3};
    private ArrayList<ImageView> mImageViewList;
    private LinearLayout llContainer;
    private ImageView mRedPoint;
    private static final String TAG = "GuideActivity";
    private int mPointDis;
    private Button btnStart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题
        setContentView(R.layout.activity_guide);
        mViewPager = findViewById(R.id.vp_guide);
        llContainer = findViewById(R.id.ll_container);
        mRedPoint = findViewById(R.id.iv_red_point);
        btnStart = findViewById(R.id.btn_start);
        initData();
        mViewPager.setAdapter(new GuideAdapter());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //页面滑动时的回调
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mRedPoint.getLayoutParams();
                layoutParams.leftMargin = (int) (mPointDis * (positionOffset + position));
                mRedPoint.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageSelected(int position) {
                if (position == mImageViewList.size()-1) {
                    btnStart.setVisibility(View.VISIBLE);
                } else {
                    btnStart.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { //视图树
            @Override
            public void onGlobalLayout() {
                //layout方法执行完后的回调
                mPointDis = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();//获取俩点之间的间距
                //移除监听 避免反复回调
                mRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePreferenceUtil.putBoolean(UIUtil.getContext(),"is_first_enter",false);
                startActivity(new Intent(UIUtil.getContext(),MainActivity.class));
                GuideActivity.this.finish();
            }
        });
    }

    @Override
    public void finish() {
        Log.i(TAG, "finish: ");
        super.finish();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }

    private void initData(){
        mImageViewList = new ArrayList<>();
        for (int i = 0;i<mImageIds.length;i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(mImageIds[i]);
            mImageViewList.add(imageView);
            //初始化小圆点
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.shape_point_gray);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            if (i>0){
                //设置左边距
                layoutParams.leftMargin = 20;
                point.setLayoutParams(layoutParams);
            }

            llContainer.addView(point);
        }
    }
    class GuideAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        //初始化item的布局
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = mImageViewList.get(position);
            container.addView(imageView);
            return imageView;
        }

        //销毁item布局
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
