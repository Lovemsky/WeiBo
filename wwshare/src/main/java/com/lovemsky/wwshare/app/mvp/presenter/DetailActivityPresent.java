package com.lovemsky.wwshare.app.mvp.presenter;

import android.content.Context;

import com.lovemsky.wwshare.app.common.entity.Status;

/**
 * Created by wenmingvs on 16/6/26.
 */
public interface DetailActivityPresent {
    public void pullToRefreshData(int groupId, Status status, Context context);

    public void requestMoreData(int groupId, Status status, Context context);
}
