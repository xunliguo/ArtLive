package com.example.rh.artlive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.Backbean;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by rh on 2017/12/12.
 */

public class BackMusicAdapter extends  BaseRecyclerAdapter<Backbean> {



    public BackMusicAdapter(Context context, int layoutId, List<Backbean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, Backbean s) {

        TextView name1=holder.getView(R.id.apply_phone);
        name1.setText(s.getTitle());
    }
}
