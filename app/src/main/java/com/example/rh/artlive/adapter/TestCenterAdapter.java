package com.example.rh.artlive.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rh.artlive.R;
import com.example.rh.artlive.activity.ApplySchoolActivity;
import com.example.rh.artlive.activity.TestCenterActivity;
import com.example.rh.artlive.bean.SchoolBean;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by rh on 2017/12/23.
 */

public class TestCenterAdapter extends BaseRecyclerAdapter<SchoolBean> {



    public TestCenterAdapter(Context context, int layoutId, List<SchoolBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, SchoolBean s) {

        TextView name=holder.getView(R.id.city);
        name.setText(s.getInfo());
        ImageView school_true=holder.getView(R.id.school_true);
        if (s.getInfo().equals(TestCenterActivity.flag)){
            school_true.setVisibility(View.VISIBLE);
        }else{
            school_true.setVisibility(View.GONE);
        }
    }
}
