package com.example.rh.artlive.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.rh.artlive.R;
import com.example.rh.artlive.richtext.RichTextEditor;
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
 * Created by rh on 2017/9/4.
 * 意见反馈
 */

public class FeedBookActivity extends BaseFragmentActivity implements View.OnClickListener{

    private ImageView mShowDraw;
    private EditText mFeedBkText;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedbook);
        init();
        setListener();
    }
    private void init(){
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mFeedBkText=(EditText)findViewById(R.id.add_content);
        mButton=(Button)findViewById(R.id.login_btn);

    }
    private void setListener(){
        mShowDraw.setOnClickListener(this);
        mButton.setOnClickListener(this);
    }
    private void setData(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token", mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("advice",mFeedBkText.getText()+"".toString().trim());
        map.put("question",mFeedBkText.getText()+"".toString().trim());
        map.put("userid",mSharePreferenceUtil.getString(SharedPerfenceConstant.USER_ID));
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.FEEDBOOK, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e(response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.has("data")) {
                        if ("1".equals(jsonObject.getString("state"))) {
                            ToastUtil.showToast(FeedBookActivity.this,"修改成功");
                            finish();
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
            case R.id.showDraw:
                finish();
                break;
            case R.id.login_btn:
                if ("".equals(mFeedBkText.getText()+"".toString().trim())){
                    ToastUtil.showToast(this,"请输入");
                }else{
                    setData();
                }

                break;
        }
    }
}
