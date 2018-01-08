package com.example.rh.artlive.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rh.artlive.R;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.just.library.AgentWeb;
import com.just.library.ChromeClientCallbackManager;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by rh on 2017/9/14.
 */

public class AboutUsActivity extends BaseFragmentActivity implements View.OnClickListener{
    private WebView mWebView;
    private ImageView mShowDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        init();
        setData();
        setListener();
    }
    private void init(){
        mWebView=(WebView)findViewById(R.id.content);
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
    }
    private void setListener(){
        mShowDraw.setOnClickListener(this);
    }

    private void setData(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token", mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.ABOUT, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                Log.e("关于我们"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("0".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            String html = data.getString("content");
                            mWebView.loadData(getHtmlData(html), "text/html; charset=UTF-8", null);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "law", null);
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head><meta charset=\"UTF-8\"><title>Document</title></head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showDraw:
                finish();
                break;
        }
    }
}
