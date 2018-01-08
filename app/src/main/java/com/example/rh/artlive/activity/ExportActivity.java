package com.example.rh.artlive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.ExportAdapter;
import com.example.rh.artlive.adapter.UserNoPayAdapter;
import com.example.rh.artlive.bean.ExportBean;
import com.example.rh.artlive.bean.NoPaiBean;
import com.example.rh.artlive.fragment.ExportFamousFragment;
import com.example.rh.artlive.fragment.ExportMineFragment;
import com.example.rh.artlive.fragment.WormFollowFragment;
import com.example.rh.artlive.fragment.WormHotFragment;
import com.example.rh.artlive.fragment.WormMineFragment;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.LoadRecyclerView;
import com.example.rh.artlive.view.OnItemClickListener;
import com.example.rh.artlive.view.PullToRefreshLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by rh on 2017/11/20.
 * 专家师范
 */

public class ExportActivity extends BaseFragmentActivity  implements View.OnClickListener{

    private ImageView mShowDraw;


    private ExportFamousFragment testFragment2;//订餐
    private ExportMineFragment testFragment3;//购物

    private TabLayout mTabLayout;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        fragmentManager = getSupportFragmentManager();

        testFragment2 = new ExportFamousFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content, testFragment2).commit();

        init();
        setListener();
        initView();
    }

    private void init(){
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
    }

    private void setListener(){
        mShowDraw.setOnClickListener(this);
    }

    /***
     * 初始化控件
     */
    public void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.mTabLayout);

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                hideFragments(transaction);
                switch (tab.getPosition()) {
                    case 0:
                        if (testFragment2 == null) {
                            testFragment2 = new ExportFamousFragment();
                            transaction.add(R.id.content, testFragment2);
                        } else {
                            transaction.show(testFragment2);
                        }
                        break;
                    case 1:
                        if (testFragment3 == null) {
                            testFragment3 = new ExportMineFragment();
                            transaction.add(R.id.content, testFragment3);
                        } else {
                            transaction.show(testFragment3);
                        }
                        break;
                }
                transaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void hideFragments(FragmentTransaction transaction) {

        if (testFragment2 != null) {
            transaction.hide(testFragment2);
        }
        if (testFragment3 != null) {
            transaction.hide(testFragment3);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showDraw:
                finish();
                break;
        }
    }
}
