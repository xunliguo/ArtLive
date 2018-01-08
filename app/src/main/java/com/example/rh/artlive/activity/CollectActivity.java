package com.example.rh.artlive.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.adapter.CollectAdapter;
import com.example.rh.artlive.adapter.UserNoPayAdapter;
import com.example.rh.artlive.bean.NoPaiBean;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.SwipeItemLayout;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.CircleImageView;
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

import okhttp3.Call;

/**
 * Created by rh on 2017/11/22.
 */

public class CollectActivity extends BaseFragmentActivity implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,LoadRecyclerView.LoadListener,OnItemClickListener<PhotoBean> {


    private ImageView mShowDraw;

//    private PullToRefreshLayout mAuto;
//    private LoadRecyclerView mLoad;
//    private CollectAdapter mChestAdapter;
//    private ArrayList<PhotoBean> mData = new ArrayList<>();

    private ArrayList<String> list = new ArrayList<>();//图标
    private ArrayList<String> title = new ArrayList<>();//标题
    private ArrayList<String> title1 = new ArrayList<>();//标题
    private ArrayList<String> time = new ArrayList<>();//内容
    private ArrayList<String> id = new ArrayList<>();//id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
        mShowDraw.setOnClickListener(this);



        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(Color.RED);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });

//        init();
//        setAdapter();
//        setListener();
        initData();

    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.Holder> {
        private Context mContext;

        public MyAdapter(Context context) {
            mContext = context;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View root = LayoutInflater.from(mContext).inflate(R.layout.item_recycler, parent, false);
            return new Holder(root);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {

            Glide.with(CollectActivity.this).load(list.get(position)).into(holder.mBmp);
            holder.mTitle.setText(title.get(position));
            holder.mType.setText(title1.get(position));
            holder.mTime.setText(time.get(position));

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            private CircleImageView mBmp;
            private TextView mTitle;
            private TextView mType;
            private TextView mTime;

            Holder(View itemView) {
                super(itemView);

                mBmp = (CircleImageView) itemView.findViewById(R.id.collect_iamge);
                mTitle=(TextView)itemView.findViewById(R.id.collect_title);
                mType=(TextView)itemView.findViewById(R.id.collect_title_value);
                mTime=(TextView)itemView.findViewById(R.id.collect_time);
                View main = itemView.findViewById(R.id.main);
                main.setOnClickListener(this);
                main.setOnLongClickListener(this);

                View delete = itemView.findViewById(R.id.delete);
                delete.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.main:
                        Toast.makeText(v.getContext(), "点击了main，位置为：" + id.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.delete:
                        setDelete(id.get(getAdapterPosition()));
                        int pos = getAdapterPosition();
                        list.remove(pos);
                        notifyItemRemoved(pos);
                        break;
                }
            }

            @Override
            public boolean onLongClick(View v) {
                switch (v.getId()) {
                    case R.id.main:
                        Toast.makeText(v.getContext(), "长按了main，位置为：" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        }
    }


    private void initData(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("userid","25");
        map.put("type","show");
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.COLLECT, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
//                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
            }
            @Override
            public void onResponse(String response) {
                Log.e("收藏列表"+response);
//                mAuto.refreshFinish(PullToRefreshLayout.FAIL);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {
                            JSONArray data=jsonObject.getJSONArray("data");
                            for (int i=0;i<data.length();i++){
                                JSONObject jsonObject1=data.getJSONObject(i);
//                                PhotoBean photoBean= JSON.parseObject(jsonObject1.toString(),PhotoBean.class);
//                                mData.add(photoBean);
                                list.add(jsonObject1.getString("collection_img"));
                                title.add(jsonObject1.getString("collection_title"));
                                title1.add(jsonObject1.getString("collection_type"));
                                time.add(jsonObject1.getString("collection_addtime"));
                                id.add(jsonObject1.getString("collection_id"));
                            }

                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                            recyclerView.setLayoutManager(new LinearLayoutManager(CollectActivity.this));
                            recyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(CollectActivity.this));
                            recyclerView.setAdapter(new MyAdapter(CollectActivity.this));
                            recyclerView.addItemDecoration(new DividerItemDecoration(CollectActivity.this,LinearLayoutManager.VERTICAL));
//                            setAdapter();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "collect", null);
    }

    /**
     * 删除收藏
     */
    private void setDelete(String id){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token",mSharePreferenceUtil.getString(SharedPerfenceConstant.TOKEN));
        map.put("collection_id",id);
        map.put("type","delete");
        HttpUtil.postHttpRequstProgess(this, "正在加载", UrlConstant.COLLECT, map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Log.e("删除收藏"+response);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if ("1".equals(jsonObject.getString("state"))) {
                        if (jsonObject.has("data")) {

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
        }
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, PhotoBean noPaiBean, int position) {

    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, PhotoBean noPaiBean, int position) {
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
