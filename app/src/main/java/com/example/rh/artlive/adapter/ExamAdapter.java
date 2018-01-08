package com.example.rh.artlive.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.activity.ExamActivity;
import com.example.rh.artlive.bean.ExamBean;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by rh on 2017/12/16.
 */

public class ExamAdapter extends BaseRecyclerAdapter<ExamBean> {

    public ExamAdapter(Context context, int layoutId, List<ExamBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, ExamBean s) {

        TextView content=holder.getView(R.id.go_exam);
        TextView time=holder.getView(R.id.exam_time);
        TextView one=holder.getView(R.id.one);
        TextView two=holder.getView(R.id.two);
        TextView third=holder.getView(R.id.third);
        one.setText(s.getSchool_name());
        two.setText(s.getMajor());
        third.setText(s.getTest());
        content.setText(s.getContent());
        time.setText(ExamActivity.StampToDate(s.getTime()));


    }
}
