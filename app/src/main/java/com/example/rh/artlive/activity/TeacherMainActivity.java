package com.example.rh.artlive.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.DynamicGridUserAdapter;
import com.example.rh.artlive.photo.ImagePagerActivity;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.ToastUtil;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.CircleImageView;
import com.example.rh.artlive.view.NoScrollGridView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by rh on 2017/11/21.
 * 老师主页
 */

public class TeacherMainActivity extends BaseFragmentActivity implements View.OnClickListener{

    private String mTeacher_Id;
    private Context context;

    private TextView mTeacherName;
    private TextView mTeaMarojView;
    private TextView mHelpView;
    private TextView mPostView;
    private TextView mFansView;
    private TextView mGoodView;
    private TextView mFollowView;

    private ImageView mShowDraw;
    private ImageView mTeaHeader;
    private CircleImageView mUserImage;
    private RelativeLayout mTeaContent;
    private LinearLayout mFollowLayout;

    private LinearLayout mApply;

    private ArrayList<String> picarr1;
    private ArrayList<String> picarr2;
    private NoScrollGridView picarr;


    //静态变量，全局调用
    public static int flag = 0;
    private String type;
    private LinearLayout mChat;
    private LinearLayout mfansi;
    private String user_id;
    private LinearLayout mtiezi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);
        context=this;
        init();
        setListener();
        setData();
    }


    private void init(){
        Intent intent=getIntent();
        mTeacher_Id=intent.getStringExtra("teacher_id");
        mTeacherName=(TextView)findViewById(R.id.teacher_name);
        mTeaMarojView=(TextView)findViewById(R.id.teacher_marj);
        mHelpView=(TextView)findViewById(R.id.tea_help);
        mPostView=(TextView)findViewById(R.id.tea_poster);
        mFansView=(TextView)findViewById(R.id.tea_fans);
        mGoodView=(TextView)findViewById(R.id.tea_nice);
        mFollowView=(TextView)findViewById(R.id.follow_text);

        mShowDraw=(ImageView)findViewById(R.id.showDraw);

        mTeaHeader=(ImageView)findViewById(R.id.teacher_header);
        mUserImage=(CircleImageView)findViewById(R.id.roundImageView);
        mTeaContent=(RelativeLayout)findViewById(R.id.content);
        mFollowLayout=(LinearLayout)findViewById(R.id.bottom_left);
        mChat=(LinearLayout)findViewById(R.id.me_user_tv_cart2);
        mfansi = (LinearLayout)findViewById(R.id.fensi_teacher);
        mtiezi = (LinearLayout)findViewById(R.id.teacher_tiezi);
        mApply=(LinearLayout)findViewById(R.id.button);
        picarr=(NoScrollGridView)findViewById(R.id.gridView_picarr);

    }
    private void setListener(){
        mTeaContent.setOnClickListener(this);
        mShowDraw.setOnClickListener(this);
        mFollowLayout.setOnClickListener(this);
        mApply.setOnClickListener(this);
        mChat.setOnClickListener(this);
        mfansi.setOnClickListener(this);
        mtiezi.setOnClickListener(this);
    }
    private void setData(){
        picarr1 =new ArrayList<>();
        picarr2 =new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("teacher_id",mTeacher_Id);
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.TEACHER_DETAILS, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("老师详情数据"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONObject info=data.getJSONObject("info");
                            mTeacherName.setText(info.getString("teacher_name"));
                            mTeaMarojView.setText(info.getString("teacher_school"));
                            Glide.with(context).load(info.getString("user_background_img")).into(mTeaHeader);
                            Glide.with(context).load(info.getString("user_img")).into(mUserImage);
                            mPostView.setText(info.getString("tiezi"));
                            mHelpView.setText(info.getString("help"));
                            mFansView.setText(info.getString("fensi"));
                            mGoodView.setText(info.getString("nice"));
                            user_id = info.getString("user_id");

                            JSONArray photo=info.getJSONArray("xiangce");
                            for (int i=0;i<photo.length();i++){
                                JSONObject jsonObject1=photo.getJSONObject(i);
                                picarr2.add(jsonObject1.getString("picarr_img_url"));
                                if (i>=5){

                                }else{
                                    picarr1.add(jsonObject1.getString("picarr_img_url"));
                                }
                            }
                            int size1 = picarr1.size();
                            int size2 = picarr2.size();
                            Log.e("图片"+picarr1);
                            final String[] array1 = (String[]) picarr1.toArray(new String[size1]);
                            final String[] array2 = (String[]) picarr2.toArray(new String[size2]);
                            if (array1.length > 0) {
                                picarr.setVisibility(View.VISIBLE);
                                picarr.setAdapter(new DynamicGridUserAdapter(array1, context));
                                picarr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        imageBrower(position, array2);
                                    }
                                });
                            } else {
                                picarr.setVisibility(View.GONE);
                            }

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "teacher", null);
    }

    /**
     * 点击图片放大浏览
     * @param position
     * @param urls
     */
    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(TeacherMainActivity.this, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }

    /**
     * 关注取消关注
     */
    private void setFollow(String type){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("teacher_id",mTeacher_Id);
        map.put("type",type);
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.FOLLOW, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("关注"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            if (flag==1) {
                                ToastUtil.showToast(TeacherMainActivity.this, "关注成功");
                                mFollowView.setText("已关注");
                            }else{
                                ToastUtil.showToast(TeacherMainActivity.this, "取消关注成功");
                                mFollowView.setText("关注");
                            }
                        }
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
            case R.id.content:
                Intent intent=new Intent(TeacherMainActivity.this,ClassDetailsActivity.class);
                startActivity(intent);
                break;
            case R.id.showDraw:
                finish();
                break;
            case R.id.bottom_left:
                if (flag==0) {
                    flag=1;
                    type="关注";
                    setFollow(type);
                }else{
                    type="取关";
                    flag=0;
                    setFollow(type);
                }
                break;
            case R.id.button:
                break;
            case R.id.me_user_tv_cart2:
                Intent intent1 = new Intent(this, ChatActivity.class);
                intent1.putExtra("username", "15996585266");
                startActivity(intent1);
                break;
            case R.id.fensi_teacher:
                Intent intent2 = new Intent(this, FansActivity.class);
                intent2.putExtra("user_id", user_id);
                startActivity(intent2);
                break;
            case R.id.teacher_tiezi:
                Toast.makeText(context, "ssaddadaadadadfa"+user_id, Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(this, UserPostActivity.class);
                intent3.putExtra("user_id", user_id);
                startActivity(intent3);
                break;
        }
    }
}
