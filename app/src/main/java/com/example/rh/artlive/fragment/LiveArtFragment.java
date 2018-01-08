package com.example.rh.artlive.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.UserNoPayAdapter;
import com.example.rh.artlive.bean.NoPaiBean;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.LoadRecyclerView;
import com.example.rh.artlive.view.OnItemClickListener;
import com.example.rh.artlive.view.PullToRefreshLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by rh on 2017/11/20.
 */

public class LiveArtFragment extends BaseFragment implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,LoadRecyclerView.LoadListener,OnItemClickListener<NoPaiBean> {

    private View view;

    private PullToRefreshLayout mAuto;
    private LoadRecyclerView mLoad;
    private UserNoPayAdapter mChestAdapter;
    private ArrayList<NoPaiBean> mData = new ArrayList<>();

    private int arrayMore;//多个商品
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    private String zt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);
        view=inflater.inflate(R.layout.fragment_live_art,null);

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
        mAuto = (PullToRefreshLayout) view.findViewById(R.id.network_pager_myAuto);
        mLoad = (LoadRecyclerView)view. findViewById(R.id.network_myLoad);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(OrientationHelper.VERTICAL);// 纵向布局
        mLoad.setLayoutManager(lm);
    }
    private void setListener(){
        mAuto.setOnRefreshListener(this);
        mLoad.setLoadListener(this);
    }

    private void setAdapter() {
        mChestAdapter = new UserNoPayAdapter(getActivity(), R.layout.recycler_adapter, mData);
        Log.e("新的数据："+mData);
        mLoad.setAdapter(mChestAdapter);
        mLoad.setIsHaveData(false);
        mChestAdapter.setOnItemClickListener(this);
    }

    private void initData(){
        mData = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        HttpUtil.postHttpRequstProgess(getActivity(), "正在加载", UrlConstant.TEACHER, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
            }
            @Override
            public void onResponse(String response) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
                Log.e("租赁全部订单"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("0".equals(jsonObject.getString("state"))) {
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "data1", null);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.showDraw:
                break;
        }
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, NoPaiBean s, int position) {

    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, NoPaiBean chestbean, int position) {
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
