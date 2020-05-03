package com.lovemsky.wwshare.app.mvp.presenter.imp;

import android.content.Context;
import android.content.Intent;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.lovemsky.wwshare.app.mvp.model.TokenListModel;
import com.lovemsky.wwshare.app.mvp.model.imp.TokenListModelImp;
import com.lovemsky.wwshare.app.mvp.presenter.SettingActivityPresent;
import com.lovemsky.wwshare.app.mvp.view.SettingActivityView;
import com.lovemsky.wwshare.app.login.AccessTokenKeeper;
import com.lovemsky.wwshare.app.home.activity.MainActivity;
import com.lovemsky.wwshare.app.unlogin.activity.UnLoginActivity;
import com.lovemsky.wwshare.utils.ToastUtil;

/**
 * Created by wenmingvs on 16/5/18.
 */
public class SettingActivityPresentImp implements SettingActivityPresent {

    private SettingActivityView settingActivityView;
    private TokenListModel tokenListModel;

    public SettingActivityPresentImp(SettingActivityView settingActivityView) {
        this.settingActivityView = settingActivityView;
        tokenListModel = new TokenListModelImp();
    }

    @Override
    public void logout(final Context context) {
        tokenListModel.deleteToken(context, AccessTokenKeeper.readAccessToken(context).getUid());
        AccessTokenKeeper.clear(context);
        tokenListModel.switchToken(context, 0, new TokenListModel.OnTokenSwitchListener() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }

            @Override
            public void onError(String error) {
                Intent intent = new Intent(context, UnLoginActivity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public void clearCache(Context context) {
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().clearMemoryCache();
        ToastUtil.showShort(context, "缓存清理成功！");
    }
}
