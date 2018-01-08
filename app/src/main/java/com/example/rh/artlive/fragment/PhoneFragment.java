package com.example.rh.artlive.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.rh.artlive.R;
import com.example.rh.artlive.activity.ApplyActivity;
import com.example.rh.artlive.activity.ApplyTwoActivity;
import com.example.rh.artlive.ottoEvent.AppBus;
import com.example.rh.artlive.ottoEvent.AppCityEvent;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Judge;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.ToastUtil;
import com.example.rh.artlive.util.UrlConstant;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by rh on 2017/12/4.
 */

public class PhoneFragment extends BaseFragment implements View.OnClickListener{

    private View view;

    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    private String zt;
    List<String> list;
    private ArrayList<String> list_details = new ArrayList<>();//广告的数据
    private EditText mPhoneText;
    private Button mButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savesInstanceState) {
        super.onCreateView(inflater, container, savesInstanceState);
        view = inflater.inflate(R.layout.fragment_phone, null);
        isPrepared = true;
        lazyLoad();
        return view;
    }

    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        isPrepared = false;
        init();
        setListener();
    }
    private void init(){
        mButton=(Button)view.findViewById(R.id.login_btn);
        mPhoneText=(EditText)view.findViewById(R.id.apply_phone);
    }
    private void setListener(){
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:
//                if(!Judge.isPhone(mPhoneText.getText()+"".toString().trim())){
//                    ToastUtil.showToast(getActivity(),"请输入正确的手机号");
//                }else if("".equals(mPhoneText.getText()+"".toString().trim())){
//                    ToastUtil.showToast(getActivity(),"请输入手机号");
//                }else{
//                    AppBus.getInstance().post(new AppCityEvent("1"));
//                    mSharePreferenceUtil.setString(SharedPerfenceConstant.REPHOEN,mPhoneText.getText()+"".toString().trim());
//                }
                AppBus.getInstance().post(new AppCityEvent("1"));
                mSharePreferenceUtil.setString(SharedPerfenceConstant.REPHOEN,mPhoneText.getText()+"".toString().trim());
                break;
        }
    }
}
