package com.example.rh.artlive.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.MatchBean;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by rh on 2017/12/4.
 */

public class MactchAdapter extends BaseRecyclerAdapter<MatchBean> {

    public MactchAdapter(Context context, int layoutId, List<MatchBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, MatchBean s) {
//        com.example.rh.artlive.util.Log.e("比赛"+s.getTitle());

        TextView title=holder.getView(R.id.key_url);
        ImageView image=holder.getView(R.id.key_name);
        TextView time=holder.getView(R.id.match_time);
        TextView area=holder.getView(R.id.match_area);
        TextView main=holder.getView(R.id.match_main);
        TextView fee=holder.getView(R.id.match_fee);
//        time.setText(s.get);
        Glide.with(mContext).load(s.getImg()).into(image);
        title.setText(s.getTitle());
        area.setText(s.getArea());
        main.setText(s.getSponsor());
        fee.setText(s.getPrice());

    }
}
