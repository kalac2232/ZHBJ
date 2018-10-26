package com.example.zhbj.Base.impl.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhbj.Base.BaseMenuDetailPager;
import com.example.zhbj.NewsDetailActivity;
import com.example.zhbj.R;
import com.example.zhbj.Utils.CacheUtils;
import com.example.zhbj.Utils.SharePreferenceUtil;
import com.example.zhbj.Utils.UIUtil;
import com.example.zhbj.View.PullToRefreshListView;
import com.example.zhbj.View.TopNewsViewPager;
import com.example.zhbj.domain.NewsTabBean;
import com.example.zhbj.domain.NewsMenu;
import com.example.zhbj.globle.GlobleConstants;
import com.google.gson.Gson;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.circlenavigator.CircleNavigator;
import net.lucode.hackware.magicindicator.ext.navigator.ScaleCircleNavigator;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

public class TabDetailPager extends BaseMenuDetailPager {
    private NewsMenu.NewsMenuData.ChildrenBean mTabData;
    private static final String TAG = "TabDetailPager";
    @ViewInject(R.id.vp_top_news)
    private TopNewsViewPager mViewPager;
    @ViewInject(R.id.tv_title)
    private TextView tvTitle;
    @ViewInject(R.id.magic_indicator1)
    private MagicIndicator magicIndicator;
    @ViewInject(R.id.lv_list)
    private PullToRefreshListView mListView;
    private List<NewsTabBean.NewsTab.TopnewsBean> mTopNews;
    private String url;
    private List<NewsTabBean.NewsTab.NewsBean> mNewsList;
    private NewsAdapter mNewsAdapter;
    private View mHeaderView;
    private String mMoreUrl;
    private Handler mHandler;
    public TabDetailPager(Activity activity, NewsMenu.NewsMenuData.ChildrenBean tabData) {
        super(activity);
        mTabData = tabData;
    }

    @Override
    public View initView() {
        View view = UIUtil.inflate(R.layout.pager_tab_detail);
        x.view().inject(this,view);
        mHeaderView = UIUtil.inflate(R.layout.list_item_header);
        x.view().inject(this,mHeaderView);
        mListView.addHeaderView(mHeaderView);
        mListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void OnRefresh() {
                getDataFromServer();
            }

            @Override
            public void OnLoadMore() {
                if (mMoreUrl != null) {
                    getMoreDataFromServer();
                } else {
                    Toast.makeText(UIUtil.getContext(),"没有更多数据了",Toast.LENGTH_SHORT).show();
                    mListView.onRefreshComplete(true);
                }
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int count = mListView.getHeaderViewsCount();
                position = position - count;
                NewsTabBean.NewsTab.NewsBean newsBean = mNewsList.get(position);
                String read_ids = SharePreferenceUtil.getString(UIUtil.getContext(), "read_ids", "");
                if (!read_ids.contains(newsBean.getId() + ",")) {
                    read_ids = read_ids + newsBean.getId() + ",";
                    SharePreferenceUtil.putString(UIUtil.getContext(),"read_ids",read_ids);
                }
                TextView tvTitle = view.findViewById(R.id.tv_title);
                tvTitle.setTextColor(Color.GRAY);
                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url",newsBean.getUrl());
                mActivity.startActivity(intent);
            }
        });
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mHandler.removeCallbacksAndMessages(null);
                        break;
                    case MotionEvent.ACTION_UP:
                        mHandler.sendEmptyMessageDelayed(0,3000);

                        break;
                    case MotionEvent.ACTION_CANCEL:
                        mHandler.sendEmptyMessageDelayed(0,3000);
                        break;
                }
                return false;
            }


        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //判断有没有缓存
        url = GlobleConstants.SERVER_URL + mTabData.getUrl();
        String cache = CacheUtils.getCache(url, mActivity);
        Log.i(TAG, "initData: cache"+cache);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache,false);
        }
        getDataFromServer();


    }
    private void initMagicIndicator() {
//        CircleNavigator circleNavigator = new CircleNavigator(UIUtil.getContext());
//        circleNavigator.setCircleCount(mTopNews.size());
//        circleNavigator.setCircleColor(Color.GRAY);
//        circleNavigator.setRadius(15);
//        circleNavigator.setCircleClickListener(new CircleNavigator.OnCircleClickListener() {
//            @Override
//            public void onClick(int index) {
//                mViewPager.setCurrentItem(index);
//            }
//        });
//        magicIndicator.setNavigator(circleNavigator);
//        ViewPagerHelper.bind(magicIndicator, mViewPager);
        ScaleCircleNavigator scaleCircleNavigator = new ScaleCircleNavigator(UIUtil.getContext());
        scaleCircleNavigator.setCircleCount(mTopNews.size());
        scaleCircleNavigator.setNormalCircleColor(Color.LTGRAY);
        scaleCircleNavigator.setSelectedCircleColor(Color.DKGRAY);
        scaleCircleNavigator.setCircleClickListener(new ScaleCircleNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                mViewPager.setCurrentItem(index);
            }
        });
        magicIndicator.setNavigator(scaleCircleNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }
    private void getDataFromServer() {

        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result: " + result);
                processData(result,false);
                //写缓存
                CacheUtils.setCache(url, result, mActivity);
                mListView.onRefreshComplete(true);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                String message = ex.getMessage();
                Log.i(TAG, "onError: message" + message);
                //Toast.makeText(UIUtil.getContext(),message,Toast.LENGTH_SHORT).show();
                mListView.onRefreshComplete(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    private void processData(String json,boolean isMore) {
        Gson gson = new Gson();
        NewsTabBean newsTabBean = gson.fromJson(json, NewsTabBean.class);
        mTopNews = newsTabBean.getData().getTopnews();
        String moreUrl = newsTabBean.getData().getMore();
        if (!TextUtils.isEmpty(moreUrl)) {
            mMoreUrl = GlobleConstants.SERVER_URL+moreUrl;
        } else {
            mMoreUrl = null;
        }
        initMagicIndicator();
        if ( !isMore ) {
            if (mTopNews != null ) {
                mViewPager.setAdapter(new TopNewsAdapter());
                mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        NewsTabBean.NewsTab.TopnewsBean topnewsBean = mTopNews.get(position);
                        tvTitle.setText(topnewsBean.getTitle());
                    }

                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                tvTitle.setText(mTopNews.get(0).getTitle());

            }
            mNewsList = newsTabBean.getData().getNews();
            if (mNewsList != null) {
                mNewsAdapter = new NewsAdapter();
                mListView.setAdapter(mNewsAdapter);
            }
            if (mHandler == null) {
                mHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        int currentItem = mViewPager.getCurrentItem();
                        currentItem++;
                        if (currentItem > mTopNews.size()) {
                            currentItem = 0;
                        }
                        mViewPager.setCurrentItem(currentItem);
                        mHandler.sendEmptyMessageDelayed(0,3000);
                    }
                };
                mHandler.sendEmptyMessageDelayed(0,3000);
            }
        } else {
            List<NewsTabBean.NewsTab.NewsBean> moreNews = newsTabBean.getData().getNews();
            mNewsList.addAll(moreNews);
            mNewsAdapter.notifyDataSetChanged();
        }

    }

    private void getMoreDataFromServer() {
        RequestParams params = new RequestParams(mMoreUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result: " + result);
                processData(result,true);
                mListView.onRefreshComplete(true);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mListView.onRefreshComplete(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    class NewsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public NewsTabBean.NewsTab.NewsBean getItem(int position) {
            //NewsTabBean.NewsTab.NewsBean newsBean = mNewsList.get(position);
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null ) {
                convertView = UIUtil.inflate(R.layout.list_item_news);
                holder = new ViewHolder();
                holder.ivIcon = convertView.findViewById(R.id.iv_icon);
                holder.tvTitle = convertView.findViewById(R.id.tv_title);
                holder.tvDate = convertView.findViewById(R.id.tv_date);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String read_ids = SharePreferenceUtil.getString(UIUtil.getContext(), "read_ids", "");

            NewsTabBean.NewsTab.NewsBean news = getItem(position);
            if (read_ids.contains(news.getId() + "")) {
                holder.tvTitle.setTextColor(Color.GRAY);
            } else {
                holder.tvTitle.setTextColor(Color.BLACK);
            }
            holder.tvTitle.setText(news.getTitle());
            holder.tvDate.setText(news.getPubdate());
            ImageOptions options = new ImageOptions.Builder().setLoadingDrawableId(R.mipmap.news_pic_default).build();
            x.image().bind(holder.ivIcon,news.getListimage(),options);

            return convertView;
        }
    }
    static class ViewHolder{
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvDate;
    }
    class TopNewsAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mTopNews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(UIUtil.getContext());
            imageView.setImageResource(R.mipmap.topnews_item_default);
            String imageUrl = mTopNews.get(position).getTopimage();
            ImageOptions options = new ImageOptions.Builder().setLoadingDrawableId(R.mipmap.topnews_item_default).build();
            x.image().bind(imageView,imageUrl,options);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
