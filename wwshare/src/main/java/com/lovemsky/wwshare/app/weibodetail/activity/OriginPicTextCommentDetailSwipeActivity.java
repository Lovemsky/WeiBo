package com.lovemsky.wwshare.app.weibodetail.activity;

import android.widget.LinearLayout;

import com.lovemsky.wwshare.app.weibodetail.headview.OriginPicTextHeaderView;
import com.lovemsky.wwshare.widget.endlessrecyclerview.RecyclerViewUtils;

/**
 * Created by wenmingvs on 16/4/21.
 */
public class OriginPicTextCommentDetailSwipeActivity extends BaseDetailSwipeActivity {
    public LinearLayout mHeaderView;

    @Override
    protected void addHeaderView(int type) {
        mHeaderView = new OriginPicTextHeaderView(mContext, mStatus, type);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mHeaderView.setLayoutParams(layoutParams);
        ((OriginPicTextHeaderView) mHeaderView).setOnDetailButtonClickListener(onDetailButtonClickListener);
        RecyclerViewUtils.setHeaderView(mRecyclerView, mHeaderView);
    }

    @Override
    protected int getHeaderViewHeight() {
        return mHeaderView.getHeight();
    }

    @Override
    protected void refreshDetailBar(int comments, int reposts, int attitudes) {
        ((OriginPicTextHeaderView) mHeaderView).refreshDetailBar(comments, reposts, attitudes);
    }


}
