package com.example.rh.artlive.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.NewsMineBean;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by rh on 2017/12/11.
 */

public class NewsMineAdapter extends BaseRecyclerAdapter<NewsMineBean> {

    public NewsMineAdapter(Context context, int layoutId, List<NewsMineBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, NewsMineBean s) {

        TextView user_name=holder.getView(R.id.collect_title);
        TextView user_content=holder.getView(R.id.collect_time);
        TextView user_titme=holder.getView(R.id.news_mine_time);
        ImageView user_image=holder.getView(R.id.collect_iamge);
        ImageView my_image=holder.getView(R.id.my_image);
        user_titme.setText(s.getAttime());
        user_name.setText(s.getUser_nickname());
        Glide.with(mContext).load(s.getUser_img()).into(user_image);
        Glide.with(mContext).load(s.getMy_img()).into(my_image);
    }
}
