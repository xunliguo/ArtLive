package com.example.rh.artlive.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.BookAllAdapter;
import com.example.rh.artlive.adapter.WormLiveAdapter;
import com.example.rh.artlive.bean.BookBean;
import com.example.rh.artlive.bean.WishBean;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.OnItemClickListener;
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

public class BookAllFragment extends BaseFragment implements View.OnClickListener,OnItemClickListener<BookBean> {

    private View view;

    private Context context;
    private RecyclerView mView;

    private BookAllAdapter mChestAdapter;
    private ArrayList<BookBean> mData;

    private String tag_name="全部";
    private BookAllDFragment bookAllDFragment;


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
        mChestAdapter = new BookAllAdapter(getActivity(), R.layout.recycler_live_header_adapter, mData,tag_name);
        mView.setAdapter(mChestAdapter);
        mChestAdapter.setOnItemClickListener(this);
    }

    private void  setData(){
        mData = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        HttpUtil.postHttpRequstProgess(getActivity(), "正在加载", UrlConstant.BOOKALL, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("书籍全部"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONArray type=data.getJSONArray("type");
                            for (int i=0;i<type.length();i++){
                                JSONObject jsonObject1=type.getJSONObject(i);
                                BookBean bookBean=JSON.parseObject(jsonObject1.toString(),BookBean.class);
                                mData.add(bookBean);
                            }
                            createFragment("全部");

                            setAdapter(tag_name);
//                            JSONArray book=data.getJSONArray("book");
//                            for (int i=0;i<book.length();i++){
//                                JSONObject jsonObject1=book.getJSONObject(i);
//                                if (i==0){
//
//                                }
//                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "book", null);
    }


    /**
     *  //跳转SortDetailsFragment 携带参数
     */
    public void createFragment(String tag_name) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        bookAllDFragment = new BookAllDFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tag_name",tag_name);
        bookAllDFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.lin_fragment, bookAllDFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(ViewGroup parent, View view, BookBean s, int position) {
        setAdapter(s.getName());
        createFragment(s.getName());

    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, BookBean wishBean, int position) {
        return false;
    }
}
