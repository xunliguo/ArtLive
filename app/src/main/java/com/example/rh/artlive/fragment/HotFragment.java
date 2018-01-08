package com.example.rh.artlive.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.HomeAdapter;
import com.example.rh.artlive.adapter.UserNoPayAdapter;
import com.example.rh.artlive.bean.FollowBean;
import com.example.rh.artlive.bean.HomeBean;
import com.example.rh.artlive.bean.NoPaiBean;
import com.example.rh.artlive.bean.PicarrBean;
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
import java.util.List;

import okhttp3.Call;

/**
 * Created by rh on 2017/11/20.
 */

public class HotFragment extends BaseFragment  implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,LoadRecyclerView.LoadListener,OnItemClickListener<FollowBean> {
    private View view;

    private PullToRefreshLayout mAuto;
    private LoadRecyclerView mLoad;
    private HomeAdapter mChestAdapter;
    private ArrayList<FollowBean> mData = new ArrayList<>();
    private  ArrayList<String>list_per = new ArrayList<>();//广告的数据

    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    private String zt;
    private List<String> list;
    private List<String> list_name;
    List<String> list_id;
    private  ArrayList<String> picarr1 ;
    private  ArrayList<String> picarr2 ;
    private  ArrayList<String> picarr3;
    private  ArrayList<String> picarr4 ;
    private  ArrayList<String> picarr5 ;
    private  ArrayList<String> picarr6 ;
    private  ArrayList<String> picarr7 ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savesInstanceState) {
        super.onCreateView(inflater, container, savesInstanceState);
        view = inflater.inflate(R.layout.fragment_hot, null);
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
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(OrientationHelper.VERTICAL);// 纵向布局
        mLoad.setLayoutManager(lm);
    }
    private void setListener(){
        mAuto.setOnRefreshListener(this);
        mLoad.setLoadListener(this);
    }

    private void setAdapter() {
        mChestAdapter = new HomeAdapter(getActivity(), R.layout.recycler_home_adapter, mData,list,list_name,list_id,picarr1,picarr2,picarr3,picarr4,picarr5,picarr6,picarr7,mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN)
                                       ,mSharePreferenceUtil.getString(SharedPerfenceConstant.USER_ID));
        mLoad.setAdapter(mChestAdapter);
        mLoad.setIsHaveData(false);
        mChestAdapter.setOnItemClickListener(this);
    }

    private void initData(){
        list=new ArrayList<String>();
        list_name=new ArrayList<String>();
        list_id=new ArrayList<String>();
        mData = new ArrayList<>();
        picarr1 = new ArrayList<>();
        picarr2 = new ArrayList<>();
        picarr3 = new ArrayList<>();
        picarr4 = new ArrayList<>();
        picarr5 = new ArrayList<>();
        picarr6 = new ArrayList<>();
        picarr7 = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("type","hot");
        HttpUtil.postHttpRequstProgess(getActivity(), "正在加载", UrlConstant.FOOLOW, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
            }
            @Override
            public void onResponse(String response) {
                Log.e("热门数据"+response);
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONArray personal=data.getJSONArray("personal");
                            for (int i=0;i<personal.length();i++){
                                JSONObject jsonObject1=personal.getJSONObject(i);
                                list.add(jsonObject1.getString("user_img"));
                                list_name.add(jsonObject1.getString("user_nickname"));
                                list_id.add(jsonObject1.getString("user_id"));
                            }
                            JSONArray brand=data.getJSONArray("forum");
                            for (int i=0;i<brand.length();i++){
                                JSONObject bobject=brand.getJSONObject(i);
                                JSONArray picarr=bobject.getJSONArray("picarr");
                                if (i==0) {
                                    for (int j = 0; j < picarr.length(); j++) {
                                        JSONObject jsonObject1 = picarr.getJSONObject(j);
                                        picarr1.add(jsonObject1.getString("picarr_img_url"));
                                    }
                                }else if (i==1){
                                    for (int j = 0; j < picarr.length(); j++) {
                                        JSONObject jsonObject1 = picarr.getJSONObject(j);
                                        picarr2.add(jsonObject1.getString("picarr_img_url"));
                                    }
                                }else if (i==2){
                                    for (int j = 0; j < picarr.length(); j++) {
                                        JSONObject jsonObject1 = picarr.getJSONObject(j);
                                        picarr3.add(jsonObject1.getString("picarr_img_url"));
                                    }
                                }else if (i==3){
                                    for (int j = 0; j < picarr.length(); j++) {
                                        JSONObject jsonObject1 = picarr.getJSONObject(j);
                                        picarr4.add(jsonObject1.getString("picarr_img_url"));
                                    }
                                }else if (i==4){
                                    for (int j = 0; j < picarr.length(); j++) {
                                        JSONObject jsonObject1 = picarr.getJSONObject(j);
                                        picarr5.add(jsonObject1.getString("picarr_img_url"));
                                    }
                                }else if (i==5){
                                    for (int j = 0; j < picarr.length(); j++) {
                                        JSONObject jsonObject1 = picarr.getJSONObject(j);
                                        picarr6.add(jsonObject1.getString("picarr_img_url"));
                                    }
                                }else if (i==6){
                                    for (int j = 0; j < picarr.length(); j++) {
                                        JSONObject jsonObject1 = picarr.getJSONObject(j);
                                        picarr7.add(jsonObject1.getString("picarr_img_url"));
                                    }
                                }
                                FollowBean followBean=JSON.parseObject(bobject.toString(),FollowBean.class);
                                mData.add(followBean);
                            }
                            setAdapter();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "hot", null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, FollowBean noPaiBean, int position) {

    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, FollowBean noPaiBean, int position) {
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
