package com.example.rh.artlive.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.CustomUrlSpan;
import com.example.rh.artlive.view.PullToRefreshLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by rh on 2017/12/21.
 */

public class ArticleDetailsActivity extends BaseFragmentActivity implements View.OnClickListener{


    private LinearLayout mPerOne;
    private LinearLayout mPerTwo;
    private LinearLayout mPerThird;
    private LinearLayout mPerFour;
    private LinearLayout mPerFive;

    private ImageView mPreView_one;
    private ImageView mPreView_two;
    private ImageView mPreView_third;
    private ImageView mPreView_four;
    private ImageView mPreView_five;



    private TextView mTitleView;   //标题
    private TextView mTimeView;//时间
    private TextView mShareView;//分享次数
    private TextView mReadingView;//阅读次数
    private TextView mContentView_one;
    private TextView mContentView_two;
    private TextView mContentView_third;
    private TextView mContentView_four;
    private TextView mContentView_five;
    private ImageView mShowDraw;


    ArrayList<String> tea_list;
    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        init();
        setData();
        setListener();
    }

    private void init(){
        Intent intent=getIntent();
        mId=intent.getStringExtra("id");
        mPreView_one=(ImageView)findViewById(R.id.pre_image_one);
        mPreView_two=(ImageView)findViewById(R.id.pre_image_two);
        mPreView_third=(ImageView)findViewById(R.id.pre_image_third);
        mPreView_four=(ImageView)findViewById(R.id.pre_image_four);
        mPreView_five=(ImageView)findViewById(R.id.pre_image_five);


        mTitleView=(TextView)findViewById(R.id.pre_title);
        mTimeView=(TextView)findViewById(R.id.art_time);
        mShareView=(TextView)findViewById(R.id.art_share);
        mReadingView=(TextView)findViewById(R.id.art_reading);
        mContentView_one=(TextView)findViewById(R.id.pre_content_one);
        mContentView_two=(TextView)findViewById(R.id.pre_content_two);
        mContentView_third=(TextView)findViewById(R.id.pre_content_third);
        mContentView_four=(TextView)findViewById(R.id.pre_content_four);
        mContentView_five=(TextView)findViewById(R.id.pre_content_five);
        mShowDraw=(ImageView)findViewById(R.id.showDraw);



        mPerOne=(LinearLayout)findViewById(R.id.per_one);
        mPerTwo=(LinearLayout)findViewById(R.id.per_two);
        mPerThird=(LinearLayout)findViewById(R.id.per_third);
        mPerFour=(LinearLayout)findViewById(R.id.per_four);
        mPerFive=(LinearLayout)findViewById(R.id.per_five);

    }

    private void setListener(){
        mShowDraw.setOnClickListener(this);
    }

    private void setData(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("forum_id",mId);
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.ARTICLE_DETAILS, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("文章详情"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            mTitleView.setText(data.getString("forum_title"));
                            mTimeView.setText(data.getString("forum_time"));
                            mShareView.setText("分享"+data.getString("fenxiang")+"次");
                            mReadingView.setText("阅读"+data.getString("forum_read")+"次");
                            JSONArray content=data.getJSONArray("forum_content");
                            for (int i=0;i<content.length();i++){
                                JSONObject jsonObject1=content.getJSONObject(i);
                                if (i==0) {
                                    if (jsonObject1.has("text")){
                                        mContentView_one.setVisibility(View.VISIBLE);
                                        mPreView_one.setVisibility(View.GONE);
                                        mContentView_one.setText(jsonObject1.getString("text"));
                                        interceptHyperLink(mContentView_one);
                                    }else{
                                        mContentView_one.setVisibility(View.GONE);
                                        mPreView_one.setVisibility(View.VISIBLE);
                                        Glide.with(ArticleDetailsActivity.this).load(jsonObject1.getString("picture")).into(mPreView_one);
//                                        mPreView_one.setImageURI(Uri.fromFile(new File(jsonObject1.getString("picture"))));
                                    }
                                }else if (i==1){
                                    mPerTwo.setVisibility(View.VISIBLE);
                                    if (jsonObject1.has("text")){
                                        mContentView_two.setVisibility(View.VISIBLE);
                                        mPreView_two.setVisibility(View.GONE);
                                        mContentView_two.setText(jsonObject1.getString("text"));
                                        interceptHyperLink(mContentView_two);
                                    }else{
                                        mContentView_two.setVisibility(View.GONE);
                                        mPreView_two.setVisibility(View.VISIBLE);
                                        Glide.with(ArticleDetailsActivity.this).load(jsonObject1.getString("picture")).into(mPreView_two);
//                                        mPreView_two.setImageURI(Uri.fromFile(new File(jsonObject1.getString("picture"))));
                                    }
                                }else if (i==2){
                                    mPerThird.setVisibility(View.VISIBLE);
                                    if (jsonObject1.has("text")){
                                        mContentView_third.setVisibility(View.VISIBLE);
                                        mPreView_third.setVisibility(View.GONE);
                                        mContentView_third.setText(jsonObject1.getString("text"));
                                        interceptHyperLink(mContentView_third);
                                    }else{
                                        mContentView_third.setVisibility(View.GONE);
                                        mPreView_third.setVisibility(View.VISIBLE);
                                        Glide.with(ArticleDetailsActivity.this).load(jsonObject1.getString("picture")).into(mPreView_third);
//                                        mPreView_third.setImageURI(Uri.fromFile(new File(jsonObject1.getString("picture"))));
                                    }
                                }else if (i==3){
                                    mPerFour.setVisibility(View.VISIBLE);
                                    if (jsonObject1.has("text")){
                                        mContentView_four.setVisibility(View.VISIBLE);
                                        mPreView_four.setVisibility(View.GONE);
                                        mContentView_four.setText(jsonObject1.getString("text"));
                                        interceptHyperLink(mContentView_four);
                                    }else{
                                        mContentView_four.setVisibility(View.GONE);
                                        mPreView_four.setVisibility(View.VISIBLE);
                                        Glide.with(ArticleDetailsActivity.this).load(jsonObject1.getString("picture")).into(mPreView_four);
//                                        mPreView_four.setImageURI(Uri.fromFile(new File(jsonObject1.getString("picture"))));
                                    }
                                }else if (i==4){
                                    mPerFive.setVisibility(View.VISIBLE);
                                    if (jsonObject1.has("text")){
                                        mContentView_five.setVisibility(View.VISIBLE);
                                        mPreView_five.setVisibility(View.GONE);
                                        mContentView_five.setText(jsonObject1.getString("text"));
                                        interceptHyperLink(mContentView_five);
                                    }else{
                                        mContentView_five.setVisibility(View.GONE);
                                        mPreView_five.setVisibility(View.VISIBLE);
                                        Glide.with(ArticleDetailsActivity.this).load(jsonObject1.getString("picture")).into(mPreView_five);
//                                        mPreView_five.setImageURI(Uri.fromFile(new File(jsonObject1.getString("picture"))));
                                    }
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "art_details", null);
    }


    /**
     * 拦截超链接
     * @param tv
     */
    private void interceptHyperLink(TextView tv) {
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = tv.getText();

        Toast.makeText(this,"dasdsa"+text,Toast.LENGTH_LONG).show();
        if (text instanceof Spannable) {
            int end = text.length();
            Spannable spannable = (Spannable) tv.getText();
            URLSpan[] urlSpans = spannable.getSpans(0, end, URLSpan.class);
            if (urlSpans.length == 0) {
                return;
            }

            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
            // 循环遍历并拦截 所有http://开头的链接
            for (URLSpan uri : urlSpans) {
                String url = uri.getURL();
                if (url.indexOf("http://") == 0) {
                    CustomUrlSpan customUrlSpan = new CustomUrlSpan(this,url);
                    spannableStringBuilder.setSpan(customUrlSpan, spannable.getSpanStart(uri),
                            spannable.getSpanEnd(uri), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }
            tv.setText(spannableStringBuilder);
        }
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
