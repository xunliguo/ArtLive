package com.example.rh.artlive.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.CircleImageView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

import static com.tencent.open.utils.ThreadManager.init;

/**
 * Created by rh on 2017/12/26.
 * 课程订单
 */

public class ClassOrderActivity extends BaseFragmentActivity implements View.OnClickListener{

    private String mClass_Id;
    private String mSum;
    private String mTitle;
    private String mPrice;
    private String mAllPrice;

    private TextView mClass_Title;
    private TextView mClass_Price;
    private TextView mClass_Sum;
    private TextView mAll_Price;

    private TextView mTeacher_name;
    private TextView mTeacher_value;
    private TextView mStudent_name;
    private TextView mStudent_value;
    private TextView mPhoneView;

    private CircleImageView mStudent;
    private CircleImageView mTeacher;

    private LinearLayout mOrderLayout;
    private Dialog datePickerDialog;

    private String mOrder;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_order);
        init();
        setListener();
        setData();
    }

    private void init(){
        Intent intent=getIntent();
        mClass_Id=intent.getStringExtra("class_id");
        mSum=intent.getStringExtra("sum");
        mTitle=intent.getStringExtra("mTitleView");
        mPrice=intent.getStringExtra("price");
        mClass_Title=(TextView)findViewById(R.id.class_title);
        mClass_Price=(TextView)findViewById(R.id.class_price);
        mClass_Sum=(TextView)findViewById(R.id.class_sum);

        mTeacher_name=(TextView)findViewById(R.id.teacher_name);
        mTeacher_value=(TextView)findViewById(R.id.teacher_value);

        mStudent_name=(TextView)findViewById(R.id.student_name);
        mStudent_value=(TextView)findViewById(R.id.student_value);
        mAll_Price=(TextView)findViewById(R.id.all_price);
        mPhoneView=(TextView)findViewById(R.id.phone);
        mOrderLayout=(LinearLayout)findViewById(R.id.button);

        mStudent=(CircleImageView)findViewById(R.id.student_ident);
        mTeacher=(CircleImageView)findViewById(R.id.teacher_ident);

        mClass_Title.setText(mTitle);
        mClass_Price.setText(mPrice);
        mClass_Sum.setText(mSum+"元/课时");
        mAllPrice=String.valueOf(Double.parseDouble(mSum)*Double.parseDouble(mPrice));
        mAll_Price.setText(mAllPrice);
        Log.e("题目"+intent.getStringExtra("mTitleView")+"价格"+intent.getStringExtra("mPriceView")+"数量"+intent.getStringExtra("sum"));
    }
    private void setListener(){
        mOrderLayout.setOnClickListener(this);
    }
    private void setData(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("class_id",mClass_Id);
        map.put("sum",mSum);
        map.put("userid",mSharePreferenceUtil.getString(SharedPerfenceConstant.USER_ID));
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.CALSS_ORDEER, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {

                Log.e("课程订单"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONObject inst=data.getJSONObject("inst");
                            mTeacher_name.setText(inst.getString("inst_name"));
                            mTeacher_value.setText(inst.getString("address")+"/"+inst.getString("major"));
                            Glide.with(ClassOrderActivity.this).load(inst.getString("header")).into(mTeacher);
                            JSONObject user=data.getJSONObject("user");
                            mStudent_name.setText(user.getString("user_nickname"));
                            mStudent_value.setText(user.getString("user_major"));
//                            mPhoneView.setText(user.getString("phone"));
                            Glide.with(ClassOrderActivity.this).load(user.getString("user_img")).into(mStudent);
                            mOrder=data.getString("order_no");
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
            case R.id.button:
                initDialog();
                break;
        }
    }

    /**
     * 添加直播选择
     */
    private void initDialog() {
        datePickerDialog = new Dialog(this, R.style.time_dialog);
        datePickerDialog.setCancelable(false);
        datePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        datePickerDialog.setContentView(R.layout.class_apply);
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
        TextView selected_class=datePickerDialog.findViewById(R.id.selected_class);
        final TextView money=datePickerDialog.findViewById(R.id.apply);
        money.setText("需支付"+mAllPrice);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.dismiss();
            }
        });
        sum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.dismiss();
                HashMap<String, String> map = new HashMap<>();
                map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
                map.put("order_no",mOrder);
                HttpUtil.postHttpRequstProgess(ClassOrderActivity.this, "正在加载", UrlConstant.CALSS_APPLY, map, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                    }
                    @Override
                    public void onResponse(String response) {
                        Log.e("课程支付"+response);
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);
                            if ("1".equals(jsonObject.getString("state"))) {
                                if (jsonObject.has("data")) {

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, "data1", null);
            }
        });
    }
}
