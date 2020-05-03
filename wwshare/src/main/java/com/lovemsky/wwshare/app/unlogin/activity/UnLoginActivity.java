package com.lovemsky.wwshare.app.unlogin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lovemsky.wwshare.app.api.UsersAPI;
import com.lovemsky.wwshare.app.common.entity.User;
import com.lovemsky.wwshare.app.home.activity.MainActivity;
import com.lovemsky.wwshare.app.mvp.model.TokenListModel;
import com.lovemsky.wwshare.app.mvp.model.imp.TokenListModelImp;
import com.lovemsky.wwshare.utils.StringUtil;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.lovemsky.wwshare.R;
import com.lovemsky.wwshare.app.login.Constants;
import com.lovemsky.wwshare.app.unlogin.fragment.DiscoverFragment;
import com.lovemsky.wwshare.app.unlogin.fragment.HomeFragment;
import com.lovemsky.wwshare.app.unlogin.fragment.MessageFragment;
import com.lovemsky.wwshare.app.unlogin.fragment.ProfileFragment;
import com.lovemsky.wwshare.utils.ToastUtil;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;


/**
 * Created by wenmingvs on 16/5/9.
 */
public class UnLoginActivity extends AppCompatActivity implements WeiboAuthListener {

    private static final int HOME_FRAGMENT = 0X001;
    private static final int MESSAGE_FRAGMENT = 0X002;
    private static final int DISCOVERY_FRAGMENT = 0X004;
    private static final int PROFILE_FRAGMENT = 0X005;

    private int mCurrentIndex;
    private Context mContext;
    private HomeFragment mHomeFragment;
    private MessageFragment mMessageFragment;
    private DiscoverFragment mDiscoverFragment;
    private ProfileFragment mProfileFragment;


    private FragmentManager mFragmentManager;
    private RelativeLayout mHomeTab, mMessageTab, mDiscoeryTab, mProfile;
    private ImageView mPostTab;

    private TokenListModel mTokenListModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unlogin_mainactivity_layout);
        mContext = this;
        mHomeTab = (RelativeLayout) findViewById(R.id.tv_home);
        mMessageTab = (RelativeLayout) findViewById(R.id.tv_message);
        mDiscoeryTab = (RelativeLayout) findViewById(R.id.tv_discovery);
        mProfile = (RelativeLayout) findViewById(R.id.tv_profile);
        mPostTab = (ImageView) findViewById(R.id.fl_post);

        mFragmentManager = getSupportFragmentManager();
        setTabFragment(HOME_FRAGMENT);
        setUpListener();
//        StatusBarUtils.from(this)
//                .setTransparentStatusbar(true)
//                .setStatusBarColor(Color.WHITE)
//                .setLightStatusBar(true)
//                .process(this);

        // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
        mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(this, mAuthInfo);
        mTokenListModel = new TokenListModelImp();
    }

    private void setTabFragment(int index) {
        if (mCurrentIndex != index) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            hideAllFragments(transaction);
            switch (index) {
                case HOME_FRAGMENT:
                    mHomeTab.setSelected(true);
                    if (mHomeFragment == null) {
                        mHomeFragment = new HomeFragment();
                        transaction.add(R.id.main_content_fl, mHomeFragment);
                    } else {
                        transaction.show(mHomeFragment);
                    }
                    mCurrentIndex = HOME_FRAGMENT;
                    break;
                case MESSAGE_FRAGMENT:
                    mMessageTab.setSelected(true);
                    if (mMessageFragment == null) {
                        mMessageFragment = new MessageFragment();
                        transaction.add(R.id.main_content_fl, mMessageFragment);
                    } else {
                        transaction.show(mMessageFragment);
                    }
                    mCurrentIndex = MESSAGE_FRAGMENT;
                    break;

                case DISCOVERY_FRAGMENT:
                    mDiscoeryTab.setSelected(true);
                    if (mDiscoverFragment == null) {
                        mDiscoverFragment = new DiscoverFragment();
                        transaction.add(R.id.main_content_fl, mDiscoverFragment);
                    } else {
                        transaction.show(mDiscoverFragment);
                    }
                    mCurrentIndex = DISCOVERY_FRAGMENT;
                    break;
                case PROFILE_FRAGMENT:
                    mProfile.setSelected(true);
                    if (mProfileFragment == null) {
                        mProfileFragment = new ProfileFragment();
                        transaction.add(R.id.main_content_fl, mProfileFragment);
                    } else {
                        transaction.show(mProfileFragment);
                    }
                    mCurrentIndex = PROFILE_FRAGMENT;
                    break;
            }
            transaction.commit();
        } else if (mCurrentIndex == HOME_FRAGMENT && mHomeFragment != null) {

        }
    }

    private void hideAllFragments(FragmentTransaction transaction) {
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mMessageFragment != null) {
            transaction.hide(mMessageFragment);
        }

        if (mDiscoverFragment != null) {
            transaction.hide(mDiscoverFragment);
        }
        if (mProfileFragment != null) {
            transaction.hide(mProfileFragment);
        }
        mHomeTab.setSelected(false);
        mMessageTab.setSelected(false);
        mDiscoeryTab.setSelected(false);
        mProfile.setSelected(false);
    }

    private void setUpListener() {
        mHomeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabFragment(HOME_FRAGMENT);

            }
        });
        mMessageTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabFragment(MESSAGE_FRAGMENT);
            }
        });
        mPostTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext, "请先登录");
            }
        });
        mDiscoeryTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabFragment(DISCOVERY_FRAGMENT);

            }
        });
        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabFragment(PROFILE_FRAGMENT);
            }
        });
    }

    public void openLoginWebView(View view) {
        //https://api.weibo.com/oauth2/authorize?client_id=4274423832&redirect_uri=www.lovemsky.com

//        Intent intent = new Intent(mContext, WebViewActivity.class);
//        intent.putExtra("url", Constants.authurl);
//        startActivity(intent);
//        finish();

        mSsoHandler.authorizeWeb(this);
    }

    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;
    private AuthInfo mAuthInfo;
    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;
    //获取用户信息的接口（需要先把官方提供的weibosdk库引入到工程当中来）
    UsersAPI mUsersAPI;

    @Override
    public void onComplete(Bundle bundle) {
//从Bundle中解析Token
        mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
        if (mAccessToken.isSessionValid()) {//授权成功
//            String token = mAccessToken.getToken();

            mTokenListModel.addToken(UnLoginActivity.this, mAccessToken);
            Toast.makeText(UnLoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
            //获取用户具体信息
            getUserInfo();
            //跳转页面
            Intent intent = new Intent(UnLoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            /**
             *  以下几种情况，您会收到 Code：
             * 1. 当您未在平台上注册应用程序的包名与签名时；
             * 2. 当您注册的应用程序包名与签名不正确时；
             * 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
             */
            String code = bundle.getString("code");//直接从bundle里边获取
            if (!StringUtil.isEmpty(code)) {
                Toast.makeText(UnLoginActivity.this, "签名不正确", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onWeiboException(WeiboException e) {

    }

    @Override
    public void onCancel() {

    }

    /**
     * 获取用户个人信息
     */
    private void getUserInfo() {
        //获取用户信息接口
        mUsersAPI = new UsersAPI(UnLoginActivity.this, Constants.APP_KEY, mAccessToken);
        System.out.println("mUsersAPI  ----->   " + mUsersAPI.toString());

        //调用接口
        long uid = Long.parseLong(mAccessToken.getUid());
        System.out.println("--------------uid-------------->    " + uid);
        mUsersAPI.show(uid, mListener);//将uid传递到listener中，通过uid在listener回调中接收到该用户的json格式的个人信息
    }

    /**
     * 实现异步请求接口回调，并在回调中直接解析User信息
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!StringUtil.isEmpty(response)) {
                //调用User#parse将JSON串解析成User对象
                User user = User.parse(response);
                String nickName = user.screen_name;
//                Constants.name = user.screen_name;
//                Constants.gender = user.gender;
//                Constants.location = user.location;
//                Toast.makeText(LogInActivity.this, "用户的昵称： " + nickName, Toast.LENGTH_SHORT).show();
            }
        }

        /**
         *如果运行测试的时候，登录的账号不是注册应用的账号，那么需要去：
         *开放平台-》管理中心-》应用信息-》测试信息-》添加测试账号（填写用户昵称）！
         * 否则便会抛出以下异常
         */
        @Override
        public void onWeiboException(WeiboException e) {
            e.printStackTrace();
            Toast.makeText(UnLoginActivity.this, "获取用户个人信息 出现异常", Toast.LENGTH_SHORT).show();
        }
    };
}
