package com.example.zhbj.Base.impl.menu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhbj.Base.BaseMenuDetailPager;
import com.example.zhbj.MainActivity;
import com.example.zhbj.R;
import com.example.zhbj.Utils.UIUtil;
import com.example.zhbj.domain.NewsMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class NewsMenuDetailPager extends BaseMenuDetailPager {

    private Activity mActivity;
    @ViewInject(R.id.vp_news_menu_detail)
    private ViewPager mViewPager;
    @ViewInject(R.id.magic_indicator)
    private MagicIndicator magicIndicator ;
    List<NewsMenu.NewsMenuData.ChildrenBean> mTabData;
    ArrayList<TabDetailPager> mPagers;
    public NewsMenuDetailPager(Activity activity, List<NewsMenu.NewsMenuData.ChildrenBean> children) {
        super(activity);
        mActivity = activity;
        mTabData = children;
    }
    public NewsMenuDetailPager(Activity activity) {
        super(activity);
        mActivity = activity;
    }


    @Override
    public View initView() {
        View view = View.inflate(UIUtil.getContext(), R.layout.pager_news_menu_detail, null);
        x.view().inject(this,view);
        return view;
    }

    @Override
    public void initData() {
        mPagers = new ArrayList<>();
        for (int i = 0; i< mTabData.size(); i++) {
            TabDetailPager tabDetailPager = new TabDetailPager(mActivity,mTabData.get(i));
            mPagers.add(tabDetailPager);
        }
        CommonNavigator commonNavigator = new CommonNavigator(UIUtil.getContext());
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mPagers.size();
            }

            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);

                simplePagerTitleView.setText(mTabData.get(index).getTitle());
                simplePagerTitleView.setNormalColor(Color.parseColor("#333333"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#e94220"));
                simplePagerTitleView.setTextSize(16);

                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });

                return simplePagerTitleView;

            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;    // 没有指示器，因为title的指示作用已经很明显了
            }

        });
        magicIndicator.setNavigator(commonNavigator);
        mViewPager.setAdapter(new NewsMenuDetailAdapter());
        ViewPagerHelper.bind(magicIndicator, mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    setSlidingMenuEnable(true);
                } else {
                    setSlidingMenuEnable(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    private void setSlidingMenuEnable(boolean enable){
        MainActivity activity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = activity.getSlidingMenu();
        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }
    class NewsMenuDetailAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            NewsMenu.NewsMenuData.ChildrenBean childrenBean = mTabData.get(position);
            return childrenBean.getTitle();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            TabDetailPager tabDetailPager = mPagers.get(position);
            View view = tabDetailPager.mRootView;
            container.addView(view);
            tabDetailPager.initData();
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    @Event(type = View.OnClickListener.class,value = R.id.btn_next)
    private void nextPage(View view) {
        int currentItem = mViewPager.getCurrentItem();
        currentItem++;
        mViewPager.setCurrentItem(currentItem);
    }
}
