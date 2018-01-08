package com.example.rh.artlive.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.activity.TeacherActivity;
import com.example.rh.artlive.bean.UserFollowBean;
import com.example.rh.artlive.util.Log;

import java.util.List;

/**
 * Created by rh on 2018/1/3.
 */

public class UserDataAdapter extends RvAdapter<UserFollowBean>{

    private int checkedPosition;

    public void setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
        notifyDataSetChanged();
    }

    public UserDataAdapter(Context context, List<UserFollowBean> list, RvListener listener) {
        super(context, list, listener);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_user_list;
    }

    @Override
    protected RvHolder getHolder(View view, int viewType) {
        return new SortHolder(view, viewType, listener);
    }

    private class SortHolder extends RvHolder<UserFollowBean> {

        private TextView tvName;
        private TextView mTeahcerName;
        private ImageView mUp;
        private ImageView mDownmDown;
        private ImageView mSex;
        private View mView;

        SortHolder(View itemView, int type, RvListener listener) {
            super(itemView, type, listener);
            this.mView = itemView;
            tvName = (TextView) itemView.findViewById(R.id.tv_sort);
            mTeahcerName=(TextView)itemView.findViewById(R.id.teach_name_two);
            mUp=(ImageView)itemView.findViewById(R.id.tea_user_image1);
            mDownmDown=(ImageView)itemView.findViewById(R.id.wish_icon1);
            mSex=(ImageView)itemView.findViewById(R.id.sex);
        }

        @Override
        public void bindHolder(UserFollowBean s, int position) {
            Log.e("用户名"+s.getName());
            tvName.setText(s.getSchool());
            mTeahcerName.setText(s.getName());
            Glide.with(mContext).load(s.getImg()).into(mDownmDown);
            Glide.with(mContext).load(s.getWishimg()).into(mUp);
            if ("男".equals(s.getSex())){
                mSex.setImageResource(R.mipmap.sex_woman1);
            }else{
                mSex.setImageResource(R.mipmap.sex_woman);
            }
            Log.e("用户名值"+tvName.getText()+"".toString().trim());

        }
    }
}
