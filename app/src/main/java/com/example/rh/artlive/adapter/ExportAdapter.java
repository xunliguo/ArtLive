package com.example.rh.artlive.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.CourseBean;
import com.example.rh.artlive.bean.ExportBean;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by rh on 2017/12/11.
 */

public class ExportAdapter extends BaseRecyclerAdapter<ExportBean> {

    public ExportAdapter(Context context, int layoutId, List<ExportBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, ExportBean s) {

        ImageView export_image=holder.getView(R.id.export_image);

        Glide.with(mContext).load(s.getPoster()).into(export_image);

        Log.e("图片"+s.getPoster());


    }
}
