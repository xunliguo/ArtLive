package com.example.rh.artlive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import org.w3c.dom.Text;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by rh on 2017/12/13.
 */

public class RecordEditActivity extends BaseFragmentActivity implements View.OnClickListener{

    private ImageView mShowDraw;
    private TextView mSignView;
    private EditText mContentText;
    private TextView mNameView;


    private String mVoice;
    private String mRecord_id;
    private String mRecord_title;
    private String type;

    @Override
    protected void onCreate(Bundle savedInsatnceState){
        super.onCreate(savedInsatnceState);
        setContentView(R.layout.activity_record_edit);
        init();
        setListener();
    }
    private void init(){
        Intent intent=getIntent();
        mVoice=intent.getStringExtra("voice");
        mRecord_id=intent.getStringExtra("record");
        mRecord_title=intent.getStringExtra("title");
        type=intent.getStringExtra("type");
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mSignView=(TextView)findViewById(R.id.editor);
        mContentText=(EditText)findViewById(R.id.add_content);
        mNameView=(TextView)findViewById(R.id.music_name);
        mNameView.setText(mRecord_title);
    }
    private void setListener(){
        mShowDraw.setOnClickListener(this);
        mSignView.setOnClickListener(this);
    }

    /**
     * 录音提交
     * @param
     */
    private void setData(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("userid","25");
        map.put("voice",mVoice);
        map.put("type",type);
        map.put("record",mRecord_id);
        Log.e("录音"+mVoice+"读物id"+mRecord_id);
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.RECORD, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("录音提交"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            ToastUtil.showToast(RecordEditActivity.this,"提交成功");
                            Intent intent=new Intent(RecordEditActivity.this,RecordActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "voice", null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showDraw:
                finish();
                break;
            case R.id.editor:
                if ("".equals(mContentText.getText()+"".toString().trim())){
                    ToastUtil.showToast(this,"请输入");
                }else{
                    setData();
                }
                break;
        }
    }
}
