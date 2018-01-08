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

public class TrueMainAdapter extends BaseRecyclerAdapter<RightBean> {

    public TrueMainAdapter(Context context, int layoutId, List<RightBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, RightBean s) {

        TextView title=holder.getView(R.id.true_title);
        TextView stuay=holder.getView(R.id.true_study);
        title.setText(s.getTitle());


    }
}
