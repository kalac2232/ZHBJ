package com.example.zhbj.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zhbj.R;
import com.example.zhbj.Utils.UIUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PullToRefreshListView extends ListView implements AbsListView.OnScrollListener{

    private static final int STATE_PULL_TO_REFRESH = 1;
    private static final int STATE_RELEASE_TO_REFRESH = 2;
    private static final int STATE_REFRESHING = 3;

    private int mCurrentState = STATE_PULL_TO_REFRESH;

    private int mHeaderMeasuredHeight;
    private int startY = -1;
    private View mHeaderView;
    private ImageView ivArrow;
    private TextView tvTime;
    private TextView tvTitle;
    private RotateAnimation animUp;
    private RotateAnimation animDown;
    private ProgressBar pbLoading;
    private int mFooterMeasuredHeight;
    private View mFooterView;

    public PullToRefreshListView(Context context) {

        super(context);
        initHeaderView();
        initFooterView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }
    private void initHeaderView () {
        mHeaderView = UIUtil.inflate(R.layout.pull_to_refresh_header);
        addHeaderView(mHeaderView);
        mHeaderView.measure(0,0);
        mHeaderMeasuredHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0,-mHeaderMeasuredHeight,0,0);

        tvTitle = mHeaderView.findViewById(R.id.tv_title);
        tvTime = mHeaderView.findViewById(R.id.tv_time);
        ivArrow = mHeaderView.findViewById(R.id.iv_arrow);
        pbLoading = mHeaderView.findViewById(R.id.pb_loading);
        initAnim();
        setCurrentTime();

    }
    private void initFooterView() {
        mFooterView = UIUtil.inflate(R.layout.pull_to_refresh_footer);
        addFooterView(mFooterView);
        mFooterView.measure(0,0);
        mFooterMeasuredHeight = mFooterView.getMeasuredHeight();
        mFooterView.setPadding(0,-mFooterMeasuredHeight,0,0);
        this.setOnScrollListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch(ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {
                    startY = (int) ev.getY();
                }
                int endY = (int) ev.getY();
                int dY = endY - startY;
                if (mCurrentState == STATE_REFRESHING) {
                    break;
                }
                int firstVisiblePosition = getFirstVisiblePosition();
                if (dY > 0 && firstVisiblePosition == 0) {
                    int padding  = dY - mHeaderMeasuredHeight;
                    mHeaderView.setPadding(0,padding,0,0);

                    if (padding > 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {
                        mCurrentState = STATE_RELEASE_TO_REFRESH;
                        refreshState();
                    } else if (padding < 0 && mCurrentState != STATE_PULL_TO_REFRESH) {
                        mCurrentState = STATE_PULL_TO_REFRESH;
                        refreshState();
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
                    mCurrentState = STATE_REFRESHING;
                    refreshState();
                    if (mListener != null ) {
                        mListener.OnRefresh();
                    }
                    mHeaderView.setPadding(0,0,0,0);
                } else if (mCurrentState == STATE_PULL_TO_REFRESH) {
                    mHeaderView.setPadding(0,-mHeaderMeasuredHeight,0,0);
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }
    public void onRefreshComplete(boolean succes) {
        if (!isLoadMore) {
            mHeaderView.setPadding(0,-mHeaderMeasuredHeight,0,0);
            mCurrentState = STATE_PULL_TO_REFRESH;
            tvTitle.setText("下拉刷新");
            ivArrow.startAnimation(animDown);
            pbLoading.setVisibility(View.INVISIBLE);
            ivArrow.setVisibility(View.VISIBLE);
            if (succes) {
                setCurrentTime();
            }
        } else {
            mFooterView.setPadding(0,-mFooterMeasuredHeight,0,0);
            isLoadMore = false;
        }


    }

    private void setCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        tvTime.setText(time);
    }

    private void initAnim() {
        animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);

        animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animDown.setDuration(200);
        animDown.setFillAfter(true);

    }

    private void refreshState() {
        switch(mCurrentState) {
            case STATE_PULL_TO_REFRESH:
                tvTitle.setText("下拉刷新");
                ivArrow.startAnimation(animDown);
                pbLoading.setVisibility(View.INVISIBLE);
                ivArrow.setVisibility(View.VISIBLE);
                break;
            case STATE_RELEASE_TO_REFRESH:
                tvTitle.setText("松开刷新");
                ivArrow.startAnimation(animUp);
                pbLoading.setVisibility(View.INVISIBLE);
                ivArrow.setVisibility(View.VISIBLE);
                break;
            case STATE_REFRESHING:
                tvTitle.setText("正在刷新。。。。");
                ivArrow.clearAnimation();
                pbLoading.setVisibility(View.VISIBLE);
                ivArrow.setVisibility(INVISIBLE);
                break;
            default:
                break;
        }
    }
    private OnRefreshListener mListener;
    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }
    private boolean isLoadMore;
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            int lastVisiblePosition = getLastVisiblePosition();
            if (lastVisiblePosition == getCount() - 1 && !isLoadMore) {

                mFooterView.setPadding(0,0,0,0);
                setSelection(getCount()-1);
                if (mListener != null) {
                    isLoadMore = true;
                    mListener.OnLoadMore();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    public interface OnRefreshListener {
        void OnRefresh();
        void OnLoadMore();
    }
}
