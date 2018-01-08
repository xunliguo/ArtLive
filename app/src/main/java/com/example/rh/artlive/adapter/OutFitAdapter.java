package com.example.rh.artlive.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapper;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.HomeBean;
import com.example.rh.artlive.bean.OutFitBean;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;
import com.example.rh.artlive.view.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rh on 2017/11/27.
 */

public class OutFitAdapter extends BaseRecyclerAdapter<OutFitBean> {



    public OutFitAdapter(Context context, int layoutId, List<OutFitBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, OutFitBean s) {

        TextView name=holder.getView(R.id.tvCity);
        TextView city=holder.getView(R.id.tvCityprice);
        TextView cityvalue=holder.getView(R.id.tvCitypricevalue);
        TextView content=holder.getView(R.id.deposit);
        RoundImageView image=holder.getView(R.id.ivAvatar);
        name.setText(s.getInst_name());
        city.setText(s.getAddress());
        cityvalue.setText(s.getMajor());
        content.setText(s.getIntroduce());
        Glide.with(mContext).load(s.getPoster()).into(image);
        Log.e("图片"+s.getPoster());

    }
}
