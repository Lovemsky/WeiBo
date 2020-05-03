package com.lovemsky.wwshare.app.mvp.model;

import android.content.Context;

import com.lovemsky.wwshare.app.common.entity.User;

/**
 * Created by wenmingvs on 16/6/6.
 */
public interface FriendShipModel {

    interface OnRequestListener {
        void onSuccess();

        void onError(String error);
    }

    public void user_destroy(User user, Context context, OnRequestListener onRequestListener, boolean updateCache);

    public void user_create(User user, Context context, OnRequestListener onRequestListener, boolean updateCache);

}
