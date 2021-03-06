package com.example.rh.artlive.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.photo.ImageDetailFragment;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.CircleImageView;
import com.example.rh.artlive.view.PullToRefreshLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by rh on 2017/11/30.
 * 课程详情
 */

public class ClassDetailsActivity extends BaseFragmentActivity implements View.OnClickListener{


    private TextView mNameView;
    private TextView mMorjView;
    private CircleImageView mImageView;
    private Context context;
    private TextView mClassView;
    private TextView mClassView_two;
    private TextView mClassView_third;
    private TextView mClassView_four;
    private TextView mClassView_five;
    private TextView mClassView_six;
    private TextView mClassView_seven;
    private TextView mReplyCountView;

    //评论
    private TextView mCommName;
    private TextView mCommClaas;
    private TextView mCommTime;
    private TextView mCommContent;
    private TextView mCommName_one;
    private TextView mCommClaas_one;
    private TextView mCommTime_one;
    private TextView mCommContent_one;
    private CircleImageView mCommImage;
    private CircleImageView mCommImage_one;

    private int mReplyCount=1;
    private ImageView mShowDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_details);
        context=this;
        init();
        setData();
        setListener();
    }
    private void init(){
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mNameView=(TextView) findViewById(R.id.teacher_name);
        mMorjView=(TextView) findViewById(R.id.teacher_marj);
        mClassView=(TextView)findViewById(R.id.class_one);
        mClassView_two=(TextView)findViewById(R.id.class_two);
        mClassView_third=(TextView)findViewById(R.id.class_third);
        mClassView_four=(TextView)findViewById(R.id.class_four);
        mClassView_five=(TextView)findViewById(R.id.class_five);
        mClassView_six=(TextView)findViewById(R.id.class_six);
        mClassView_seven=(TextView)findViewById(R.id.class_seven);
        mImageView=(CircleImageView)findViewById(R.id.roundImageView);
        mReplyCountView=(TextView)findViewById(R.id.left);
        //评论
        mCommName=(TextView)findViewById(R.id.class_name);
        mCommTime=(TextView)findViewById(R.id.class_time);
        mCommClaas=(TextView)findViewById(R.id.class_class);
        mCommContent=(TextView)findViewById(R.id.class_content);
        mCommName_one=(TextView)findViewById(R.id.class_name_one);
        mCommTime_one=(TextView)findViewById(R.id.class_time_one);
        mCommClaas_one=(TextView)findViewById(R.id.class_class_one);
        mCommContent_one=(TextView)findViewById(R.id.class_content_one);
        mCommImage=(CircleImageView)findViewById(R.id.class_image);
        mCommImage_one=(CircleImageView)findViewById(R.id.class_image_one);

    }
    private void setListener(){
        mShowDraw.setOnClickListener(this);

    }
    private void setData(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("class_id","38");
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.CLASSDETAILS, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("课程详情列表"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            Log.e("课程");
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONObject obclass=data.getJSONObject("class");
                            Log.e("课程"+obclass.getString("description"));
                            mClassView.setText(obclass.getString("description"));
                            mClassView_two.setText(obclass.getString("time"));
                            mClassView_third.setText(obclass.getString("content"));
//                            mClassView_four.setText(data.getString("summary"));
                            mClassView_five.setText(obclass.getString("require"));
                            mClassView_six.setText(obclass.getString("class_time"));
                            mClassView_seven.setText(obclass.getString("address"));
                            JSONObject inst=data.getJSONObject("inst");
                            mNameView.setText(inst.getString("inst_name"));
                            mMorjView.setText(inst.getString("major"));
                            Glide.with(context).load(inst.getString("header")).into(mImageView);

                            //评论
                            JSONArray reply=data.getJSONArray("reply");
                            mReplyCount=reply.length();
                            mReplyCountView.setText("查看全部全部"+mReplyCount+"条评论");
                            for (int i=0;i<reply.length();i++){
                                JSONObject jsonObject1=reply.getJSONObject(i);
                                if (i==0) {
                                    mCommName.setText(jsonObject1.getString("user_name"));
                                    mCommTime.setText(jsonObject1.getString("createtime"));
                                    mCommContent.setText(jsonObject1.getString("content"));
                                    mCommClaas.setText(jsonObject1.getString("title"));
                                    Glide.with(context).load(jsonObject1.getString("user_pic")).into(mCommImage);
                                    Log.e("评论"+jsonObject1.getString("user_pic"));
                                }else if(i==1){
                                    mCommName_one.setText(jsonObject1.getString("user_name"));
                                    mCommTime_one.setText(jsonObject1.getString("createtime"));
                                    mCommContent_one.setText(jsonObject1.getString("content"));
                                    mCommClaas_one.setText(jsonObject1.getString("title"));
                                    Glide.with(context).load(jsonObject1.getString("user_pic")).into(mCommImage_one);
                                }
                            }
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
        }
    }
}
