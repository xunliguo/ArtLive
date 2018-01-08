package com.example.rh.artlive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rh.artlive.R;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.ToastUtil;
import com.example.rh.artlive.util.UrlConstant;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by rh on 2018/1/3.
 */

public class ForgetTwoActivity extends BaseFragmentActivity implements View.OnClickListener{


    private EditText phone;
    private EditText passwrd;
    private Button mStup;
    private ImageView mShowDraw;
    private String mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_two);
        init();
        setListener();
    }

    private void init(){
        Intent intent=getIntent();
        mPhone=intent.getStringExtra("phone");
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        phone=(EditText)findViewById(R.id.apply_phone);
        passwrd=(EditText)findViewById(R.id.apply_pass);
        mStup=(Button)findViewById(R.id.login_btn);
    }
    private void setListener(){
        phone.setOnClickListener(this);
        passwrd.setOnClickListener(this);
        mStup.setOnClickListener(this);
        mShowDraw.setOnClickListener(this);
    }

    /**
     * 提交
     */
    private void setAdmine(){
        HashMap<String,String> map=new HashMap<>();
        map.put("tel",mPhone);
        map.put("password",passwrd.getText()+"".toString().trim());
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.FORGET_PASSWORD_TWO, map, new StringCallback() {
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
                        ToastUtil.showToast(ForgetTwoActivity.this,"修改成功");
                        finish();
                    }else{
                        ToastUtil.showToast(ForgetTwoActivity.this,"修改失败");
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "registercode", null);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showDraw:
                finish();
                break;
            case R.id.login_btn:
                String name=phone.getText()+"".toString().trim();
                if ("".equals(phone.getText()+"".toString().trim())){
                    ToastUtil.showToast(this,"请输入密码");
                }else if ("".equals(passwrd.getText()+"".toString().trim())){
                    ToastUtil.showToast(this,"请再次输入密码");
                }else if (!name.equals(passwrd.getText()+"".toString().trim())){
                    ToastUtil.showToast(this,"两次密码输入的不正确");
                }else{
                    setAdmine();
                }
                break;
        }
    }
}
