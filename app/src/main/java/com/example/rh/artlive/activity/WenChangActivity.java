package com.example.rh.artlive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.articlechoice.QuestionBean;
import com.example.rh.artlive.articlechoice.QuestionOptionBean;
import com.example.rh.artlive.bean.CorrectBean;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.PercentProgressView;
import com.example.rh.artlive.widget.HorizontalProgressBarWithNumber;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by rh on 2017/11/20.
 */

public class WenChangActivity extends BaseFragmentActivity implements View.OnClickListener{

    private RelativeLayout mOneLayout;
    private RelativeLayout mTwoLayout;
    private RelativeLayout mThirdLayout;
    private RelativeLayout mFourLayout;
    private RelativeLayout mFiveLayout;
    private RelativeLayout mSixLayout;
    private RelativeLayout mSevenLayout;
    private RelativeLayout mEightLayout;
    private RelativeLayout mNightLayout;
    private ImageView mShowDraw;
    private HorizontalProgressBarWithNumber mWenView;
    private HorizontalProgressBarWithNumber mWenVideo;
    private HorizontalProgressBarWithNumber mWenBrodcast;
    private HorizontalProgressBarWithNumber mWenPlay;
    private HorizontalProgressBarWithNumber mWenArt;
    private HorizontalProgressBarWithNumber mWenCarm;
    private HorizontalProgressBarWithNumber mWenMusic;

    ArrayList<String> tea_list ;
    ArrayList<String> tea_list1 ;

    private TextView mEditView;
    private String mType;

    public static List<QuestionBean> questionlist ;
    public static QuestionBean question;
    public List<QuestionOptionBean> options1 ;
    public static QuestionOptionBean option;


    public static StringBuffer errorlist = new StringBuffer();//购物车id
    public static StringBuffer truelist = new StringBuffer();//购物车id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wenchang);
        init();
        setData();
        setListener();

    }
    private void init(){
        mOneLayout=(RelativeLayout)findViewById(R.id.wen_one);
        mTwoLayout=(RelativeLayout)findViewById(R.id.wen_two);
        mThirdLayout=(RelativeLayout)findViewById(R.id.wen_third);
        mFourLayout=(RelativeLayout)findViewById(R.id.wen_four);
        mFiveLayout=(RelativeLayout)findViewById(R.id.wen_five);
        mSixLayout=(RelativeLayout)findViewById(R.id.wen_six);
        mSevenLayout=(RelativeLayout)findViewById(R.id.wen_seven);
        mEightLayout=(RelativeLayout)findViewById(R.id.wen_eight);
        mNightLayout=(RelativeLayout)findViewById(R.id.wen_nine);
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mEditView=(TextView)findViewById(R.id.editor);
        mWenView = (HorizontalProgressBarWithNumber) findViewById(R.id.art_wen);
        mWenBrodcast = (HorizontalProgressBarWithNumber) findViewById(R.id.wen_brodcast);
        mWenPlay = (HorizontalProgressBarWithNumber) findViewById(R.id.wen_play);
        mWenArt = (HorizontalProgressBarWithNumber) findViewById(R.id.wen_art);
        mWenCarm = (HorizontalProgressBarWithNumber) findViewById(R.id.wen_carm);
        mWenMusic = (HorizontalProgressBarWithNumber) findViewById(R.id.wen_music);
        mWenVideo = (HorizontalProgressBarWithNumber) findViewById(R.id.wen_video);
    }
    private void  setListener(){
        mOneLayout.setOnClickListener(this);
        mTwoLayout.setOnClickListener(this);
        mThirdLayout.setOnClickListener(this);
        mFourLayout.setOnClickListener(this);
        mFiveLayout.setOnClickListener(this);
        mSixLayout.setOnClickListener(this);
        mSevenLayout.setOnClickListener(this);
        mEightLayout.setOnClickListener(this);
        mNightLayout.setOnClickListener(this);
        mShowDraw.setOnClickListener(this);
        mEditView.setOnClickListener(this);
    }
    private void setData(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.WENCHANG, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("文常"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONArray info=data.getJSONArray("info");
                            for (int i=0;i<info.length();i++){
                                JSONObject jsonObject1=info.getJSONObject(i);
                                if (i==0) {
                                    mWenBrodcast.setProgress(Integer.parseInt(jsonObject1.getString("sum")));
                                }else if (i==1){
                                    mWenPlay.setProgress(Integer.parseInt(jsonObject1.getString("sum")));
                                }else if (i==2){
                                    mWenView.setProgress(Integer.parseInt(jsonObject1.getString("sum")));
                                }else if (i==3){
                                    mWenVideo.setProgress(Integer.parseInt(jsonObject1.getString("sum")));
                                }else if (i==4){
                                    mWenMusic.setProgress(Integer.parseInt(jsonObject1.getString("sum")));
                                }else if (i==5){
                                    mWenCarm.setProgress(Integer.parseInt(jsonObject1.getString("sum")));
                                }else if (i==6){
                                    mWenArt.setProgress(Integer.parseInt(jsonObject1.getString("sum")));
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "wen", null);
    }

    /**
     * 文常里面的四个选项以及题目
     */
    private void setDataDetails(String mType){
        tea_list = new ArrayList<String>();
        tea_list1 = new ArrayList<String>();
        questionlist = new ArrayList<QuestionBean>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("type",mType);
        map.put("wrong","www");
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.WENCHANG_DETAILS, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                com.example.rh.artlive.util.Log.e("文常详情"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONArray info=data.getJSONArray("info");
                            for (int i=0;i<info.length();i++){
                                JSONObject jsonObject1=info.getJSONObject(i);
                                tea_list.add(jsonObject1.getString("exercises_name"));
                                JSONArray answer=jsonObject1.getJSONArray("answer");
                                //确保每一题是i对应的数据，所以在这里必须实例化一下potions1
                                options1 = new ArrayList<QuestionOptionBean>();
                                for (int j=0;j<answer.length();j++){
                                    JSONObject jsonObject2=answer.getJSONObject(j);
                                    tea_list1.add(jsonObject2.getString("hide"));
                                    if (j==0) {
                                        option = new QuestionOptionBean("A", jsonObject2.getString("hide"));
                                        options1.add(option);
                                    }else if (j==1){
                                        option = new QuestionOptionBean("B", jsonObject2.getString("hide"));
                                        options1.add(option);
                                    }else if (j==2){
                                        option = new QuestionOptionBean("C", jsonObject2.getString("hide"));
                                        options1.add(option);
                                    }else if (j==3){
                                        option = new QuestionOptionBean("D", jsonObject2.getString("hide"));
                                        options1.add(option);
                                    }
                                }
                                question = new QuestionBean("1", jsonObject1.getString("exercises_name"), jsonObject1.getString("exercises_id"), jsonObject1.getString("exercises_correct"), jsonObject1.getString("exercises_analysis"), options1);
                                questionlist.add(question);
                            }
                            Intent intent=new Intent(WenChangActivity.this,WHTrainActivity.class);
                            intent.putStringArrayListExtra("tea_list",tea_list);
                            intent.putStringArrayListExtra("tea_list1",tea_list1);
                            startActivity(intent);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "wen", null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wen_one:
                mType="文学";
                setDataDetails(mType);
                break;
            case R.id.wen_two:
                mType="电影";
                setDataDetails(mType);
                break;
            case R.id.wen_third:
                mType="广播电视";
                setDataDetails(mType);
                break;
            case R.id.wen_four:
                mType="戏剧戏曲";
                setDataDetails(mType);
                break;
            case R.id.wen_five:
                mType="美术";
                setDataDetails(mType);
                break;
            case R.id.wen_six:
                mType="摄影";
                setDataDetails(mType);
                break;
            case R.id.wen_seven:
                mType="音乐舞蹈";
                setDataDetails(mType);
                break;
            case R.id.wen_eight:
                mType="其他";
                setDataDetails(mType);
                break;
            case R.id.wen_nine:
                mType="全部";
                setDataDetails(mType);
                break;

            case R.id.showDraw:
                finish();
                break;
            case R.id.editor:
                Intent intent=new Intent(WenChangActivity.this,WenChangErrorActivity.class);
                startActivity(intent);
                break;
        }
    }
}
