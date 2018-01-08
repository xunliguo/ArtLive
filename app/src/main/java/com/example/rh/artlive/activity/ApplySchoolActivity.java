package com.example.rh.artlive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.AppSchoolAdapter;
import com.example.rh.artlive.adapter.ReadAdapter;
import com.example.rh.artlive.adapter.ShowAdapter;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.bean.SchoolBean;
import com.example.rh.artlive.bean.ShowBean;
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
 * Created by rh on 2017/12/23.
 * 报考院校
 */

public class ApplySchoolActivity extends BaseFragmentActivity implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,LoadRecyclerView.LoadListener,OnItemClickListener<SchoolBean> {


    private PullToRefreshLayout mAuto;
    private LoadRecyclerView mLoad;
    private AppSchoolAdapter mChestAdapter;
    private ArrayList<SchoolBean> mData = new ArrayList<>();
    public static String flag="";
    private String flag_name;
    private  int pageNo=1;

    private ImageView mShowDraw;
    private ImageView mSearch;
    private EditText mText;
    private RelativeLayout mSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_school);
        init();
        setAdapter();
        setListener();
        initData();
    }

    private void init(){
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mSearch=(ImageView)findViewById(R.id.search);
        mText=(EditText)findViewById(R.id.et_password);
        mSetting=(RelativeLayout)findViewById(R.id.setting);
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
        mSearch.setOnClickListener(this);
        mSetting.setOnClickListener(this);
    }

    private void setAdapter() {
        mChestAdapter = new AppSchoolAdapter(this, R.layout.recycler_apply_school, mData);
        mLoad.setAdapter(mChestAdapter);
        mLoad.setIsHaveData(false);
        mChestAdapter.setOnItemClickListener(this);
    }

    private void initData(){
        mData = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("type","school");
        map.put("page",String.valueOf(pageNo));//分页
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.ADD_EXAM, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
            }
            @Override
            public void onResponse(String response) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
                Log.e("院校"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.has("data")) {
                        if ("1".equals(jsonObject.getString("state"))) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONArray school=data.getJSONArray("school");
                            for (int i=0;i<school.length();i++){
                                JSONObject jsonObject1=school.getJSONObject(i);
                                SchoolBean showBean= JSON.parseObject(jsonObject1.toString(),SchoolBean.class);
                                mData.add(showBean);
                            }
                            mChestAdapter.setData(mData);
                            mChestAdapter.notifyDataSetChanged();
                            mLoad.setIsHaveData(true);//为false时走加载更多
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "data1", null);
    }
    private void setSearch(){
        mData = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("type","school");
        map.put("keywords",mText.getText()+"".toString().trim());
        map.put("page",String.valueOf(pageNo));//分页
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.ADD_EXAM, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
            }
            @Override
            public void onResponse(String response) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
                Log.e("院校"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.has("data")) {
                        if ("1".equals(jsonObject.getString("state"))) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONArray school=data.getJSONArray("school");
                            for (int i=0;i<school.length();i++){
                                JSONObject jsonObject1=school.getJSONObject(i);
                                SchoolBean showBean= JSON.parseObject(jsonObject1.toString(),SchoolBean.class);
                                mData.add(showBean);
                            }
                            mChestAdapter.setData(mData);
                            mChestAdapter.notifyDataSetChanged();
                            mLoad.setIsHaveData(true);//为false时走加载更多
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
            case R.id.search:
                if ("".equals(mText.getText()+"".toString().trim())){
                    ToastUtil.showToast(ApplySchoolActivity.this,"亲输入");
                }else {
                    setSearch();
                }
                break;
            case R.id.setting:
                mSharePreferenceUtil.setString(SharedPerfenceConstant.APPLY_SCHOOL,flag_name);
                Intent intent=new Intent(ApplySchoolActivity.this,ExamMajorActivity.class);
                intent.putExtra("school_id",flag);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, SchoolBean s, int position) {
        mSetting.setVisibility(View.VISIBLE);
        flag=s.getSchool_id();
        flag_name=s.getSchool_name();
        setAdapter();
    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, SchoolBean noPaiBean, int position) {
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
