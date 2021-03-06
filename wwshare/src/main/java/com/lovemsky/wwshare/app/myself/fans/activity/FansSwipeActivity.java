package com.lovemsky.wwshare.app.myself.fans.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lovemsky.wwshare.R;
import com.lovemsky.wwshare.app.api.StatusesAPI;
import com.lovemsky.wwshare.app.common.entity.User;
import com.lovemsky.wwshare.app.mvp.presenter.FollowerActivityPresent;
import com.lovemsky.wwshare.app.mvp.presenter.imp.FollowerActivityPresentImp;
import com.lovemsky.wwshare.app.mvp.view.FollowActivityView;
import com.lovemsky.wwshare.app.common.base.BaseSwipeActivity;
import com.lovemsky.wwshare.app.common.FillContent;
import com.lovemsky.wwshare.app.login.AccessTokenKeeper;
import com.lovemsky.wwshare.app.myself.fans.adapter.FansAdapter;
import com.lovemsky.wwshare.widget.endlessrecyclerview.EndlessRecyclerOnScrollListener;
import com.lovemsky.wwshare.widget.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.lovemsky.wwshare.widget.endlessrecyclerview.utils.RecyclerViewStateUtils;
import com.lovemsky.wwshare.widget.endlessrecyclerview.weight.LoadingFooter;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/1.
 */
public class FansSwipeActivity extends BaseSwipeActivity implements FollowActivityView {

    public FansAdapter mAdapter;
    private ArrayList<User> mDatas;
    public Context mContext;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public RecyclerView mRecyclerView;
    public StatusesAPI mStatusesAPI;
    public boolean mRefrshAllData;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private FollowerActivityPresent mFollowerActivityPresent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_follower_layout);
        mContext = this;
        mFollowerActivityPresent = new FollowerActivityPresentImp(this);
        initRefreshLayout();
        initRecyclerView();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mFollowerActivityPresent.pullToRefreshData(Long.valueOf(AccessTokenKeeper.readAccessToken(mContext).getUid()), mContext);
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
                mFollowerActivityPresent.pullToRefreshData(Long.valueOf(AccessTokenKeeper.readAccessToken(mContext).getUid()), mContext);
            }
        });
    }


    public void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.base_RecyclerView);
        mAdapter = new FansAdapter(mDatas, mContext) {
            @Override
            public void followerLayoutClick(User user, int position, ImageView follwerIcon, TextView follwerText) {
                follwerIcon.setImageResource(R.drawable.bga_refresh_loading02);
                follwerText.setText("");
                if (user.following) {
                    mFollowerActivityPresent.user_destroy(user, mContext, follwerIcon, follwerText);
                } else {
                    mFollowerActivityPresent.user_create(user, mContext, follwerIcon, follwerText);
                }
            }
        };
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
    }


    public EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if (mDatas != null && mDatas.size() > 0) {
                showLoadFooterView();
                mFollowerActivityPresent.requestMoreData(Long.valueOf(AccessTokenKeeper.readAccessToken(mContext).getUid()), mContext);
            }
        }
    };

    public void onArrorClick(View view) {
        finish();
    }

    @Override
    public void updateListView(ArrayList<User> userlist) {
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mDatas = userlist;
        mAdapter.setData(userlist);
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
        RecyclerViewStateUtils.setFooterViewState(FansSwipeActivity.this, mRecyclerView, mDatas.size(), LoadingFooter.State.Loading, null);
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
    public void updateRealtionShip(Context context,User user, ImageView icon, TextView text) {
        FillContent.updateRealtionShip(context,user, icon, text);
    }


}
