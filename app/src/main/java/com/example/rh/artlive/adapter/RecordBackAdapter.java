package com.example.rh.artlive.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.RecordBackBean;
import com.example.rh.artlive.bean.WormBean;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rh on 2018/1/2.
 */

public class RecordBackAdapter extends BaseRecyclerAdapter<RecordBackBean> {

    public RecordBackAdapter(Context context, int layoutId, List<RecordBackBean> datas) {
        super(context, layoutId, datas);

    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, RecordBackBean s) {

        ImageView imageView=holder.getView(R.id.image);
        Glide.with(mContext).load(s.getPoster()).into(imageView);
        Log.e("主题图片"+s.getPoster());


    }
}
