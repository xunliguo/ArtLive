package com.example.rh.artlive.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.OrderAdapter;
import com.example.rh.artlive.adapter.PostDetailsAdapter;
import com.example.rh.artlive.bean.BookIntroBean;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.bean.PostDetailsBean;
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

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by rh on 2017/12/9.
 * 帖子详情
 */

public class PostDetailsActivity extends BaseFragmentActivity  implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,LoadRecyclerView.LoadListener,OnItemClickListener<PostDetailsBean> {


    private ImageView mShowDraw;

    private PullToRefreshLayout mAuto;
    private LoadRecyclerView mLoad;
    private PostDetailsAdapter mChestAdapter;
    private ArrayList<PostDetailsBean> mData = new ArrayList<>();

    private String mForum_Id;

    private  ArrayList<String> list_details ;//广告的数据

    private String mPost_Title;
    private String mPost_Content;
    private String mPost_image;

    private LinearLayout mCommLayout;
    private Dialog datePickerDialog;
    private int commleght;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        init();
        setAdapter();
        setListener();
        initData();

    }

    private void init(){
        Intent intent=getIntent();
        mForum_Id=intent.getStringExtra("forum_id");
        Log.e("帖子id"+mForum_Id);
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mAuto = (PullToRefreshLayout)findViewById(R.id.network_pager_myAuto);
        mLoad = (LoadRecyclerView) findViewById(R.id.network_myLoad);
        mCommLayout=(LinearLayout)findViewById(R.id.me_user_tv_cart2);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(OrientationHelper.VERTICAL);// 纵向布局
        mLoad.setLayoutManager(lm);
    }
    private void setListener(){
        mAuto.setOnRefreshListener(this);
        mLoad.setLoadListener(this);
        mShowDraw.setOnClickListener(this);
        mCommLayout.setOnClickListener(this);
    }

    private void setAdapter() {
        mChestAdapter = new PostDetailsAdapter(this, R.layout.recycler_post_details_adapter, mData,list_details,mPost_Title,mPost_Content,mPost_image,commleght);
        mLoad.setAdapter(mChestAdapter);
        mLoad.setIsHaveData(false);
        mChestAdapter.setOnItemClickListener(this);
    }

    private void initData(){
        mData = new ArrayList<>();
        list_details = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("forum_id",mForum_Id);
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.POSTDETAILS, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
            }
            @Override
            public void onResponse(String response) {
                Log.e("帖子详情"+response);
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONObject details=data.getJSONObject("details");
                            mPost_Title=details.getString("forum_title");
                            mPost_Content=details.getString("forum_content");
                            mPost_image=details.getString("user_img");
                            JSONArray picarr=details.getJSONArray("picarr");
                            for (int i=0;i<picarr.length();i++){
                                JSONObject jsonObject1=picarr.getJSONObject(i);
                                list_details.add(UrlConstant.URL_PEOPLE+jsonObject1.getString("picarr_img_url"));
                            }
                            JSONArray comm=data.getJSONArray("comment");
                            commleght=comm.length();
                            Log.e("大小"+commleght);
                            for (int i=0;i<comm.length();i++){
                                JSONObject jsonObject1=comm.getJSONObject(i);
                                PostDetailsBean photoBean= JSON.parseObject(jsonObject1.toString(),PostDetailsBean.class);
                                mData.add(photoBean);
                            }
                            //如果评论数组
                            if (commleght==0){
                                mChestAdapter.notifyDataSetChanged();
                            }
                            setAdapter();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "collect", null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showDraw:
                finish();
                break;
            case R.id.me_user_tv_cart2:
                initDialog();
                break;
        }
    }

    /**
     * 添加评论
     */
    private void initDialog() {
        datePickerDialog = new Dialog(this, R.style.time_dialog);
        datePickerDialog.setCancelable(false);
        datePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        datePickerDialog.setContentView(R.layout.custom_comm_picker);
        Window window = datePickerDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager manager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = dm.widthPixels;
        window.setAttributes(lp);
        datePickerDialog.show();

        TextView cancel=datePickerDialog.findViewById(R.id.tv_cancle);
        final TextView sum=datePickerDialog.findViewById(R.id.tv_select);
        final EditText editText=datePickerDialog.findViewById(R.id.phone);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.dismiss();
            }
        });
        sum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> map = new HashMap<>();
                map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
                map.put("id",mForum_Id);
                map.put("type","forum");
                map.put("content",editText.getText()+"".toString().trim());
                HttpUtil.postHttpRequstProgess(PostDetailsActivity.this, "正在加载", UrlConstant.COMM, map, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                    }
                    @Override
                    public void onResponse(String response) {
                        com.example.rh.artlive.util.Log.e("评论"+response);
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);
                            if ("1".equals(jsonObject.getString("state"))) {
                                if (jsonObject.has("data")) {
                                    datePickerDialog.dismiss();
                                    ToastUtil.showToast(PostDetailsActivity.this,"评论成功");
                                    initData();
                                }
                            }else if ("-1".equals(jsonObject.getString("state"))){
                                datePickerDialog.dismiss();
                                ToastUtil.showToast(PostDetailsActivity.this,"评论失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, "wen", null);
            }
        });
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, PostDetailsBean noPaiBean, int position) {

    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, PostDetailsBean noPaiBean, int position) {
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
