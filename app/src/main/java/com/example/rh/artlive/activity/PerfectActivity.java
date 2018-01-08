package com.example.rh.artlive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.PullToRefreshLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by rh on 2017/12/26.
 * 完善信息
 */

public class PerfectActivity extends BaseFragmentActivity implements View.OnClickListener{

    private ImageView mShowDraw;

    private String mClass_Id;
    private TextView mTitleView;
    private TextView mPriceView;
    private RelativeLayout mUpLayout;

    private TextView mNumView;
    private Button mAddButton;
    private Button mCutButton;
    private String price;

    private int shopNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect);
        init();
        setListener();
        setData();
    }
    private void  init(){
        Intent intent=getIntent();
        mClass_Id=intent.getStringExtra("class_id");
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mTitleView=(TextView)findViewById(R.id.class_title);
        mPriceView=(TextView)findViewById(R.id.class_price);
        mUpLayout=(RelativeLayout)findViewById(R.id.setting);

         mNumView=(TextView)findViewById(R.id.tv_shop_num);
         mAddButton=(Button)findViewById(R.id.btn_shop_add);
         mCutButton=(Button)findViewById(R.id.btn_shop_cut);

    }
    private void setListener(){
        mUpLayout.setOnClickListener(this);
        mShowDraw.setOnClickListener(this);
        mAddButton.setOnClickListener(this);
        mCutButton.setOnClickListener(this);
    }
    private void setData(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("class_id",mClass_Id);
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.PREFECT, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {

                Log.e("完善信息"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            mTitleView.setText(data.getString("title"));
                            mPriceView.setText(data.getString("price")+"元/课时");
                            price=data.getString("price");
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
            case R.id.setting:
                Intent intent=new Intent(PerfectActivity.this,ClassOrderActivity.class);
                intent.putExtra("class_id",mClass_Id);
                intent.putExtra("sum",mNumView.getText()+"".toString().trim());
                intent.putExtra("mTitleView",mTitleView.getText()+"".toString().trim());
                intent.putExtra("price",price);
                startActivity(intent);
                break;
            case R.id.showDraw:
                finish();
                break;
            case R.id.btn_shop_add:
                if (shopNum < 99)
                    shopNum++;
                else {
                }
                mNumView.setText(shopNum + "");
                break;
            case R.id.btn_shop_cut:
                if (shopNum > 1)
                    shopNum--;
                else {
                }
                mNumView.setText(shopNum + "");
                break;
        }
    }
}
