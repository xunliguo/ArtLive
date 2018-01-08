package com.example.rh.artlive.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.MajorAdapter;
import com.example.rh.artlive.adapter.TestCenterAdapter;
import com.example.rh.artlive.bean.SchoolBean;
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
import java.util.Map;

import okhttp3.Call;

/**
 * Created by rh on 2017/12/23.
 * 考点
 */

public class TestCenterActivity extends BaseFragmentActivity  implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,LoadRecyclerView.LoadListener,OnItemClickListener<SchoolBean> {

    private PullToRefreshLayout mAuto;
    private LoadRecyclerView mLoad;
    private TestCenterAdapter mChestAdapter;
    private ArrayList<SchoolBean> mData = new ArrayList<>();
    private ImageView mShowDraw;
    private String mSchool_id;
    private String majortwo_id;
    private RelativeLayout mSetting;
    public static String flag="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_center);
        init();
        setAdapter();
        setListener();
        initData();
    }

    private void init(){
        Intent intent=getIntent();
        mSchool_id=intent.getStringExtra("majortop_id");
        majortwo_id=intent.getStringExtra("majortwo_id");
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
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
        mSetting.setOnClickListener(this);
    }

    private void setAdapter() {
        mChestAdapter = new TestCenterAdapter(this, R.layout.recycler_apply_test_center, mData);
        mLoad.setAdapter(mChestAdapter);
        mLoad.setIsHaveData(false);
        mChestAdapter.setOnItemClickListener(this);
    }

    private void initData(){
        mData = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("type","test");
        map.put("majortop_id",mSchool_id);
        map.put("majortwo_id",majortwo_id);
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.ADD_EXAM, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
            }
            @Override
            public void onResponse(String response) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
                Log.e("考点"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONArray data=jsonObject.getJSONArray("data");
                            for (int i=0;i<data.length();i++){
                                JSONObject jsonObject1=data.getJSONObject(i);
                                SchoolBean schoolBean= JSON.parseObject(jsonObject1.toString(),SchoolBean.class);
                                mData.add(schoolBean);
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
            case R.id.showTime:
                break;
            case R.id.setting:
                mSharePreferenceUtil.setString(SharedPerfenceConstant.APPLY_CENTER,flag);
                Log.e("考点"+mSharePreferenceUtil.getString(SharedPerfenceConstant.APPLY_CENTER));
                Intent intent=new Intent(TestCenterActivity.this,AddExamActivity.class);
                startActivity(intent);
                break;
        }
    }



    @Override
    public void onItemClick(ViewGroup parent, View view, SchoolBean s, int position) {
        mSetting.setVisibility(View.VISIBLE);
        flag=s.getInfo();
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
        initData();

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }

}
