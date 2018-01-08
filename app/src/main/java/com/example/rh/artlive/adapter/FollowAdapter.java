package com.example.rh.artlive.adapter;

import android.content.Context;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.FansBean;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;
import com.example.rh.artlive.view.CircleImageView;

import java.util.List;

/**
 * Created by rh on 2017/12/7.
 */

public class FollowAdapter  extends BaseRecyclerAdapter<FansBean> {

    public FollowAdapter(Context context, int layoutId, List<FansBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, FansBean s) {

        CircleImageView imageView=holder.getView(R.id.collect_iamge);
        TextView title=holder.getView(R.id.collect_title);
        TextView content=holder.getView(R.id.collect_title_value);
        TextView time=holder.getView(R.id.collect_time);

        Glide.with(mContext).load(s.getImg()).into(imageView);
        time.setText(s.getCity()+"/"+s.getMajor());
        title.setText(s.getName());
        content.setText(s.getBrand());

    }
}
