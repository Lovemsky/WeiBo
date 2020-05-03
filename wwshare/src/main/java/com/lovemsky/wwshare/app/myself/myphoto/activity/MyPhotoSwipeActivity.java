package com.lovemsky.wwshare.app.myself.myphoto.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.GridLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.view.View;

import com.lovemsky.wwshare.R;
import com.lovemsky.wwshare.app.common.entity.Status;
import com.lovemsky.wwshare.app.common.entity.User;
import com.lovemsky.wwshare.app.mvp.presenter.UserActivityPresent;
import com.lovemsky.wwshare.app.mvp.presenter.imp.UserActivityPresentImp;
import com.lovemsky.wwshare.app.mvp.view.UserActivityView;
import com.lovemsky.wwshare.app.common.base.BaseSwipeActivity;
import com.lovemsky.wwshare.app.profile.activity.ProfileSwipeActivity;
import com.lovemsky.wwshare.app.home.adapter.UserPhotoAdapter;
import com.lovemsky.wwshare.widget.endlessrecyclerview.EndlessRecyclerOnScrollListener;
import com.lovemsky.wwshare.widget.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.lovemsky.wwshare.widget.endlessrecyclerview.utils.RecyclerViewStateUtils;
import com.lovemsky.wwshare.widget.endlessrecyclerview.weight.LoadingFooter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by wenmingvs on 16/4/27.
 */
public class MyPhotoSwipeActivity extends BaseSwipeActivity implements UserActivityView {
    public UserPhotoAdapter mImageAdapter;
    private ArrayList<String> mMyPhotoDatas = new ArrayList<>();
    public Context mContext;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public RecyclerView mRecyclerView;
    public boolean mRefrshAllData;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private UserActivityPresent mUserActivityPresent;
    private boolean mNoMoreData = false;
    private String screeenName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mUserActivityPresent = new UserActivityPresentImp(this);
        setContentView(R.layout.profilefragment_myphoto_layout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.base_swipe_refresh_widget);
        mRecyclerView = (RecyclerView) findViewById(R.id.base_RecyclerView);
        screeenName = getIntent().getStringExtra("screeenName");
        initRefreshLayout();
        initRecyclerView();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mUserActivityPresent.pullToRefreshData(ProfileSwipeActivity.USER_ACTIVITY__USER_PHOTO, screeenName, mContext);
            }
        });
    }

    protected void initRefreshLayout() {
        mRefrshAllData = true;
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mUserActivityPresent.pullToRefreshData(ProfileSwipeActivity.USER_ACTIVITY__USER_PHOTO, screeenName, mContext);
            }
        });
    }

    public void initRecyclerView() {
        mImageAdapter = new UserPhotoAdapter(mMyPhotoDatas, mContext);
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mImageAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
    }


    public void onArrorClick(View view) {
        finish();
    }

    @Override
    public void updateStatusListView(ArrayList<Status> statuselist, boolean resetAdapter) {

    }

    @Override
    public void updatePhotoListView(ArrayList<Status> statuselist, boolean resetAdapter) {
        mMyPhotoDatas.clear();
        mRecyclerView.clearOnScrollListeners();
        for (Status status : statuselist) {
            if (status.bmiddle_pic_urls.size() > 0) {
                mMyPhotoDatas.addAll(status.bmiddle_pic_urls);
            } else {
                if (status.retweeted_status != null) {
                    mMyPhotoDatas.addAll(status.retweeted_status.bmiddle_pic_urls);
                }
            }
        }
        removeDuplicateWithOrder(mMyPhotoDatas);
        if (resetAdapter) {
            mNoMoreData = false;
            mImageAdapter = new UserPhotoAdapter(mMyPhotoDatas, mContext);
            mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mImageAdapter);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
        }
        mRecyclerView.addOnScrollListener(mScrollListener);
        mImageAdapter.setData(mMyPhotoDatas);
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateUserInfoListView(User user, boolean resetAdapter) {

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
    public void showLoadFooterView(String currentgroup) {
        RecyclerViewStateUtils.setFooterViewState(MyPhotoSwipeActivity.this, mRecyclerView, mMyPhotoDatas.size(), LoadingFooter.State.Loading, null);
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

    @Override
    public void restoreScrollOffset(boolean refreshData) {

    }

    public EndlessRecyclerOnScrollListener mScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if (mMyPhotoDatas != null && mMyPhotoDatas.size() > 0) {
                showLoadFooterView("");
                mUserActivityPresent.requestMoreData(ProfileSwipeActivity.USER_ACTIVITY__USER_PHOTO, screeenName, mContext);
            }
        }
    };

    /**
     * 去重
     *
     * @param list
     */
    public static void removeDuplicateWithOrder(List list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Object element = iter.next();
            if (set.add(element))
                newList.add(element);
        }
        list.clear();
        list.addAll(newList);
        System.out.println(" remove duplicate " + list);
    }

}
