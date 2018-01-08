package com.example.rh.artlive.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.SearchBean;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by rh on 2017/9/12.
 */

public class SearchAdapter extends BaseRecyclerAdapter<SearchBean> {

    public SearchAdapter(Context context, int layoutId, List<SearchBean> datas) {
        super(context, layoutId,datas);
        mContext=context;

    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, SearchBean s) {
        ImageView delete=holder.getView(R.id.ivAvatar);
        TextView title=holder.getView(R.id.tvCity);
        title.setText(s.getTitle());
        Glide.with(mContext).load(UrlConstant.URL_PEOPLE+s.getImg_url()).into(delete);

    }
}
