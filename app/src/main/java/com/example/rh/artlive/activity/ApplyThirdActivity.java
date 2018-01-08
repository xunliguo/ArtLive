package com.example.rh.artlive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by rh on 2017/11/22.
 */

public class ApplyThirdActivity extends BaseFragmentActivity implements View.OnClickListener{
    private ImageView mShowDarw;

    private RelativeLayout mStepLayout;

    private String data;
    private String teacher_id;

    private TextView mView;
    private TextView mView_one;
    private TextView mView_two;
    private TextView mView_third;

    private EditText mText;
    private EditText mText_one;
    private EditText mText_two;
    private EditText mText_third;

    private String mQues;
    private String mQues_one;
    private String mQues_two;
    private String mQues_third;

    private ArrayList<String> list=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_third);
        init();
        setListener();
    }

    private void init(){
        Intent intent=getIntent();
        teacher_id=intent.getStringExtra("teacher_id");
        list=intent.getStringArrayListExtra("tea_list");

        mStepLayout=(RelativeLayout)findViewById(R.id.setting);
        mShowDarw=(ImageView)findViewById(R.id.showDraw);

        mView=(TextView)findViewById(R.id.question);
        mView_one=(TextView)findViewById(R.id.question_one);
        mView_two=(TextView)findViewById(R.id.question_two);
        mView_third=(TextView)findViewById(R.id.question_third);

        mText=(EditText)findViewById(R.id.add_content);
        mText_one=(EditText)findViewById(R.id.add_content_one);
        mText_two=(EditText)findViewById(R.id.add_content_two);
        mText_third=(EditText)findViewById(R.id.add_content_third);
        int size=list.size();
        String[] array=list.toArray(new String[size]);
        for (int i=0;i<array.length;i++){
            if (i==0){
                mQues=array[i];
                mView.setText(array[i]);
            }else if (i==1){
                mQues_one=array[i];
                mView_one.setText(array[i]);
            }else if (i==2){
                mQues_two=array[i];
                mView_two.setText(array[i]);
            }else if (i==3){
                mQues_third=array[i];
                mView_third.setText(array[i]);
            }
        }
    }
    private void setListener(){
        mStepLayout.setOnClickListener(this);
        mShowDarw.setOnClickListener(this);
    }

    private void setData(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token", mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("teacher_id",teacher_id);
        map.put("teacher_content",mText.getText()+"".toString().trim());
        map.put("teacher_situation",mText_one.getText()+"".toString().trim());
        map.put("teacher_experience",mText_two.getText()+"".toString().trim());
        map.put("teacher_contract",mText_third.getText()+"".toString().trim());
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.THIRDTEP, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("第三步请求数据"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.has("data")) {
                        if ("1".equals(jsonObject.getString("state"))) {
                            ToastUtil.showToast(ApplyThirdActivity.this,"请求成功");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "phone", null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting:
                if ("".equals(mText.getText()+"".toString().trim())){
                    ToastUtil.showToast(this,"请输入");
                }else if ("".equals(mText_one.getText()+"".toString().trim())){
                    ToastUtil.showToast(this,"请输入");
                }else if ("".equals(mText_two.getText()+"".toString().trim())){
                    ToastUtil.showToast(this,"请输入");
                }else if ("".equals(mText_third.getText()+"".toString().trim())){
                    ToastUtil.showToast(this,"请输入");
                }else{
                    setData();
                }

                break;
            case R.id.showDraw:
                finish();
                break;
        }
    }
}
