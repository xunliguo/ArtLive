package com.example.rh.artlive.adapter;

import android.content.Context;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.VoiceBean;
import com.example.rh.artlive.bean.WormBean;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;
import com.example.rh.artlive.view.CircleImageView;

import java.util.List;

/**
 * Created by rh on 2017/12/11.
 */

public class VoiceAdapter extends BaseRecyclerAdapter<VoiceBean> {

    public VoiceAdapter(Context context, int layoutId, List<VoiceBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, VoiceBean s) {

        TextView name=holder.getView(R.id.user_title);
        TextView time=holder.getView(R.id.time);
        TextView content=holder.getView(R.id.content);
        CircleImageView follow_user_icon=holder.getView(R.id.follow_user_icon);
        name.setText(s.getUser_nickname());
        time.setText(s.getCreatetime());
        content.setText(s.getIntroduce());
        Glide.with(mContext).load(s.getUser_img()).into(follow_user_icon);

    }
}
