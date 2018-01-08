package com.example.rh.artlive.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.ExamAdapter;
import com.example.rh.artlive.adapter.MajorAdapter;
import com.example.rh.artlive.bean.ExamBean;
import com.example.rh.artlive.bean.SchoolBean;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.LoadRecyclerView;
import com.example.rh.artlive.view.OnItemClickListener;
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
 * Created by rh on 2017/12/23.
 * 根据院校选择专业
 */

public class ExamMajorActivity extends BaseFragmentActivity  implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,LoadRecyclerView.LoadListener,OnItemClickListener<SchoolBean> {
    private static final String MENU_DATA_KEY = "name";
    private int supplierMenuIndex = 0;



    private ImageView mShowDraw;
    private ImageView mAddView;
    private TextView mShop;

    private List<Map<String, String>> mMenuData1 = new ArrayList<>();; //默认
    private ListView mPopListView;
    private PopupWindow mPopupWindow;
    private SimpleAdapter mMenuAdapter1;
    List<String> tea_list ;
    List<String> tea_list_id ;


    private PullToRefreshLayout mAuto;
    private LoadRecyclerView mLoad;
    private MajorAdapter mChestAdapter;
    private ArrayList<SchoolBean> mData = new ArrayList<>();
    private String mSchool_id;
    private RelativeLayout mSett;

    private String major_title="";
    private String major_content="";
    private String major_content_id="";
    private String mType="";
    private String flag_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_major);

        init();
        setAdapter(major_title,major_content);
        setListener();
        initData();
        initPopMenu();

    }

    private void init(){
        Intent intent=getIntent();
        mSchool_id=intent.getStringExtra("school_id");
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mAddView=(ImageView)findViewById(R.id.showTime);
        mSett=(RelativeLayout)findViewById(R.id.setting);
        mShop=(TextView)findViewById(R.id.shop);

        mAuto = (PullToRefreshLayout)findViewById(R.id.network_pager_myAuto);
        mLoad = (LoadRecyclerView) findViewById(R.id.network_myLoad);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(OrientationHelper.VERTICAL);// 纵向布局
        mLoad.setLayoutManager(lm);
    }
    private void setListener(){
        mAuto.setOnRefreshListener(this);
        mLoad.setLoadListener(this);
        mShowDraw.setOnClickListener(this);
        mAddView.setOnClickListener(this);
        mSett.setOnClickListener(this);
    }

    private void setAdapter(String major_title,String major_content) {
        mChestAdapter = new MajorAdapter(this, R.layout.recycler_apply_major, mData,major_title,major_content);
        mLoad.setAdapter(mChestAdapter);
        mLoad.setIsHaveData(false);
        mChestAdapter.setOnItemClickListener(this);
    }

    private void initData(){
        mData = new ArrayList<>();
        tea_list = new ArrayList<String>();
        tea_list_id = new ArrayList<String>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("type","major");
        map.put("school_id",mSchool_id);
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.ADD_EXAM, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
            }
            @Override
            public void onResponse(String response) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
                Log.e("专业"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONArray major=data.getJSONArray("info");
                            for (int i=0;i<major.length();i++){
                                JSONObject jsonObject1=major.getJSONObject(i);
                                if (i==0) {
                                    JSONArray majo = jsonObject1.getJSONArray("majortwo");
                                    for (int j = 0; j < majo.length(); j++) {
                                        JSONObject jsonObject2 = majo.getJSONObject(j);
                                        tea_list.add(jsonObject2.getString("major_content"));
                                        tea_list_id.add(jsonObject2.getString("major_id"));
                                    }
                                    int size = tea_list.size();
                                    String[] array = (String[]) tea_list.toArray(new String[size]);
                                    for (int j = 0; j < array.length; j++) {
                                        Map<String, String> map = new HashMap<>();
                                        map.put(MENU_DATA_KEY, array[j]);
                                        mMenuData1.add(map);
                                    }
                                }
                                SchoolBean schoolBean=JSON.parseObject(jsonObject1.toString(),SchoolBean.class);
                                mData.add(schoolBean);
                            }
                            setAdapter(major_title,major_content);
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
            case R.id.showDraw:
                finish();
                break;
            case R.id.showTime:
                break;
            case R.id.setting:
                mSharePreferenceUtil.setString(SharedPerfenceConstant.APPLY_MAJOR,flag_name);
                Log.e("专业"+mSharePreferenceUtil.getString(SharedPerfenceConstant.APPLY_MAJOR));
                Intent intent=new Intent(ExamMajorActivity.this,TestCenterActivity.class);
                intent.putExtra("majortop_id",mSchool_id);
                intent.putExtra("majortwo_id",major_content_id);
                startActivity(intent);
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
//        mMenuAdapter2 = new SimpleAdapter(this, mMenuData2, R.layout.item_popwin_list,
//                new String[]{"name"}, new int[]{R.id.item_popwin_tv});
//        mMenuAdapter3 = new SimpleAdapter(this, mMenuData3, R.layout.item_popwin_list,
//                new String[]{"name"}, new int[]{R.id.item_popwin_tv});
//        mMenuAdapter4 = new SimpleAdapter(this, mMenuData4, R.layout.item_popwin_list,
//                new String[]{"name"}, new int[]{R.id.item_popwin_tv});
        /**
         * 获取点击item 的name
         */
        mPopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (supplierMenuIndex) {
                    case 0:
//                        mArea=mMenuData1.get(i).get(MENU_DATA_KEY);
//                        mAddressCity.setText(mArea);
                        int size=tea_list_id.size();
                        String[] array=tea_list_id.toArray(new String[size]);
                        Log.e("id"+array[i]);
                        flag_name=mMenuData1.get(i).get(MENU_DATA_KEY);
                        major_content_id=array[i];
                        Log.e("dsa"+mMenuData1.get(i).get(MENU_DATA_KEY));
                        setAdapter(mType,mMenuData1.get(i).get(MENU_DATA_KEY));
                        mPopupWindow.dismiss();
                        break;
//                    case 1:
//                        mStatus=mMenuData2.get(i).get(MENU_DATA_KEY);
//                        Log.e("地址"+mMenuData2.get(i).get(MENU_DATA_KEY));
//                        mStatusView.setText(mMenuData2.get(i).get(MENU_DATA_KEY));
//                        mPopupWindow.dismiss();
//                        break;
//                    case 2:
//                        mOpen=mMenuData3.get(i).get(MENU_DATA_KEY);
//                        mTrainView.setText(mMenuData3.get(i).get(MENU_DATA_KEY));
//                        mPopupWindow.dismiss();
//                        break;
//                    case 3:
//                        mIdent=mMenuData4.get(i).get(MENU_DATA_KEY);
//                        mIdentView.setText(mMenuData4.get(i).get(MENU_DATA_KEY));
//                        mPopupWindow.dismiss();
//                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, SchoolBean s, int position) {
        mType=s.getMajortop();
        mSett.setVisibility(View.VISIBLE);
        mPopListView.setAdapter(mMenuAdapter1);
        mPopupWindow.showAsDropDown(mShop, 0, 2);
        supplierMenuIndex = 0;
    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, SchoolBean noPaiBean, int position) {
        return false;
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void loadFinish() {

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        initData();

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }
}
