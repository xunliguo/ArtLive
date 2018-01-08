package com.example.rh.artlive.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Judge;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.ToastUtil;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.PullToRefreshLayout;
import com.example.rh.artlive.widget.CountDownTimer;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by rh on 2017/11/17.
 */

public class ForgetActivity extends BaseFragmentActivity implements View.OnClickListener{
    private EditText phone;
    private EditText passwrd;

    private Button mCodePhone;//手机验证码

    private Button mStup;
    private ImageView mShowDraw;
    private TextView mHaveTextView;
    private String name;
    private ImageView showdraw;
    private FloatingActionButton fab;
    private CardView cvAdd;
    private int currentapiVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        init();
        setListener();
    }

    private void init(){
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        phone=(EditText)findViewById(R.id.apply_phone);
        passwrd=(EditText)findViewById(R.id.apply_pass);
        mCodePhone=(Button)findViewById(R.id.login_code);
        mStup=(Button)findViewById(R.id.login_btn);
    }
    private void setListener(){
        phone.setOnClickListener(this);
        passwrd.setOnClickListener(this);
        mCodePhone.setOnClickListener(this);
        mStup.setOnClickListener(this);
        mShowDraw.setOnClickListener(this);
    }
    /**
     * 获取验证码
     */
    private void getCodePhone(){

        HashMap<String,String> map=new HashMap<>();
        map.put("tel",phone.getText()+"".toString().trim());
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.CODE, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                Log.e("获取验证码"+response);
                JSONObject obj;
                try {
                    obj=new JSONObject(response);
                    if("1".equals(obj.getString("state"))){
                        ToastUtil.showToast(ForgetActivity.this,"发送成功");
                        JSONObject data=obj.getJSONObject("data");
                    }else{
                        ToastUtil.showToast(ForgetActivity.this,"发送失败请重新尝试");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "registercode", null);
    }

    /**
     * 提交查询是否为用户
     */
    private void setAdmine(){

        HashMap<String,String> map=new HashMap<>();
        map.put("tel",phone.getText()+"".toString().trim());
        map.put("yzm",passwrd.getText()+"".toString().trim());
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.FORGET_PASSWORD, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                Log.e("提交数据"+response);
                JSONObject obj;
                try {
                    obj=new JSONObject(response);
                    if("1".equals(obj.getString("state"))){
                        Intent intent=new Intent(ForgetActivity.this,ForgetTwoActivity.class);
                        intent.putExtra("phone",phone.getText()+"".toString().trim());
                        startActivity(intent);
                        finish();
                    }else{
                        ToastUtil.showToast(ForgetActivity.this,"对不起你还不是该用户");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "registercode", null);
    }

    final CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            mCodePhone.setText(millisUntilFinished/1000 + "秒");
        }

        @Override
        public void onFinish() {
            mCodePhone.setEnabled(true);
            mCodePhone.setText("获取验证码");
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showDraw:
                finish();
                break;
            case R.id.login_code:
                if ("".equals(phone.getText()+"".toString().trim())){
                    ToastUtil.showToast(this,"请输入手机号");
                }else{
                    timer.start();
                    getCodePhone();
                }
                break;
            case R.id.login_btn:
                setAdmine();
                break;
        }
    }
}
