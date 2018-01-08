package com.example.rh.artlive.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.WishBean;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by rh on 2017/12/19.
 */

public class WormLiveAdapter extends BaseRecyclerAdapter<WishBean> {

    private String tag_name;

    public WormLiveAdapter(Context context, int layoutId, List<WishBean> datas,String tag_name) {
        super(context, layoutId, datas);
        mContext=context;
        this.tag_name=tag_name;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, WishBean s) {
        TextView name=holder.getView(R.id.tv_sort);
        name.setText(s.getTag_name());
        if (tag_name.equals(s.getTag_name())){
            name.setTextColor(Color.parseColor("#ffff6900"));
        }else{
            name.setTextColor(Color.parseColor("#000000"));
        }
    }
}
