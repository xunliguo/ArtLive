package com.example.rh.artlive.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.rh.artlive.R;
import com.example.rh.artlive.ottoEvent.AppBus;
import com.example.rh.artlive.ottoEvent.AppCityEvent;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.ToastUtil;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.widget.CountDownTimer;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

import static com.example.rh.artlive.R.id.phone;
import static com.tencent.open.utils.ThreadManager.init;

/**
 * Created by rh on 2017/12/4.
 * 15231115232
 */

public class CodeFragment extends BaseFragment implements View.OnClickListener{

    private View view;

    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    private Button mCodePhone;//手机验证码
    private Button mButton;
    private EditText mCodeText;
    private String mCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savesInstanceState) {
        super.onCreateView(inflater, container, savesInstanceState);
        view = inflater.inflate(R.layout.fragment_code, null);
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
    private void  init(){
        mButton=(Button)view.findViewById(R.id.login_btn);
        mCodeText=(EditText)view.findViewById(R.id.apply_code);
        mCodePhone=(Button)view.findViewById(R.id.login_code);
        mCodePhone.setEnabled(false);
        timer.start();
        getCodePhone();
    }
    private void setListener(){
        mButton.setOnClickListener(this);
    }


    /**
     * 获取验证码
     */
    private void getCodePhone(){

        HashMap<String,String> map=new HashMap<>();
        map.put("tel",mSharePreferenceUtil.getString(SharedPerfenceConstant.REPHOEN));
        HttpUtil.postHttpRequstProgess(getActivity(), "正在加载", UrlConstant.CODE, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                Log.e("获取验证码"+response);
                JSONObject obj;
                try {
                    obj=new JSONObject(response);
                    if("1".equals(obj.getString("state"))){
                        ToastUtil.showToast(getActivity(),"发送成功");
                        JSONObject data=obj.getJSONObject("data");
                        mCode=data.getString("yzm");
//                        mCodeText.setText(mCode);
                    }else{
                        ToastUtil.showToast(getActivity(),"发送失败请重新尝试");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "registercode", null);
    }

    final CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            mCodePhone.setText(millisUntilFinished/1000 + "秒");
        }

        @Override
        public void onFinish() {
            mCodePhone.setEnabled(true);
            mCodePhone.setText("获取验证码");
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:
                if ("".equals(mCodeText.getText()+"".toString().trim())){
                    ToastUtil.showToast(getActivity(),"请输入验证码");
                }else{
                    mSharePreferenceUtil.setString(SharedPerfenceConstant.RECODE,mCode);
                    AppBus.getInstance().post(new AppCityEvent("2"));
                }
                break;

        }
    }
}
