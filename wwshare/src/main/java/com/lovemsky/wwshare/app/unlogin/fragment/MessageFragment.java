package com.lovemsky.wwshare.app.unlogin.fragment;


import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lovemsky.wwshare.R;

/**
 * Created by wenmingvs on 15/12/26.
 */
public class MessageFragment extends BaseFragment {
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.unlogin_messagefragment_layout, container, false);
        return mView;
    }


}
