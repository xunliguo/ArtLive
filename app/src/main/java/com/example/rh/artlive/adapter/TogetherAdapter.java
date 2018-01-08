package com.example.rh.artlive.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.TogetherBean;
import com.example.rh.artlive.bean.TopicBean;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by rh on 2018/1/4.
 */

public class TogetherAdapter extends BaseRecyclerAdapter<TogetherBean> {

    public TogetherAdapter(Context context, int layoutId, List<TogetherBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }

    @Override
    public void convert(BaseRecyclerViewHolder holder, TogetherBean s) {

//        TextView topic=holder.getView(R.id.topic_name);
//        TextView content=holder.getView(R.id.topic_marj);
//        ImageView imageView=holder.getView(R.id.roundImageView);
//        topic.setText(s.getThe_title());
//        content.setText(s.getThe_description());
//        Glide.with(mContext).load(s.getThe_img()).into(imageView);
    }
}
