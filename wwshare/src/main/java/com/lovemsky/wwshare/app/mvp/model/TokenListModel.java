package com.lovemsky.wwshare.app.mvp.model;

import android.content.Context;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * Created by wenmingvs on 16/5/18.
 */
public interface TokenListModel {

    public interface OnTokenSwitchListener {
        void onSuccess();

        void onError(String error);

    }


    public void addToken(Context context, String token, String expiresIn, String refresh_token, String uid);

    public void addToken(Context context, Oauth2AccessToken token);


    public void deleteToken(Context context, String uid);

    public void switchToken(Context context, String uid);

    public void switchToken(Context context, int positonInCache, OnTokenSwitchListener onTokenSwitchListener);


}
