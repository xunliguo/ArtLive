package com.example.rh.artlive.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rh.artlive.R;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.view.CustomUrlSpan;

import org.w3c.dom.Text;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by rh on 2017/12/5.
 */

public class PreViewActivity extends BaseFragmentActivity implements View.OnClickListener{

    private LinearLayout mPerOne;
    private LinearLayout mPerTwo;
    private LinearLayout mPerThird;
    private LinearLayout mPerFour;
    private LinearLayout mPerFive;

    private ImageView mImageHeader;
    private ImageView mPreView_one;
    private ImageView mPreView_two;
    private ImageView mPreView_third;
    private ImageView mPreView_four;
    private ImageView mPreView_five;



    private TextView mTitleView;
    private TextView mContentView_one;
    private TextView mContentView_two;
    private TextView mContentView_third;
    private TextView mContentView_four;
    private TextView mContentView_five;
    private TextView mShowDraw;


    ArrayList<String> tea_list;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        com.example.rh.artlive.util.Log.e("数组");
        tea_list =new ArrayList<>();

        init();
        setListener();
    }

    private void init(){
        mImageHeader=(ImageView)findViewById(R.id.pre_image);
        mPreView_one=(ImageView)findViewById(R.id.pre_image_one);
        mPreView_two=(ImageView)findViewById(R.id.pre_image_two);
        mPreView_third=(ImageView)findViewById(R.id.pre_image_third);
        mPreView_four=(ImageView)findViewById(R.id.pre_image_four);
        mPreView_five=(ImageView)findViewById(R.id.pre_image_five);


        mTitleView=(TextView)findViewById(R.id.pre_title);
        mContentView_one=(TextView)findViewById(R.id.pre_content_one);
        mContentView_two=(TextView)findViewById(R.id.pre_content_two);
        mContentView_third=(TextView)findViewById(R.id.pre_content_third);
        mContentView_four=(TextView)findViewById(R.id.pre_content_four);
        mContentView_five=(TextView)findViewById(R.id.pre_content_five);
        mShowDraw=(TextView)findViewById(R.id.showDraw);



        mPerOne=(LinearLayout)findViewById(R.id.per_one);
        mPerTwo=(LinearLayout)findViewById(R.id.per_two);
        mPerThird=(LinearLayout)findViewById(R.id.per_third);
        mPerFour=(LinearLayout)findViewById(R.id.per_four);
        mPerFive=(LinearLayout)findViewById(R.id.per_five);



        mImageHeader.setImageURI(Uri.fromFile(new File(mSharePreferenceUtil.getString(SharedPerfenceConstant.ARTHEADER))));
        Intent intent=getIntent();
        tea_list=intent.getStringArrayListExtra("art_content");
        mTitleView.setText(intent.getStringExtra("art_title"));
        /**
         * 文章预览功能，获取数组用str和pat来区分是文字还是图片
         */
        int size=tea_list.size();
        String[] array=tea_list.toArray(new String[size]);
        for (int i=0;i<array.length;i++){
            if (i==0) {
                if (array[i].indexOf("str") != -1) {
                    mContentView_one.setVisibility(View.VISIBLE);
                    mPreView_one.setVisibility(View.GONE);
                    //去掉字符串 str
                    String path=array[i].replace("str","");
                    mContentView_one.setText(path);
                    interceptHyperLink(mContentView_one);
                }else if (array[i].indexOf("pat") != -1){
                    mContentView_one.setVisibility(View.GONE);
                    mPreView_one.setVisibility(View.VISIBLE);
                    String path=array[i].replace("pat","");
                    mPreView_one.setImageURI(Uri.fromFile(new File(path)));
                }
            }else if (i==1){
                mPerTwo.setVisibility(View.VISIBLE);
                if (array[i].indexOf("str") != -1) {
                    mContentView_two.setVisibility(View.VISIBLE);
                    mPreView_two.setVisibility(View.GONE);
                    //去掉字符串 str
                    String path=array[i].replace("str","");
                    mContentView_two.setText(path);
                    interceptHyperLink(mContentView_two);
                }else if (array[i].indexOf("pat") != -1){
                    mContentView_two.setVisibility(View.GONE);
                    mPreView_two.setVisibility(View.VISIBLE);
                    String path=array[i].replace("pat","");
                    mPreView_two.setImageURI(Uri.fromFile(new File(path)));
                }
            }else if (i==2){
                mPerThird.setVisibility(View.VISIBLE);
                if (array[i].indexOf("str") != -1) {
                    mContentView_third.setVisibility(View.VISIBLE);
                    mPreView_third.setVisibility(View.GONE);
                    //去掉字符串 str
                    String path=array[i].replace("str","");
                    mContentView_third.setText(path);
                    interceptHyperLink(mContentView_third);
                }else if (array[i].indexOf("pat") != -1){
                    mContentView_third.setVisibility(View.GONE);
                    mPreView_third.setVisibility(View.VISIBLE);
                    String path=array[i].replace("pat","");
                    mPreView_third.setImageURI(Uri.fromFile(new File(path)));
                }
            }else if (i==3){
                mPerFour.setVisibility(View.VISIBLE);
                if (array[i].indexOf("str") != -1) {
                    mContentView_four.setVisibility(View.VISIBLE);
                    mPreView_four.setVisibility(View.GONE);
                    //去掉字符串 str
                    String path=array[i].replace("str","");
                    mContentView_four.setText(path);
                    interceptHyperLink(mContentView_four);

                }else if (array[i].indexOf("pat") != -1){
                    mContentView_four.setVisibility(View.GONE);
                    mPreView_four.setVisibility(View.VISIBLE);
                    String path=array[i].replace("pat","");
                    mPreView_four.setImageURI(Uri.fromFile(new File(path)));
                }
            }else if (i==4){
                mPerFive.setVisibility(View.VISIBLE);
                if (array[i].indexOf("str") != -1) {
                    mContentView_five.setVisibility(View.VISIBLE);
                    mPreView_five.setVisibility(View.GONE);
                    //去掉字符串 str
                    String path=array[i].replace("str","");
                    mContentView_five.setText(path);
                    interceptHyperLink(mContentView_five);
                }else if (array[i].indexOf("pat") != -1){
                    mContentView_five.setVisibility(View.GONE);
                    mPreView_five.setVisibility(View.VISIBLE);
                    String path=array[i].replace("pat","");
                    mPreView_five.setImageURI(Uri.fromFile(new File(path)));
                }
            }
        }
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
    private void setListener(){
        mShowDraw.setOnClickListener(this);
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
