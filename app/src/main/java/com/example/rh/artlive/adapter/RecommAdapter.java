package com.example.rh.artlive.adapter;

import android.content.Context;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.ReadBean;
import com.example.rh.artlive.bean.WormBean;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;
import com.example.rh.artlive.view.RoundImageView;

import java.util.List;

/**
 * Created by rh on 2017/12/11.
 */

public class RecommAdapter extends BaseRecyclerAdapter<WormBean> {

    public RecommAdapter(Context context, int layoutId, List<WormBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, WormBean s) {


        TextView name=holder.getView(R.id.tvCity);
        TextView city=holder.getView(R.id.tvCityprice);
        TextView cityvalue=holder.getView(R.id.tvCitypricevalue);
        TextView content=holder.getView(R.id.deposit);
        RoundImageView image=holder.getView(R.id.ivAvatar);
        name.setText(s.getUser_nickname());
        city.setText(s.getUser_attendschool());
        cityvalue.setText(s.getUser_major());
        content.setText(s.getUser_introduce());
        Glide.with(mContext).load(s.getUser_img()).into(image);
        Log.e("图片"+s.getPoster());

    }

}
