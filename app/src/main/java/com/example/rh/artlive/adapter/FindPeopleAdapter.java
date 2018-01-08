package com.example.rh.artlive.adapter;

import android.content.Context;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.FansBean;
import com.example.rh.artlive.bean.FindPeopleBean;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;
import com.example.rh.artlive.view.CircleImageView;

import java.util.List;

/**
 * Created by rh on 2017/12/12.
 */

public class FindPeopleAdapter extends BaseRecyclerAdapter<FindPeopleBean> {

    public FindPeopleAdapter(Context context, int layoutId, List<FindPeopleBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, FindPeopleBean s) {

        Log.e("找人"+s.getName());

        TextView name=holder.getView(R.id.collect_title);
        TextView content=holder.getView(R.id.collect_time);
        TextView fans=holder.getView(R.id.collect_fans);
        CircleImageView name_image=holder.getView(R.id.collect_iamge);

        name.setText(s.getName());
        content.setText(s.getMajor());
        fans.setText("粉丝数:"+s.getFanscount());
        Glide.with(mContext).load(s.getImg()).into(name_image);

    }
}
