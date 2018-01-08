package com.example.rh.artlive.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.activity.ApplySchoolActivity;
import com.example.rh.artlive.bean.SchoolBean;
import com.example.rh.artlive.bean.ShowBean;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by rh on 2017/12/23.
 */

public class AppSchoolAdapter extends BaseRecyclerAdapter<SchoolBean> {

    public AppSchoolAdapter(Context context, int layoutId, List<SchoolBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, SchoolBean s) {
        TextView name=holder.getView(R.id.name);
        ImageView school_true=holder.getView(R.id.school_true);
        if (s.getSchool_id().equals(ApplySchoolActivity.flag)){
            school_true.setVisibility(View.VISIBLE);
        }else{
            school_true.setVisibility(View.GONE);
        }
        name.setText(s.getSchool_name());

    }
}
