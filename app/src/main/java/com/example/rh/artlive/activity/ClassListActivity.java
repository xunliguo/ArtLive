package com.example.rh.artlive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.ClassAdapter;
import com.example.rh.artlive.adapter.PhotoAdapter;
import com.example.rh.artlive.bean.ClassBean;
import com.example.rh.artlive.bean.PhotoBean;
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
 * Created by rh on 2017/12/26.
 * 课程列表
 */

public class ClassListActivity extends BaseFragmentActivity implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,LoadRecyclerView.LoadListener,OnItemClickListener<ClassBean> {

    private PullToRefreshLayout mAuto;
    private LoadRecyclerView mLoad;
    private ClassAdapter mChestAdapter;
    private ArrayList<ClassBean> mData = new ArrayList<>();

    private ImageView mShowDraw;
    private String mOut_id;


    @Override
    protected void onCreate(Bundle savedInsaveState){
        super.onCreate(savedInsaveState);
        setContentView(R.layout.activity_class_list);

        init();
        setAdapter();
        setListener();
        initData();

    }

    private void init(){
        Intent intent=getIntent();
        mOut_id=intent.getStringExtra("inst_id");
        mAuto = (PullToRefreshLayout)findViewById(R.id.network_pager_myAuto);
        mLoad = (LoadRecyclerView) findViewById(R.id.network_myLoad);


        LinearLayoutManager lm=new LinearLayoutManager(this);
        lm.setOrientation(OrientationHelper.VERTICAL);
        mLoad.setLayoutManager(lm);
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
    }
    private void setListener(){
        mAuto.setOnRefreshListener(this);
        mLoad.setLoadListener(this);
        mShowDraw.setOnClickListener(this);
    }

    private void setAdapter() {
        mChestAdapter = new ClassAdapter(this, R.layout.recycler_class_list_adapter, mData);
        mLoad.setAdapter(mChestAdapter);
        mLoad.setIsHaveData(false);
        mChestAdapter.setOnItemClickListener(this);
    }

    private void initData(){
        mData = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("inst_id",mOut_id);
        map.put("userid",mSharePreferenceUtil.getString(SharedPerfenceConstant.USER_ID));
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.OUTFIT_DETAILS, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {

                Log.e("课程列表"+response);
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONArray css=data.getJSONArray("class");
                            for (int i=0;i<css.length();i++){
                                JSONObject bobject=css.getJSONObject(i);
                                ClassBean homeBean= JSON.parseObject(bobject.toString(), ClassBean.class);
                                mData.add(homeBean);
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
    public void onItemClick(ViewGroup parent, View view, ClassBean s, int position) {
        Intent intent=new Intent(ClassListActivity.this,OutFitClassDetailsActivity.class);
        intent.putExtra("class_id",s.getId());
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, ClassBean noPaiBean, int position) {
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

    @Override
    public void onClick(View view) {

    }

}
