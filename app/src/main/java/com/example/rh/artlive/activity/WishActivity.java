package com.example.rh.artlive.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.CollectAdapter;
import com.example.rh.artlive.adapter.FollowAdapter;
import com.example.rh.artlive.adapter.RvListener;
import com.example.rh.artlive.adapter.SortAdapter;
import com.example.rh.artlive.adapter.UserDataAdapter;
import com.example.rh.artlive.adapter.WishAdapter;
import com.example.rh.artlive.adapter.WishAdapter1;
import com.example.rh.artlive.adapter.WishAdapter2;
import com.example.rh.artlive.bean.MatchBean;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.bean.SortBean;
import com.example.rh.artlive.bean.UserFollowBean;
import com.example.rh.artlive.bean.WishBean;
import com.example.rh.artlive.bean.WishBean1;
import com.example.rh.artlive.bean.WishBean2;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.OnItemClickListener;
import com.example.rh.artlive.view.ReboundScrollView_Horizontal;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by rh on 2017/11/20.
 * 希望树
 */

public class WishActivity extends BaseFragmentActivity implements View.OnClickListener,OnItemClickListener<WishBean> {

    private ImageView mShowDraw;

    private Button mButton;

    private RecyclerView rvSort;
    private RecyclerView rvSort2;
    private RecyclerView rvSort3;
    private Context context;
    private UserDataAdapter mSortAdapter;
    private ArrayList<UserFollowBean> mSortBean;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish);
        context=this;

        init();
        setAdapter();
        setListener();
        setData();
    }

    private void init(){
        mShowDraw=(ImageView)findViewById(R.id.showDraw);

        mButton=(Button)findViewById(R.id.login_btn);
        rvSort = (RecyclerView) findViewById(R.id.rv_sort);
        rvSort2 = (RecyclerView) findViewById(R.id.rv_sort2);
        rvSort3 = (RecyclerView) findViewById(R.id.rv_sort3);
        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.setOrientation(OrientationHelper.HORIZONTAL);
        LinearLayoutManager lm1 = new LinearLayoutManager(context);
        lm1.setOrientation(OrientationHelper.HORIZONTAL);
        LinearLayoutManager lm2 = new LinearLayoutManager(context);
        lm2.setOrientation(OrientationHelper.HORIZONTAL);
        rvSort.setLayoutManager(lm);
        rvSort2.setLayoutManager(lm1);
        rvSort3.setLayoutManager(lm2);
    }

    private void setAdapter() {

    }
    private void setListener(){
        mShowDraw.setOnClickListener(this);
        mButton.setOnClickListener(this);
    }
    private void setData(){
        mSortBean=new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.WISH, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("许愿树"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {

                            JSONObject data=jsonObject.getJSONObject("data");
                            //文曲星护法
                            JSONArray legendary=data.getJSONArray("legendary");
                            mSortBean=new ArrayList<>();
                            for (int i=0;i<legendary.length();i++){
                                JSONObject jsonObject1=legendary.getJSONObject(i);
                                UserFollowBean userFollowBean=JSON.parseObject(jsonObject1.toString(),UserFollowBean.class);
                                mSortBean.add(userFollowBean);
                            }
                            mSortAdapter = new UserDataAdapter(context, mSortBean, new RvListener() {
                                @Override
                                public void onItemClick(int id, int position) {
                                }
                            });
                            rvSort.setAdapter(mSortAdapter);
                            //天使护法
                            JSONArray angel =data.getJSONArray("angel");
                            mSortBean=new ArrayList<>();
                            for (int i=0;i<angel.length();i++){
                                JSONObject jsonObject1=angel.getJSONObject(i);
                                UserFollowBean userFollowBean=JSON.parseObject(jsonObject1.toString(),UserFollowBean.class);
                                mSortBean.add(userFollowBean);
                            }
                            mSortAdapter = new UserDataAdapter(context, mSortBean, new RvListener() {
                                @Override
                                public void onItemClick(int id, int position) {
                                }
                            });
                            rvSort2.setAdapter(mSortAdapter);
                            //鲜花护法
                            JSONArray flower =data.getJSONArray("flower");
                            mSortBean=new ArrayList<>();
                            for (int i=0;i<flower.length();i++){
                                JSONObject jsonObject1=flower.getJSONObject(i);
                                UserFollowBean userFollowBean=JSON.parseObject(jsonObject1.toString(),UserFollowBean.class);
                                mSortBean.add(userFollowBean);
                            }
                            mSortAdapter = new UserDataAdapter(context, mSortBean, new RvListener() {
                                @Override
                                public void onItemClick(int id, int position) {
                                }
                            });
                            rvSort3.setAdapter(mSortAdapter);
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
            case R.id.login_btn:
                Intent intent=new Intent(WishActivity.this,WishingActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, WishBean photoBean, int position) {

    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, WishBean photoBean, int position) {
        return false;
    }
}
