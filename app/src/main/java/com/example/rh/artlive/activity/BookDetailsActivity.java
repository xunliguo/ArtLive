package com.example.rh.artlive.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.BookItemAdapter;
import com.example.rh.artlive.articlechoice.ItemAdapter;
import com.example.rh.artlive.articlechoice.MyViewPager;
import com.example.rh.artlive.articlechoice.QuestionBean;
import com.example.rh.artlive.articlechoice.QuestionOptionBean;
import com.example.rh.artlive.bean.BookBean;
import com.example.rh.artlive.fragment.BookAllFragment;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.PullToRefreshLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by rh on 2017/12/19.
 * 书籍详情
 */

public class BookDetailsActivity extends BaseFragmentActivity implements View.OnClickListener{


    private MyViewPager vp;
    private BookItemAdapter pagerAdapter;
    public static int currentIndex = 0;
    private Context context;
    private ImageView mShowDraw;
    private ImageView mBook_Back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        init();
        setListener();
        initView();

    }
    private void init(){
        Intent intent=getIntent();
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mBook_Back=(ImageView)findViewById(R.id.back_image);
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
                pagerAdapter = new BookItemAdapter(getSupportFragmentManager());
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
