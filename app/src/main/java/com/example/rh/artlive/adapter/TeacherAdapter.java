package com.example.rh.artlive.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.HomeBean;
import com.example.rh.artlive.fragment.LeaseOrderFragment;
import com.example.rh.artlive.photo.DynamicGridAdapter;
import com.example.rh.artlive.photo.ImagePagerActivity;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;
import com.example.rh.artlive.view.NoScrollGridView;
import com.example.rh.artlive.view.ReboundScrollView_Horizontal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rh on 2017/11/27.
 */

public class TeacherAdapter extends BaseRecyclerAdapter<HomeBean> {

    private ArrayList<String> list1 =new ArrayList<>();


    public TeacherAdapter(Context context, int layoutId, List<HomeBean> datas,ArrayList<String> list) {
        super(context, layoutId, datas);
        mContext=context;
        list1=list;
    }


    @Override
    public void convert(BaseRecyclerViewHolder holder, HomeBean s) {

        Log.e("老师"+list1);


        TextView name=holder.getView(R.id.teacher_name);
        TextView help=holder.getView(R.id.teacher_help);
        TextView details=holder.getView(R.id.teacher_details);
        ImageView image=holder.getView(R.id.follow_user_image);
        ImageView image_one=holder.getView(R.id.follow_user_image_one);
        ImageView image_two=holder.getView(R.id.follow_user_image_two);
        ImageView image_third=holder.getView(R.id.follow_user_image_third);
        ImageView image_four=holder.getView(R.id.follow_user_image_four);

//        NoScrollGridView noScrollGridView=holder.getView(R.id.gridView);
        name.setText(s.getTeacher_name());
        help.setText("已帮助"+s.getHelp()+"人");
        details.setText(s.getTeacher_major());

        int size=list1.size();
        final String[] array = (String[])list1.toArray(new String[size]);

        for (int i=0;i<array.length;i++){
            if (i==0){
                Glide.with(mContext).load(array[i]).into(image);
            }
            else if (i==1){
                Glide.with(mContext).load(array[i]).into(image_one);
            }
            else if (i==2){
                Glide.with(mContext).load(array[i]).into(image_two);
            }else if (i==3){
                Glide.with(mContext).load(array[i]).into(image_third);
            }else if (i==4){
                Glide.with(mContext).load(array[i]).into(image_four);
            }
        }


        ImageView mHeader=holder.getView(R.id.header_image);
        Glide.with(mContext).load(s.getUser_img()).into(mHeader);
    }

}
