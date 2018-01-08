package com.example.rh.artlive.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.activity.PhotoActivity;
import com.example.rh.artlive.bean.ClassBean;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by rh on 2017/12/26.
 */

public class ClassAdapter extends BaseRecyclerAdapter<ClassBean> {


    public ClassAdapter(Context context, int layoutId, List<ClassBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }

    @Override
    public void convert(BaseRecyclerViewHolder holder, final ClassBean s) {

        TextView title=holder.getView(R.id.class_title);
        TextView time=holder.getView(R.id.class_price);
        title.setText(s.getTitle());
        time.setText(s.getPrice()+"元/课时");

    }
}
