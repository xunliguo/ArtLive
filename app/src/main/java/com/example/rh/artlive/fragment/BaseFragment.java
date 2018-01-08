package com.example.rh.artlive.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;


import com.example.rh.artlive.util.ActivityUtils;
import com.example.rh.artlive.util.SharePreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by PC on 2016/6/24.
 */
public  class BaseFragment extends Fragment {
    protected int width;
    protected int height;
    protected WindowManager mWindowManager;
    protected ActivityUtils mActivityUtils;
    protected SharePreferenceUtil mSharePreferenceUtil;
    protected SharePreferenceUtil msharedPreferencesMgr;

    protected boolean isVisible;
    /**
     * 在这里实现Fragment数据的缓加载.
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }
    protected void onVisible(){
        lazyLoad();
    }
    protected void lazyLoad(){};
    protected void onInvisible(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mWindowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);

        mActivityUtils = ActivityUtils.getInstance();
        mSharePreferenceUtil = SharePreferenceUtil.getInstance(getActivity().getApplicationContext());
        msharedPreferencesMgr = SharePreferenceUtil.getInstance(getActivity().getApplicationContext());

        return super.onCreateView(inflater, container, savedInstanceState);

    }

    /**
     * 通过类名启动Activity
     *
     * @param pClass
     */
    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    /**
     * 通过类名启动Activity，并且含有Bundle数据
     *
     * @param pClass
     * @param pBundle
     */
    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(getActivity(), pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    protected int getScreenWidth() {
        return width = mWindowManager.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    protected int getScreenHeight() {
        return height = mWindowManager.getDefaultDisplay().getHeight();
    }

    protected String getStatus(JSONObject json) {
        String str = "";
        try {
            str = json.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * 滑动到指定位置
     *
     * @param myScrolls
     * @param imgView
     * @param isBottom
     */
    protected void scrollToPosition(final ScrollView myScrolls, final ImageView imgView, final boolean isBottom) {
        myScrolls.post(new Runnable() {
            @Override
            public void run() {
                int[] location = new int[2];
                imgView.getLocationOnScreen(location);
                int offset = location[1] - myScrolls.getMeasuredHeight();
                if (offset < 0) {
                    offset = 0;
                }
                if (isBottom) {
                    myScrolls.fullScroll(ScrollView.FOCUS_DOWN);
                } else {
                    myScrolls.smoothScrollTo(0, offset);
                }
            }
        });
    }

    protected void scrollToPosition3(final ScrollView myScrolls, final ImageView imgView, final LinearLayout ly4, final LinearLayout chart4Show, final boolean isBottom) {
        myScrolls.post(new Runnable() {
            @Override
            public void run() {
                int viewHeight = imgView.getMeasuredHeight();
                int allHeight = myScrolls.getChildAt(0).getHeight();
                int ly4Height = ly4.getMeasuredHeight();
                int chart4ShowHeight = chart4Show.getMeasuredHeight();
                myScrolls.smoothScrollTo(0, allHeight - (ly4Height + chart4ShowHeight + viewHeight + 30) * 2);
            }
        });
    }

    /**
     * @param myScrolls scrollView view 对象
     * @param chart 隐藏显示的chart <点击的chart和他位于点击chart上面的char对象>
     * @param title 隐藏显示的箭头上面的非隐藏布局 <同上>
     * @param arror 隐藏显示的箭头 <同上>
     */
    int heightAdd = 0;

    protected void scrollToPositionDemo(final ScrollView myScrolls, final View[] chart, final View[] title, final View[] arror, String type) {
        heightAdd = 0;
        if (0 == Integer.parseInt(type)) {// App 1 1 1 1
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < chart.length; i++) {
                        if (chart[i].getVisibility() == View.VISIBLE) {
                            heightAdd = heightAdd + chart[i].getHeight() + title[i].getHeight() + arror[i].getHeight();
                        }else{
                            heightAdd = heightAdd+title[i].getHeight() + arror[i].getHeight();
                        }
                    }
                    heightAdd = heightAdd - (chart[chart.length-1].getHeight() + title[chart.length-1].getHeight() + arror[chart.length-1].getHeight());
                    myScrolls.scrollTo(0, heightAdd);
                }
            }, 100);
        } else if (1 == Integer.parseInt(type) || 2 == Integer.parseInt(type) || 5 == Integer.parseInt(type) || 6 == Integer.parseInt(type) || 7 == Integer.parseInt(type) || 9 == Integer.parseInt(type)) {// Network 2 2 1
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < chart.length; i++) {
                        if (chart[i].getVisibility() == View.VISIBLE) {
                            heightAdd = heightAdd + chart[i].getHeight() + title[i].getHeight() + arror[i].getHeight();
                        }else{
                            heightAdd = heightAdd + title[i].getHeight() + arror[i].getHeight();
                        }
                    }
                    heightAdd = heightAdd - (chart[chart.length-1].getHeight() + title[chart.length-1].getHeight() + arror[chart.length-1].getHeight());
                    myScrolls.scrollTo(0, heightAdd);
                }
            }, 100);
        } else if (3 == Integer.parseInt(type) || 8 == Integer.parseInt(type)) {// Network 1 0 0

        } else if (4 == Integer.parseInt(type)) {// Network 2 1 1
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < chart.length; i++) {
                        if (chart[i].getVisibility() == View.VISIBLE) {
                            heightAdd = heightAdd + chart[i].getHeight() + title[i].getHeight() + arror[i].getHeight();
                        }else{
                            heightAdd = heightAdd+title[i].getHeight() + arror[i].getHeight();
                        }
                    }
                    heightAdd = heightAdd - (chart[chart.length-1].getHeight() + title[chart.length-1].getHeight() + arror[chart.length-1].getHeight());
                    myScrolls.scrollTo(0, heightAdd);
                }
            }, 100);
        }
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            String FRAGMENTS_TAG = "android:support:fragments";
            // remove掉保存的Fragment
            outState.remove(FRAGMENTS_TAG);
        }
    }
}
