package com.example.rh.artlive.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.rh.artlive.R;
import com.example.rh.artlive.articlechoice.ItemAdapter;
import com.example.rh.artlive.articlechoice.MyViewPager;
import com.example.rh.artlive.articlechoice.QuestionBean;
import com.example.rh.artlive.articlechoice.QuestionOptionBean;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by rh on 2017/11/24.
 */

public class WHTrainActivity extends BaseFragmentActivity implements View.OnClickListener{

    public static List<QuestionBean> questionlist ;
    public static QuestionBean question;
    public List<QuestionOptionBean> options1 ;
    public List<QuestionOptionBean> options2 = new ArrayList<QuestionOptionBean>();
    public List<QuestionOptionBean> options3 = new ArrayList<QuestionOptionBean>();
    public List<QuestionOptionBean> options4 = new ArrayList<QuestionOptionBean>();
    public static QuestionOptionBean option;
    private MyViewPager vp;
    private ItemAdapter pagerAdapter;
    public static int currentIndex = 0;
    private Context context;
    ArrayList<String> tea_list ;
    ArrayList<String> tea_list1 ;
    private ImageView mShowDraw;

    @Override
    protected void onResume(){
        super.onResume();
         WenChangActivity.truelist.append("");// 取得字符串的长度
         WenChangActivity.errorlist.append("");// 取得字符串的长度
    }

     @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whtrain);
        context=this;
        init();
        initView();
        setListener();
    }
    private void init(){
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
    }
    private void setListener(){
        mShowDraw.setOnClickListener(this);
    }
    private void initView(){
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                vp = (MyViewPager) findViewById(R.id.vp);
                vp.setCurrentItem(0);
                vp.setScroll(false);
                pagerAdapter = new ItemAdapter(getSupportFragmentManager(),mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
                vp.setAdapter(pagerAdapter);
                vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int arg0) {
                    }
                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                    }
                    @Override
                    public void onPageScrollStateChanged(int position) {
                        currentIndex = position;
                    }
                });

                LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(context);
                IntentFilter filter = new IntentFilter();
                filter.addAction("com.leyikao.jumptonext");
                filter.addAction("com.leyikao.jumptopage");
                lbm.registerReceiver(mMessageReceiver, filter);
            }
        },500);
    }

    /**
     * 接收点击事件，下一题接受广播
     */
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.leyikao.jumptonext")) {
                jumpToNext() ;
            } else if (intent.getAction().equals("com.leyikao.jumptopage")) {
                int index = intent.getIntExtra("index", 0);
                jumpToPage(index);
            }
        }
    };

    public void jumpToNext() {
        int position = vp.getCurrentItem();
        vp.setCurrentItem(position + 1);
    }
    public void jumpToPage(int index) {
        vp.setCurrentItem(index);
    }

    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showDraw:
                finish();
                break;
        }
    }
}
