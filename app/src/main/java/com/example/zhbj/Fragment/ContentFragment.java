package com.example.zhbj.Fragment;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.zhbj.Base.BasePager;
import com.example.zhbj.Base.impl.GovAffairsPager;
import com.example.zhbj.Base.impl.HomePager;
import com.example.zhbj.Base.impl.NewsCenterPager;
import com.example.zhbj.Base.impl.SettingPager;
import com.example.zhbj.Base.impl.SmartServicePager;
import com.example.zhbj.MainActivity;
import com.example.zhbj.R;
import com.example.zhbj.Utils.UIUtil;
import com.example.zhbj.View.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

public class ContentFragment extends BaseFragment {

    private NoScrollViewPager mViewPager;
    private ArrayList<BasePager> mPagers;
    private RadioGroup rgGropup;

    @Override
    public View initView() {
        View view = UIUtil.inflate(R.layout.fragment_content);
        mViewPager = view.findViewById(R.id.vp_content);
        rgGropup = view.findViewById(R.id.rg_group);
        rgGropup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        mViewPager.setCurrentItem(0,false);
                        break;
                    case R.id.rb_news:
                        mViewPager.setCurrentItem(1,false);
                        break;
                    case R.id.rb_smart:
                        mViewPager.setCurrentItem(2,false);
                        break;
                    case R.id.rb_gov:
                        mViewPager.setCurrentItem(3,false);
                        break;
                    case R.id.rb_setting:
                        mViewPager.setCurrentItem(4,false);
                        break;
                    default:
                        break;
                }
            }
        });

        return view;
    }

    @Override
    public void initData() {
        mPagers = new ArrayList<>();
        mPagers.add(new HomePager(mActivity));
        mPagers.add(new NewsCenterPager(mActivity));
        mPagers.add(new SmartServicePager(mActivity));
        mPagers.add(new GovAffairsPager(mActivity));
        mPagers.add(new SettingPager(mActivity));
        mViewPager.setAdapter(new ContentAdapter());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BasePager basePager = mPagers.get(position);
                basePager.initData();
                if (position == 0 || position == mPagers.size()-1) {
                    setSlidingMenuEnable(false);
                } else {
                    setSlidingMenuEnable(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPagers.get(0).initData();
        setSlidingMenuEnable(false);
    }

    /**
     * 获取新闻中心的页面
     */
    public NewsCenterPager getNewsCenterPager() {
        NewsCenterPager newsCenterPager = (NewsCenterPager) mPagers.get(1);
        return newsCenterPager;
    }
    void setSlidingMenuEnable(boolean enable){
        MainActivity activity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = activity.getSlidingMenu();
        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }
    class ContentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            BasePager basePager = mPagers.get(position);
            View mRootView = basePager.mRootView;
            container.addView(mRootView);
            return mRootView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
