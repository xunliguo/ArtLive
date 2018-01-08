package com.example.rh.artlive.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;
import com.example.rh.artlive.view.XCRoundImageView;

import java.util.List;

/**
 * Created by rh on 2017/11/28.
 */

public class OrderAdapter extends BaseRecyclerAdapter<PhotoBean> {


    public OrderAdapter(Context context, int layoutId, List<PhotoBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, PhotoBean s) {

        ImageView image=holder.getView(R.id.user_ordeer);
        TextView name=holder.getView(R.id.number);
        TextView title=holder.getView(R.id.leaseprice);
        TextView classtime=holder.getView(R.id.leaseprice11);
        TextView allprice=holder.getView(R.id.allPrice);
        TextView time=holder.getView(R.id.price);
        TextView status=holder.getView(R.id.priceValue);
        classtime.setText(s.getTime());
        name.setText(s.getUser_name());
        title.setText(s.getTitle());
        time.setText(s.getPublished());
        allprice.setText("合计"+s.getPrice());
        if ("1".equals(s.getState())){
            status.setText("已付款");
        }else if("2".equals(s.getState())){
            status.setText("已取消");
        }else if("0".equals(s.getState())){
            status.setText("待付款");
        }

        Glide.with(mContext).load(s.getUser_img()).into(image);

    }
}
