package com.example.rh.artlive.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.activity.BookDetailsActivity;
import com.example.rh.artlive.activity.BookPayActivity;
import com.example.rh.artlive.adapter.BookContentAdapter;
import com.example.rh.artlive.adapter.BookSelfAdapter;
import com.example.rh.artlive.bean.BookBean;
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
 * Created by rh on 2017/12/19.
 */

public class BookShelfFragment extends BaseFragment implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,LoadRecyclerView.LoadListener,OnItemClickListener<BookBean> {

    private View view;

    private String mTag_Name;

    private PullToRefreshLayout mAuto;
    private LoadRecyclerView mLoad;
    private BookSelfAdapter mChestAdapter;
    private ArrayList<BookBean> mData = new ArrayList<>();
    private GridLayoutManager mManager;

    private  int pageNo=1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savesInstanceState) {
        super.onCreateView(inflater, container, savesInstanceState);
        view = inflater.inflate(R.layout.fragment_book_content, null);

        init();
        setListener();
        setAdapter();
        initData();

        return view;
    }

    private void init(){
        Log.e("专业方向"+mTag_Name);
        mAuto = (PullToRefreshLayout)view.findViewById(R.id.network_pager_myAuto);
        mLoad = (LoadRecyclerView)view. findViewById(R.id.network_myLoad);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(OrientationHelper.VERTICAL);// 纵向布局
        mLoad.setLayoutManager(lm);
        mManager = new GridLayoutManager(getActivity(), 3);
        mLoad.setLayoutManager(mManager);
    }
    private void setListener(){
        mAuto.setOnRefreshListener(this);
        mLoad.setLoadListener(this);
    }

    private void setAdapter() {
        mChestAdapter = new BookSelfAdapter(getActivity(), R.layout.recycler_book_content_adapter, mData);
        mLoad.setAdapter(mChestAdapter);
        mLoad.setIsHaveData(false);
        mChestAdapter.setOnItemClickListener(this);
    }


    private void initData(){
        mData = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("page",String.valueOf(pageNo));//分页
        HttpUtil.postHttpRequstProgess(getActivity(), "正在加载", UrlConstant.BOOKADD, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
            }
            @Override
            public void onResponse(String response) {
                Log.e("书架"+response);
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONArray data=jsonObject.getJSONArray("data");
                            for (int i=0;i<data.length();i++){
                                JSONObject jsonObject1=data.getJSONObject(i);
                                BookBean photoBean= JSON.parseObject(jsonObject1.toString(),BookBean.class);
                                mData.add(photoBean);
                            }
                            setAdapter();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "shelf", null);
    }


    @Override
    public void onItemClick(ViewGroup parent, View view, BookBean s, int position) {
        Intent intent=new Intent(getActivity(),BookDetailsActivity.class);
        intent.putExtra("book_id",s.getId());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, BookBean noPaiBean, int position) {
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

    @Override
    public void onClick(View view) {

    }
}
