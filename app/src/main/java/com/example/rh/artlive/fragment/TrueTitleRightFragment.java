package com.example.rh.artlive.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.activity.CollegesMainActivity;
import com.example.rh.artlive.activity.TrueMainActivity;
import com.example.rh.artlive.adapter.RightAdapter;
import com.example.rh.artlive.adapter.TrueRightAdapter;
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
 */

public class TrueTitleRightFragment extends BaseFragment implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,LoadRecyclerView.LoadListener,OnItemClickListener<RightBean> {

    private View view;

    private PullToRefreshLayout mAuto;
    private LoadRecyclerView mLoad;
    private TrueRightAdapter mChestAdapter;
    private ArrayList<RightBean> mData = new ArrayList<>();

    private String mTag_Name;
    private String mType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savesInstanceState) {
        super.onCreateView(inflater, container, savesInstanceState);
        view = inflater.inflate(R.layout.fragment_colleges_right, null);
        init();
        setAdapter();
        setListener();
        setData(getFragmentManager());

        return view;
    }

    private void init(){
        Bundle bundle=getArguments();
        mTag_Name=bundle.getString("tag_name");
        mType=bundle.getString("type");
        Log.e("值"+mTag_Name);
        mAuto = (PullToRefreshLayout)view.findViewById(R.id.network_pager_myAuto);
        mLoad = (LoadRecyclerView) view.findViewById(R.id.network_myLoad);
        LinearLayoutManager lm=new LinearLayoutManager(getActivity());
        lm.setOrientation(OrientationHelper.VERTICAL);
        mLoad.setLayoutManager(lm);
    }
    private void setListener(){
        mAuto.setOnRefreshListener(this);
        mLoad.setLoadListener(this);
    }

    private void setAdapter() {
        mChestAdapter = new TrueRightAdapter(getActivity(), R.layout.recycler_right_true_adapter, mData);
        mLoad.setAdapter(mChestAdapter);
        mLoad.setIsHaveData(false);
        mChestAdapter.setOnItemClickListener(this);
    }
    private void setData(FragmentManager fm){

        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        if ("1".equals(mType)) {
            map.put("type", "统考");
        }else{
            map.put("type", mTag_Name);
        }
        HttpUtil.postHttpRequstProgess(getActivity(), "正在加载", UrlConstant.TRUE_RIGHT, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("真题右侧数据"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONArray data=jsonObject.getJSONArray("data");
                            for (int i=0;i<data.length();i++){
                                JSONObject jsonObject1=data.getJSONObject(i);
                                RightBean rightBean = JSON.parseObject(jsonObject1.toString(), RightBean.class);
                                mData.add(rightBean);
                            }
                            setAdapter();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "teacher", fm);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, RightBean s, int position) {
        Intent intent=new Intent(getActivity(), TrueMainActivity.class);
        intent.putExtra("school_id",s.getId());
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
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }
}
