package com.example.rh.artlive.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.HomeBean;
import com.example.rh.artlive.bean.TopicBean;
import com.example.rh.artlive.photo.DynamicGridAdapter;
import com.example.rh.artlive.photo.ImagePagerActivity;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;
import com.example.rh.artlive.view.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rh on 2017/12/1.
 */

public class TopicAdapter extends BaseRecyclerAdapter<TopicBean> {


    public TopicAdapter(Context context, int layoutId, List<TopicBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }

    @Override
    public void convert(BaseRecyclerViewHolder holder, TopicBean s) {

        TextView topic=holder.getView(R.id.topic_name);
        TextView content=holder.getView(R.id.topic_marj);
        ImageView imageView=holder.getView(R.id.roundImageView);
        topic.setText(s.getThe_title());
        content.setText(s.getThe_description());
        Glide.with(mContext).load(s.getThe_img()).into(imageView);
    }
}
