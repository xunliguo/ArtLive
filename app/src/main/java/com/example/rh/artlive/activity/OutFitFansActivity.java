package com.example.rh.artlive.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.FansAdapter;
import com.example.rh.artlive.bean.FansBean;
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

public class OutFitFansActivity extends BaseFragmentActivity implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,LoadRecyclerView.LoadListener,OnItemClickListener<FansBean> {

    private ImageView mShowDraw;

    private PullToRefreshLayout mAuto;
    private LoadRecyclerView mLoad;
    private FansAdapter mChestAdapter;
    private ArrayList<FansBean> mData = new ArrayList<>();

    private String mUser_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_fit_fans);

        init();
        setAdapter();
        setListener();
        initData();

    }


    private void init(){
        Intent intent=getIntent();
        mUser_Id=intent.getStringExtra("inst_id");
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
        mChestAdapter = new FansAdapter(this, R.layout.recycler_fans_adapter, mData);
        mLoad.setAdapter(mChestAdapter);
        mLoad.setIsHaveData(false);
        mChestAdapter.setOnItemClickListener(this);
    }

    private void initData(){
        mData = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("inst_id",mUser_Id);
        map.put("type","fans");
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.OUTFOTUSERFANS, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
            }
            @Override
            public void onResponse(String response) {
                Log.e("粉丝"+response);
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONArray fans=data.getJSONArray("fans");
                            for (int i=0;i<fans.length();i++){
                                JSONObject jsonObject1=fans.getJSONObject(i);
                                FansBean photoBean= JSON.parseObject(jsonObject1.toString(),FansBean.class);
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
    public void onItemClick(ViewGroup parent, View view, FansBean noPaiBean, int position) {

    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, FansBean noPaiBean, int position) {
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
