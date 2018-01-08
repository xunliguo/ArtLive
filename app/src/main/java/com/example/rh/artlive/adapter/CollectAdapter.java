package com.example.rh.artlive.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.HomeBean;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;
import com.example.rh.artlive.view.ReboundScrollView_Horizontal;

import java.util.List;

/**
 * Created by rh on 2017/11/28.
 */

public class CollectAdapter  extends BaseRecyclerAdapter<PhotoBean> {


    public CollectAdapter(Context context, int layoutId, List<PhotoBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, PhotoBean s) {

        ImageView imageView=holder.getView(R.id.collect_iamge);
        TextView title=holder.getView(R.id.collect_title);
        TextView content=holder.getView(R.id.collect_title_value);
        TextView time=holder.getView(R.id.collect_time);

        Glide.with(mContext).load(s.getCollection_img()).into(imageView);
        time.setText(s.getCollection_addtime());
        title.setText(s.getCollection_title());
        content.setText(s.getCollection_type());

    }
}
