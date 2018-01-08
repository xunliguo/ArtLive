package com.example.rh.artlive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.CollectAdapter;
import com.example.rh.artlive.adapter.TrueMainAdapter;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.bean.RightBean;
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
 * Created by rh on 2017/12/22.
 * 真题主页
 */

public class TrueMainActivity extends BaseFragmentActivity implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,LoadRecyclerView.LoadListener,OnItemClickListener<RightBean> {

    private ImageView mShowDraw;

    private PullToRefreshLayout mAuto;
    private LoadRecyclerView mLoad;
    private TrueMainAdapter mChestAdapter;
    private ArrayList<RightBean> mData = new ArrayList<>();
    private String mTrue_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true_main);

        init();
        setAdapter();
        setListener();
        initData();

    }

    private void init(){
        Intent intent=getIntent();
        mTrue_Id=intent.getStringExtra("school_id");
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
        mChestAdapter = new TrueMainAdapter(this, R.layout.recycler_true_main_adapter, mData);
        mLoad.setAdapter(mChestAdapter);
        mLoad.setIsHaveData(false);
        mChestAdapter.setOnItemClickListener(this);
    }

    private void initData(){
        mData = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("id",mTrue_Id);
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.TRUE_MAIN, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
            }
            @Override
            public void onResponse(String response) {
                Log.e("真题主页"+response);
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONArray topic=data.getJSONArray("topic");
                            for (int i=0;i<topic.length();i++){
                                JSONObject jsonObject1=topic.getJSONObject(i);
                                RightBean photoBean= JSON.parseObject(jsonObject1.toString(),RightBean.class);
                                mData.add(photoBean);
                            }
                            setAdapter();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "collect", null);
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
    public void onItemClick(ViewGroup parent, View view, RightBean s, int position) {

        Intent intent=new Intent(TrueMainActivity.this,TrueMainDetailsActivity.class);
        intent.putExtra("sid",s.getSid());
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, RightBean noPaiBean, int position) {
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
