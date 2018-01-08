package com.example.rh.artlive.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.LiveHotBean;
import com.example.rh.artlive.bean.MatchBean;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;
import com.example.rh.artlive.view.RoundImageView;

import java.util.List;

/**
 * Created by rh on 2017/12/29.
 */

public class LiveHotAdapter extends BaseRecyclerAdapter<LiveHotBean> {

    public LiveHotAdapter(Context context, int layoutId, List<LiveHotBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, LiveHotBean s) {


        TextView title= holder.getView(R.id.tvCityprice);
        TextView count=holder.getView(R.id.tvCitypricevalue);
        TextView name=holder.getView(R.id.tvCity);
        name.setText(s.getName());
        title.setText("在播："+s.getLive_title());
        count.setText(s.getUsernum()+"在看");


        RoundImageView roundImageView=holder.getView(R.id.ivAvatar);
        Glide.with(mContext).load(s.getPoster()).into(roundImageView);

    }
}
