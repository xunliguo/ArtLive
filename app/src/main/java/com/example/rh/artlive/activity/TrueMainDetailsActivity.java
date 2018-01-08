package com.example.rh.artlive.activity;

import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.RightBean;
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
 * Created by rh on 2017/12/22.
 */

public class TrueMainDetailsActivity extends BaseFragmentActivity {

    private String mSid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true_details);
        Intent intent=getIntent();
        mSid=intent.getStringExtra("sid");
        setData();
    }
    private void setData(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("id",mSid);
        Log.e("题 id"+mSid);
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.TRUE_DETAILS, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("真题详情"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONArray topic=data.getJSONArray("topic");
                            for (int i=0;i<topic.length();i++){
                                JSONObject jsonObject1=topic.getJSONObject(i);
                                RightBean photoBean= JSON.parseObject(jsonObject1.toString(),RightBean.class);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "collect", null);
    }
}
