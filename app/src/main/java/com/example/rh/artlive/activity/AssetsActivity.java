package com.example.rh.artlive.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.live.DemoActivity;
import com.example.rh.artlive.util.Constants;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.ToastUtil;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.CircleImageView;
import com.example.rh.artlive.view.PullToRefreshLayout;
import com.example.rh.artlive.widget.PayResult;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by rh on 2017/11/22.
 * 我的的资产
 */

public class AssetsActivity extends BaseFragmentActivity implements View.OnClickListener{
    private static final int SDK_PAY_FLAG = 1;


    private ImageView mShowDraw;
    private TextView mNameView;
    private TextView mMoneyView;
    private CircleImageView mUserView;

    private TextView mArt_Money;
    private TextView mArt_Money1;
    private TextView mArt_Money2;
    private TextView mArt_Money3;
    private TextView mArt_Money4;
    private TextView mArt_Money5;

    private TextView mArt_Rmb;
    private TextView mArt_Rmb1;
    private TextView mArt_Rmb2;
    private TextView mArt_Rmb3;
    private TextView mArt_Rmb4;
    private TextView mArt_Rmb5;

    private RelativeLayout mLeftLayout;
    private RelativeLayout mModdleLayout;
    private RelativeLayout mRightLayout;
    private RelativeLayout mLeft1Layout;
    private RelativeLayout mModdle1Layout;
    private RelativeLayout mRight1Layout;

    private RelativeLayout mAplyLayout;
    private RelativeLayout mWeChatLayout;
    private TextView mAplyView;
    private TextView mWeChatView;

    private Dialog datePickerDialog;
    private Button mChangeButton;
    private Button mWithButton;

    private RelativeLayout mMoreLayout;
    private TextView mMoreView;

    private int selected_id=0;
    private int selected_type=0;//区分支付宝、微信
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets);
        api = WXAPIFactory.createWXAPI(AssetsActivity.this,null);
        api.registerApp(Constants.APP_ID);
        init();
        setData();
        setListener();
    }

    private void init(){
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mNameView=(TextView)findViewById(R.id.user);
        mMoneyView=(TextView)findViewById(R.id.sex);
        mUserView=(CircleImageView) findViewById(R.id.roundImageView);

        mLeftLayout=(RelativeLayout)findViewById(R.id.left);
        mModdleLayout=(RelativeLayout)findViewById(R.id.moddle);
        mRightLayout=(RelativeLayout)findViewById(R.id.right);
        mLeft1Layout=(RelativeLayout)findViewById(R.id.left1);
        mModdle1Layout=(RelativeLayout)findViewById(R.id.moddle1);
        mRight1Layout=(RelativeLayout)findViewById(R.id.right1);

        mArt_Money=(TextView)findViewById(R.id.art_money);
        mArt_Money1=(TextView)findViewById(R.id.art_money1);
        mArt_Money2=(TextView)findViewById(R.id.art_money2);
        mArt_Money3=(TextView)findViewById(R.id.art_money3);
        mArt_Money4=(TextView)findViewById(R.id.art_money4);
        mArt_Money5=(TextView)findViewById(R.id.art_money5);

        mArt_Rmb=(TextView)findViewById(R.id.art_rmb);
        mArt_Rmb1=(TextView)findViewById(R.id.art_rmb1);
        mArt_Rmb2=(TextView)findViewById(R.id.art_rmb2);
        mArt_Rmb3=(TextView)findViewById(R.id.art_rmb3);
        mArt_Rmb4=(TextView)findViewById(R.id.art_rmb4);
        mArt_Rmb5=(TextView)findViewById(R.id.art_rmb5);

        mAplyLayout=(RelativeLayout)findViewById(R.id.ass_aply);
        mWeChatLayout=(RelativeLayout)findViewById(R.id.ass_wechat);
        mAplyView=(TextView)findViewById(R.id.aply_view);
        mWeChatView=(TextView)findViewById(R.id.aply_wechat);

        mChangeButton=(Button)findViewById(R.id.recharge);
        mWithButton=(Button)findViewById(R.id.withdrawals);
        mMoreLayout=(RelativeLayout)findViewById(R.id.art_more);
        mMoreView=(TextView)findViewById(R.id.art_more_view);

    }

    private void setListener(){
        mShowDraw.setOnClickListener(this);
        mLeftLayout.setOnClickListener(this);
        mModdleLayout.setOnClickListener(this);
        mRightLayout.setOnClickListener(this);
        mLeft1Layout.setOnClickListener(this);
        mModdle1Layout.setOnClickListener(this);
        mRight1Layout.setOnClickListener(this);
        mAplyLayout.setOnClickListener(this);
        mWeChatLayout.setOnClickListener(this);

        mChangeButton.setOnClickListener(this);
        mWithButton.setOnClickListener(this);
    }

    private void setData(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("userid",mSharePreferenceUtil.getString(SharedPerfenceConstant.USER_ID));
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.ASSETS, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("资产"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            mNameView.setText("账户："+data.getString("user_nickname"));
                            mMoneyView.setText("余额："+data.getString("user_jine"));
                            Glide.with(AssetsActivity.this).load(data.getString("user_img")).into(mUserView);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "collect", null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showDraw:
                finish();
                break;
            case R.id.left:
                selected_id=1;
                mLeftLayout.setBackgroundResource(R.drawable.button_unlogin);
                mArt_Money.setTextColor(Color.parseColor("#ffffff"));
                mArt_Rmb.setTextColor(Color.parseColor("#ffffff"));

                mModdleLayout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money1.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb1.setTextColor(Color.parseColor("#666666"));

                mRightLayout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money2.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb2.setTextColor(Color.parseColor("#666666"));

                mLeft1Layout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money3.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb3.setTextColor(Color.parseColor("#666666"));

                mModdle1Layout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money4.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb4.setTextColor(Color.parseColor("#666666"));

                mRight1Layout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money5.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb5.setTextColor(Color.parseColor("#666666"));

                break;
            case R.id.moddle:
                selected_id=2;
                mLeftLayout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb.setTextColor(Color.parseColor("#666666"));

                mModdleLayout.setBackgroundResource(R.drawable.button_unlogin);
                mArt_Money1.setTextColor(Color.parseColor("#ffffff"));
                mArt_Rmb1.setTextColor(Color.parseColor("#ffffff"));

                mRightLayout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money2.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb2.setTextColor(Color.parseColor("#666666"));

                mLeft1Layout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money3.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb3.setTextColor(Color.parseColor("#666666"));

                mModdle1Layout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money4.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb4.setTextColor(Color.parseColor("#666666"));

                mRight1Layout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money5.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb5.setTextColor(Color.parseColor("#666666"));
                break;
            case R.id.right:
                selected_id=3;
                mLeftLayout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb.setTextColor(Color.parseColor("#666666"));

                mModdleLayout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money1.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb1.setTextColor(Color.parseColor("#666666"));

                mRightLayout.setBackgroundResource(R.drawable.button_unlogin);
                mArt_Money2.setTextColor(Color.parseColor("#ffffff"));
                mArt_Rmb2.setTextColor(Color.parseColor("#ffffff"));

                mLeft1Layout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money3.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb3.setTextColor(Color.parseColor("#000000"));

                mModdle1Layout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money4.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb4.setTextColor(Color.parseColor("#666666"));

                mRight1Layout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money5.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb5.setTextColor(Color.parseColor("#666666"));
                break;
            case R.id.left1:
                selected_id=4;
                mLeftLayout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb.setTextColor(Color.parseColor("#666666"));

                mModdleLayout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money1.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb1.setTextColor(Color.parseColor("#666666"));

                mRightLayout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money2.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb2.setTextColor(Color.parseColor("#666666"));

                mLeft1Layout.setBackgroundResource(R.drawable.button_unlogin);
                mArt_Money3.setTextColor(Color.parseColor("#ffffff"));
                mArt_Rmb3.setTextColor(Color.parseColor("#ffffff"));

                mModdle1Layout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money4.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb4.setTextColor(Color.parseColor("#666666"));

                mRight1Layout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money5.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb5.setTextColor(Color.parseColor("#666666"));
                break;
            case R.id.moddle1:
                selected_id=5;
                mLeftLayout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb.setTextColor(Color.parseColor("#666666"));

                mModdleLayout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money1.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb1.setTextColor(Color.parseColor("#666666"));

                mRightLayout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money2.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb2.setTextColor(Color.parseColor("#666666"));

                mLeft1Layout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money3.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb3.setTextColor(Color.parseColor("#666666"));

                mModdle1Layout.setBackgroundResource(R.drawable.button_unlogin);
                mArt_Money4.setTextColor(Color.parseColor("#ffffff"));
                mArt_Rmb4.setTextColor(Color.parseColor("#ffffff"));

                mRight1Layout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money5.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb5.setTextColor(Color.parseColor("#666666"));
                break;
            case R.id.right1:
                initDialog();
                break;
            case R.id.ass_aply:
                selected_type=1;
                mAplyLayout.setBackgroundResource(R.drawable.button_unlogin);
                mWeChatLayout.setBackgroundResource(R.drawable.layout_biankuang);
                mAplyView.setTextColor(Color.parseColor("#ffffff"));
                mWeChatView.setTextColor(Color.parseColor("#666666"));
                break;
            case R.id.ass_wechat:
                selected_type=2;
                mAplyLayout.setBackgroundResource(R.drawable.layout_biankuang);
                mWeChatLayout.setBackgroundResource(R.drawable.button_unlogin);
                mAplyView.setTextColor(Color.parseColor("#666666"));
                mWeChatView.setTextColor(Color.parseColor("#ffffff"));
                break;
            case R.id.recharge:
                if (selected_id==0){
                    ToastUtil.showToast(AssetsActivity.this,"请选择交易面额");
                }else if (selected_type==0){
                    ToastUtil.showToast(AssetsActivity.this,"请选择支付方式");
                }else{
                    if (selected_type==1) {
                        initAply();
                    }else{
                        initWechat();
                    }
                }
                break;
            case R.id.withdrawals:
                break;
        }
    }

    /**
     * 支付宝
     */
    private void initAply(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("type","充值");
        if (selected_id==1){
            map.put("price","1");
            map.put("title","1");
        }else if (selected_id==2){
            map.put("price","10");
            map.put("title","10");
        }else if (selected_id==3){
            map.put("price","30");
            map.put("title","30");
        }else if (selected_id==4){
            map.put("price","50");
            map.put("title","50");
        }else if (selected_id==5){
            map.put("price","10");
            map.put("title","100");
        }else if (selected_id==6){
            map.put("price",mArt_Money5.getText()+"".toString().trim());
            map.put("title",mArt_Rmb5.getText()+"".toString().trim());
        }
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.APLIPAY, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("支付宝支付"+response);
                mSharePreferenceUtil.setString(SharedPerfenceConstant.KEY,response);
                new AliPayThread().start();
            }
        }, "apliapy", null);
    }

    /**
     * 微信
     */
    private void initWechat(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("trade_type","APP");
        if (selected_id==1){
            map.put("total_fee","1");
        }else if (selected_id==2){
            map.put("total_fee","10");
        }else if (selected_id==3){
            map.put("total_fee","30");
        }else if (selected_id==4){
            map.put("total_fee","50");
        }else if (selected_id==5){
            map.put("total_fee","100");
        }else if (selected_id==6){
            map.put("total_fee",mArt_Money5.getText()+"".toString().trim());
        }
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.WECHAT, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("微信数据" + response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    PayReq req = new PayReq();
                    req.appId = jsonObject.getString("appid");
                    req.partnerId = jsonObject.getString("partnerid");
                    req.prepayId = jsonObject.getString("prepayid");
                    req.packageValue = jsonObject.getString("package");
                    req.nonceStr = jsonObject.getString("noncestr");
                    req.timeStamp = jsonObject.getString("timestamp");
                    req.sign = jsonObject.getString("sign");
                    api.sendReq(req);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "wecaht", null);
    }

    /**
     * 支付宝支付异步任务
     *
     * @author Simon
     */
    private class AliPayThread extends Thread {
        @Override
        public void run() {
            PayTask alipay = new PayTask(AssetsActivity.this);
            Log.e("key得知"+mSharePreferenceUtil.getString(SharedPerfenceConstant.KEY));
            String result = alipay.pay(mSharePreferenceUtil.getString(SharedPerfenceConstant.KEY),true);
            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
            finish();
        }
    }

    private Handler mHandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        ToastUtil.showToast(AssetsActivity.this,"支付成功");
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtil.showToast(AssetsActivity.this,"支付结果确认中");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            ToastUtil.showToast(AssetsActivity.this,"支付失败");
                        }
                    }
                    break;
                }
            }
        };
    };

    /**
     * 添加直播选择
     */
    private void initDialog() {
        datePickerDialog = new Dialog(this, R.style.time_dialog);
        datePickerDialog.setCancelable(false);
        datePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        datePickerDialog.setContentView(R.layout.assets_money);
        Window window = datePickerDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager manager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = dm.widthPixels;
        window.setAttributes(lp);
        datePickerDialog.show();

        TextView cancel=datePickerDialog.findViewById(R.id.tv_cancle);
        final TextView sum=datePickerDialog.findViewById(R.id.tv_select);
        TextView selected_class=datePickerDialog.findViewById(R.id.selected_class);
        final EditText editText=datePickerDialog.findViewById(R.id.phone);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.dismiss();
            }
        });
        sum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoreLayout.setVisibility(View.VISIBLE);
                mMoreView.setVisibility(View.GONE);
                selected_id=6;
                mArt_Money5.setText(editText.getText()+"".toString().trim()+"虫币");
                mArt_Rmb5.setText(editText.getText()+"".toString().trim()+"元");

                mLeftLayout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb.setTextColor(Color.parseColor("#50666666"));

                mModdleLayout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money1.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb1.setTextColor(Color.parseColor("#50666666"));

                mRightLayout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money2.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb2.setTextColor(Color.parseColor("#50666666"));

                mLeft1Layout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money3.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb3.setTextColor(Color.parseColor("#50666666"));

                mModdle1Layout.setBackgroundResource(R.drawable.layout_biankuang);
                mArt_Money4.setTextColor(Color.parseColor("#000000"));
                mArt_Rmb4.setTextColor(Color.parseColor("#50666666"));

                mRight1Layout.setBackgroundResource(R.drawable.button_unlogin);
                mArt_Money5.setTextColor(Color.parseColor("#ffffff"));
                mArt_Rmb5.setTextColor(Color.parseColor("#ffffff"));
                datePickerDialog.dismiss();

            }
        });
    }
}
