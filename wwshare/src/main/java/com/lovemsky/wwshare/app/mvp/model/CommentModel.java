package com.lovemsky.wwshare.app.mvp.model;

import android.content.Context;

import com.lovemsky.wwshare.app.common.entity.Comment;
import com.lovemsky.wwshare.app.common.entity.list.CommentList;

import java.util.ArrayList;

/**
 * Created by wenmingvs on 16/5/15.
 */
public interface CommentModel {

    interface OnDataFinishedListener {
        void noMoreDate();

        void onDataFinish(ArrayList<Comment> commentlist);

        void onError(String error);
    }


    public void toMe(int groupType, Context context, OnDataFinishedListener onDataFinishedListener);

    public void toMeNextPage(int groupType, Context context, OnDataFinishedListener onDataFinishedListener);

    public void byMe(Context context, OnDataFinishedListener onDataFinishedListener);

    public void byMeNextPage(Context context, OnDataFinishedListener onDataFinishedListener);

    public void cacheSave(int groupType, Context context, CommentList commentList);

    public void cacheLoad(int groupType, Context context, OnDataFinishedListener onDataFinishedListener);

}
