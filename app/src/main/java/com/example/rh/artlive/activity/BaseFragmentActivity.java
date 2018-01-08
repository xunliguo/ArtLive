package com.example.rh.artlive.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;


import com.example.rh.artlive.util.ActivityUtils;
import com.example.rh.artlive.util.SharePreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by PC on 2016/6/24.
 */
public class BaseFragmentActivity extends FragmentActivity {

    protected int width;
    protected int height;
    protected WindowManager mWindowManager;
    protected ActivityUtils mActivityUtils;
    protected SharePreferenceUtil mSharePreferenceUtil;
    protected SharePreferenceUtil msharedPreferencesMgr;
//    protected BottomFragment mBottomFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWindowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        mActivityUtils = ActivityUtils.getInstance();
        mSharePreferenceUtil = SharePreferenceUtil.getInstance(this.getApplicationContext());
        msharedPreferencesMgr = SharePreferenceUtil.getInstance(this.getApplicationContext());
//        showBottom(true);
    }

//    /**
//     * @param show 显示或关闭底部导航栏
//     */
//    protected void showBottom(boolean show) {
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        if (show) {
//            if (mBottomFragment == null) {
//                mBottomFragment = BottomFragment.newInstance();
//                ft.add(R.id.bottom_container, mBottomFragment).commitAllowingStateLoss();
//            } else {
//                ft.show(mBottomFragment).commitAllowingStateLoss();
//            }
//        } else {
//            if (mBottomFragment != null) {
//                ft.hide(mBottomFragment).commitAllowingStateLoss();
//            }
//        }
//    }

    /*
    * 将时间戳转换为时间
    */
    public static String StampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     *去两个字符串之间内容
     * @param str
     * @param strStart
     * @param strEnd
     * @return
     */
    public  String  getInsideString(String  str, String strStart, String strEnd ) {
        if ( str.indexOf(strStart) < 0 ){
            return "";
        }
        if ( str.indexOf(strEnd) < 0 ){
            return "";
        }
        return str.substring(str.indexOf(strStart) + strStart.length(), str.indexOf(strEnd));
    }

    /**
     * 时间转换为时间戳
     *
     * @param timeStr 时间 例如: 2016-03-09
     * @return
     */
    public static long getTimeStamp(String timeStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = simpleDateFormat.parse(timeStr);
            long timeStamp = date.getTime();
            return timeStamp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
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
        Intent intent = new Intent(this, pClass);
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

    protected int timeReturn() {
        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //皮肤相关
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
