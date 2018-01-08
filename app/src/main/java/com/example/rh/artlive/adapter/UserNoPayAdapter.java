package com.example.rh.artlive.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.bean.NoPaiBean;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/8/25.
 */

public class UserNoPayAdapter extends BaseRecyclerAdapter<NoPaiBean> {
    private String zt;
    private String mToken;

    public UserNoPayAdapter(Context context, int layoutId, List<NoPaiBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
        this.zt=zt;
        this.mToken=mToken;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, NoPaiBean s) {

    }
}
