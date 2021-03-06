package com.lovemsky.wwshare.app.discover.hotweibo;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.lovemsky.wwshare.R;
import com.lovemsky.wwshare.app.common.entity.Status;
import com.lovemsky.wwshare.app.mvp.presenter.HotWeiBoPresent;
import com.lovemsky.wwshare.app.mvp.presenter.imp.HotWeiBoPresentImp;
import com.lovemsky.wwshare.app.mvp.view.HotWeiBoActivityView;
import com.lovemsky.wwshare.app.common.base.BaseSwipeActivity;
import com.lovemsky.wwshare.app.home.weiboitem.SeachHeadView;
import com.lovemsky.wwshare.app.home.weiboitem.TimelineArrowWindow;
import com.lovemsky.wwshare.app.home.adapter.WeiboAdapter;
import com.lovemsky.wwshare.app.home.weiboitem.WeiboItemSapce;
import com.lovemsky.wwshare.utils.DensityUtil;
import com.lovemsky.wwshare.utils.ScreenUtil;
import com.lovemsky.wwshare.widget.endlessrecyclerview.EndlessRecyclerOnScrollListener;
import com.lovemsky.wwshare.widget.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.lovemsky.wwshare.widget.endlessrecyclerview.RecyclerViewUtils;
import com.lovemsky.wwshare.widget.endlessrecyclerview.utils.RecyclerViewStateUtils;
import com.lovemsky.wwshare.widget.endlessrecyclerview.weight.LoadingFooter;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/4/27.
 */
public class HotWeiBoSwipeActivity extends BaseSwipeActivity implements HotWeiBoActivityView {
    private ArrayList<Status> mDatas = new ArrayList<Status>();
    public WeiboAdapter mAdapter;
    public Context mContext;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public RecyclerView mRecyclerView;
    public boolean mRefrshAllData;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private HotWeiBoPresent mHotWeiBoPresent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discover_popweibo_layout);
        mContext = this;
        mHotWeiBoPresent = new HotWeiBoPresentImp(this);
        initRefreshLayout();
        initRecyclerView();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mHotWeiBoPresent.pullToRefreshData(mContext);
            }
        });
    }


    protected void initRefreshLayout() {
        mRefrshAllData = true;
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.base_swipe_refresh_widget);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHotWeiBoPresent.pullToRefreshData(mContext);
            }
        });
    }


    public void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.base_RecyclerView);
        mAdapter = new WeiboAdapter(mDatas, mContext) {
            @Override
            public void arrowClick(Status status, int position,Bitmap bitmap) {
//                TimelineArrowWindow popupWindow = new TimelineArrowWindow(mContext, mDatas.get(position), mAdapter, position, "");
//                popupWindow.showAtLocation(mRecyclerView, Gravity.CENTER, 0, 0);

                TimelineArrowWindow arrowDialog = new TimelineArrowWindow(mContext, mDatas.get(position), mAdapter, position, "");
                arrowDialog.show();
                int width = ScreenUtil.getScreenWidth(mContext) - DensityUtil.dp2px(mContext, 80);
                arrowDialog.getWindow().setLayout(width, (ViewGroup.LayoutParams.WRAP_CONTENT));

            }
        };
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        RecyclerViewUtils.setHeaderView(mRecyclerView, new SeachHeadView(mContext));
        mRecyclerView.addItemDecoration(new WeiboItemSapce((int) mContext.getResources().getDimension(R.dimen.home_weiboitem_space)));
    }


    public EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if (mDatas != null && mDatas.size() > 0) {
                showLoadFooterView();
                mHotWeiBoPresent.requestMoreData(mContext);
            }
        }
    };

    public void onArrorClick(View view) {
        finish();
    }

    @Override
    public void updateListView(final ArrayList<Status> statuselist) {
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mDatas = statuselist;
        mAdapter.setData(statuselist);
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoadingIcon() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingIcon() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoadFooterView() {
        RecyclerViewStateUtils.setFooterViewState(HotWeiBoSwipeActivity.this, mRecyclerView, mDatas.size(), LoadingFooter.State.Loading, null);
    }

    @Override
    public void hideFooterView() {
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
    }

    @Override
    public void showEndFooterView() {
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.TheEnd);
    }

    @Override
    public void showErrorFooterView() {
        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.NetWorkError);
    }

}
