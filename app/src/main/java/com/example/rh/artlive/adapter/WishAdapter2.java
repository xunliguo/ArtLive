package com.example.rh.artlive.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.WishBean;
import com.example.rh.artlive.bean.WishBean2;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by rh on 2017/12/19.
 */

public class WishAdapter2 extends BaseRecyclerAdapter<WishBean> {

    public WishAdapter2(Context context, int layoutId, List<WishBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, WishBean s) {

        TextView name=holder.getView(R.id.teach_name_one);
        TextView teach_content_one=holder.getView(R.id.teach_content_one);
        ImageView tea_user_image=holder.getView(R.id.tea_user_image);
        name.setText(s.getName());
        teach_content_one.setText(s.getSchool());
        Glide.with(mContext).load(s.getImg()).into(tea_user_image);

    }
}
