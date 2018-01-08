package com.example.rh.artlive.activity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.ToastUtil;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.PullToRefreshLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by rh on 2017/12/28.
 * 添加课程
 */

public class AddClassActivity extends BaseFragmentActivity implements View.OnClickListener{

    private ImageView mShowDraw;

    private EditText mName;
    private EditText mContent;
    private EditText mContent1;
    private EditText mtime;
    private EditText mSee;
    private EditText mLocal;
    private EditText mCount;

    private Button mSign;


    @Override
    protected void onCreate(Bundle savedInsaveState){
        super.onCreate(savedInsaveState);
        setContentView(R.layout.activity_add_class);
        init();
        setListener();
    }

    private void init(){
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mName=(EditText)findViewById(R.id.add_name);
        mContent=(EditText)findViewById(R.id.add_content);
        mContent1=(EditText)findViewById(R.id.add_content1);
        mtime=(EditText)findViewById(R.id.add_time);
        mSee=(EditText)findViewById(R.id.add_see);
        mLocal=(EditText)findViewById(R.id.add_local);
        mCount=(EditText)findViewById(R.id.add_count);

        mSign=(Button)findViewById(R.id.login_btn2);
    }

    private void setListener(){
        mShowDraw.setOnClickListener(this);
        mSign.setOnClickListener(this);
    }

    private void setSign(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("type","发布");
        map.put("title",mName.getText()+"".toString().trim());
        map.put("description",mContent.getText()+"".toString().trim());
        map.put("content",mContent1.getText()+"".toString().trim());
        map.put("require",mSee.getText()+"".toString().trim());
        map.put("time",mCount.getText()+"".toString().trim());
        map.put("class_time",mtime.getText()+"".toString().trim());
        map.put("address",mLocal.getText()+"".toString().trim());
        map.put("video",testUpload(mSharePreferenceUtil.getString(SharedPerfenceConstant.VIDEO_PATH)));
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.ADD_CLASS, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {

                Log.e("添加课程"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            ToastUtil.showToast(AddClassActivity.this,"发布成功");
                            finish();
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
            case R.id.showDraw:
                finish();
                break;
            case R.id.login_btn2:
                if ("".equals(mName.getText()+"".toString().trim())){
                    ToastUtil.showToast(AddClassActivity.this,"请输入名称");
                }else if ("".equals(mContent.getText()+"".toString().trim())){
                    ToastUtil.showToast(AddClassActivity.this,"请输入简介");
                }else if ("".equals(mContent1.getText()+"".toString().trim())){
                    ToastUtil.showToast(AddClassActivity.this,"请输入要求");
                }else if ("".equals(mCount.getText()+"".toString().trim())){
                    ToastUtil.showToast(AddClassActivity.this,"请输入课次");
                }else if ("".equals(mtime.getText()+"".toString().trim())){
                    ToastUtil.showToast(AddClassActivity.this,"请输入课时");
                }else if ("".equals(mLocal.getText()+"".toString().trim())){
                    ToastUtil.showToast(AddClassActivity.this,"请输入地点");
                }else{
                    setSign();
                }
                break;
        }
    }

    /**
     * 图片转换成文件流
     * @param path
     * @return
     */
    public String testUpload(String path) {
        try {

            String srcUrl = path; // "/mnt/sdcard/"; //路径
            // String fileName = PhotoName+".jpg"; //文件名
            FileInputStream fis = new FileInputStream(srcUrl);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[65536];
            int count = 0;
            while ((count = fis.read(buffer)) >= 0) {
                baos.write(buffer, 0, count);
            }
            String uploadBuffer = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT)); // 进行Base64编码
            fis.close();//这两行原来没有
            baos.flush();
            return uploadBuffer;

        } catch (Exception e) {
            e.printStackTrace();
        }
        // return soapObject;
        return null;
    }
}
