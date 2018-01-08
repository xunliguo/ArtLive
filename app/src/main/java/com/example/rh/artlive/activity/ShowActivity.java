package com.example.rh.artlive.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.ShowAdapter;
import com.example.rh.artlive.adapter.UserNoPayAdapter;
import com.example.rh.artlive.bean.NoPaiBean;
import com.example.rh.artlive.bean.ShowBean;
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
 */

public class ShowActivity extends BaseFragmentActivity implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,LoadRecyclerView.LoadListener,OnItemClickListener<ShowBean> {

    private PullToRefreshLayout mAuto;
    private LoadRecyclerView mLoad;
    private ShowAdapter mChestAdapter;
    private ArrayList<ShowBean> mData = new ArrayList<>();

    private ImageView mShowDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        init();
        setAdapter();
        setListener();
        initData();
    }

    private void init(){
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mAuto = (PullToRefreshLayout)findViewById(R.id.network_pager_myAuto);
        mLoad = (LoadRecyclerView) findViewById(R.id.network_myLoad);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(OrientationHelper.VERTICAL);// 纵向布局
        mLoad.setLayoutManager(lm);
    }
    private void setListener(){
        mAuto.setOnRefreshListener(this);
        mLoad.setLoadListener(this);
        mShowDraw.setOnClickListener(this);
    }

    private void setAdapter() {
        mChestAdapter = new ShowAdapter(this, R.layout.recycler_show_adapter, mData);
        mLoad.setAdapter(mChestAdapter);
        mLoad.setIsHaveData(false);
        mChestAdapter.setOnItemClickListener(this);
    }

    private void initData(){
        mData = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.SHOW, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
            }
            @Override
            public void onResponse(String response) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
                Log.e("活动展示"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.has("data")) {
                        if ("1".equals(jsonObject.getString("state"))) {
                            JSONArray data=jsonObject.getJSONArray("data");
                            for (int i=0;i<data.length();i++){
                                JSONObject jsonObject1=data.getJSONObject(i);
                                ShowBean showBean= JSON.parseObject(jsonObject1.toString(),ShowBean.class);
                                mData.add(showBean);
                            }
                            setAdapter();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "data1", null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showDraw:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, ShowBean noPaiBean, int position) {

    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, ShowBean noPaiBean, int position) {
        return false;
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void loadFinish() {

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        initData();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }
}
