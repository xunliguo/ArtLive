package com.example.rh.artlive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.CollectAdapter;
import com.example.rh.artlive.adapter.ExamAdapter;
import com.example.rh.artlive.bean.ExamBean;
import com.example.rh.artlive.bean.PhotoBean;
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
 * Created by rh on 2017/12/16.
 * 考试
 */

public class ExamActivity extends BaseFragmentActivity  implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,LoadRecyclerView.LoadListener,OnItemClickListener<ExamBean> {


    private ImageView mShowDraw;
    private ImageView mAddView;

    private PullToRefreshLayout mAuto;
    private LoadRecyclerView mLoad;
    private ExamAdapter mChestAdapter;
    private ArrayList<ExamBean> mData = new ArrayList<>();

    private RelativeLayout mSett;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        init();
        setAdapter();
        setListener();
        initData();

    }


    private void init(){
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mAddView=(ImageView)findViewById(R.id.showTime);
        mSett=(RelativeLayout)findViewById(R.id.setting);
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
        mAddView.setOnClickListener(this);
        mSett.setOnClickListener(this);
    }

    private void setAdapter() {
        mChestAdapter = new ExamAdapter(this, R.layout.recycler_exam_adapter, mData);
        mLoad.setAdapter(mChestAdapter);
        mLoad.setIsHaveData(false);
        mChestAdapter.setOnItemClickListener(this);
    }

    private void initData(){
        mData = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.EXAM, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
            }
            @Override
            public void onResponse(String response) {
                Log.e("考试列表"+response);
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONArray data=jsonObject.getJSONArray("data");
                            for (int i=0;i<data.length();i++){
                                JSONObject jsonObject1=data.getJSONObject(i);
                                ExamBean photoBean= JSON.parseObject(jsonObject1.toString(),ExamBean.class);
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
            case R.id.showTime:
                Intent intent=new Intent(ExamActivity.this,AddExamActivity.class);
                startActivity(intent);
                break;
            case R.id.setting:
                Intent intent1=new Intent(ExamActivity.this,TogetherActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, ExamBean noPaiBean, int position) {

    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, ExamBean noPaiBean, int position) {
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
