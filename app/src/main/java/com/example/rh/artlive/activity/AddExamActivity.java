package com.example.rh.artlive.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.bean.SchoolBean;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.ToastUtil;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.PullToRefreshLayout;
import com.example.rh.artlive.widget.CustomDatePicker;
import com.squareup.haha.trove.THash;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.Call;

/**
 * Created by rh on 2017/12/23.
 * 添加考试行程
 */

public class AddExamActivity extends BaseFragmentActivity implements View.OnClickListener{

    private ImageView mShowDraw;

    private TextView mDateView;
    private TextView mTimeView;
    private TextView mAreaView;
    private TextView mSaveView;
    private EditText mText;

    private RelativeLayout mSchool;
    private RelativeLayout mDate;
    private RelativeLayout mTime;
    private RelativeLayout mOneDay;

    private CustomDatePicker customDatePicker1;


    private int hour;
    private int minute;
    private String time1;
    private String time2;
    private String dateStr;
    private AlertDialog dialog;
    private String mSaveTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam);
        init();
        setListener();
        setData();
        initDatePicker();
    }
    private void init(){
        mSchool=(RelativeLayout)findViewById(R.id.apply_two);
        mDate=(RelativeLayout)findViewById(R.id.date);
        mTime=(RelativeLayout)findViewById(R.id.time);
        mSaveView=(TextView)findViewById(R.id.editor);
        mAreaView=(TextView)findViewById(R.id.apply_area);
        mOneDay=(RelativeLayout)findViewById(R.id.one_day);
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mDateView=(TextView)findViewById(R.id.apply_date);
        mTimeView=(TextView)findViewById(R.id.apply_time);
        mText=(EditText)findViewById(R.id.edittext);
        mAreaView.setText(mSharePreferenceUtil.getString(SharedPerfenceConstant.APPLY_SCHOOL)+"/"+mSharePreferenceUtil.getString(SharedPerfenceConstant.APPLY_MAJOR)+"/"+mSharePreferenceUtil.getString(SharedPerfenceConstant.APPLY_CENTER));
        Log.e("选择"+mSharePreferenceUtil.getString(SharedPerfenceConstant.APPLY_SCHOOL)+"/"+mSharePreferenceUtil.getString(SharedPerfenceConstant.APPLY_MAJOR+"/"+mSharePreferenceUtil.getString(SharedPerfenceConstant.APPLY_CENTER)));
    }


    private void setListener(){
        mSchool.setOnClickListener(this);
        mDate.setOnClickListener(this);
        mTime.setOnClickListener(this);
        mOneDay.setOnClickListener(this);
        mShowDraw.setOnClickListener(this);
        mSaveView.setOnClickListener(this);
    }
    private  void setData(){

    }


    //时间选择器
    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        mDateView.setText(now.split(" ")[0]);
        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                mDateView.setText(time);
                mSaveTime=String.valueOf(getTimeStamp(time));
                Log.e("时间"+getTimeStamp(time));
                //截取字符串后四位
                String b=time.substring(time.length()-5,time.length());
                mTimeView.setText(b);
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(true); // 不显示时和分
        customDatePicker1.setIsLoop(true); // 不允许循环滚动
    }


    /**
     * 掉此方法输入所要转换的时间输入例如（"2014年06月14日16时09分00秒"）返回时间戳
     *
     * @param time
     * @return
     */
    public String data(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * 保存
     */
    private void setSave(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("type","add");
        map.put("remindtime",mSaveTime);
        map.put("time",mSaveTime);
        map.put("content",mText.getText()+"".toString().trim());
        map.put("school_name",mSharePreferenceUtil.getString(SharedPerfenceConstant.APPLY_SCHOOL));
        map.put("major",mSharePreferenceUtil.getString(SharedPerfenceConstant.APPLY_MAJOR));
        map.put("test",mSharePreferenceUtil.getString(SharedPerfenceConstant.APPLY_CENTER));
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.ADD_EXAM, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("院校"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.has("data")) {
                        if ("1".equals(jsonObject.getString("state"))) {
                            ToastUtil.showToast(AddExamActivity.this,"保存成功");
                            Intent intent=new Intent(AddExamActivity.this,ExamActivity.class);
                            startActivity(intent);
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
            case R.id.apply_two:
                Intent intent=new Intent(AddExamActivity.this,ApplySchoolActivity.class);
                startActivity(intent);
                break;
            case R.id.date:
                // 日期格式为yyyy-MM-dd
                customDatePicker1.show(mDateView.getText().toString());
                break;
            case R.id.time:
                break;
            case R.id.one_day:

                break;
            case R.id.showDraw:
                finish();
                break;
            case R.id.editor:
                setSave();
                break;
        }
    }
}
