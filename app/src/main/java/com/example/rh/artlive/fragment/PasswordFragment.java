package com.example.rh.artlive.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rh.artlive.R;
import com.example.rh.artlive.activity.LoginActivity;
import com.example.rh.artlive.activity.RegisterActivity;
import com.example.rh.artlive.application.DemoApplication;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.ToastUtil;
import com.example.rh.artlive.util.UrlConstant;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.tencent.open.web.security.SecureJsInterface;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by rh on 2017/12/4.
 */

public class PasswordFragment extends BaseFragment implements View.OnClickListener{

    private View view;

    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    private EditText mPhone;
    private EditText mPassw;
    private Button mButton;
    private String name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savesInstanceState) {
        super.onCreateView(inflater, container, savesInstanceState);
        view = inflater.inflate(R.layout.fragment_password, null);
        isPrepared = true;
        lazyLoad();
        return view;
    }

    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
         }
        isPrepared = false;
        init();
        setListener();
    }
    private void init(){
        mPhone=(EditText)view.findViewById(R.id.apply_phone);
        mPassw=(EditText)view.findViewById(R.id.apply_pass);
        mButton=(Button)view.findViewById(R.id.login_btn);
    }
    private void setListener(){
        mButton.setOnClickListener(this);
    }
    private void setData(){
        HashMap<String,String> map=new HashMap<>();
        map.put("tel",mSharePreferenceUtil.getString(SharedPerfenceConstant.REPHOEN));
        map.put("yzm",mSharePreferenceUtil.getString(SharedPerfenceConstant.RECODE));
        map.put("password",mPassw.getText()+"".toString().trim());
        Log.e("手机号"+mSharePreferenceUtil.getString(SharedPerfenceConstant.REPHOEN)+"验证码"+mSharePreferenceUtil.getString(SharedPerfenceConstant.RECODE)+"密码"+mPassw.getText()+"".toString().trim());
        HttpUtil.postHttpRequstProgess(getActivity(), "正在加载", UrlConstant.REGISTER, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                Log.e("注册"+response);
                JSONObject obj;
                try {
                    obj=new JSONObject(response);
                    Log.e("信息"+obj.getString("msg"));
                    if("1".equals(obj.getString("state"))){
                        register();
                    }else{
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "register", null);
    }

    /**
     * 环信注册账号接口,鉴于忘记密码功能，
     */
    public void register() {
			new Thread(new Runnable() {
				public void run() {
					try {
                            // 调用sdk注册方法
						EMClient.getInstance().createAccount(mSharePreferenceUtil.getString(SharedPerfenceConstant.REPHOEN), mPassw.getText()+"".toString().trim());
						getActivity().runOnUiThread(new Runnable() {
							public void run() {

								// 保存用户名
								DemoApplication.getInstance().setCurrentUserName(mSharePreferenceUtil.getString(SharedPerfenceConstant.REPHOEN));
                                ToastUtil.showToast(getActivity(),"注册成功");
                                Intent intent=new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
							}
						});
					} catch (final HyphenateException e) {
						getActivity().runOnUiThread(new Runnable() {
							public void run() {

								int errorCode=e.getErrorCode();
								if(errorCode== EMError.NETWORK_ERROR){
									Toast.makeText(getActivity(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
								}else if(errorCode == EMError.USER_ALREADY_EXIST){
									Toast.makeText(getActivity(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
								}else if(errorCode == EMError.USER_AUTHENTICATION_FAILED){
									Toast.makeText(getActivity(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
								}else if(errorCode == EMError.USER_ILLEGAL_ARGUMENT){
								    Toast.makeText(getActivity(), getResources().getString(R.string.illegal_user_name), Toast.LENGTH_SHORT).show();
								}else{
									Toast.makeText(getActivity(), getResources().getString(R.string.Registration_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
								}
							}
						});
					}
				}
			}).start();
//		}
	}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:
                name=mPhone.getText()+"".toString().trim();
                if("".equals(mPhone.getText()+"".toString().trim())){
                    ToastUtil.showToast(getActivity(),"请输入密码");
                }else if ("".equals(mPassw.getText()+"".toString().trim())){
                    ToastUtil.showToast(getActivity(),"请再次输入密码");
                }else if(!name.equals(mPassw.getText()+"".toString().trim())){
                    ToastUtil.showToast(getActivity(),"两次输入的密码不一致");
                }else{
                    setData();
//                    register();
                }
//                register();
                break;
        }
    }
}
