package com.example.rh.artlive.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.activity.CollegesMainActivity;
import com.example.rh.artlive.adapter.LiveHotAdapter;
import com.example.rh.artlive.adapter.RightAdapter;
import com.example.rh.artlive.bean.LiveHotBean;
import com.example.rh.artlive.bean.RightBean;
import com.example.rh.artlive.bean.WishBean;
import com.example.rh.artlive.live.LookActivity;
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
 * Created by rh on 2017/12/29.
 *直播列表
 */

public class LiveHotFragment extends BaseFragment implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,LoadRecyclerView.LoadListener,OnItemClickListener<LiveHotBean> {

    private View view;

    private PullToRefreshLayout mAuto;
    private LoadRecyclerView mLoad;
    private LiveHotAdapter mChestAdapter;
    private ArrayList<LiveHotBean> mData = new ArrayList<>();
    private GridLayoutManager mManager;

    private String mTag_Name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savesInstanceState) {
        super.onCreateView(inflater, container, savesInstanceState);
        view = inflater.inflate(R.layout.fargment_live_hot, null);
        init();
        setAdapter();
        setListener();
        setData(getFragmentManager());

        return view;
    }

    private void init(){
        Bundle bundle=getArguments();
        mTag_Name=bundle.getString("tag_name");
        Log.e("值"+mTag_Name);
        mAuto = (PullToRefreshLayout)view.findViewById(R.id.network_pager_myAuto);
        mLoad = (LoadRecyclerView) view.findViewById(R.id.network_myLoad);
        LinearLayoutManager lm=new LinearLayoutManager(getActivity());
        lm.setOrientation(OrientationHelper.VERTICAL);
        mManager = new GridLayoutManager(getActivity(), 2);
        mLoad.setLayoutManager(lm);
        mLoad.setLayoutManager(mManager);
    }
    private void setListener(){
        mAuto.setOnRefreshListener(this);
        mLoad.setLoadListener(this);
    }

    private void setAdapter() {
        mChestAdapter = new LiveHotAdapter(getActivity(), R.layout.recycler_live_hot_adapter, mData);
        mLoad.setAdapter(mChestAdapter);
        mLoad.setIsHaveData(false);
        mChestAdapter.setOnItemClickListener(this);
    }
    private void  setData(FragmentManager fm){
        mData = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        HttpUtil.postHttpRequstProgess(getActivity(), "正在加载", UrlConstant.WORMHOT, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
            }
            @Override
            public void onResponse(String response) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
                Log.e("虫窝直播列表"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONArray lists=data.getJSONArray("lists");
                            for (int i=0;i<lists.length();i++){
                                JSONObject jsonObject1=lists.getJSONObject(i);
                                LiveHotBean liveHotBean=JSON.parseObject(jsonObject1.toString(),LiveHotBean.class);
                                mData.add(liveHotBean);
                            }
                            setAdapter();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "hot", fm);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, LiveHotBean s, int position) {
        Intent intent=new Intent(getActivity(), LookActivity.class);
        intent.putExtra("path",s.getPull());
        intent.putExtra("room_id",s.getLive_no());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, LiveHotBean noPaiBean, int position) {
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
        setData(null);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }
}
