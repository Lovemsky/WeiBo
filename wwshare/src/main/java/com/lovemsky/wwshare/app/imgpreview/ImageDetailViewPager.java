package com.lovemsky.wwshare.app.imgpreview;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by wenmingvs on 16/4/19.
 */
public class ImageDetailViewPager extends ViewPager {


    public ImageDetailViewPager(Context context) {
        super(context);
    }


    public ImageDetailViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }


    }
}
