package com.example.rh.artlive.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.ArtTeaherAdapter;
import com.example.rh.artlive.adapter.RvListener;
import com.example.rh.artlive.adapter.SchoolMainAdapter;
import com.example.rh.artlive.adapter.SortAdapter;
import com.example.rh.artlive.bean.SchoolMainBean;
import com.example.rh.artlive.bean.SortBean;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.ToastUtil;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.CircleImageView;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by rh on 2017/12/6.
 * 院校主页
 */

public class CollegesMainActivity extends BaseFragmentActivity implements View.OnClickListener{

    private ImageView mShowDraw;

    private SchoolMainBean mSortBean;
    private RecyclerView rvSort;
    private SchoolMainAdapter mSortAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private Context context;

    private TextView mSchool_Name;
    private TextView mSchool_area;
    private TextView mSchool_area_value;
    private TextView mSchool_Major;
    private TextView mSchool_Point;
    private TextView mRecordView;
    private TextView mSchoolPeople;

    private TextView mView1;
    private TextView mView2;
    private TextView mView3;
    private TextView mView4;
    private CircleImageView mSchool_Logo;

    private RelativeLayout mMajor1;
    private RelativeLayout mMajor2;
    private RelativeLayout mMajor3;
    private RelativeLayout mMajor4;

    private LinearLayout mMajorDe1;
    private LinearLayout mMajorDe2;
    private LinearLayout mMajorDe3;
    private LinearLayout mMajorDe4;

    private Button mThird;
    private Button mFour;
    private Button mTwo;
    private Button mOne;
    private Button mCollect;

    private String mSchool_Id;
    private String mSchoolname;
    private String mSchool_image;
    private ArrayList<String> list = new ArrayList<>();//广告的数据
    private static int flag=0;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colleges_main);
        context=this;
        init();
        initData();
        setListener();
    }

    private void init(){
        Intent intent=getIntent();
        mSchool_Id=intent.getStringExtra("school_id");
        mSchoolname=intent.getStringExtra("school_name");
        mSchool_image=intent.getStringExtra("image");
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mSchool_Name=(TextView)findViewById(R.id.colleges_name);
        mSchool_area=(TextView)findViewById(R.id.colleges_address);
        mSchool_area_value=(TextView)findViewById(R.id.colleges_value);
        mSchool_Major=(TextView)findViewById(R.id.colleges_class);
        mSchool_Logo=(CircleImageView)findViewById(R.id.colleges_logo);
        mSchool_Point=(TextView)findViewById(R.id.colleges_point);
        mRecordView=(TextView)findViewById(R.id.colleges_record);
        mSchoolPeople=(TextView)findViewById(R.id.colleges_count_people);

        mView1=(TextView)findViewById(R.id.colleges_hospital_content1);
        mView2=(TextView)findViewById(R.id.colleges_hospital_content2);
        mView3=(TextView)findViewById(R.id.colleges_hospital_content3);
        mView4=(TextView)findViewById(R.id.colleges_hospital_content4);

        mMajor1=(RelativeLayout)findViewById(R.id.colleges_details_video);
        mMajorDe1=(LinearLayout)findViewById(R.id.colleges_video_list);

        mThird=(Button)findViewById(R.id.login_btn_third);
        mFour=(Button)findViewById(R.id.login_btn_four);
        mTwo=(Button)findViewById(R.id.login_btn_two);
        mOne=(Button)findViewById(R.id.login_btn_one);
        mCollect=(Button)findViewById(R.id.login_btn);


        rvSort = (RecyclerView) findViewById(R.id.rv_sort);
        mLinearLayoutManager = new LinearLayoutManager(context);
        rvSort.setLayoutManager(mLinearLayoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        rvSort.addItemDecoration(decoration);
    }

    private void setListener(){
        mShowDraw.setOnClickListener(this);
        mThird.setOnClickListener(this);
        mFour.setOnClickListener(this);
        mTwo.setOnClickListener(this);
        mOne.setOnClickListener(this);
        mMajor1.setOnClickListener(this);
        mCollect.setOnClickListener(this);
    }

    private void initData( ){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("school_id",mSchool_Id);
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.COLLEAGE_DETAILS, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("院校详情数据"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {

                            JSONObject data=jsonObject.getJSONObject("data");
//                            JSONObject info=data.getJSONObject("info");
                            mSchool_Name.setText(data.getString("school_name"));
                            mSchool_area.setText(data.getString("school_area"));
                            mSchool_area_value.setText(data.getString("school_test"));
                            mSchool_Major.setText(data.getString("school_major"));
                            Glide.with(CollegesMainActivity.this).load(data.getString("school_img_url")).into(mSchool_Logo);
                            mSchool_Point.setText("文化分："+ data.getString("school_culture"));
                            mRecordView.setText("录取批次："+data.getString("school_batch"));
                            mSchoolPeople.setText("招取人数："+data.getString("school_enrollment"));
//                            mCollect.setText(data.getString("collection"));
//                            mMajorDe1.setVisibility(View.VISIBLE);
//                            String aa=jsonObject.getString("data");
//                            Gson gson = new Gson();
//                            mSortBean=gson.fromJson(aa,SchoolMainBean.class);
//                            List<SchoolMainBean.CategoryMajorArrayBean> categoryOneArray = mSortBean.getMajor();
//                            for (int i = 0; i < categoryOneArray.size(); i++) {
//                                list.add(categoryOneArray.get(i).getMajor_content());
//                            }
                            JSONArray guide=data.getJSONArray("Guide");
                            for (int i=0;i<guide.length();i++){
                                JSONObject jsonObject1=guide.getJSONObject(i);
                                if (i==0){
                                    mView1.setText(jsonObject1.getString("forum_title"));
                                }else if (i==1){
                                    mView2.setText(jsonObject1.getString("forum_title"));
                                }else if (i==2){
                                    mView3.setText(jsonObject1.getString("forum_title"));
                                }
                            }
                            JSONArray major=data.getJSONArray("major");
                            for (int i=0;i<major.length();i++){
                                JSONObject jsonObject1=major.getJSONObject(i);
                                JSONArray info=jsonObject1.getJSONArray("info");
                                for (int j=0;j<info.length();j++){
                                    JSONObject jsonObject11=info.getJSONObject(j);
                                    list.add(jsonObject11.getString("major_content"));
                                }
                            }
                            Log.e("专业详情"+list);
                            mSortAdapter = new SchoolMainAdapter(context, list, new RvListener() {
                                @Override
                                public void onItemClick(int id, int position) {
//                                    if (mSortDetailFragment != null) {
//                                    isMoved = true;
//                                        targetPosition = position;
//                                    setChecked(position, true);
//                                    }
                                }
                            });
                            rvSort.setAdapter(mSortAdapter);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "teacher", null);
    }

    /**
     * 添加收藏
     */
    private void setCollect( ){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("userid",mSharePreferenceUtil.getString(SharedPerfenceConstant.USER_ID));
        map.put("type","add");
        map.put("table","school");
        map.put("img",mSchool_image);
        map.put("title",mSchoolname);
        map.put("primary","school_id");
        map.put("table_id",mSchool_Id);
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.ADD_COLLECT, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("添加收藏"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            mCollect.setText("已收藏");
                        }
                    }else{
                        ToastUtil.showToast(CollegesMainActivity.this,"收藏失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "teacher", null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showDraw:
                finish();
                break;
            case R.id.login_btn_one:
                Intent intent3=new Intent(CollegesMainActivity.this,WishActivity.class);
                startActivity(intent3);
                break;
            case R.id.login_btn_third:
                Intent intent=new Intent(CollegesMainActivity.this,ProblemActivity.class);
                intent.putExtra("school_id",mSchool_Id);
                startActivity(intent);
                break;
            case R.id.login_btn_four:
                Intent intent1=new Intent(CollegesMainActivity.this,ExamActivity.class);
                intent1.putExtra("school_id",mSchool_Id);
                startActivity(intent1);
                break;
            case R.id.login_btn_two:
                Intent intent2=new Intent(CollegesMainActivity.this,ArtTeacherActivity.class);
                intent2.putExtra("school_name",mSchoolname);
                startActivity(intent2);
                break;
            case R.id.colleges_details_video:
                if (flag==0){
                    mMajorDe1.setVisibility(View.VISIBLE);
                    flag=1;
                }else{
                    mMajorDe1.setVisibility(View.GONE);
                    flag=0;
                }
                break;
            case R.id.login_btn:
                setCollect();
                break;
        }
    }
}
