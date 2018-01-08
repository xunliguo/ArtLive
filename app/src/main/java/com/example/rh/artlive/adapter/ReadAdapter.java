package com.example.rh.artlive.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.bean.ReadBean;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by rh on 2017/12/8.
 */

public class ReadAdapter extends BaseRecyclerAdapter<ReadBean> {


    public ReadAdapter(Context context, int layoutId, List<ReadBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, ReadBean s) {

        TextView name=holder.getView(R.id.reading_name);
        name.setText(s.getTitle());

    }
}
