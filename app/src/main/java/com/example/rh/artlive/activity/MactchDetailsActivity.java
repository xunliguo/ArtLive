package com.example.rh.artlive.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.MactchDataAdapter;
import com.example.rh.artlive.adapter.MactchNewsAdapter;
import com.example.rh.artlive.adapter.RvListener;
import com.example.rh.artlive.bean.UserFollowBean;
import com.example.rh.artlive.photo.ImagePagerActivity;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by rh on 2017/12/5.
 * 比赛详情
 */

public class MactchDetailsActivity extends BaseFragmentActivity implements View.OnClickListener{

    private String mActivity_Id;
    private String mPrice;

    private ImageView mShowDraw;
    private ImageView mHeaderImage;
    private ImageView mBottom_Image;

    private TextView mTitleView;
    private TextView mContentView;
    private TextView mMatchTimeView;
    private TextView mMatchXainzhi;
    private TextView mMatchDay;
    private TextView mCount;
    private TextView mClickView;
    private TextView mCountNews;

    private int flag=0;

    private RelativeLayout mSignUpLayout;
    private RecyclerView rvSort;
    private RecyclerView rvSort2;
    private Context context;
    private MactchDataAdapter mSortAdapter;
    private MactchNewsAdapter mSortNewsAdapter;
    private ArrayList<UserFollowBean> mSortBean;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_details);
        context=this;
        init();
        setAdapter();
        setListener();
        initData();

    }


    private void init(){
        Intent intent=getIntent();
        mActivity_Id=intent.getStringExtra("activity_id");
        mPrice=intent.getStringExtra("price");
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mHeaderImage=(ImageView)findViewById(R.id.match_header_image);
        mBottom_Image=(ImageView)findViewById(R.id.bottom_image);


        mTitleView=(TextView)findViewById(R.id.match_title_content);
        mContentView=(TextView)findViewById(R.id.match_details);
        mMatchTimeView=(TextView)findViewById(R.id.match_time_right);
        mMatchXainzhi=(TextView)findViewById(R.id.match_xianzhi);
        mMatchDay=(TextView)findViewById(R.id.match_day);
        mCount=(TextView)findViewById(R.id.count_value);
        mClickView=(TextView)findViewById(R.id.click_match);
        mCountNews=(TextView)findViewById(R.id.new_value);

        mSignUpLayout=(RelativeLayout)findViewById(R.id.sign_up);

        rvSort = (RecyclerView) findViewById(R.id.rv_sort);
        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.setOrientation(OrientationHelper.HORIZONTAL);
        rvSort.setLayoutManager(lm);

        rvSort2 = (RecyclerView) findViewById(R.id.rv_sort2);
        LinearLayoutManager lm1 = new LinearLayoutManager(context);
        lm1.setOrientation(OrientationHelper.VERTICAL);
        rvSort2.setLayoutManager(lm1);

    }
    private void setListener(){
        mShowDraw.setOnClickListener(this);
        mSignUpLayout.setOnClickListener(this);
        mClickView.setOnClickListener(this);
    }

    private void setAdapter() {
    }

    private void initData(){
        mSortBean=new ArrayList<>();
        final HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("activity_id",mActivity_Id);
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.MATCHDETAILS, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("比赛详情数据"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject jsonObject1=jsonObject.getJSONObject("data");
                            JSONObject match=jsonObject1.getJSONObject("match");

                            Log.e("标题"+match.getString("title"));
                            mTitleView.setText(match.getString("title"));
                            mContentView.setText(match.getString("content"));
                            mMatchTimeView.setText(match.getString("opentime")+"--"+match.getString("stoptime"));
                            mMatchXainzhi.setText(match.getString("limi"));
                            mMatchDay.setText(match.getString("endtime"));
                            Glide.with(context).load(match.getString("img")).into(mHeaderImage);
                            Glide.with(context).load(match.getString("js_img_url")).into(mBottom_Image);
                            //评委
                            JSONArray pingwei=jsonObject1.getJSONArray("pingwei");
                            mCount.setText(String.valueOf(pingwei.length())+"名");
                            mSortBean=new ArrayList<>();
                            for (int i=0;i<pingwei.length();i++){
                                JSONObject jsonObject2=pingwei.getJSONObject(i);
                                UserFollowBean userFollowBean=JSON.parseObject(jsonObject2.toString(),UserFollowBean.class);
                                mSortBean.add(userFollowBean);
                            }
                            mSortAdapter = new MactchDataAdapter(context, mSortBean, new RvListener() {
                                @Override
                                public void onItemClick(int id, int position) {
                                }
                            });
                            rvSort.setAdapter(mSortAdapter);
                            //新闻
                            JSONArray news=jsonObject1.getJSONArray("news");
                            mCountNews.setText(news.length()+"名");
                            mSortBean=new ArrayList<>();
                            for (int i=0;i<news.length();i++){
                                JSONObject jsonObject2=news.getJSONObject(i);
                                UserFollowBean userFollowBean=JSON.parseObject(jsonObject2.toString(),UserFollowBean.class);
                                mSortBean.add(userFollowBean);
                            }
                            mSortNewsAdapter = new MactchNewsAdapter(context, mSortBean, new RvListener() {
                                @Override
                                public void onItemClick(int id, int position) {
                                }
                            });
                            rvSort2.setAdapter(mSortNewsAdapter);
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
            case R.id.showDraw:
                finish();
                break;
            case R.id.sign_up:
                Intent intent=new Intent(MactchDetailsActivity.this,MatchSignActivity.class);
                intent.putExtra("activity_id",mActivity_Id);
                intent.putExtra("price",mPrice);
                startActivity(intent);
                break;
            case R.id.click_match:
                if (flag==0){
                    flag=1;
                    mContentView.setMaxLines(50);
                    mClickView.setText("收起");
                }else if (flag==1){
                    flag=0;
                    mContentView.setMaxLines(3);
                    mClickView.setText("查看比赛详情");
                }
                break;
        }
    }


    /**
     * 点击图片放大浏览
     * @param position
     * @param urls
     */
    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        context.startActivity(intent);
    }
}
