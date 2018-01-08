package com.example.rh.artlive.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.BookBean;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by rh on 2017/12/19.
 */

public class BookContentAdapter extends BaseRecyclerAdapter<BookBean> {

    public BookContentAdapter(Context context, int layoutId, List<BookBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, BookBean s) {

        Log.e("图片"+s.getImg());

        ImageView image=holder.getView(R.id.ivAvatar);
        TextView name=holder.getView(R.id.tvCity);
        name.setText(s.getBname());
        Glide.with(mContext).load(s.getImg()).into(image);

    }
}
