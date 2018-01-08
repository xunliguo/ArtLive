package com.example.rh.artlive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.SearchAdapter;
import com.example.rh.artlive.bean.SearchBean;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
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
 * Created by rh on 2017/9/12.
 * 搜索
 */

public class SearchActivity extends BaseFragmentActivity implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,LoadRecyclerView.LoadListener,OnItemClickListener<SearchBean> {

    private PullToRefreshLayout mAuto;
    private LoadRecyclerView mLoad;
    private SearchAdapter mChestAdapter;
    private ArrayList<SearchBean> mData = new ArrayList<>();

    private TextView mShowDraw;

    private ImageView mSearchView;

    private EditText mText;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        setListener();
//        setAdapter();
    }

    private void initView() {
        mText=(EditText)findViewById(R.id.login_quick_login_et_phoneNumber);
        mSearchView=(ImageView)findViewById(R.id.search);

        mAuto = (PullToRefreshLayout) findViewById(R.id.network_pager_myAuto);
        mLoad = (LoadRecyclerView) findViewById(R.id.network_myLoad);
        mShowDraw=(TextView)findViewById(R.id.class_img);
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
        mChestAdapter = new SearchAdapter(this, R.layout.recycler_topic_details_adapter, mData);
        mLoad.setAdapter(mChestAdapter);
        mLoad.setIsHaveData(true);
        mChestAdapter.setOnItemClickListener(this);
    }

    private void setData(){
        mData = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("value",mText.getText()+"".toString().trim());
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.SEARCH, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("搜索数据"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.has("data")) {
                        if ("0".equals(jsonObject.getString("state"))) {
                            JSONArray data=jsonObject.getJSONArray("data");
                            for(int i=0;i<data.length();i++){
                                JSONObject jsonObject1=data.getJSONObject(i);
                                SearchBean searchBean= JSON.parseObject(jsonObject1.toString(),SearchBean.class);
                                mData.add(searchBean);
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
            case R.id.class_img:
                finish();
                break;
            case R.id.search:
                setData();
                break;
        }
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, SearchBean s, int position) {
//        Intent intent=new Intent(SearchActivity.this,DetailsActivity.class);
//        intent.putExtra("id",s.getId());
//        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, SearchBean paidBean, int position) {
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

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }
}
