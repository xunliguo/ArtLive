package com.example.rh.artlive.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.HomeAdapter;
import com.example.rh.artlive.adapter.PhotoAdapter;
import com.example.rh.artlive.bean.HomeBean;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.ToastUtil;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.LoadRecyclerView;
import com.example.rh.artlive.view.OnItemClickListener;
import com.example.rh.artlive.view.PullToRefreshLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by rh on 2017/11/29.
 */

public class PhotoActivity extends BaseFragmentActivity implements View.OnClickListener,PhotoAdapter.CheckInterface,PullToRefreshLayout.OnRefreshListener,LoadRecyclerView.LoadListener,OnItemClickListener<PhotoBean> {

    private PullToRefreshLayout mAuto;
    private LoadRecyclerView mLoad;
    private PhotoAdapter mChestAdapter;
    private ArrayList<PhotoBean> mData = new ArrayList<>();
    private GridLayoutManager mManager;

    private ImageView mShowDraw;
    private ImageView mDeleteView;
    private TextView mEditView;
    private RelativeLayout mBottomLayout;

    private LinearLayout mAllLayout;
    private CheckBox all;//全选按钮;



    //静态变量，全局调用
    public static int flag = 0;
    private int totalCount=0;
    StringBuffer bufferId = new StringBuffer();//商品id
    private String WareId;//......商品id数组



    @Override
    protected void onCreate(Bundle savedInsaveState){
        super.onCreate(savedInsaveState);
        setContentView(R.layout.activity_photo);
        init();
        setAdapter();
        setListener();
        initData();
    }

    private void init(){
        mAuto = (PullToRefreshLayout)findViewById(R.id.network_pager_myAuto);
        mLoad = (LoadRecyclerView) findViewById(R.id.network_myLoad);
        mBottomLayout=(RelativeLayout)findViewById(R.id.photo_bottom);
        mAllLayout=(LinearLayout)findViewById(R.id.all_layout);

        mEditView=(TextView)findViewById(R.id.showTime);
        all=(CheckBox)findViewById(R.id.cb);
        mDeleteView=(ImageView)findViewById(R.id.delete);


        LinearLayoutManager lm=new LinearLayoutManager(this);
        mManager = new GridLayoutManager(this, 4);
        lm.setOrientation(OrientationHelper.VERTICAL);
        mLoad.setLayoutManager(lm);
        mLoad.setLayoutManager(mManager);
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
    }
    private void setListener(){
        mAuto.setOnRefreshListener(this);
        mLoad.setLoadListener(this);
        mShowDraw.setOnClickListener(this);
        mEditView.setOnClickListener(this);
        all.setOnClickListener(this);
        mDeleteView.setOnClickListener(this);
    }

    private void setAdapter() {
        mChestAdapter = new PhotoAdapter(this, R.layout.recycler_photo_adapter, mData);
        mLoad.setAdapter(mChestAdapter);
        mLoad.setIsHaveData(false);
        mChestAdapter.setOnItemClickListener(this);
        mChestAdapter.setCheckInterface(this);
    }

    private void initData(){
        mData = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("type","show");
        map.put("userid",mSharePreferenceUtil.getString(SharedPerfenceConstant.USER_ID));
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.PHOTO, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
            }
            @Override
            public void onResponse(String response) {

                Log.e("相册数据"+response);
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONArray data=jsonObject.getJSONArray("data");
                            for (int i=0;i<data.length();i++){
                                JSONObject bobject=data.getJSONObject(i);
                                PhotoBean homeBean= JSON.parseObject(bobject.toString(), PhotoBean.class);
                                mData.add(homeBean);
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

    /**
     * 删除
     */
    private void setDelete(){
        mData = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("type","delete");
        map.put("picarr_id",WareId);
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.PHOTO, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
            }
            @Override
            public void onResponse(String response) {

                Log.e("删除相册"+response);
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            ToastUtil.showToast(PhotoActivity.this,"删除成功");
                            initData();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "data1", null);
    }


    /**
     * 单选
     * @param position  组元素位置
     * @param isChecked 组元素选中与否
     */
    @Override
    public void checkGroup(int position, boolean isChecked) {
        mData.get(position).setIsChecked(isChecked);
        if (isAllCheck()) {
            all.setChecked(true);
        }
        else {
            all.setChecked(false);
        }
        mChestAdapter.notifyDataSetChanged();
        statistics();
    }
    /**
     * 遍历list集合
     * @return
     */
    private boolean isAllCheck() {

        for (PhotoBean group : mData) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }
    /**
     * 统计操作
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作
     * 3.给底部的textView进行数据填充
     */
    public void statistics() {
        totalCount = 0;
        int  sb_length1 = bufferId.length();// 取得字符串的长度
        bufferId.delete(0,sb_length1);

        for (int i = 0; i < mData.size(); i++) {
            PhotoBean shoppingCartBean = mData.get(i);

            if (shoppingCartBean.isChoosed()) {
                totalCount++;
                bufferId.append(shoppingCartBean.getPicarr_id()+",");

            }else{
            }
        }

        WareId=new String(bufferId);

        Log.e("图片id"+WareId);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showDraw:
                finish();
                break;
            case R.id.showTime:
                if (flag == 0) {
                    // 第一次单击触发的事件
                    mEditView.setText("取消");
                    mShowDraw.setVisibility(View.GONE);
                    mBottomLayout.setVisibility(View.VISIBLE);
                    mAllLayout.setVisibility(View.VISIBLE);
                    flag = 1;
                } else {
                    // 第二次单击buttont改变触发的事件
                    mEditView.setText("编辑");
                    mShowDraw.setVisibility(View.VISIBLE);
                    mBottomLayout.setVisibility(View.GONE);
                    mAllLayout.setVisibility(View.GONE);
                    flag = 0;
                }
                setAdapter();
                break;
            case R.id.cb:
                /**
                 * 点击全选按钮
                 */
                if (mData.size() != 0) {
                    if (all.isChecked()) {
                        for (int i = 0; i < mData.size(); i++) {
                            mData.get(i).setIsChecked(true);
                        }
                        mChestAdapter.notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < mData.size(); i++) {
                            mData.get(i).setIsChecked(false);
                        }
                        mChestAdapter.notifyDataSetChanged();
                    }
                }
                statistics();
                break;
            case R.id.delete:
                setDelete();
                break;

        }
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, PhotoBean noPaiBean, int position) {

    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, PhotoBean noPaiBean, int position) {
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
