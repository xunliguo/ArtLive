package com.example.rh.artlive.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.activity.BaseFragmentActivity;
import com.example.rh.artlive.activity.LiveActivity;
import com.example.rh.artlive.adapter.RvListener;
import com.example.rh.artlive.adapter.SortAdapter;
import com.example.rh.artlive.adapter.WishAdapter;
import com.example.rh.artlive.adapter.WormLiveAdapter;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.bean.SortBean;
import com.example.rh.artlive.bean.WishBean;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.CustomViewPager;
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
 * Created by rh on 2017/12/12.
 * 虫窝直播热门
 */

public class WormHotFragment extends BaseFragment  implements View.OnClickListener,OnItemClickListener<WishBean> {

    private View view;

    private Context context;
    private RecyclerView mView;

    private WormLiveAdapter mChestAdapter;
    private ArrayList<WishBean> mData;

    private String tag_name="全部";
    private LiveHotFragment mHotFragment;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savesInstanceState) {
        super.onCreateView(inflater, container, savesInstanceState);
        view = inflater.inflate(R.layout.fragment_worm_hot, null);
        context=getActivity();

        init();
        setData();
        setAdapter(tag_name);
        return view;
    }

    private void init(){
        mView=(RecyclerView)view.findViewById(R.id.rv_sort);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(OrientationHelper.HORIZONTAL);// 纵向布局
        mView.setLayoutManager(lm);
    }

    private void setAdapter(String tag_name) {
        mChestAdapter = new WormLiveAdapter(getActivity(), R.layout.recycler_live_header_adapter, mData,tag_name);
        mView.setAdapter(mChestAdapter);
        mChestAdapter.setOnItemClickListener(this);
    }

    private void  setData(){
        mData = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        HttpUtil.postHttpRequstProgess(getActivity(), "正在加载", UrlConstant.WORMHOT, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("虫窝直播热门"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONArray cate=data.getJSONArray("category");
                            for (int i=0;i<cate.length();i++){
                                JSONObject jsonObject1=cate.getJSONObject(i);
                                WishBean wishBean=JSON.parseObject(jsonObject1.toString(),WishBean.class);
                                mData.add(wishBean);
                            }
                            setAdapter(tag_name);
                            createFragment(tag_name);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "hot", null);
    }


    /**
     *  //跳转SortDetailsFragment 携带参数
     */
    public void createFragment(String tag_name) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        mHotFragment = new LiveHotFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tag_name",tag_name);
        mHotFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.lin_fragment, mHotFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(ViewGroup parent, View view, WishBean s, int position) {
        setAdapter(s.getTag_name());

    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, WishBean wishBean, int position) {
        return false;
    }
}
