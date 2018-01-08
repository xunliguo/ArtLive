package com.example.rh.artlive.adapter;

import android.content.Context;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.CourseBean;
import com.example.rh.artlive.bean.FansBean;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;
import com.example.rh.artlive.view.CircleImageView;

import java.util.List;

/**
 * Created by rh on 2017/12/11.
 */

public class CourseAdapter extends BaseRecyclerAdapter<CourseBean> {


    public CourseAdapter(Context context, int layoutId, List<CourseBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, CourseBean s) {


    }
}
