package com.example.rh.artlive.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.RightBean;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by rh on 2017/12/22.
 */

public class TrueRightAdapter extends BaseRecyclerAdapter<RightBean> {

    public TrueRightAdapter(Context context, int layoutId, List<RightBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, RightBean s) {
        TextView name=holder.getView(R.id.colleges_name);
        ImageView logo=holder.getView(R.id.colleges_logo);
        TextView true_title=holder.getView(R.id.true_title);
        true_title.setText(s.getTopicsum()+"套真题");
        name.setText(s.getName());
        Glide.with(mContext).load(s.getImg()).into(logo);
    }
}
