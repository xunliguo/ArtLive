package com.example.rh.artlive.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.RvListener;
import com.example.rh.artlive.adapter.SortAdapter;
import com.example.rh.artlive.bean.SortBean;
import com.example.rh.artlive.fragment.CollegesRightFragment;
import com.example.rh.artlive.fragment.TrueTitleRightFragment;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by rh on 2017/12/21.
 * 真题
 */

public class TrueTitleActivity extends BaseFragmentActivity implements View.OnClickListener{

    private ImageView mShowDraw;

    private SortBean mSortBean;
    private RecyclerView rvSort;
    private LinearLayoutManager mLinearLayoutManager;
    private Context context;
    private SortAdapter mSortAdapter;
    private String mFirst_Tag_Name="全部";
    private TrueTitleRightFragment mRightFragment;
    private RelativeLayout mSett;


    private ArrayList<String> list = new ArrayList<>();//广告的数据


    @Override
    protected void  onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true_title);
        context=this;
        init();
        setListener();
        setData();
    }
    private void init(){
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mSett=(RelativeLayout)findViewById(R.id.setting);

        rvSort = (RecyclerView) findViewById(R.id.rv_sort);
        mLinearLayoutManager = new LinearLayoutManager(context);
        rvSort.setLayoutManager(mLinearLayoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        rvSort.addItemDecoration(decoration);

    }
    private void setListener(){
        mShowDraw.setOnClickListener(this);
        mSett.setOnClickListener(this);

    }
    private void setData(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.TRUE_TITLE, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("真题数据"+response);
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
                                list.add(categoryOneArray.get(i).getName());
                            }
                            mSortAdapter = new SortAdapter(context, list, new RvListener() {
                                @Override
                                public void onItemClick(int id, int position) {
//                                    if (mSortDetailFragment != null) {
//                                    isMoved = true;
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
                                    mFirst_Tag_Name=jsonObject1.getString("name");
                                }
                            }
                            if ("统考".equals(mFirst_Tag_Name)) {
                                createFragment(mFirst_Tag_Name, "1");
                            }else{
                                createFragment(mFirst_Tag_Name, "0");
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "teacher", null);
    }

    /**
     *  //跳转SortDetailsFragment 携带参数
     */
    public void createFragment(String tag_name,String type) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mRightFragment = new TrueTitleRightFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tag_name",tag_name);
        bundle.putString("type",type);
        mRightFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.lin_fragment, mRightFragment);
        fragmentTransaction.commit();
    }


    /**
     * 点击最左侧实现联动
     * @param position
     * @param isLeft
     */
    private void setChecked(int position, boolean isLeft) {

//        Log.e("点击的位置"+mSortBean.getCategoryOneArray().get(i).getTag_name());

        mSortAdapter.setCheckedPosition(position);
        if (mSortBean.getCategoryOneArray().get(position).getName().equals("统考")) {
            createFragment(mSortBean.getCategoryOneArray().get(position).getName(), "1");
        }else{
            createFragment(mSortBean.getCategoryOneArray().get(position).getName(), "0");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showDraw:
                finish();
                break;
            case R.id.setting:
                break;
        }
    }
}
