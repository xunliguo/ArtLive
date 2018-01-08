package com.example.rh.artlive.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.BookBean;
import com.example.rh.artlive.bean.WishBean;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by rh on 2017/12/19.
 */

public class BookAllAdapter extends BaseRecyclerAdapter<BookBean> {

    private String tag_name;

    public BookAllAdapter(Context context, int layoutId, List<BookBean> datas, String tag_name) {
        super(context, layoutId, datas);
        mContext=context;
        this.tag_name=tag_name;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, BookBean s) {
        TextView name=holder.getView(R.id.tv_sort);
        name.setText(s.getName());
        if (tag_name.equals(s.getName())){
            name.setTextColor(Color.parseColor("#ffff6900"));
        }else{
            name.setTextColor(Color.parseColor("#000000"));
        }
    }
}
