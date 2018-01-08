package com.example.rh.artlive.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.OutFitAdapter;
import com.example.rh.artlive.adapter.TeacherAdapter;
import com.example.rh.artlive.bean.HomeBean;
import com.example.rh.artlive.bean.OutFitBean;
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
 * Created by rh on 2017/11/20.
 */

public class OutFitActivity extends BaseFragmentActivity  implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,LoadRecyclerView.LoadListener,OnItemClickListener<OutFitBean> {

    private static final String MENU_DATA_KEY = "name";
    private int supplierMenuIndex = 0;

    private ImageView mShowDraw;

    private PullToRefreshLayout mAuto;
    private LoadRecyclerView mLoad;
    private OutFitAdapter mChestAdapter;
    private ArrayList<OutFitBean> mData = new ArrayList<>();
    private GridLayoutManager mManager;

    private List<Map<String, String>> mMenuData1 = new ArrayList<>();; //默认
    private List<Map<String, String>> mMenuData2 = new ArrayList<>();; //品牌
    private List<Map<String, String>> mMenuData3 = new ArrayList<>();; //种类
    private ListView mPopListView;
    private PopupWindow mPopupWindow;
    private SimpleAdapter mMenuAdapter1;
    private SimpleAdapter mMenuAdapter2;
    private SimpleAdapter mMenuAdapter3;

    private LinearLayout mSupplierListProduct;
    private LinearLayout mSupplierListSort;
    private LinearLayout mSupplierListActivity;

    private TextView mSupplierListTvProduct;
    private TextView mSupplierListTvSort;
    private TextView mSupplierListTvActivity;

    List<String> tea_list ;
    List<String> tea_list1 ;
    List<String> tea_list2 ;

    private String mMoir="";
    private String mCity="";
    private String mSchool="";

    private  int pageNo=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit);

        init();
        getTeacherClass();
        setAdapter();
        setListener();
        initPopMenu();
        initData(getSupportFragmentManager(),mMoir,mCity,mSchool);

    }
    private void init(){
        mSupplierListTvProduct = (TextView) findViewById(R.id.around_supplier_list_tv_product);
        mSupplierListTvSort = (TextView) findViewById(R.id.around_supplier_list_tv_sort);
        mSupplierListTvActivity = (TextView) findViewById(R.id.around_supplier_list_tv_activity);

        mSupplierListProduct = (LinearLayout) findViewById(R.id.around_supplier_list_product);
        mSupplierListSort = (LinearLayout) findViewById(R.id.around_supplier_list_sort);
        mSupplierListActivity = (LinearLayout) findViewById(R.id.around_supplier_list_activity);

        mAuto = (PullToRefreshLayout)findViewById(R.id.network_pager_myAuto);
        mLoad = (LoadRecyclerView) findViewById(R.id.network_myLoad);
        mManager = new GridLayoutManager(this, 2);
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        LinearLayoutManager lm=new LinearLayoutManager(this);
        lm.setOrientation(OrientationHelper.VERTICAL);
        mLoad.setLayoutManager(lm);
        mLoad.setLayoutManager(mManager);
    }
    private void setListener(){
        mAuto.setOnRefreshListener(this);
        mLoad.setLoadListener(this);
        mShowDraw.setOnClickListener(this);
        mSupplierListProduct.setOnClickListener(this);
        mSupplierListSort.setOnClickListener(this);
        mSupplierListActivity.setOnClickListener(this);
    }

    private void setAdapter() {
        mChestAdapter = new OutFitAdapter(this, R.layout.recycler_outfit_adapter, mData);
        mLoad.setAdapter(mChestAdapter);
        mLoad.setIsHaveData(false);
        mChestAdapter.setOnItemClickListener(this);
    }

    /**
     * 三个列表popu文件的数据
     */
    private void getTeacherClass(){
        tea_list = new ArrayList<String>();
        tea_list1 = new ArrayList<String>();
        tea_list2 = new ArrayList<String>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        HttpUtil.postHttpRequstProgess(OutFitActivity.this, "正在加载", UrlConstant.OUTFIT_DOWN, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("机构下拉"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.has("data")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        //专业
                        JSONArray major=data.getJSONArray("major");
                        for (int i = 0; i < data.length(); i++) {
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
                        //所在地
                        JSONArray local=data.getJSONArray("address");
                        for (int i=0;i<local.length();i++){
                            JSONObject obj=local.getJSONObject(i);
                            tea_list1.add(obj.getString("city_name"));
                        }
                        int size1=tea_list1.size();
                        String[] array1 = (String[])tea_list1.toArray(new String[size1]);
                        for(int i=0;i<array1.length;i++){
                            Map<String, String> map = new HashMap<>();
                            map.put(MENU_DATA_KEY, array1[i]);
                            mMenuData2.add(map);
                        }
                        //排序
                        JSONArray sort=data.getJSONArray("paixu");
                        for (int i=0;i<sort.length();i++){
                            JSONObject obj=sort.getJSONObject(i);
                            tea_list2.add(obj.getString("sort"));
                        }
                        int size2=tea_list2.size();
                        String[] array2 = (String[])tea_list2.toArray(new String[size2]);
                        for(int i=0;i<array2.length;i++){
                            Map<String, String> map = new HashMap<>();
                            map.put(MENU_DATA_KEY, array2[i]);
                            mMenuData3.add(map);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "dataleaseclass", null);
    }


    private void initData(FragmentManager fm,String mMoir, String mCity, String mSchool){
        mData = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("major",mMoir);
        map.put("city",mCity);
        map.put("school",mSchool);
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.OUTFIT, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
            }
            @Override
            public void onResponse(String response) {
                Log.e("机构数据"+response);
                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data=jsonObject.getJSONObject("data");
                            JSONArray inst=data.getJSONArray("institution");
                            for (int i=0;i<inst.length();i++){
                                JSONObject bobject=inst.getJSONObject(i);
                                OutFitBean homeBean= JSON.parseObject(bobject.toString(), OutFitBean.class);
                                mData.add(homeBean);
                            }
                            setAdapter();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "teacher", fm);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showDraw:
                finish();
                break;
            case R.id.around_supplier_list_product:
                mSupplierListTvProduct.setTextColor(getResources().getColor(R.color.around_supplier_title_selected_color));
                mPopListView.setAdapter(mMenuAdapter1);
                mPopupWindow.showAsDropDown(mSupplierListProduct, 0, 2);
                supplierMenuIndex = 0;
                break;
            case R.id.around_supplier_list_sort:
                mSupplierListTvSort.setTextColor(getResources().getColor(R.color.around_supplier_title_selected_color));
                mPopListView.setAdapter(mMenuAdapter2);
                mPopupWindow.showAsDropDown(mSupplierListSort, 0, 2);
                supplierMenuIndex = 1;
                break;
            case R.id.around_supplier_list_activity:
                mSupplierListTvActivity.setTextColor(getResources().getColor(R.color.around_supplier_title_selected_color));
                mPopListView.setAdapter(mMenuAdapter3);
                mPopupWindow.showAsDropDown(mSupplierListActivity, 0, 2);
                supplierMenuIndex = 2;
                break;
        }
    }


    /**
     * popu文件
     */
    private void initPopMenu() {
        View popView = LayoutInflater.from(this).inflate(R.layout.layout_popwin_supplier_list, null);
        mPopListView = (ListView) popView.findViewById(R.id.popwin_list_view);
        mPopupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mSupplierListTvProduct.setTextColor(getResources().getColor(R.color.around_supplier_title_color));
                mSupplierListTvSort.setTextColor(getResources().getColor(R.color.around_supplier_title_color));
                mSupplierListTvActivity.setTextColor(getResources().getColor(R.color.around_supplier_title_color));
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

        /**
         * 获取点击item 的name
         */
        mPopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (supplierMenuIndex) {
                    case 0:
                        mMoir=mMenuData1.get(i).get(MENU_DATA_KEY);
                        mSupplierListTvProduct.setText(mMenuData1.get(i).get(MENU_DATA_KEY));
                        mPopupWindow.dismiss();
                        Log.e("点击的专业"+mMoir);
                        initData(getSupportFragmentManager(),mMoir,mCity,mSchool);
                        break;
                    case 1:
                        mCity=mMenuData2.get(i).get(MENU_DATA_KEY);
                        mSupplierListTvSort.setText(mMenuData2.get(i).get(MENU_DATA_KEY));
                        mPopupWindow.dismiss();
                        Log.e("点击的城市"+mCity);
                        initData(getSupportFragmentManager(),mMoir,mCity,mSchool);
                        break;
                    case 2:
                        mSchool=mMenuData3.get(i).get(MENU_DATA_KEY);
                        mSupplierListTvActivity.setText(mMenuData3.get(i).get(MENU_DATA_KEY));
                        mPopupWindow.dismiss();
                        Log.e("点击的排序"+mSchool);
                        initData(getSupportFragmentManager(),mMoir,mCity,mSchool);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, OutFitBean s, int position) {
        Intent intent=new Intent(OutFitActivity.this,OutFitMainActivity.class);
        intent.putExtra("inst_id",s.getInst_id());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, OutFitBean s, int position) {

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
        mLoad.setIsHaveData(false);
        pageNo = 1;
        mData = new ArrayList<>();
        initData(getSupportFragmentManager(),mMoir,mCity,mSchool);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        pageNo=pageNo+1;
        initData(null,mMoir,mCity,mSchool);

    }
}
