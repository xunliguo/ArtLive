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

public class MactchDataAdapter extends RvAdapter<UserFollowBean> {

    private int checkedPosition;

    public void setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
        notifyDataSetChanged();
    }

    public MactchDataAdapter(Context context, List<UserFollowBean> list, RvListener listener) {
        super(context, list, listener);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_match_list;
    }

    @Override
    protected RvHolder getHolder(View view, int viewType) {
        return new SortHolder(view, viewType, listener);
    }

    private class SortHolder extends RvHolder<UserFollowBean> {

        private View mView;
        private CircleImageView imageView;

        SortHolder(View itemView, int type, RvListener listener) {
            super(itemView, type, listener);
            this.mView = itemView;
            imageView=(CircleImageView)itemView.findViewById(R.id.follow_user_icon);

        }

        @Override
        public void bindHolder(UserFollowBean s, int position) {
            Log.e("用户名"+s.getName());

            Glide.with(mContext).load(s.getUser_img()).into(imageView);


        }
    }
}
