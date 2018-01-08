package com.example.rh.artlive.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.bean.RightBean;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by rh on 2017/12/7.
 */

public class RightAdapter extends BaseRecyclerAdapter<RightBean> {

    public RightAdapter(Context context, int layoutId, List<RightBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, RightBean s) {
        TextView name=holder.getView(R.id.colleges_name);
        ImageView logo=holder.getView(R.id.colleges_logo);
        TextView colleges_class=holder.getView(R.id.colleges_class);
        name.setText(s.getSchool_name());
        colleges_class.setText("人气   "+s.getRenqi());
        Glide.with(mContext).load(s.getSchool_img_url()).into(logo);
    }
}
