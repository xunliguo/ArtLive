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
import com.example.rh.artlive.adapter.HomeAdapter;
import com.example.rh.artlive.adapter.RightAdapter;
import com.example.rh.artlive.adapter.RvListener;
import com.example.rh.artlive.adapter.SortAdapter;
import com.example.rh.artlive.bean.FollowBean;
import com.example.rh.artlive.bean.RightBean;
import com.example.rh.artlive.bean.SortBean;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.LoadRecyclerView;
import com.example.rh.artlive.view.OnItemClickListener;
import com.example.rh.artlive.view.PullToRefreshLayout;
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
 * Created by rh on 2017/12/6.
 * //院校右侧
 */

public class CollegesRightFragment extends BaseFragment implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,LoadRecyclerView.LoadListener,OnItemClickListener<RightBean> {
    private View view;

    private PullToRefreshLayout mAuto;
    private LoadRecyclerView mLoad;
    private RightAdapter mChestAdapter;
    private ArrayList<RightBean> mData = new ArrayList<>();

    private String mTag_Name;
    private  int pageNo=1;

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
        mChestAdapter = new RightAdapter(getActivity(), R.layout.recycler_right_adapter, mData);
        mLoad.setAdapter(mChestAdapter);
        mLoad.setIsHaveData(false);
        mChestAdapter.setOnItemClickListener(this);
    }
    private void setData(FragmentManager fm){

        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("page",String.valueOf(pageNo));//分页
        HttpUtil.postHttpRequstProgess(getActivity(), "正在加载", UrlConstant.COLLEAGE, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
            }
            @Override
            public void onResponse(String response) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
                Log.e("院校数据"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {

                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONArray left=data.getJSONArray("left");
                            for (int i=0;i<left.length();i++){
                                JSONObject jsonObject1=left.getJSONObject(i);
                                if (mTag_Name.equals(jsonObject1.getString("tag_name"))) {
                                    JSONArray school = jsonObject1.getJSONArray("school");
                                    for (int j = 0; j < school.length(); j++) {
                                        JSONObject jsonObject2 = school.getJSONObject(j);
                                        RightBean rightBean = JSON.parseObject(jsonObject2.toString(), RightBean.class);
                                        mData.add(rightBean);
                                    }
                                    setAdapter();
                                }
                            }
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
        Intent intent=new Intent(getActivity(), CollegesMainActivity.class);
        intent.putExtra("school_id",s.getSchool_id());
        intent.putExtra("school_name",s.getSchool_name());
        intent.putExtra("image",s.getSchool_img_url());
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
        mLoad.setIsHaveData(false);
        pageNo = 1;
        mData = new ArrayList<>();
        setData(getChildFragmentManager());
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        pageNo=pageNo+1;
        setData(getChildFragmentManager());

    }

}
