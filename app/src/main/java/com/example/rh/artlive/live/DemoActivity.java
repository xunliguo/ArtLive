package com.example.rh.artlive.live;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.livecloud.live.AlivcMediaFormat;
import com.example.rh.artlive.R;
import com.example.rh.artlive.activity.BaseFragmentActivity;
import com.example.rh.artlive.activity.TeacherActivity;
import com.example.rh.artlive.util.*;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import okhttp3.Call;

/**
 * 直播页面
 */

public class DemoActivity extends BaseFragmentActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{
    private static final String MENU_DATA_KEY = "name";
    private int supplierMenuIndex = 0;



//    private EditText urlET;
//    private Button connectBT;
    private RadioGroup resolutionCB;
    private RadioButton resolution240button;
    private RadioButton resolution360button;
    private RadioButton resolution480button;
    private RadioButton resolution540button;
    private RadioButton resolution720button;
    private RadioButton resolution1080button;
    private RadioGroup rotationGroup;
    private RadioButton screenOrientation1;
    private RadioButton screenOrientation2;
    private CheckBox frontCameraMirror;
    private EditText mEtMaxBitrate;
    private EditText mEtMinBitrate;
    private EditText mEtBestBitrate;
    private EditText mEtInitialBitrate;
    private EditText mEtFrameRate;

    private EditText watermarkET;
    private EditText dxET;
    private EditText dyET;
    private EditText siteET;

    private Dialog datePickerDialog;
    private List<Map<String, String>> mMenuData1 = new ArrayList<>();; //默认

    private ListView mPopListView;
    private PopupWindow mPopupWindow;
    private SimpleAdapter mMenuAdapter1;
    List<String> tea_list ;
    private TextView mShop;
    private Button mSign;
    private RelativeLayout mLayout;
    private CheckBox cb;
    private CheckBox cb1;
    private EditText mName;
    private String mClass="ss";
    private String mRtmp;
    private TextView mSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity);
        init();
        setListener();
        getTeacherClass();
//        connectBT = (Button) findViewById(R.id.connectBT);
//        connectBT.setOnClickListener(this);
//        urlET = (EditText) findViewById(R.id.rtmpUrl);
        resolutionCB = (RadioGroup) findViewById(R.id.resolution_group);
        resolution240button = (RadioButton) findViewById(R.id.radiobutton0);
        resolution360button = (RadioButton) findViewById(R.id.radiobutton1);
        resolution480button = (RadioButton) findViewById(R.id.radiobutton2);
        resolution540button = (RadioButton) findViewById(R.id.radiobutton3);
        resolution720button = (RadioButton) findViewById(R.id.radiobutton4);
        resolution1080button= (RadioButton) findViewById(R.id.radiobutton5);
        rotationGroup =(RadioGroup)findViewById(R.id.rotation_group);
        screenOrientation1 = (RadioButton) findViewById(R.id.screenOrientation1);
        screenOrientation2 = (RadioButton) findViewById(R.id.screenOrientation2);
        frontCameraMirror = (CheckBox) findViewById(R.id.front_camera_mirror);
        resolutionCB.setOnCheckedChangeListener(this);
        rotationGroup.setOnCheckedChangeListener(this);
        mEtBestBitrate = (EditText) findViewById(R.id.et_best_bitrate);
        mEtMaxBitrate = (EditText) findViewById(R.id.et_max_bitrate);
        mEtMinBitrate = (EditText) findViewById(R.id.et_min_bitrate);
        mEtInitialBitrate = (EditText) findViewById(R.id.et_init_bitrate);
        mEtFrameRate = (EditText) findViewById(R.id.et_frame_rate);


        watermarkET = (EditText) findViewById(R.id.watermark_path);
        dxET = (EditText) findViewById(R.id.dx);
        dyET = (EditText) findViewById(R.id.dy);
        siteET = (EditText) findViewById(R.id.site);
        mShop=(TextView)findViewById(R.id.shop);

        initPopMenu();


    }

    private void init(){
        mSign=(Button)findViewById(R.id.login_btn);
        mLayout=(RelativeLayout) findViewById(R.id.layout2);
        cb=(CheckBox)findViewById(R.id.cb);
        cb1=(CheckBox)findViewById(R.id.cb1);
        mName=(EditText)findViewById(R.id.phone);
        mSelected=(TextView)findViewById(R.id.selected);

    }
    private void setListener(){
        mSign.setOnClickListener(this);
        mLayout.setOnClickListener(this);
        cb.setOnClickListener(this);
        cb1.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }

    private void startLive(final String mRtmpUrl){

        Log.e("推流"+mRtmpUrl);

        final UserInfo user = FakeServer.getLoginUser("13", "123");



        int videoResolution = 0;
        int cameraFrontFacing = 0;
        boolean screenOrientation = false;
        if(resolution240button.isChecked()){
            videoResolution = AlivcMediaFormat.OUTPUT_RESOLUTION_240P;
        }else if (resolution360button.isChecked()) {
            videoResolution = AlivcMediaFormat.OUTPUT_RESOLUTION_360P;
        } else if (resolution480button.isChecked()) {
            videoResolution = AlivcMediaFormat.OUTPUT_RESOLUTION_480P;
        } else if (resolution540button.isChecked()) {
            videoResolution = AlivcMediaFormat.OUTPUT_RESOLUTION_540P;
        } else if (resolution720button.isChecked()) {
            videoResolution = AlivcMediaFormat.OUTPUT_RESOLUTION_720P;
        } else if (resolution1080button.isChecked()) {
            videoResolution = AlivcMediaFormat.OUTPUT_RESOLUTION_1080P;
        }

        if(frontCameraMirror.isChecked()){
            cameraFrontFacing = AlivcMediaFormat.CAMERA_FACING_FRONT;
        }else {
            cameraFrontFacing = AlivcMediaFormat.CAMERA_FACING_BACK;
        }

        if (screenOrientation1.isChecked()){
            screenOrientation = true;
        }else {
            screenOrientation = false;
        }

//       connectBT

        final String watermark = watermarkET.getText().toString();
        final int dx = dxET.getText().toString() == null ? 14 : Integer.parseInt(dxET.getText().toString());
        final int dy = dyET.getText().toString() == null ? 14 : Integer.parseInt(dyET.getText().toString());
        final int site = siteET.getText().toString() == null ? 1 : Integer.parseInt(siteET.getText().toString());
        int minBitrate = 500;
        int maxBitrate = 800;
        int bestBitrate = 600;
        int initBitrate = 600;
        final int frameRate = TextUtils.isEmpty(mEtFrameRate.getText().toString())?
                30: Integer.parseInt(mEtFrameRate.getText().toString());
        try{
            minBitrate = Integer.parseInt(mEtMinBitrate.getText().toString());
        }catch (NumberFormatException e) {
        }

        try{
            maxBitrate = Integer.parseInt(mEtMaxBitrate.getText().toString());
        }catch(NumberFormatException e){}

        try {
            bestBitrate = Integer.parseInt(mEtBestBitrate.getText().toString());
        }catch (NumberFormatException e){}

        try {
            initBitrate = Integer.parseInt(mEtInitialBitrate.getText().toString());
        }catch(NumberFormatException e){}

        final int finalBestBitrate = bestBitrate;
        final int finalCameraFrontFacing = cameraFrontFacing;
        final int finalVideoResolution = videoResolution;
        final boolean finalScreenOrientation = screenOrientation;
        final int finalMinBitrate = minBitrate;
        final int finalMaxBitrate = maxBitrate;
        final int finalInitBitrate = initBitrate;
        LiveKit.connect(mSharePreferenceUtil.getString(SharedPerfenceConstant.RONG_TOKEN), new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                // 检查appKey 与token是否匹配.
            }

            @Override
            public void onSuccess(String userId) {
//                        RcLog.d(TAG, "connect onSuccess");
                LiveKit.setCurrentUser(user);
                LiveCameraActivity.RequestBuilder builder = new LiveCameraActivity.RequestBuilder()
                        .bestBitrate(finalBestBitrate)
                        .cameraFacing(finalCameraFrontFacing)
                        .dx(dx)
                        .dy(dy)
                        .site(site)
                        .rtmpUrl(mRtmpUrl)
                        .videoResolution(finalVideoResolution)
                        .portrait(finalScreenOrientation)
                        .watermarkUrl(watermark)
                        .minBitrate(finalMinBitrate)
                        .maxBitrate(finalMaxBitrate)
                        .frameRate(frameRate)
                        .initBitrate(finalInitBitrate);
                LiveCameraActivity.startActivity(DemoActivity.this, builder);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                // 根据errorCode 检查原因.
            }
        });
    }


    /**
     * 专业分类
     */
    private void getTeacherClass(){
        tea_list = new ArrayList<String>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        com.example.rh.artlive.util.HttpUtil.postHttpRequstProgess(DemoActivity.this, "正在加载", UrlConstant.LIVE_CLASS, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("老师下拉"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.has("data")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        //专业
                        JSONArray major=data.getJSONArray("category");
                        for (int i = 0; i < major.length(); i++) {
                            JSONObject obj = major.getJSONObject(i);
                            tea_list.add(obj.getString("tag_name"));
                        }
                        int size=tea_list.size();
                        String[] array = (String[])tea_list.toArray(new String[size]);
                        for(int i=0;i<array.length;i++){
                            Map<String, String> map = new HashMap<>();
                            map.put(MENU_DATA_KEY, array[i]);
                            mMenuData1.add(map);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "dataleaseclass", null);
    }
    /**
     * 课程分类
     */
    private void getClassClass(){
        tea_list = new ArrayList<String>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        com.example.rh.artlive.util.HttpUtil.postHttpRequstProgess(DemoActivity.this, "正在加载", UrlConstant.LIVE_CLASS_l, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("直播课程"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.has("data")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        //专业
                        JSONArray major=data.getJSONArray("category");
                        for (int i = 0; i < major.length(); i++) {
                            JSONObject obj = major.getJSONObject(i);
                            tea_list.add(obj.getString("tag_name"));
                        }
                        int size=tea_list.size();
                        String[] array = (String[])tea_list.toArray(new String[size]);
                        for(int i=0;i<array.length;i++){
                            Map<String, String> map = new HashMap<>();
                            map.put(MENU_DATA_KEY, array[i]);
                            mMenuData1.add(map);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "dataleaseclass", null);
    }

    /**
     * 开启直播
     */
    private void setStar(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("little",mName.getText()+"".toString().trim());
        map.put("category",mClass);
        com.example.rh.artlive.util.HttpUtil.postHttpRequstProgess(DemoActivity.this, "正在加载", UrlConstant.START_LIVE, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("开启直播"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.has("data")) {
                        JSONObject data=jsonObject.getJSONObject("data");
                        mRtmp=data.getString("push");
                        mSharePreferenceUtil.setString(SharedPerfenceConstant.CHART_ID,data.getString("roomid"));
                        startLive(mRtmp);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "dataleaseclass", null);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()){
            case R.id.login_btn:
                if ("".equals(mName.getText()+"".toString().trim())){
                    ToastUtil.showToast(this,"请输入标题");
                }else {
                    setStar();
                }
                break;
            case R.id.layout2:
                mPopListView.setAdapter(mMenuAdapter1);
                Log.e("数据"+mMenuAdapter1);
                mPopupWindow.showAsDropDown(mShop, 0, 2);
                supplierMenuIndex = 0;
                break;
            case R.id.cb:
                cb1.setChecked(false);
                getTeacherClass();
                break;
            case R.id.cb1:
                cb.setChecked(false);
                getClassClass();
                break;
        }
    }


    /**
     * popu文件
     */
    private void initPopMenu() {
        View popView = LayoutInflater.from(this).inflate(R.layout.layout_popwin_supplier_list, null);
        mPopListView = (ListView) popView.findViewById(R.id.popwin_list_view);
//        mPopupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        mPopupWindow = new PopupWindow(popView,(getScreenWidth() / 5) * 3, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        // 设置动画效果
//
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
//        params.alpha = 0.5f;
        this.getWindow().setAttributes(params);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setAnimationStyle(R.style.AnimationFade);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });

        popView.findViewById(R.id.popwin_bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
        /**
         * 用来显示item的标题
         */
        mMenuAdapter1 = new SimpleAdapter(this, mMenuData1, R.layout.item_popwin_list,
                new String[]{"name"}, new int[]{R.id.item_popwin_tv});
        /**
         * 获取点击item 的name
         */
        mPopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (supplierMenuIndex) {
                    case 0:
                        mPopupWindow.dismiss();
                        mClass=mMenuData1.get(i).get(MENU_DATA_KEY);
                        mSelected.setText(mClass);

                        break;
                    default:
                        break;
                }
            }
        });
    }
}
