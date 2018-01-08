package com.example.rh.artlive.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.PostDetailsBean;
import com.example.rh.artlive.photo.DynamicGridAdapter;
import com.example.rh.artlive.photo.ImagePagerActivity;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;
import com.example.rh.artlive.view.CircleImageView;
import com.example.rh.artlive.view.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rh on 2017/12/21.
 */

public class VideoDetailsAdapter extends BaseRecyclerAdapter<PostDetailsBean> {
    private ArrayList<String> list_details =new ArrayList<>();

    private String mPost_Title;
    private String mPost_Content;
    private String mPost_image;




    public VideoDetailsAdapter(Context context, int layoutId, List<PostDetailsBean> datas, ArrayList<String> list_det) {
        super(context, layoutId, datas);
        mContext=context;
        list_details=list_det;
        this.mPost_Title=mPost_Title;
        this.mPost_Content=mPost_Content;
        this.mPost_image=mPost_image;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, PostDetailsBean s) {

        LinearLayout mPostHeadeer=holder.getView(R.id.post_user);
        TextView title=holder.getView(R.id.user_title);
        TextView content=holder.getView(R.id.content);
        CircleImageView image=holder.getView(R.id.follow_user_icon);


        TextView mNameValue=holder.getView(R.id.name1_value);
        TextView mUserName=holder.getView(R.id.user_name_value);
        TextView mUserContent=holder.getView(R.id.user_content_value);
        TextView mUserData=holder.getView(R.id.data_value);
        CircleImageView mUserImage=holder.getView(R.id.roundImageView);
        NoScrollGridView picarr=holder.getView(R.id.gridView_picarr);
        RelativeLayout mGridLayout=holder.getView(R.id.grid_layout);

        if (s.getI()==0){
            mPostHeadeer.setVisibility(View.VISIBLE);
            title.setText(mPost_Title);
            content.setText(mPost_Content);
            Glide.with(mContext).load(mPost_image).into(image);
        }else{
            mPostHeadeer.setVisibility(View.GONE);
        }

        Log.e("名字"+s.getUser_name());

        mUserName.setText(s.getUser_name());
        mUserContent.setText(s.getContent());
        mUserData.setText(s.getTime());
        Glide.with(mContext).load(s.getUser_pic()).into(mUserImage);

    }
}
