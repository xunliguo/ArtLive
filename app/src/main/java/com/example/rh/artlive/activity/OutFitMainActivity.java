package com.example.rh.artlive.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.photo.DynamicGridAdapter;
import com.example.rh.artlive.photo.ImagePagerActivity;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.CircleImageView;
import com.example.rh.artlive.view.NoScrollGridView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by rh on 2017/11/30.
 * 机构主页
 */

public class OutFitMainActivity extends BaseFragmentActivity implements View.OnClickListener{

    private ImageView mShowDraw;
    private CircleImageView mOutImage;
    private ImageView mOutHeaderVIew;
    private Context context;

    private String inst_id;
    private int mReplyCount=1;

    private TextView mOutName;
    private TextView mTeaMarojView;
    private TextView mHelpView;
    private TextView mPostView;
    private TextView mFansView;
    private TextView mGoodView;

    //相册
    private ImageView image_one;
    private ImageView image_two;
    private ImageView image_third;
    private ImageView image_four;

    private TextView mTea_Name;
    private TextView mTea_Name_one;
    private TextView mTea_Name_two;
    private TextView mTea_Name_third;

    private TextView mTea_content;
    private TextView mTea_content_one;
    private TextView mTea_content_two;
    private TextView mTea_content_third;

    //师资力量

    private ImageView tea_one;
    private ImageView tea_two;
    private ImageView tea_third;
    private ImageView tea_four;


    //课程
    private TextView mClassTitle;
    private TextView mClassContent;
    private TextView mClassTime;
    private TextView mClassTitle_one;
    private TextView mClassContent_one;
    private TextView mClassTime_one;
    private TextView mClassTitle_two;
    private TextView mClassContent_two;
    private TextView mClassTime_two;


    //机构简介
    private TextView mAllCount;
    private TextView mOut_details;
    private TextView mOut_address;
    private TextView mOut_phone;
    private TextView mOut_details_address;

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

    private LinearLayout mSign_Up;
    private LinearLayout mfansi_jigou;
    private LinearLayout mjigou_tiezi;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit_main);
        context=this;
        init();
        setListener();
        setData();
    }

    private void init(){
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mOutImage=(CircleImageView)findViewById(R.id.roundImageView);
        mOutHeaderVIew=(ImageView)findViewById(R.id.out_header);

        mOutName=(TextView)findViewById(R.id.out_name);
        mTeaMarojView=(TextView)findViewById(R.id.out_marj);
        mHelpView=(TextView)findViewById(R.id.out_help);
        mPostView=(TextView)findViewById(R.id.out_poster);
        mFansView=(TextView)findViewById(R.id.out_fans);
        mGoodView=(TextView)findViewById(R.id.out_nice);
        mAllCount=(TextView)findViewById(R.id.left);
        //课程
        mClassTitle=(TextView)findViewById(R.id.out_class_title);
        mClassContent=(TextView)findViewById(R.id.out_class_content);
        mClassTime=(TextView)findViewById(R.id.price);
        mClassTitle_one=(TextView)findViewById(R.id.out_class_title_one);
        mClassContent_one=(TextView)findViewById(R.id.out_class_content_one);
        mClassTime_one=(TextView)findViewById(R.id.price_one);
        mClassTitle_two=(TextView)findViewById(R.id.out_class_title_two);
        mClassContent_two=(TextView)findViewById(R.id.out_class_content_two);
        mClassTime_two=(TextView)findViewById(R.id.price_two);
        //机构简介
        mOut_details=(TextView)findViewById(R.id.class_details_marj);
        mOut_address=(TextView)findViewById(R.id.class_details_address);
        mOut_phone=(TextView)findViewById(R.id.class_details_phone);
        mOut_details_address=(TextView)findViewById(R.id.out_address_one);
        //评论
        mCommName=(TextView)findViewById(R.id.comm_name);
        mCommTime=(TextView)findViewById(R.id.comm_time);
        mCommClaas=(TextView)findViewById(R.id.comm_class);
        mCommContent=(TextView)findViewById(R.id.comm_content);
        mCommName_one=(TextView)findViewById(R.id.comm_name_one);
        mCommTime_one=(TextView)findViewById(R.id.comm_time_one);
        mCommClaas_one=(TextView)findViewById(R.id.comm_class_one);
        mCommContent_one=(TextView)findViewById(R.id.comm_content_one);
        mCommImage=(CircleImageView)findViewById(R.id.comm_image);
        mCommImage_one=(CircleImageView)findViewById(R.id.comm_image_one);

        //相册
        image_one=(ImageView)findViewById(R.id.follow_user_image);
        image_two=(ImageView)findViewById(R.id.follow_user_image_one);
        image_third=(ImageView)findViewById(R.id.follow_user_image_two);
        image_four=(ImageView)findViewById(R.id.follow_user_image_third);

        //师资力量
        tea_one=(ImageView)findViewById(R.id.tea_user_image);
        tea_two=(ImageView)findViewById(R.id.tea_user_image_one);
        tea_third=(ImageView)findViewById(R.id.tea_user_image_two);
        tea_four=(ImageView)findViewById(R.id.tea_user_image_third);

        mTea_Name=(TextView)findViewById(R.id.teach_name_one);
        mTea_Name_one=(TextView)findViewById(R.id.teach_name_two);
        mTea_Name_two=(TextView)findViewById(R.id.teach_name_third);
        mTea_Name_third=(TextView)findViewById(R.id.teach_name_four);

        mTea_content=(TextView)findViewById(R.id.teach_content_one);
        mTea_content_one=(TextView)findViewById(R.id.teach_content_two);
        mTea_content_two=(TextView)findViewById(R.id.teach_content_third);
        mTea_content_third=(TextView)findViewById(R.id.teach_content_four);
         mSign_Up=(LinearLayout)findViewById(R.id.button);
         mfansi_jigou =(LinearLayout)findViewById(R.id.jigou_fensi);
         mjigou_tiezi =(LinearLayout)findViewById(R.id.jigou_tiezi);


        Intent intent=getIntent();
        inst_id=intent.getStringExtra("inst_id");
        Log.e("机构id"+inst_id);
    }
    private void setListener(){
        mShowDraw.setOnClickListener(this);
        mSign_Up.setOnClickListener(this);
//        mGridView.setOnClickListener(this);
        mfansi_jigou.setOnClickListener(this);
        mjigou_tiezi.setOnClickListener(this);
    }
    private void setData(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("inst_id",inst_id);
        map.put("userid","1");
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.OUTFIT_DETAILS, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("机构详情数据"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            //详情头部
                            JSONObject data=jsonObject.getJSONObject("data");
                            mHelpView.setText(data.getString("help"));
                            mGoodView.setText(data.getString("nice"));
                            mFansView.setText(data.getString("forum"));
                            mPostView.setText(data.getString("follow"));
                            JSONArray details=data.getJSONArray("details");
                            for (int i=0;i<details.length();i++){
                                JSONObject jsonObject1=details.getJSONObject(i);
                                Glide.with(OutFitMainActivity.this).load(jsonObject1.getString("poster")).into(mOutImage);
                                Glide.with(OutFitMainActivity.this).load(jsonObject1.getString("header")).into(mOutHeaderVIew);
                                mOutName.setText(jsonObject1.getString("inst_name"));
                                mTeaMarojView.setText(jsonObject1.getString("address")+"/"+jsonObject1.getString("major"));
                                mOut_details.setText(jsonObject1.getString("inst_name"));
                                mOut_address.setText(jsonObject1.getString("address"));
                                mOut_details_address.setText(jsonObject1.getString("region"));
                                mOut_phone.setText(jsonObject1.getString("phone"));
                            }
                            //课程
                            JSONArray out_class=data.getJSONArray("class");
                            for (int i=0;i<out_class.length();i++){
                                JSONObject jsonObject1=out_class.getJSONObject(i);
                                if (i==0) {
                                    mClassTitle.setText(jsonObject1.getString("title"));
                                    mClassContent.setText(jsonObject1.getString("description"));
                                    mClassTime.setText(jsonObject1.getString("price"));
                                }else if (i==1){
                                    mClassTitle_one.setText(jsonObject1.getString("title"));
                                    mClassContent_one.setText(jsonObject1.getString("description"));
                                    mClassTime_one.setText(jsonObject1.getString("price"));
                                }else if (i==2){
                                    mClassTitle_two.setText(jsonObject1.getString("title"));
                                    mClassContent_two.setText(jsonObject1.getString("description"));
                                    mClassTime_two.setText(jsonObject1.getString("price"));
                                }
                            }
                            //相册
                            JSONArray photo=data.getJSONArray("photos");
                            for (int i=0;i<photo.length();i++){
                                JSONObject jsonObject1=photo.getJSONObject(i);
                                if (i==0){
                                    Glide.with(OutFitMainActivity.this).load(jsonObject1.getString("picarr_img_url")).into(image_one);
                                }else if (i==1){
                                    Glide.with(OutFitMainActivity.this).load(jsonObject1.getString("picarr_img_url")).into(image_two);
                                }else if (i==2){
                                    Glide.with(OutFitMainActivity.this).load(jsonObject1.getString("picarr_img_url")).into(image_third);
                                }else if (i==3){
                                    Glide.with(OutFitMainActivity.this).load(jsonObject1.getString("picarr_img_url")).into(image_four);
                                }
                            }

                            //师资力量
                            JSONArray teacher=data.getJSONArray("teacher");
                            for (int i=0;i<teacher.length();i++){
                                JSONObject jsonObject1=teacher.getJSONObject(i);
                                if (i==0){
                                    mTea_Name.setText(jsonObject1.getString("name"));
                                    mTea_content.setText(jsonObject1.getString("introduce"));
                                    Glide.with(OutFitMainActivity.this).load(jsonObject1.getString("head_picture")).into(tea_one);
                                }else if (i==1){
                                    mTea_Name_one.setText(jsonObject1.getString("name"));
                                    mTea_content_third.setText(jsonObject1.getString("introduce"));
                                    Glide.with(OutFitMainActivity.this).load(jsonObject1.getString("head_picture")).into(tea_two);
                                }else if (i==2){
                                    mTea_Name_two.setText(jsonObject1.getString("name"));
                                    mTea_content_third.setText(jsonObject1.getString("introduce"));
                                    Glide.with(OutFitMainActivity.this).load(jsonObject1.getString("head_picture")).into(tea_third);
                                }else if (i==3){
                                    mTea_Name_third.setText(jsonObject1.getString("name"));
                                    mTea_content_third.setText(jsonObject1.getString("introduce"));
                                    Glide.with(OutFitMainActivity.this).load(jsonObject1.getString("head_picture")).into(tea_four);
                                }
                            }

                            //评论
                            JSONArray reply=data.getJSONArray("reply");
                            mReplyCount=reply.length();
                            mAllCount.setText("查看全部全部"+mReplyCount+"条评论");
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
        }, "teacher", null);
    }

    /**
     * 点击图片放大浏览
     * @param position
     * @param urls
     */
    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(OutFitMainActivity.this, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showDraw:
                finish();
                break;
            case R.id.button:
                Intent intent=new Intent(OutFitMainActivity.this,ClassListActivity.class);
                intent.putExtra("inst_id",inst_id);
                startActivity(intent);
                break;
            case R.id.jigou_fensi:
                Intent intent1=new Intent(OutFitMainActivity.this,OutFitFansActivity.class);
                intent1.putExtra("inst_id",inst_id);
                startActivity(intent1);
                break;
            case R.id.jigou_tiezi:
                Intent intent2=new Intent(OutFitMainActivity.this,OutFitPostActivity.class);
                intent2.putExtra("inst_id",inst_id);
                startActivity(intent2);
                break;

        }
    }
}
