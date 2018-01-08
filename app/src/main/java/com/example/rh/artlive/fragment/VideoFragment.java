package com.example.rh.artlive.fragment;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.HomeAdapter;
import com.example.rh.artlive.adapter.VideoAdapter;
import com.example.rh.artlive.adapter.WormAdapter;
import com.example.rh.artlive.bean.FollowBean;
import com.example.rh.artlive.bean.VideoBean;
import com.example.rh.artlive.bean.WormBean;
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

public class VideoFragment  extends BaseFragment implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,LoadRecyclerView.LoadListener,OnItemClickListener<VideoBean> {


    private View view;

    private PullToRefreshLayout mAuto;
    private LoadRecyclerView mLoad;
    private VideoAdapter mChestAdapter;
    private ArrayList<VideoBean> mData = new ArrayList<>();

    // 标志位，标志已经初始化完成。
    private boolean isPrepared;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savesInstanceState) {
        super.onCreateView(inflater, container, savesInstanceState);
        view = inflater.inflate(R.layout.fragment_video, null);
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
        mChestAdapter = new VideoAdapter(getActivity(), R.layout.recycler_worm_video_adapter, mData);
        mLoad.setAdapter(mChestAdapter);
        mLoad.setIsHaveData(false);
        mChestAdapter.setOnItemClickListener(this);
    }

    private void initData(){
        mData = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("type","video");
        HttpUtil.postHttpRequstProgess(getActivity(), "正在加载", UrlConstant.WORMVIDEO, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
            }
            @Override
            public void onResponse(String response) {

                Log.e("视频"+response);
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONArray video=data.getJSONArray("video");
                            for (int i=0;i<video.length();i++){
                                JSONObject jsonObject1=video.getJSONObject(i);
                                VideoBean videoBean=JSON.parseObject(jsonObject1.toString(),VideoBean.class);
                                mData.add(videoBean);
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
    public void onItemClick(ViewGroup parent, View view, VideoBean noPaiBean, int position) {

    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, VideoBean noPaiBean, int position) {
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
