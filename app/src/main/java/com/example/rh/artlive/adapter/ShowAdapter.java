package com.example.rh.artlive.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.ShowBean;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by rh on 2017/12/12.
 */

public class ShowAdapter  extends BaseRecyclerAdapter<ShowBean> {


    public ShowAdapter(Context context, int layoutId, List<ShowBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, ShowBean s) {

        ImageView imageView=holder.getView(R.id.export_image);
        Glide.with(mContext).load(s.getImg()).into(imageView);


    }
}
