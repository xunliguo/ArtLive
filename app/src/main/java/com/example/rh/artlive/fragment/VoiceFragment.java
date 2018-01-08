package com.example.rh.artlive.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.activity.ViceoDetailsActivity;
import com.example.rh.artlive.adapter.HomeAdapter;
import com.example.rh.artlive.adapter.VoiceAdapter;
import com.example.rh.artlive.adapter.WormAdapter;
import com.example.rh.artlive.bean.FollowBean;
import com.example.rh.artlive.bean.VoiceBean;
import com.example.rh.artlive.bean.WormBean;
import com.example.rh.artlive.live.InfoMsgView;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.CustomViewPager;
import com.example.rh.artlive.view.LoadRecyclerView;
import com.example.rh.artlive.view.OnItemClickListener;
import com.example.rh.artlive.view.PullToRefreshLayout;
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
 */

public class VoiceFragment extends BaseFragment implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,LoadRecyclerView.LoadListener,OnItemClickListener<VoiceBean> {

    private View view;

    private PullToRefreshLayout mAuto;
    private LoadRecyclerView mLoad;
    private VoiceAdapter mChestAdapter;
    private ArrayList<VoiceBean> mData = new ArrayList<>();

    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    private String zt;
    List<String> list;
    private  ArrayList<String>list_details = new ArrayList<>();//广告的数据

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savesInstanceState) {
        super.onCreateView(inflater, container, savesInstanceState);
        view = inflater.inflate(R.layout.fragment_voice, null);
        isPrepared = true;
        lazyLoad();
        return view;
    }

    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        isPrepared = false;
        init();
        setAdapter();
        setListener();
        initData();
    }

    private void init(){
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
        mChestAdapter = new VoiceAdapter(getActivity(), R.layout.recycler_video_adapter, mData);
        mLoad.setAdapter(mChestAdapter);
        mLoad.setIsHaveData(false);
        mChestAdapter.setOnItemClickListener(this);
    }

    private void initData(){
        list=new ArrayList<String>();
        mData = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("type","voice");
        HttpUtil.postHttpRequstProgess(getActivity(), "正在加载", UrlConstant.WORMVIDEO, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
            }
            @Override
            public void onResponse(String response) {

                Log.e("语音数据"+response);
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONArray voice=data.getJSONArray("voice");
                            for (int i=0;i<voice.length();i++){
                                JSONObject jsonObject1=voice.getJSONObject(i);
                                VoiceBean voiceBean=JSON.parseObject(jsonObject1.toString(),VoiceBean.class);
                                mData.add(voiceBean);
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

        }
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, VoiceBean s, int position) {


        Intent intent=new Intent(getActivity(), ViceoDetailsActivity.class);
        intent.putExtra("voice_id",s.getId());
        startActivity(intent);


    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, VoiceBean noPaiBean, int position) {
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
