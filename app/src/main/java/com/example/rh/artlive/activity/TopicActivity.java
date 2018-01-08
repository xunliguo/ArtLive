package com.example.rh.artlive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.TopicAdapter;
import com.example.rh.artlive.adapter.UserNoPayAdapter;
import com.example.rh.artlive.bean.NoPaiBean;
import com.example.rh.artlive.bean.TopicBean;
import com.example.rh.artlive.fragment.HomeFragment;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.ToastUtil;
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
 * 话题
 */

public class TopicActivity extends BaseFragmentActivity  implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,LoadRecyclerView.LoadListener,OnItemClickListener<TopicBean> {

    private ImageView mShowDraw;
    private ImageView mSearchView;
    private EditText mText;
    private  int pageNo=1;

    private PullToRefreshLayout mAuto;
    private LoadRecyclerView mLoad;
    private TopicAdapter mChestAdapter;
    private ArrayList<TopicBean> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        init();
        setAdapter();
        setListener();
        initData();
    }

    private void init(){
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mSearchView=(ImageView)findViewById(R.id.search);
        mText=(EditText)findViewById(R.id.et_password);

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
        mSearchView.setOnClickListener(this);
    }

    private void setAdapter() {
        mChestAdapter = new TopicAdapter(this, R.layout.recycler_topic_adpater, mData);
        mLoad.setAdapter(mChestAdapter);
        mLoad.setIsHaveData(false);
        mChestAdapter.setOnItemClickListener(this);
    }

    private void initData(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("page",String.valueOf(pageNo));//分页
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.TOPIC, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
            }
            @Override
            public void onResponse(String response) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
                Log.e("话题"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONArray data=jsonObject.getJSONArray("data");
                            for (int i=0;i<data.length();i++){
                                JSONObject jsonObject1=data.getJSONObject(i);
                                TopicBean topicBean=JSON.parseObject(jsonObject1.toString(),TopicBean.class);
                                mData.add(topicBean);
                            }
//                            setAdapter();
                            mChestAdapter.setData(mData);
//                            mChestAdapter.notifyDataSetChanged();
                            mLoad.setIsHaveData(true);//为false时走加载更多
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "data1", null);
    }

    /**
     * 搜索
     */
    private void setSearch(){
        mData = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("keywords",mText.getText()+"".toString().trim());//分页
        map.put("page",String.valueOf(pageNo));//分页
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.TOPIC, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
            }
            @Override
            public void onResponse(String response) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
                Log.e("话题搜索"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONArray data=jsonObject.getJSONArray("data");
                            for (int i=0;i<data.length();i++){
                                JSONObject jsonObject1=data.getJSONObject(i);
                                TopicBean topicBean=JSON.parseObject(jsonObject1.toString(),TopicBean.class);
                                mData.add(topicBean);
                            }
//                            setAdapter();
                            mChestAdapter.setData(mData);
//                            mChestAdapter.notifyDataSetChanged();
                            mLoad.setIsHaveData(true);//为false时走加载更多
                        }
                    }else{
                        ToastUtil.showToast(TopicActivity.this,"没有该数据");
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
            case R.id.search:
                if ("".equals(mText.getText()+"".toString().trim())){
                    ToastUtil.showToast(TopicActivity.this,"请输入");
                }else{
                    setSearch();
                }
                break;
        }
    }



    @Override
    public void onItemClick(ViewGroup parent, View view, TopicBean s, int position) {
//        Log.e("话题标题"+getInsideString(s.getThe_title(),"#","#"));
        Intent intent=new Intent(TopicActivity.this,TopicDetailsActivity.class);
        intent.putExtra("topic_title",s.getThe_title());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, TopicBean noPaiBean, int position) {
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
        mLoad.setIsHaveData(false);
        pageNo = 1;
        mData = new ArrayList<>();
        initData();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        pageNo=pageNo+1;
        initData();
    }
}
