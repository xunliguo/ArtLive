package com.example.rh.artlive.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.activity.ArticleDetailsActivity;
import com.example.rh.artlive.activity.BaseFragmentActivity;
import com.example.rh.artlive.adapter.HomeAdapter;
import com.example.rh.artlive.adapter.WormAdapter;
import com.example.rh.artlive.bean.FollowBean;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.bean.WormBean;
import com.example.rh.artlive.ottoEvent.AppBus;
import com.example.rh.artlive.ottoEvent.AppCityEvent;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.LoadRecyclerView;
import com.example.rh.artlive.view.OnItemClickListener;
import com.example.rh.artlive.view.PullToRefreshLayout;
import com.squareup.otto.Subscribe;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by rh on 2017/12/1.
 * //头条
 */

public class HeadlinesFragment extends BaseFragment implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,LoadRecyclerView.LoadListener,OnItemClickListener<WormBean> {

    private View view;

    private PullToRefreshLayout mAuto;
    private LoadRecyclerView mLoad;
    private WormAdapter mChestAdapter;
    private ArrayList<WormBean> mData = new ArrayList<>();

    private RelativeLayout mHeaderRfresh;
    private  int pageNo=1;

    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    private String zt;
    private String major="";
    List<String> list;

    private RelativeLayout mAllLayout;
    private RelativeLayout mLeftLayout;
    private RelativeLayout mThirdLayout;
    private RelativeLayout mRightLayout;
    private LinearLayout mHeaderLayout;

    private TextView mAllView;
    private TextView mLeftView;
    private TextView mThirdView;
    private TextView mRightView;


    @Override
    public void onStart() {
        super.onStart();
        //注册到bus事件总线中
        AppBus.getInstance().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        AppBus.getInstance().unregister(this);
    }
    // 接受事件
    @Subscribe
    public void setContent(AppCityEvent data) {
        if ("worm".equals(data.getName())){
            mHeaderLayout.setVisibility(View.VISIBLE);
        }else if ("worm_1".equals(data.getName())){
            mHeaderLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savesInstanceState) {
        super.onCreateView(inflater, container, savesInstanceState);
        view = inflater.inflate(R.layout.fragment_headlines, null);
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
        mAllLayout=(RelativeLayout)view.findViewById(R.id.worm_all);
        mLeftLayout=(RelativeLayout)view.findViewById(R.id.worm_left);
        mThirdLayout=(RelativeLayout)view.findViewById(R.id.worm_third);
        mRightLayout=(RelativeLayout)view.findViewById(R.id.worm_right);
        mHeaderLayout=(LinearLayout)view.findViewById(R.id.bottom_left_content);

        mAllView=(TextView)view.findViewById(R.id.worm_all_view);
        mLeftView=(TextView)view.findViewById(R.id.worm_left_view);
        mThirdView=(TextView)view.findViewById(R.id.worm_third_view);
        mRightView=(TextView)view.findViewById(R.id.worm_right_view);

        mAuto = (PullToRefreshLayout)view.findViewById(R.id.network_pager_myAuto);
        mLoad = (LoadRecyclerView) view.findViewById(R.id.network_myLoad);
        mHeaderRfresh=(RelativeLayout)view.findViewById(R.id.head_view);
        LinearLayoutManager lm=new LinearLayoutManager(getActivity());
        lm.setSmoothScrollbarEnabled(false);
        lm.setOrientation(OrientationHelper.VERTICAL);
        mLoad.setLayoutManager(lm);
    }
    private void setListener(){
        mAuto.setOnRefreshListener(this);
        mLoad.setLoadListener(this);
        mAllLayout.setOnClickListener(this);
        mLeftLayout.setOnClickListener(this);
        mThirdLayout.setOnClickListener(this);
        mRightLayout.setOnClickListener(this);
    }

    private void setAdapter() {
        mChestAdapter = new WormAdapter(getActivity(), R.layout.recycler_headlines_adapter, mData);
        mLoad.setAdapter(mChestAdapter);
        mLoad.setIsHaveData(false);
        mChestAdapter.setOnItemClickListener(this);
    }

    private void initData(){
        mData = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("type","top");
        map.put("who","Android");
        map.put("major","");
        map.put("p",String.valueOf(pageNo));//分页
        HttpUtil.postHttpRequstProgess(getActivity(), "正在加载", UrlConstant.HEADER, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
            }
            @Override
            public void onResponse(String response) {

                Log.e("头条数据"+response);
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONArray top=data.getJSONArray("top");
                            for (int i=0;i<top.length();i++){
                                JSONObject bobject=top.getJSONObject(i);
                                WormBean followBean= JSON.parseObject(bobject.toString(),WormBean.class);
                                mData.add(followBean);
                            }
                            setAdapter();
//                            mChestAdapter.setData(mData);
//                            mChestAdapter.notifyDataSetChanged();
//                            mLoad.setIsHaveData(true);//为false时走加载更多
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
            case R.id.worm_all:
                mAllLayout.setBackgroundResource(R.drawable.buttom_orgen);
                mLeftLayout.setBackgroundResource(R.drawable.buttom_huise);
                mThirdLayout.setBackgroundResource(R.drawable.buttom_huise);
                mRightLayout.setBackgroundResource(R.drawable.buttom_huise);
                mAllView.setTextColor(Color.parseColor("#ffffff"));
                mLeftView.setTextColor(Color.parseColor("#80666666"));
                mThirdView.setTextColor(Color.parseColor("#80666666"));
                mRightView.setTextColor(Color.parseColor("#80666666"));
                major="";
                initData();
                break;
            case R.id.worm_left:
                mAllLayout.setBackgroundResource(R.drawable.buttom_huise);
                mLeftLayout.setBackgroundResource(R.drawable.buttom_orgen);
                mThirdLayout.setBackgroundResource(R.drawable.buttom_huise);
                mRightLayout.setBackgroundResource(R.drawable.buttom_huise);
                mAllView.setTextColor(Color.parseColor("#80666666"));
                mLeftView.setTextColor(Color.parseColor("#ffffff"));
                mThirdView.setTextColor(Color.parseColor("#80666666"));
                mRightView.setTextColor(Color.parseColor("#80666666"));
                major="播音";
                initData();
                break;
            case R.id.worm_third:
                mAllLayout.setBackgroundResource(R.drawable.buttom_huise);
                mLeftLayout.setBackgroundResource(R.drawable.buttom_huise);
                mThirdLayout.setBackgroundResource(R.drawable.buttom_orgen);
                mRightLayout.setBackgroundResource(R.drawable.buttom_huise);
                mAllView.setTextColor(Color.parseColor("#80666666"));
                mLeftView.setTextColor(Color.parseColor("#80666666"));
                mThirdView.setTextColor(Color.parseColor("#ffffff"));
                mRightView.setTextColor(Color.parseColor("#80666666"));
                major="编导";
                initData();
                break;
            case R.id.worm_right:
                mAllLayout.setBackgroundResource(R.drawable.buttom_huise);
                mLeftLayout.setBackgroundResource(R.drawable.buttom_huise);
                mThirdLayout.setBackgroundResource(R.drawable.buttom_huise);
                mRightLayout.setBackgroundResource(R.drawable.buttom_orgen);
                mAllView.setTextColor(Color.parseColor("#80666666"));
                mLeftView.setTextColor(Color.parseColor("#80666666"));
                mThirdView.setTextColor(Color.parseColor("#80666666"));
                mRightView.setTextColor(Color.parseColor("#ffffff"));
                major="播音";
                initData();
                break;
        }
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, WormBean s, int position) {

        Intent intent=new Intent(getActivity(), ArticleDetailsActivity.class);
        intent.putExtra("id",s.getForum_id());
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, WormBean noPaiBean, int position) {
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
