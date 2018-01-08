package com.example.rh.artlive.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.FollowBean;
import com.example.rh.artlive.bean.WormBean;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rh on 2017/12/9.
 */

public class WormAdapter extends BaseRecyclerAdapter<WormBean> {


    public WormAdapter(Context context, int layoutId, List<WormBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, WormBean s) {


        TextView top_name=holder.getView(R.id.top_name);
        TextView art_name=holder.getView(R.id.textView3);
        ImageView content=holder.getView(R.id.content);
        ImageView content1=holder.getView(R.id.content1);
        ImageView content2=holder.getView(R.id.content2);

        Glide.with(mContext).load(s.getA()).into(content);
        Glide.with(mContext).load(s.getB()).into(content1);
        Glide.with(mContext).load(s.getC()).into(content2);
        top_name.setText(s.getForum_title());
        art_name.setText(s.getName());

    }
}
