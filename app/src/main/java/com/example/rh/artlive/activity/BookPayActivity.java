package com.example.rh.artlive.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.articlechoice.QuestionBean;
import com.example.rh.artlive.bean.BookBean;
import com.example.rh.artlive.bean.BookIntroBean;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.ToastUtil;
import com.example.rh.artlive.util.UrlConstant;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by rh on 2017/12/20.
 */

public class BookPayActivity extends BaseFragmentActivity implements View.OnClickListener{

    private ImageView mShowDraw;

    private String mBook_Id;
    private ImageView mBackImage;
    private TextView mContentView;
    private Button mPay;

    public static List<BookIntroBean> bookBeanList ;
    public static BookIntroBean bookBean;

    private Dialog datePickerDialog;

    private String mPay_Red="0";
    private String image;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_pay);
        init();
        setData();
        setListener();
    }
    private void init(){
        Intent intent=getIntent();
        mBook_Id=intent.getStringExtra("book_id");
        mPay_Red=intent.getStringExtra("flag");

        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mBackImage=(ImageView)findViewById(R.id.back_image);
        mContentView=(TextView)findViewById(R.id.book_content);
        mPay=(Button)findViewById(R.id.login_btn);
        if ("1".equals(mPay_Red)){
            mPay.setText("阅读");
        }else{
            mPay.setText("购买");
        }
    }

    private void setListener(){
        mShowDraw.setOnClickListener(this);
        mPay.setOnClickListener(this);
    }
    private void setData(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("id",mBook_Id);
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.BOOKINTRO, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("书籍简介"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            mContentView.setText(data.getString("content"));
                            mSharePreferenceUtil.setString(SharedPerfenceConstant.BOOK_PAY,data.getString("price"));
                            Glide.with(BookPayActivity.this).load(data.getString("img")).into(mBackImage);
                            image=data.getString("img");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "shelf", null);
    }

    private void setDetails(){
        bookBeanList = new ArrayList<BookIntroBean>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("id",mBook_Id);
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.BOOKDEATILS, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("书籍详情"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONObject cont=data.getJSONObject("Cont");
                            JSONArray info=cont.getJSONArray("info");
                            for (int i=0;i<info.length();i++){
                                JSONObject jsonObject1=info.getJSONObject(i);
                                bookBean=new BookIntroBean(jsonObject1.getString("name"),"",jsonObject1.getString("content"),"");
                                bookBeanList.add(bookBean);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "shelf", null);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showDraw:
                finish();
                break;
            case R.id.login_btn:
                if ("0".equals(mPay_Red)) {
                    initDialog();
                }else{
                    Intent intent=new Intent(BookPayActivity.this,BookDetailsActivity.class);
                    intent.putExtra("book_id",mBook_Id);
                    startActivity(intent);
                }
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
        datePickerDialog.setContentView(R.layout.custom_book_pay_picker);
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
        final EditText editText=datePickerDialog.findViewById(R.id.phone);
        final LinearLayout one=datePickerDialog.findViewById(R.id.one);
        final LinearLayout third=datePickerDialog.findViewById(R.id.third);
        TextView mBook_Pay=datePickerDialog.findViewById(R.id.book_pay);
        mBook_Pay.setText("当前书需支付"+mSharePreferenceUtil.getString(SharedPerfenceConstant.BOOK_PAY)+"虫币");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePickerDialog.dismiss();
            }
        });
        sum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookBeanList = new ArrayList<BookIntroBean>();
                HashMap<String, String> map = new HashMap<>();
                map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
                map.put("book_id",mBook_Id);
                Log.e("书"+mBook_Id);
                HttpUtil.postHttpRequstProgess(BookPayActivity.this, "正在加载", UrlConstant.BBOK_PAY, map, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                    }
                    @Override
                    public void onResponse(String response) {
                        Log.e("书籍购买"+response);
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);
                            if ("1".equals(jsonObject.getString("state"))) {
                                if (jsonObject.has("data")) {
                                    mPay.setText("阅读");
                                    mPay_Red="1";
                                    ToastUtil.showToast(BookPayActivity.this,"支付成功");
                                    setDetails();
                                }
                            }if ("-1".equals(jsonObject.getString("state"))){
                                ToastUtil.showToast(BookPayActivity.this,jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, "shelf", null);
                datePickerDialog.dismiss();
            }
        });
    }
}
