package com.example.zhbj.Fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhbj.Base.impl.NewsCenterPager;
import com.example.zhbj.MainActivity;
import com.example.zhbj.R;
import com.example.zhbj.Utils.UIUtil;
import com.example.zhbj.domain.NewsMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

public class LeftMenuFragment extends BaseFragment {
    @ViewInject(R.id.lv_list)
    private ListView lvList;
    private List<NewsMenu.NewsMenuData> mNewsData;
    private int mCurrentPos = 0;//当前被选中的item的位置
    private LeftMenuAdapter mAdapter;

    @Override
    public View initView() {
        View view = UIUtil.inflate(R.layout.fragment_left_menu);
        x.view().inject(this,view);
        return view;
    }

    @Override
    public void initData() {

    }
    public void setMenuData(List<NewsMenu.NewsMenuData> data){
        //将当前选中的位置归零
        mCurrentPos = 0;

        mNewsData = data;
        mAdapter = new LeftMenuAdapter();
        lvList.setAdapter(mAdapter);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos = position;
                mAdapter.notifyDataSetChanged();
                toggle();//开关侧边栏
                //设置新闻中间的内容
                setCurrentDetailPager(position);
            }
        });
    }

    /**
     * 设置当前的详情页
     * @param position
     */
    private void setCurrentDetailPager(int position) {
        MainActivity activity = (MainActivity) mActivity;
        ContentFragment contentMenuFragment = activity.getContentMenuFragment();
        NewsCenterPager centerPager = contentMenuFragment.getNewsCenterPager();
        centerPager.setCurrentDetailPager(position);


    }

    private void toggle() {
        MainActivity activity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = activity.getSlidingMenu();
        slidingMenu.toggle();
    }

    class LeftMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mNewsData.size();
        }

        @Override
        public NewsMenu.NewsMenuData getItem(int position) {
            return mNewsData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity, R.layout.list_item_left_menu, null);
            TextView tvMenu = view.findViewById(R.id.tv_menu);
            NewsMenu.NewsMenuData item = getItem(position);
            tvMenu.setText(item.getTitle());
            if (position == mCurrentPos) {
                tvMenu.setEnabled(true);//被选中 文字变为红色
            } else {
              tvMenu.setEnabled(false);//文字变为白色
            }
            return view;
        }
    }

}
