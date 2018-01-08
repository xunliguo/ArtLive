package com.example.rh.artlive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.ToastUtil;
import com.example.rh.artlive.util.UrlConstant;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by rh on 2017/12/28.
 */

public class WishingActivity extends BaseFragmentActivity implements View.OnClickListener{
    private ImageView mShowDraw;
    private EditText mSchool;
    private EditText mWish;
    private Button mButton;
    private TextView one;
    private TextView two;
    private TextView third;
    private TextView mPrice;

    private String mOne;
    private String mTwo;
    private String mThird;

    private String id1;
    private String id2;
    private String id3;

    private CheckBox mCb1;
    private CheckBox mCb2;
    private CheckBox mCb3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishing);
        init();
        setData();
        setListener();
    }
    private void init(){
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mSchool=(EditText)findViewById(R.id.phone);
        mWish=(EditText)findViewById(R.id.phone1);
        mButton=(Button)findViewById(R.id.login_btn);
        mPrice=(TextView)findViewById(R.id.price);

        one=(TextView)findViewById(R.id.one);
        two=(TextView)findViewById(R.id.two);
        third=(TextView)findViewById(R.id.third);

        mCb1=(CheckBox)findViewById(R.id.cb1);
        mCb2=(CheckBox)findViewById(R.id.cb2);
        mCb3=(CheckBox)findViewById(R.id.cb3);
    }
    private void setListener(){
        mShowDraw.setOnClickListener(this);
        mButton.setOnClickListener(this);
        mCb1.setOnClickListener(this);
        mCb2.setOnClickListener(this);
        mCb3.setOnClickListener(this);
    }

    private void setData(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.APPLY_WISH, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("许愿树"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                if (i==0){
                                    mOne=jsonObject1.getString("wish_price");
                                    id1=jsonObject1.getString("wish_id");
                                    one.setText(jsonObject1.getString("wish_price"));
                                }else if (i==1){
                                    mTwo=jsonObject1.getString("wish_price");
                                    id2=jsonObject1.getString("wish_id");
                                    two.setText(jsonObject1.getString("wish_price"));
                                }else if (i==2){
                                    mThird=jsonObject1.getString("wish_price");
                                    id3=jsonObject1.getString("wish_id");
                                    third.setText(jsonObject1.getString("wish_price"));
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "data1", null);
    }

    /**
     * 支付虫币
     */
    private void setApply(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("school",mSchool.getText()+"".toString().trim());
        map.put("content",mWish.getText()+"".toString().trim());
        if (mCb1.isChecked()){
            map.put("type",id1);
        }else if (mCb2.isChecked()){
            map.put("type",id2);
        }else if (mCb3.isChecked()){
            map.put("type",id3);
        }

        Log.e("school"+mSchool.getText()+"".toString().trim()+"content"+mWish.getText()+"".toString().trim()+"type"+id1);
        HttpUtil.postHttpRequstProgess(WishingActivity.this, "正在加载", UrlConstant.WISH_APPLY, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("许愿树支付"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            ToastUtil.showToast(WishingActivity.this,"支付成功");
                            Intent intent=new Intent(WishingActivity.this,WishActivity.class);
                            startActivity(intent);
                        }
                    }if ("-1".equals(jsonObject.getString("state"))){
                        ToastUtil.showToast(WishingActivity.this,jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "shelf", null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showDraw:
                finish();
                break;
            case R.id.login_btn:
                if ("".equals(mSchool.getText()+"".toString().trim())){
                    ToastUtil.showToast(WishingActivity.this,"请输入学校");
                }else if ("".equals(mWish.getText()+"".toString().trim())){
                    ToastUtil.showToast(WishingActivity.this,"请输入心愿");
                }else{
                    setApply();
                }
                break;
            case R.id.cb1:
                mCb2.setChecked(false);
                mCb3.setChecked(false);
                mPrice.setText(mOne);
                break;
            case R.id.cb2:
                mCb1.setChecked(false);
                mCb3.setChecked(false);
                mPrice.setText(mTwo);
                break;
            case R.id.cb3:
                mCb1.setChecked(false);
                mCb2.setChecked(false);
                mPrice.setText(mThird );
                break;
        }
    }
}
