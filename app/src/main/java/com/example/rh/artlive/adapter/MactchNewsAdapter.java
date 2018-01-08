package com.example.rh.artlive.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.UserFollowBean;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.view.CircleImageView;

import java.util.List;

/**
 * Created by rh on 2018/1/5.
 */

public class MactchNewsAdapter extends RvAdapter<UserFollowBean> {

    private int checkedPosition;

    public void setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
        notifyDataSetChanged();
    }

    public MactchNewsAdapter(Context context, List<UserFollowBean> list, RvListener listener) {
        super(context, list, listener);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_news_list;
    }

    @Override
    protected RvHolder getHolder(View view, int viewType) {
        return new SortHolder(view, viewType, listener);
    }

    private class SortHolder extends RvHolder<UserFollowBean> {

        private View mView;
        private TextView mName;
        private ImageView imageView;
        private ImageView imageView1;
        private ImageView imageView2;

        SortHolder(View itemView, int type, RvListener listener) {
            super(itemView, type, listener);
            this.mView = itemView;
            mName=(TextView)itemView.findViewById(R.id.top_name);
            imageView=(ImageView)itemView.findViewById(R.id.content);
            imageView1=(ImageView)itemView.findViewById(R.id.content1);
            imageView2=(ImageView)itemView.findViewById(R.id.content2);
        }

        @Override
        public void bindHolder(UserFollowBean s, int position) {
            Log.e("用户名"+s.getForum_title());
            mName.setText(s.getForum_title());
            Glide.with(mContext).load(s.getA()).into(imageView);
            Glide.with(mContext).load(s.getB()).into(imageView1);
            Glide.with(mContext).load(s.getC()).into(imageView2);

        }
    }
}
