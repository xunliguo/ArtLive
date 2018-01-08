package com.example.rh.artlive.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.ExportBean;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;
import com.example.rh.artlive.view.CircleImageView;

import java.util.List;

/**
 * Created by rh on 2017/12/11.
 */

public class ExportDetailsAdapter extends BaseRecyclerAdapter<ExportBean> {

    public ExportDetailsAdapter(Context context, int layoutId, List<ExportBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, ExportBean s) {

        CircleImageView circleImageView=holder.getView(R.id.follow_user_icon);
        TextView title=holder.getView(R.id.user_title);
        TextView time=holder.getView(R.id.time);
        TextView content=holder.getView(R.id.content);
        TextView music_name=holder.getView(R.id.music_name);

        title.setText(s.getExpert_name());
        time.setText(s.getTime());
        content.setText(s.getTitle());
        music_name.setText(s.getIntroduce());
        Glide.with(mContext).load(s.getExpert_img()).into(circleImageView);


    }
}
