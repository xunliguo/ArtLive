package com.example.rh.artlive.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.DynamicGridUserAdapter;
import com.example.rh.artlive.bean.FansBean;
import com.example.rh.artlive.bean.HomeBean;
import com.example.rh.artlive.ottoEvent.AppBus;
import com.example.rh.artlive.ottoEvent.AppCityEvent;
import com.example.rh.artlive.photo.DynamicGridAdapter;
import com.example.rh.artlive.photo.ImagePagerActivity;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.CircleImageView;
import com.example.rh.artlive.view.NoScrollGridView;
import com.example.rh.artlive.view.PullToRefreshLayout;
import com.example.rh.artlive.view.XCRoundImageView;
import com.squareup.otto.Subscribe;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by rh on 2017/11/22.
 * 个人主页
 */

public class UserMainActivity extends BaseFragmentActivity implements View.OnClickListener{

    private ImageView mShowDraw;
    private ImageView mGroundView;
//    private ImageView image_one;
//    private ImageView image_two;
//    private ImageView image_third;
//    private ImageView image_four;
//    private ImageView image_five;

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
    private TextView mUser_Mine;
    private NoScrollGridView picarr;

    private String mUser_Id;
    private Context context;

    private ArrayList<String> picarr1;
    private ArrayList<String> picarr2;


    @Override
    public void onStart() {
        super.onStart();
        //注册到bus事件总线中
        AppBus.getInstance().register(this);
        Log.e("1");
    }

    @Override
    public void onStop() {
        super.onStop();
        AppBus.getInstance().unregister(this);
        Log.e("1");
    }
    // 接受事件
    @Subscribe
    public void setContent(AppCityEvent data) {
        if ("save".equals(data.getName())){
            setData();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        context=this;
        init();
        setData();
        setListener();
    }
    private void  init(){
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mSetLayout=(RelativeLayout)findViewById(R.id.setting);
        mGroundView=(ImageView)findViewById(R.id.user_image);
//        image_one=(ImageView)findViewById(R.id.follow_user_image);
//        image_two=(ImageView)findViewById(R.id.follow_user_image_one);
//        image_third=(ImageView)findViewById(R.id.follow_user_image_two);
//        image_four=(ImageView)findViewById(R.id.follow_user_image_third);
//        image_five=(ImageView)findViewById(R.id.follow_user_image_four);

        mMineOne=(TextView)findViewById(R.id.mine_one);
        mMineTwo=(TextView)findViewById(R.id.mine_two);
        mMineThird=(TextView)findViewById(R.id.mine_third);
        mMineFour=(TextView)findViewById(R.id.mine_four);
        mMineFive=(TextView)findViewById(R.id.mine_five);
        mView=(TextView)findViewById(R.id.diezivalue);
        mgaunzhu=(TextView)findViewById(R.id.guanzhu);
        mFansi=(TextView)findViewById(R.id.fans);
        mMineName=(TextView)findViewById(R.id.mine_name);
        mUser_Mine=(TextView)findViewById(R.id.mine_user);

        mXCR=(CircleImageView) findViewById(R.id.roundImageView);

        mUser_Post=(LinearLayout)findViewById(R.id.me_user_tv_cart);
        mUser_Follow=(LinearLayout)findViewById(R.id.me_user_tv_follow);
        mUser_Fans=(LinearLayout)findViewById(R.id.me_user_tv_fans);
        picarr=(NoScrollGridView)findViewById(R.id.gridView_picarr);
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
        picarr1 =new ArrayList<>();
        picarr2 =new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("userid",mSharePreferenceUtil.getString(SharedPerfenceConstant.USER_ID));
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
                            Glide.with(UserMainActivity.this).load(data.getString("user_background_img")).into(mGroundView);
                            Glide.with(UserMainActivity.this).load(data.getString("user_img")).into(mXCR);
                            mView.setText(data.getString("tiezi"));
                            mgaunzhu.setText(data.getString("guanzhu"));
                            mFansi.setText(data.getString("fans"));
                            mMineName.setText(data.getString("user_nickname"));
                            mMineOne.setText(data.getString("user_artdirection"));
                            mMineTwo.setText(data.getString("user_school"));
                            mMineThird.setText(data.getString("user_achievement"));
                            mMineFour.setText(data.getString("user_experience"));
                            mMineFive.setText(data.getString("user_attendschool"));
                            mUser_Id=data.getString("user_id");

                            JSONArray user_xiangce=data.getJSONArray("user_xiangce");
                            for(int i=0;i<user_xiangce.length();i++){
                                JSONObject jsonObject1=user_xiangce.getJSONObject(i);
                                picarr2.add(jsonObject1.getString("picarr_img_url"));
                                if (i>=5){

                                }else{
                                    picarr1.add(jsonObject1.getString("picarr_img_url"));
                                }
                            }

                            int size1 = picarr1.size();
                            int size2 = picarr2.size();
                            Log.e("图片"+picarr1);
                            final String[] array1 = (String[]) picarr1.toArray(new String[size1]);
                            final String[] array2 = (String[]) picarr2.toArray(new String[size2]);
                            if (array1.length > 0) {
                                picarr.setVisibility(View.VISIBLE);
                                picarr.setAdapter(new DynamicGridUserAdapter(array1, context));
                                picarr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        imageBrower(position, array2);
                                    }
                                });
                            } else {
                                picarr.setVisibility(View.GONE);
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
        Intent intent = new Intent(UserMainActivity.this, ImagePagerActivity.class);
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
            case R.id.setting:
                Intent intent=new Intent(UserMainActivity.this,UserActivity.class);
                startActivity(intent);
                break;
            case R.id.me_user_tv_cart:
                Intent intent3=new Intent(UserMainActivity.this, UserPostActivity.class);
                intent3.putExtra("user_id",mUser_Id);
                startActivity(intent3);
                break;
            case R.id.me_user_tv_follow:
                Intent intent2=new Intent(UserMainActivity.this, FollowActivity.class);
                intent2.putExtra("user_id",mUser_Id);
                startActivity(intent2);
                break;
            case R.id.me_user_tv_fans:
                Intent intent1=new Intent(UserMainActivity.this, FansActivity.class);
                intent1.putExtra("user_id",mUser_Id);
                startActivity(intent1);
                break;
        }
    }
}
