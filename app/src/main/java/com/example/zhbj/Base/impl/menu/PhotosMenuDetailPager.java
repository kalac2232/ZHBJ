package com.example.zhbj.Base.impl.menu;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhbj.Base.BaseMenuDetailPager;
import com.example.zhbj.R;
import com.example.zhbj.Utils.CacheUtils;
import com.example.zhbj.Utils.UIUtil;
import com.example.zhbj.domain.PhotoBean;
import com.example.zhbj.globle.GlobleConstants;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;


public class PhotosMenuDetailPager extends BaseMenuDetailPager implements View.OnClickListener{
    @ViewInject(R.id.lv_photo)
    private ListView lvView;
    @ViewInject(R.id.gv_photo)
    private GridView gvView;
    private ImageButton btnPhoto;
    private static final String TAG = "PhotosMenuDetailPager";
    private boolean isListView = true;
    private List<PhotoBean.PhotoNews.NewsBean> newsList;

    public PhotosMenuDetailPager(Activity activity, ImageButton btnPhoto) {
        super(activity);
        this.btnPhoto = btnPhoto;
        this.btnPhoto.setOnClickListener(this);
    }

    @Override
    public View initView() {
        View view = UIUtil.inflate(R.layout.pager_photos_menu_detail);
        x.view().inject(this,view);

        return view;
    }

    @Override
    public void initData() {
        super.initData();

        String cache = CacheUtils.getCache(GlobleConstants.PHOTO_URL, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache);
        }
        getDataFromServer();
    }

    public void getDataFromServer() {
        RequestParams params = new RequestParams(GlobleConstants.PHOTO_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                processData(result);
                //写缓存
                CacheUtils.setCache(GlobleConstants.PHOTO_URL, result, mActivity);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    private void processData(String json) {
        Gson gson = new Gson();
        PhotoBean photoBean = gson.fromJson(json, PhotoBean.class);
        newsList = photoBean.getData().getNews();
        lvView.setAdapter(new PhotoAdapter());
        gvView .setAdapter(new PhotoAdapter());

    }

    @Override
    public void onClick(View v) {
        if (isListView) {
            lvView.setVisibility(View.GONE);
            gvView.setVisibility(View.VISIBLE);
            isListView = false;
            btnPhoto.setImageResource(R.mipmap.icon_pic_list_type);
        } else {
            lvView.setVisibility(View.VISIBLE);
            gvView.setVisibility(View.GONE);
            isListView = true;
            btnPhoto.setImageResource(R.mipmap.icon_pic_grid_type);

        }
    }

    class PhotoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return newsList.size();
        }

        @Override
        public PhotoBean.PhotoNews.NewsBean getItem(int position) {
            return newsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null ) {
                convertView = UIUtil.inflate(R.layout.list_item_photos);
                viewHolder = new ViewHolder();
                viewHolder.ivPic = convertView.findViewById(R.id.iv_pic);
                viewHolder.tvTitle = convertView.findViewById(R.id.tv_title);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            PhotoBean.PhotoNews.NewsBean item = getItem(position);
            viewHolder.tvTitle.setText(item.getTitle());
            x.image().bind(viewHolder.ivPic,item.getListimage());
            return convertView;
        }
        class ViewHolder {
            ImageView ivPic;
            TextView tvTitle;
        }
    }
}
