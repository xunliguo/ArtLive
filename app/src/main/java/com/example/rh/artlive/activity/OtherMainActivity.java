package com.example.rh.artlive.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.photo.ImagePagerActivity;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.ToastUtil;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.CircleImageView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by rh on 2017/12/26.
 */

public class OtherMainActivity extends BaseFragmentActivity implements View.OnClickListener{


    private ImageView mShowDraw;
    private ImageView mGroundView;
    private ImageView image_one;
    private ImageView image_two;
    private ImageView image_third;
    private ImageView image_four;
    private ImageView image_five;

    private TextView mMineOne;
    private TextView mMineTwo;
    private TextView mMineThird;
    private TextView mMineFour;
    private TextView mMineFive;
    private TextView mMineSix;

    private CircleImageView mXCR;

    private RelativeLayout mSetLayout;
    private LinearLayout mUser_Post;
    private LinearLayout mUser_Follow;
    private LinearLayout mUser_Fans;

    private TextView mView;
    private TextView mgaunzhu;
    private TextView mFansi;
    private TextView mMineName;
    private TextView mLocal;
    private TextView mUser_Mine;



    private String mUser_Id;
    private Context context;

    private ArrayList<String> list = new ArrayList<>();//广告的数据

    private String mOther_Id;
    //静态变量，全局调用
    private String type;
    public static int flag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_main);
        context=this;
        init();
        setData();
        setListener();

    }

    private void  init(){
        Intent intent=getIntent();
        mUser_Id=intent.getStringExtra("id");
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mSetLayout=(RelativeLayout)findViewById(R.id.setting);
        mGroundView=(ImageView)findViewById(R.id.user_image);
        image_one=(ImageView)findViewById(R.id.follow_user_image);
        image_two=(ImageView)findViewById(R.id.follow_user_image_one);
        image_third=(ImageView)findViewById(R.id.follow_user_image_two);
        image_four=(ImageView)findViewById(R.id.follow_user_image_third);
        image_five=(ImageView)findViewById(R.id.follow_user_image_four);

        mMineOne=(TextView)findViewById(R.id.mine_one);
        mMineTwo=(TextView)findViewById(R.id.mine_two);
        mMineThird=(TextView)findViewById(R.id.mine_third);
        mMineFour=(TextView)findViewById(R.id.mine_four);
        mMineFive=(TextView)findViewById(R.id.mine_five);
        mView=(TextView)findViewById(R.id.diezivalue);
        mgaunzhu=(TextView)findViewById(R.id.guanzhu);
        mFansi=(TextView)findViewById(R.id.fans);
        mMineName=(TextView)findViewById(R.id.mine_name);
        mLocal=(TextView)findViewById(R.id.local);
        mUser_Mine=(TextView)findViewById(R.id.mine_user);


        mXCR=(CircleImageView) findViewById(R.id.roundImageView);

        mUser_Post=(LinearLayout)findViewById(R.id.me_user_tv_cart);
        mUser_Follow=(LinearLayout)findViewById(R.id.me_user_tv_follow);
        mUser_Fans=(LinearLayout)findViewById(R.id.me_user_tv_fans);
    }
    private void setListener(){
        mShowDraw.setOnClickListener(this);
        mSetLayout.setOnClickListener(this);
        mUser_Post.setOnClickListener(this);
        mUser_Follow.setOnClickListener(this);
        mUser_Fans.setOnClickListener(this);
    }

    /**
     * 个人主页的数据
     */
    private void setData(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("userid",mUser_Id);
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.USERMAIN, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("个人主页"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            Glide.with(context).load(data.getString("user_background_img")).into(mGroundView);
//                            Log.e("新的数据"+data.getString("user_img"));
                            Glide.with(context).load(data.getString("user_img")).into(mXCR);
                            mView.setText(data.getString("tiezi"));
                            mgaunzhu.setText(data.getString("guanzhu"));
                            mFansi.setText(data.getString("fans"));
                            mMineName.setText(data.getString("user_nickname"));
                            mLocal.setText(data.getString("user_area")+"/"+data.getString("user_grade"));
                            mMineOne.setText(data.getString("user_artdirection"));
                            mMineTwo.setText(data.getString("user_school"));
                            mMineThird.setText(data.getString("user_achievement"));
                            mMineFour.setText(data.getString("user_experience"));
                            mMineFive.setText(data.getString("user_attendschool"));
                            mUser_Id=data.getString("user_id");

                            JSONArray user_xiangce=data.getJSONArray("user_xiangce");
                            for(int i=0;i<user_xiangce.length();i++){
                                JSONObject jsonObject1=user_xiangce.getJSONObject(i);
                                if (i==0){
                                    Glide.with(context).load(jsonObject1.getString("picarr_img_url")).into(image_one);
                                }else if (i==1){
                                    Glide.with(context).load(jsonObject1.getString("picarr_img_url")).into(image_two);
                                }else if (i==2){
                                    Glide.with(context).load(jsonObject1.getString("picarr_img_url")).into(image_third);
                                }else if (i==3){
                                    Glide.with(context).load(jsonObject1.getString("picarr_img_url")).into(image_four);
                                }else if (i==4){
                                    Glide.with(context).load(jsonObject1.getString("picarr_img_url")).into(image_five);
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
     * 关注取消关注
     */
    private void setFollow(String type){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("teacher_id",mUser_Id);
        map.put("type",type);
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.FOLLOW, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("关注"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            if (flag==1) {
                                ToastUtil.showToast(OtherMainActivity.this, "关注成功");
                                mUser_Mine.setText("已关注");
                            }else{
                                ToastUtil.showToast(OtherMainActivity.this, "取消关注成功");
                                mUser_Mine.setText("关注");
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "teacher", null);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showDraw:
                finish();
                break;
            case R.id.setting:
                if (flag==0) {
                    flag=1;
                    type="关注";
                    setFollow(type);
                }else{
                    type="取关";
                    flag=0;
                    setFollow(type);
                }
                break;
            case R.id.me_user_tv_cart:
                Intent intent3=new Intent(context, UserPostActivity.class);
                intent3.putExtra("user_id",mUser_Id);
                startActivity(intent3);
                break;
            case R.id.me_user_tv_follow:
                Intent intent2=new Intent(context, FollowActivity.class);
                intent2.putExtra("user_id",mUser_Id);
                startActivity(intent2);
                break;
            case R.id.me_user_tv_fans:
                Intent intent1=new Intent(context, FansActivity.class);
                intent1.putExtra("user_id",mUser_Id);
                startActivity(intent1);
                break;
        }
    }
}
