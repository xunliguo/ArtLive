package com.example.rh.artlive.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.ToastUtil;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.PullToRefreshLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by rh on 2017/11/22.
 */

public class ApplyActivity extends BaseFragmentActivity implements View.OnClickListener{
    private static final String MENU_DATA_KEY = "name";
    private int supplierMenuIndex = 0;

    private ImageView mShowDraw;
    private TextView mShop;
    private TextView mAddressCity;
    private TextView mStatusView;
    private TextView mTrainView;
    private TextView mIdentView;

    private EditText mApplyText;
    private EditText mApplySchool;
    private EditText mApplyMarj;

    private List<Map<String, String>> mMenuData1 = new ArrayList<>();; //默认
    private List<Map<String, String>> mMenuData2 = new ArrayList<>();; //品牌
    private List<Map<String, String>> mMenuData3 = new ArrayList<>();; //种类
    private List<Map<String, String>> mMenuData4 = new ArrayList<>();; //种类

    private ListView mPopListView;
    private PopupWindow mPopupWindow;
    private SimpleAdapter mMenuAdapter1;
    private SimpleAdapter mMenuAdapter2;
    private SimpleAdapter mMenuAdapter3;
    private SimpleAdapter mMenuAdapter4;

    List<String> tea_list ;
    List<String> tea_list1 ;
    List<String> tea_list2 ;
    List<String> tea_list3 ;

    private RelativeLayout mCityLayout;
    private RelativeLayout mStatusLayout;
    private RelativeLayout mEduLayout;
    private RelativeLayout mIdentLayout;
    private RelativeLayout mSetupLayout;

    private String mArea;
    private String mStatus;
    private String mOpen;
    private String mIdent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        init();
        getData();
        initPopMenu();
        setListener();
    }
    private void init(){
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mCityLayout=(RelativeLayout)findViewById(R.id.apply_two);
        mStatusLayout=(RelativeLayout)findViewById(R.id.third);
        mEduLayout=(RelativeLayout)findViewById(R.id.six);
        mIdentLayout=(RelativeLayout)findViewById(R.id.seven);
        mSetupLayout=(RelativeLayout)findViewById(R.id.setting);
        mShop=(TextView)findViewById(R.id.shop);
        mAddressCity=(TextView)findViewById(R.id.apply_area);
        mStatusView=(TextView)findViewById(R.id.phone_value);
        mTrainView=(TextView)findViewById(R.id.activity_test);
        mIdentView=(TextView)findViewById(R.id.ident);
        mApplyText=(EditText)findViewById(R.id.apply_phone);
        mApplySchool=(EditText)findViewById(R.id.apply_school);
        mApplyMarj=(EditText)findViewById(R.id.apply_marj);
    }
    private void setListener(){
        mShowDraw.setOnClickListener(this);
        mCityLayout.setOnClickListener(this);
        mStatusLayout.setOnClickListener(this);
        mEduLayout.setOnClickListener(this);
        mIdentLayout.setOnClickListener(this);
        mSetupLayout.setOnClickListener(this);
    }
    private void getData(){
        tea_list = new ArrayList<String>();
        tea_list1 = new ArrayList<String>();
        tea_list2 = new ArrayList<String>();
        tea_list3 = new ArrayList<String>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("type","teacher");
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.APPLY, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("申请入住"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONArray city=data.getJSONArray("city");
                            for (int i=0;i<city.length();i++){
                                JSONObject jsonObject1=city.getJSONObject(i);
                                tea_list.add(jsonObject1.getString("city_name"));
                            }
                            int size=tea_list.size();
                            String[] array = (String[])tea_list.toArray(new String[size]);
                            for(int i=0;i<array.length;i++){
                                Map<String, String> map = new HashMap<>();
                                map.put(MENU_DATA_KEY, array[i]);
                                mMenuData1.add(map);
                            }
                            JSONArray status=data.getJSONArray("state");
                            for(int i=0;i<status.length();i++){
                                JSONObject jsonObject1=status.getJSONObject(i);
                                tea_list1.add(jsonObject1.getString("tag_name"));
                            }
                            Log.e("one"+tea_list1);
                            int size1=tea_list1.size();
                            String[] array1 = (String[])tea_list1.toArray(new String[size1]);
                            for(int i=0;i<array1.length;i++){
                                Map<String, String> map = new HashMap<>();
                                map.put(MENU_DATA_KEY, array1[i]);
                                mMenuData2.add(map);
                            }
                            Log.e("第1个"+tea_list2);
                            JSONArray third=data.getJSONArray("direction");
                            for(int i=0;i<third.length();i++){
                                JSONObject direction=third.getJSONObject(i);
                                tea_list2.add(direction.getString("tag_name"));
                                Log.e("第2个"+tea_list2);
                            }

                            int sizetwo=tea_list2.size();
                            String[] array2 = (String[])tea_list2.toArray(new String[sizetwo]);
                            for(int i=0;i<array2.length;i++){
                                Map<String, String> map = new HashMap<>();
                                map.put(MENU_DATA_KEY, array2[i]);
                                mMenuData3.add(map);
                            }
                            JSONArray identity=data.getJSONArray("identity");
                            for(int i=0;i<identity.length();i++){
                                JSONObject jsonObject1=identity.getJSONObject(i);
                                tea_list3.add(jsonObject1.getString("tag_name"));
                            }
                            int size3=tea_list3.size();
                            String[] array3 = (String[])tea_list3.toArray(new String[size3]);
                            for(int i=0;i<array3.length;i++){
                                Map<String, String> map = new HashMap<>();
                                map.put(MENU_DATA_KEY, array3[i]);
                                mMenuData4.add(map);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "collect", null);
    }
    private void Setup(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("name",mApplyText.getText()+"".toString().trim());
        map.put("area",mArea);
        map.put("state",mStatus);
        map.put("school",mApplySchool.getText()+"".toString().trim());
        map.put("major",mApplyMarj.getText()+"".toString().trim());
        map.put("direction",mOpen);
        map.put("identity",mIdent);
        map.put("type","borther");
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.ONETEP, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("下一步"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    Log.e("msg"+jsonObject.getString("msg"));
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            Intent intent=new Intent(ApplyActivity.this,ApplyTwoActivity.class);
                            intent.putExtra("teacher_id",data.getString("teacher_id"));
                            startActivity(intent);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "setup", null);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.setting:
                if ("".equals(mApplyText.getText()+"".toString().trim())){
                    ToastUtil.showToast(this,"请输入用户名");
                }else if ("".equals(mApplySchool.getText()+"".toString().trim())){
                    ToastUtil.showToast(this,"请输入学校");
                }else if ("".equals(mApplyMarj.getText()+"".toString().trim())){
                    ToastUtil.showToast(this,"请输入专业");
                }else{
                    Setup();
                }
                break;
            case R.id.showDraw:
                finish();
                break;
            case R.id.apply_two:
                mPopListView.setAdapter(mMenuAdapter1);
                mPopupWindow.showAsDropDown(mShop, 0, 2);
                supplierMenuIndex = 0;
                break;
            case R.id.third:
                mPopListView.setAdapter(mMenuAdapter2);
                mPopupWindow.showAsDropDown(mShop, 0, 2);
                supplierMenuIndex = 1;
                break;
            case R.id.six:
                mPopListView.setAdapter(mMenuAdapter3);
                mPopupWindow.showAsDropDown(mShop, 0, 2);
                supplierMenuIndex = 2;
                break;
            case R.id.seven:
                mPopListView.setAdapter(mMenuAdapter4);
                mPopupWindow.showAsDropDown(mShop, 0, 2);
                supplierMenuIndex = 3;
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
        mPopupWindow.setAnimationStyle(R.style.AnimationFade);

        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        WindowManager.LayoutParams params = this.getWindow().getAttributes();
//        params.alpha = 0.5f;
        this.getWindow().setAttributes(params);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
//        mPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
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
        mMenuAdapter2 = new SimpleAdapter(this, mMenuData2, R.layout.item_popwin_list,
                new String[]{"name"}, new int[]{R.id.item_popwin_tv});
        mMenuAdapter3 = new SimpleAdapter(this, mMenuData3, R.layout.item_popwin_list,
                new String[]{"name"}, new int[]{R.id.item_popwin_tv});
        mMenuAdapter4 = new SimpleAdapter(this, mMenuData4, R.layout.item_popwin_list,
                new String[]{"name"}, new int[]{R.id.item_popwin_tv});
        /**
         * 获取点击item 的name
         */
        mPopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (supplierMenuIndex) {
                    case 0:
                        mArea=mMenuData1.get(i).get(MENU_DATA_KEY);
                        mAddressCity.setText(mArea);
                        mPopupWindow.dismiss();
                        break;
                    case 1:
                        mStatus=mMenuData2.get(i).get(MENU_DATA_KEY);
                        Log.e("地址"+mMenuData2.get(i).get(MENU_DATA_KEY));
                        mStatusView.setText(mMenuData2.get(i).get(MENU_DATA_KEY));
                        mPopupWindow.dismiss();
                        break;
                    case 2:
                        mOpen=mMenuData3.get(i).get(MENU_DATA_KEY);
                        mTrainView.setText(mMenuData3.get(i).get(MENU_DATA_KEY));
                        mPopupWindow.dismiss();
                        break;
                    case 3:
                        mIdent=mMenuData4.get(i).get(MENU_DATA_KEY);
                        mIdentView.setText(mMenuData4.get(i).get(MENU_DATA_KEY));
                        mPopupWindow.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
