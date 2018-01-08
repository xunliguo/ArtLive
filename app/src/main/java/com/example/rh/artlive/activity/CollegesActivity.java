package com.example.rh.artlive.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
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
import com.example.rh.artlive.adapter.RvListener;
import com.example.rh.artlive.adapter.SortAdapter;
import com.example.rh.artlive.adapter.TeacherAdapter;
import com.example.rh.artlive.bean.HomeBean;
import com.example.rh.artlive.bean.SortBean;
import com.example.rh.artlive.fragment.CollegesRightFragment;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.LoadRecyclerView;
import com.example.rh.artlive.view.OnItemClickListener;
import com.example.rh.artlive.view.PullToRefreshLayout;
import com.google.gson.Gson;
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
 * 院校
 */

public class CollegesActivity extends BaseFragmentActivity implements View.OnClickListener {
    private static final String MENU_DATA_KEY = "name";
    private int supplierMenuIndex = 0;

    private ImageView mShowDraw;
    private TextView mTrueTitleView;


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

    private LinearLayout mSupplierListProduct;
    private LinearLayout mSupplierListSort;
    private LinearLayout mSupplierListActivity;
    private LinearLayout mSupplinerScreen;

    private TextView mSupplierListTvProduct;
    private TextView mSupplierListTvSort;
    private TextView mSupplierListTvActivity;
    private TextView mSupplierListFour;

    List<String> tea_list ;
    List<String> tea_list1 ;
    List<String> tea_list2 ;
    List<String> tea_list3 ;

    private String mMoir="";
    private String mCity="";
    private String mAll="";
    private String mSchool="";
    private int pageNo=1;

    private SortBean mSortBean;
    private RecyclerView rvSort;
    private LinearLayoutManager mLinearLayoutManager;
    private Context context;
    private SortAdapter mSortAdapter;
    private CollegesRightFragment mRightFragment;
    private boolean isMoved;
    private String mFirst_Tag_Name="8大传媒";

    private  ArrayList<String>list;//广告的数据



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colleges);
        context=this;
        init();
        getTeacherClass();
        setAdapter();
        setListener();
        initPopMenu();
        initData(getSupportFragmentManager(),mMoir,mCity,mAll,mSchool);

    }


    private void init(){
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mTrueTitleView=(TextView)findViewById(R.id.editor);
        mSupplierListTvProduct = (TextView) findViewById(R.id.around_supplier_list_tv_product);
        mSupplierListTvSort = (TextView) findViewById(R.id.around_supplier_list_tv_sort);
        mSupplierListTvActivity = (TextView) findViewById(R.id.around_supplier_list_tv_activity);
        mSupplierListFour=(TextView)findViewById(R.id.around_supplier_list_tv_activitymenu);

        mSupplierListProduct = (LinearLayout) findViewById(R.id.around_supplier_list_product);
        mSupplierListSort = (LinearLayout) findViewById(R.id.around_supplier_list_sort);
        mSupplierListActivity = (LinearLayout) findViewById(R.id.around_supplier_list_activity);
        mSupplinerScreen=(LinearLayout)findViewById(R.id.around_supplier_list_activitymenu);


        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(OrientationHelper.HORIZONTAL);// 纵向布局

        rvSort = (RecyclerView) findViewById(R.id.rv_sort);
        mLinearLayoutManager = new LinearLayoutManager(context);
        rvSort.setLayoutManager(mLinearLayoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        rvSort.addItemDecoration(decoration);

    }
    private void setListener(){
        mShowDraw.setOnClickListener(this);
        mSupplierListProduct.setOnClickListener(this);
        mSupplierListSort.setOnClickListener(this);
        mSupplierListActivity.setOnClickListener(this);
        mSupplinerScreen.setOnClickListener(this);
        mSupplierListFour.setOnClickListener(this);
        mTrueTitleView.setOnClickListener(this);
    }

    private void setAdapter() {

    }

    /**
     * 三个列表popu文件的数据
     */
    private void getTeacherClass(){
        tea_list = new ArrayList<String>();
        tea_list1 = new ArrayList<String>();
        tea_list2 = new ArrayList<String>();
        tea_list3 = new ArrayList<String>();
        HashMap<String, String> map = new HashMap<>();
        map.put("access",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        HttpUtil.postHttpRequstProgess(CollegesActivity.this, "正在加载", UrlConstant.COLLEAGE_DOWN, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("院校下拉"+response);
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
                        JSONArray local=data.getJSONArray("city");
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
                        JSONArray sort=data.getJSONArray("fraction");
                        for (int i=0;i<sort.length();i++){
                            JSONObject obj=sort.getJSONObject(i);
                            tea_list2.add(obj.getString("fraction"));
                        }
                        int size2=tea_list2.size();
                        String[] array2 = (String[])tea_list2.toArray(new String[size2]);
                        for(int i=0;i<array2.length;i++){
                            Map<String, String> map = new HashMap<>();
                            map.put(MENU_DATA_KEY, array2[i]);
                            mMenuData3.add(map);
                        }
                        Log.e("学校数据"+tea_list3);
                        //学校
                        JSONArray school=data.getJSONArray("test");
                        for (int i=0;i<school.length();i++){
                            JSONObject obj=school.getJSONObject(i);
                            tea_list3.add(obj.getString("city_name"));
                        }
                        int size3=tea_list3.size();
                        String[] array3 = (String[])tea_list3.toArray(new String[size3]);
                        for(int i=0;i<array3.length;i++){
                            Map<String, String> map = new HashMap<>();
                            map.put(MENU_DATA_KEY, array3[i]);
                            mMenuData4.add(map);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "dataleaseclass", null);
    }

    private void initData(FragmentManager fm,String mMoir,String mCity,String mAll,String mSchool){
        list = new ArrayList<>();//广告的数据
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("page",String.valueOf(pageNo));
        map.put("major",mMoir);
        map.put("test",mSchool);
        map.put("city",mCity);
        map.put("fraction",mAll);
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.COLLEAGE, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("院校数据"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            String aa=jsonObject.getString("data");
                            Gson gson = new Gson();
                            mSortBean=gson.fromJson(aa,SortBean.class);
                            List<SortBean.CategoryOneArrayBean> categoryOneArray = mSortBean.getCategoryOneArray();
                            for (int i = 0; i < categoryOneArray.size(); i++) {
                                list.add(categoryOneArray.get(i).getTag_name());
                            }
                            mSortAdapter = new SortAdapter(context, list, new RvListener() {
                                @Override
                                public void onItemClick(int id, int position) {
//                                    if (mSortDetailFragment != null) {
                                        isMoved = true;
//                                        targetPosition = position;
                                        setChecked(position, true);
//                                    }
                                }
                            });
                            rvSort.setAdapter(mSortAdapter);
                            JSONArray left=data.getJSONArray("left");
                            for (int j=0;j<left.length();j++){
                                JSONObject jsonObject1=left.getJSONObject(j);
                                if (j==0){
                                    mFirst_Tag_Name=jsonObject1.getString("tag_name");
                                }
                            }
                            createFragment(mFirst_Tag_Name);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "teacher", fm);
    }


    /**
     *  //跳转SortDetailsFragment 携带参数
     */
    public void createFragment(String tag_name) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mRightFragment = new CollegesRightFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tag_name",tag_name);
        mRightFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.lin_fragment, mRightFragment);
        fragmentTransaction.commit();
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
            case R.id.around_supplier_list_tv_activitymenu:
                mSupplierListFour.setTextColor(getResources().getColor(R.color.around_supplier_title_selected_color));
                mPopListView.setAdapter(mMenuAdapter4);
                mPopupWindow.showAsDropDown(mSupplinerScreen, 0, 2);
                supplierMenuIndex = 3;
                break;
            case R.id.editor:
                Intent intent=new Intent(CollegesActivity.this,TrueTitleActivity.class);
                startActivity(intent);
                break;
        }
    }




    /**
     * 点击最左侧实现联动
     * @param position
     * @param isLeft
     */
    private void setChecked(int position, boolean isLeft) {

//        Log.e("点击的位置"+mSortBean.getCategoryOneArray().get(i).getTag_name());


        Log.e("点击的位置"+mSortBean.getCategoryOneArray().get(position).getTag_name());



//        for (int i = 0; i < position; i++) {
//            mSortBean.getCategoryOneArray().get(i).getCategoryTwoArray().size();
//        }
//        android.util.Log.d("p-------->", String.valueOf(position));
//        if (isLeft) {
            mSortAdapter.setCheckedPosition(position);
        createFragment(mSortBean.getCategoryOneArray().get(position).getTag_name());
//            //此处的位置需要根据每个分类的集合来进行计算
//            int count = 0;
//            for (int i = 0; i < position; i++) {
//                count += mSortBean.getCategoryOneArray().get(i).getCategoryTwoArray().size();
//            }
//            count += position;
////            mSortDetailFragment.setData(count);
//        } else {
//            if (isMoved) {
//                isMoved = false;
//            } else
//                mSortAdapter.setCheckedPosition(position);
////            ItemHeaderDecoration.setCurrentTag(String.valueOf(targetPosition));
//        }
////        moveToCenter(position);
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
                mSupplierListFour.setTextColor(getResources().getColor(R.color.around_supplier_title_color));
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
                        mMoir=mMenuData1.get(i).get(MENU_DATA_KEY);
                        mSupplierListTvProduct.setText(mMenuData1.get(i).get(MENU_DATA_KEY));
                        mPopupWindow.dismiss();
                        initData(getSupportFragmentManager(),mMoir,mCity,mAll,mSchool);
                        break;
                    case 1:
                        mCity=mMenuData2.get(i).get(MENU_DATA_KEY);
                        mSupplierListTvSort.setText(mMenuData2.get(i).get(MENU_DATA_KEY));
                        mPopupWindow.dismiss();
                        initData(getSupportFragmentManager(),mMoir,mCity,mAll,mSchool);
                        break;
                    case 2:
                        mAll=mMenuData3.get(i).get(MENU_DATA_KEY);
                        mSupplierListTvActivity.setText(mMenuData3.get(i).get(MENU_DATA_KEY));
                        mPopupWindow.dismiss();
                        initData(getSupportFragmentManager(),mMoir,mCity,mAll,mSchool);
                        break;
                    case 3:
                        mSchool=mMenuData4.get(i).get(MENU_DATA_KEY);
                        mSupplierListFour.setText(mMenuData4.get(i).get(MENU_DATA_KEY));
                        mPopupWindow.dismiss();
                        initData(getSupportFragmentManager(),mMoir,mCity,mAll,mSchool);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
