package com.lovemsky.wwshare.app.message.comment.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lovemsky.wwshare.R;
import com.lovemsky.wwshare.app.common.entity.Comment;
import com.lovemsky.wwshare.app.message.comment.adapter.CommentAdapter;
import com.lovemsky.wwshare.app.message.comment.widget.GroupPopWindow;
import com.lovemsky.wwshare.app.mvp.presenter.CommentActivityPresent;
import com.lovemsky.wwshare.app.mvp.presenter.imp.CommentActivityPresentImp;
import com.lovemsky.wwshare.app.mvp.view.CommentActivityView;
import com.lovemsky.wwshare.app.common.base.BaseSwipeActivity;
import com.lovemsky.wwshare.app.login.Constants;
import com.lovemsky.wwshare.app.message.IGroupItemClick;
import com.lovemsky.wwshare.utils.DensityUtil;
import com.lovemsky.wwshare.utils.ScreenUtil;
import com.lovemsky.wwshare.widget.endlessrecyclerview.EndlessRecyclerOnScrollListener;
import com.lovemsky.wwshare.widget.endlessrecyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.lovemsky.wwshare.widget.endlessrecyclerview.utils.RecyclerViewStateUtils;
import com.lovemsky.wwshare.widget.endlessrecyclerview.weight.LoadingFooter;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/4/26.
 */
public class CommentSwipeActivity extends BaseSwipeActivity implements CommentActivityView {
    private ArrayList<Comment> mDatas;
    private CommentAdapter mAdapter;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    public Context mContext;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    public RecyclerView mRecyclerView;
    public boolean mRefrshAllData;
    private CommentActivityPresent mCommentPresent;
    private LinearLayout mGroup;
    private TextView mGroupName;
    private GroupPopWindow mCommentPopWindow;
    private int mCurrentGroup = Constants.GROUP_COMMENT_TYPE_ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.messagefragment_comment_layout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.base_swipe_refresh_widget);
        mRecyclerView = (RecyclerView) findViewById(R.id.base_RecyclerView);
        mGroup = (LinearLayout) findViewById(R.id.commment_gourp);
        mGroupName = (TextView) findViewById(R.id.commment_name);
        mCommentPresent = new CommentActivityPresentImp(this);
        initRefreshLayout();
        initRecyclerView();
        initGroupWindows();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mCommentPresent.pullToRefreshData(mCurrentGroup, mContext);
            }
        });
    }

    private void initGroupWindows() {
        mGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rect rect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                int statusBarHeight = rect.top;
                mCommentPopWindow = GroupPopWindow.getInstance(mContext, ScreenUtil.getScreenWidth(mContext) * 3 / 5, ScreenUtil.getScreenHeight(mContext) * 2 / 3);
                mCommentPopWindow.showAtLocation(mGroupName, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, mGroupName.getHeight() + statusBarHeight + DensityUtil.dp2px(mContext, 8));
                mCommentPopWindow.setOnGroupItemClickListener(new IGroupItemClick() {
                    @Override
                    public void onGroupItemClick(long groupId, String groupName) {
                        mCurrentGroup = (int) groupId;
                        setGroupName(groupName);
                        mCommentPopWindow.dismiss();
                        mCommentPresent.pullToRefreshData(mCurrentGroup, mContext);
                    }

                });
            }
        });
    }

    protected void initRefreshLayout() {
        mRefrshAllData = true;
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCommentPresent.pullToRefreshData(mCurrentGroup, mContext);
            }
        });
    }


    private void initRecyclerView() {
        mAdapter = new CommentAdapter(mContext, mDatas);
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
                mCommentPresent.requestMoreData(mCurrentGroup, mContext);
            }
        }
    };

    public void onArrorClick(View view) {
        finish();
    }

    @Override
    public void updateListView(ArrayList<Comment> commentlist) {
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mDatas = commentlist;
        mAdapter.setData(commentlist);
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
        RecyclerViewStateUtils.setFooterViewState(CommentSwipeActivity.this, mRecyclerView, mDatas.size(), LoadingFooter.State.Loading, null);
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
    public void scrollToTop(boolean refreshData) {
        mRecyclerView.scrollToPosition(0);
        if (refreshData) {
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mCommentPresent.pullToRefreshData(mCurrentGroup, mContext);
                }
            });
        }
    }

    public void setGroupName(String groupName) {
        mGroupName.setText(groupName);
    }

    @Override
    protected void onDestroy() {
        if (mCommentPopWindow != null) {
            mCommentPopWindow.onDestory();
        }
        super.onDestroy();
    }
}
