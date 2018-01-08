package com.example.rh.artlive.articlechoice;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by rh on 2017/11/24.
 */

public class MyViewPager extends ViewPager {

    private boolean isScroll=true;
    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param isScroll 是否滑动（true 滑动，false 禁止）
     */
    public void setScroll(boolean isScroll){
        this.isScroll=isScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isScroll) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isScroll) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }
}
