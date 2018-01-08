package com.example.rh.artlive.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.activity.ApplyActivity;
import com.example.rh.artlive.activity.AssetsActivity;
import com.example.rh.artlive.activity.CollectActivity;
import com.example.rh.artlive.activity.InviterActivity;
import com.example.rh.artlive.activity.OrderActivity;
import com.example.rh.artlive.activity.PhotoActivity;
import com.example.rh.artlive.activity.SettingActivity;
import com.example.rh.artlive.activity.SisterActivity;
import com.example.rh.artlive.activity.UserMainActivity;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.CircleImageView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by rh on 2017/11/16.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener{

    private View view;

    private RelativeLayout mPhotoLayout;
    private RelativeLayout mCollectLayout;
    private RelativeLayout mOrderLayout;
    private RelativeLayout mAssetsLayout;
    private RelativeLayout mSisterLayout;
    private RelativeLayout mApplyLayout;
    private RelativeLayout mInviterLayout;

    private LinearLayout mUserLayout;

    private ImageView mSetting;

    private CircleImageView mUserImage;

    private TextView mUserName;
    private TextView mUserSex;
    private TextView mUserSchool;

    private String mUser_Id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savesInstanceState) {
        super.onCreateView(inflater, container, savesInstanceState);
        view = inflater.inflate(R.layout.fragment_user, null);
        init();
        setData();
        setListener();
        return view;
    }
    private void init(){
        mPhotoLayout=(RelativeLayout)view.findViewById(R.id.me_item_unpaid_layout);
        mCollectLayout=(RelativeLayout)view.findViewById(R.id.me_item_lottery_layout);
        mOrderLayout=(RelativeLayout)view.findViewById(R.id.me_item_treasure_layout);
        mAssetsLayout=(RelativeLayout)view.findViewById(R.id.me_item_bankcard_layout);
        mSisterLayout=(RelativeLayout)view.findViewById(R.id.me_item_coupons_layout);
        mApplyLayout=(RelativeLayout)view.findViewById(R.id.promotion);
        mInviterLayout=(RelativeLayout)view.findViewById(R.id.me_item_qr_layout);

        mUserLayout=(LinearLayout)view.findViewById(R.id.me_nologin_layout);

        mSetting=(ImageView)view.findViewById(R.id.showTime);
        mUserImage=(CircleImageView)view.findViewById(R.id.roundImageView);

        mUserName=(TextView)view.findViewById(R.id.user);
        mUserSex=(TextView)view.findViewById(R.id.sex);
        mUserSchool=(TextView)view.findViewById(R.id.user_school);
    }
    private void setData(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        HttpUtil.postHttpRequstProgess(getActivity(), "正在加载", UrlConstant.MINE, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("我的数据"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            mUserName.setText(data.getString("user_nickname"));
                            mUserSex.setText(data.getString("user_sex"));
                            mUserSchool.setText(data.getString("user_school"));
                            mUser_Id=data.getString("user_id");
                            Glide.with(getActivity()).load(data.getString("user_img")).into(mUserImage);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "collect", null);
    }
    private void setListener(){
        mPhotoLayout.setOnClickListener(this);
        mCollectLayout.setOnClickListener(this);
        mOrderLayout.setOnClickListener(this);
        mAssetsLayout.setOnClickListener(this);
        mSisterLayout.setOnClickListener(this);
        mApplyLayout.setOnClickListener(this);
        mInviterLayout.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mUserLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.me_item_unpaid_layout:
                Intent intent=new Intent(getActivity(), PhotoActivity.class);
                startActivity(intent);
                break;
            case R.id.me_item_lottery_layout:
                Intent intent1=new Intent(getActivity(), CollectActivity.class);
                startActivity(intent1);
                break;
            case R.id.me_item_treasure_layout:
                Intent intent2=new Intent(getActivity(), OrderActivity.class);
                startActivity(intent2);
                break;
            case R.id.me_item_bankcard_layout:
                Intent intent3=new Intent(getActivity(), AssetsActivity.class);
                startActivity(intent3);
                break;
            case R.id.me_item_coupons_layout:
                Intent intent4=new Intent(getActivity(), SisterActivity.class);
                startActivity(intent4);
                break;
            case R.id.promotion:
                Intent intent5=new Intent(getActivity(), ApplyActivity.class);
                startActivity(intent5);
                break;
            case R.id.me_item_qr_layout:
                Intent intent6=new Intent(getActivity(), InviterActivity.class);
                startActivity(intent6);
                break;
            case R.id.me_nologin_layout:
                Intent intent7=new Intent(getActivity(), UserMainActivity.class);
                startActivity(intent7);
                break;
            case R.id.showTime:
                Intent intent8=new Intent(getActivity(), SettingActivity.class);
                startActivity(intent8);
                break;

        }
    }
}
