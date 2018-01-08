package com.example.rh.artlive.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.MatchBean;
import com.example.rh.artlive.util.ActionSheetDialog;
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
 * Created by rh on 2017/11/22.
 */

public class MatchSignActivity extends BaseFragmentActivity implements View.OnClickListener{

    private Context context;

    private ImageView mShowDraw;
    private EditText mNameText;
    private EditText mPhoneText;
    private EditText mAgeText;
    private EditText mAddressText;
    private EditText mTeacherText;
    private EditText mSexText;

    private TextView mSexView;
    private TextView mPriceView;
    private String mSex="0";

    private String mActivity_Id;
    private String mPrice;


    private RelativeLayout mSignLayout;
    private RelativeLayout mSexLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        context=this;
        init();
        setListener();

    }

    private void  init(){
        Intent intent=getIntent();
        mActivity_Id=intent.getStringExtra("activity_id");
        mPrice=intent.getStringExtra("price");
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mNameText=(EditText)findViewById(R.id.sign_name);
        mPhoneText=(EditText)findViewById(R.id.sign_phone);
        mAgeText=(EditText)findViewById(R.id.sign_age);
        mAddressText=(EditText)findViewById(R.id.sign_address);
        mTeacherText=(EditText)findViewById(R.id.sign_teacher);

        mSignLayout=(RelativeLayout)findViewById(R.id.setting);
        mSexLayout=(RelativeLayout)findViewById(R.id.sex);

        mSexView=(TextView)findViewById(R.id.sex_value);
        mPriceView=(TextView)findViewById(R.id.sign_price);
        mPriceView.setText(mPrice);
    }
    private void setListener(){
        mShowDraw.setOnClickListener(this);
        mSignLayout.setOnClickListener(this);
        mSexLayout.setOnClickListener(this);
    }

    /**
     * 报名
     */
    private void setData(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("name",mNameText.getText()+"".toString().trim());
        map.put("tel",mPhoneText.getText()+"".toString().trim());
        if ("1".equals(mSex)){
            map.put("sex","男");
        }else{
            map.put("sex","女");
        }
        map.put("match_id",mActivity_Id);
        map.put("price",mPrice);
        map.put("age",mAgeText.getText()+"".toString().trim());
        map.put("teacher",mTeacherText.getText()+"".toString().trim());
        map.put("area",mAddressText.getText()+"".toString().trim());
        Log.e("price"+mPrice+"age"+mAgeText.getText()+"".toString().trim()+"teahcer"+mTeacherText.getText()+"".toString().trim()+"area"+mAddressText.getText()+"".toString().trim()+"macth_id"+mActivity_Id
                +"sex"+"男"+"tel"+mPhoneText.getText()+"".toString().trim()+"name"+mNameText.getText()+"".toString().trim());
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.SIGNUP, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("报名"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    Log.e("msg",jsonObject.getString("msg"));
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                           ToastUtil.showToast(MatchSignActivity.this,"报名成功");
                            finish();
                        }
                    }else if ("-2".equals(jsonObject.getString("state"))){
                        ToastUtil.showToast(MatchSignActivity.this,"请登录");
                    }else if ("-1".equals(jsonObject.getString("state"))){
                        ToastUtil.showToast(MatchSignActivity.this,"余额不足，或者输入格式不对");
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
            case R.id.setting:
                if ("".equals(mNameText.getText()+"".toString().trim())){
                    ToastUtil.showToast(context,"请输入姓名");
                }else if ("".equals(mPhoneText.getText()+"".toString().trim())){
                    ToastUtil.showToast(context,"请输入手机号");
                }else if ("".equals(mAgeText.getText()+"".toString().trim())){
                    ToastUtil.showToast(context,"请输入年龄");
                }else if ("".equals(mAddressText.getText()+"".toString().trim())){
                    ToastUtil.showToast(context,"请输入地址");
                }else if ("".equals(mTeacherText.getText()+"".toString().trim())){
                    ToastUtil.showToast(context,"请输入指导老师");
                }else{
                    setData();
                }
                break;
            case R.id.sex:
                setSex();
                break;
        }
    }

    private void setSex(){
        new ActionSheetDialog(MatchSignActivity.this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .addSheetItem("男", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                mSexView.setText("男");
                                mSex="1";
                            }
                        })
                .addSheetItem("女", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                mSexView.setText("女");
                                mSex="0";
                            }
                        }).show();
    }

}
