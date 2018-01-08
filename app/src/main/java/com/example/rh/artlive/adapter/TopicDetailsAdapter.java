package com.example.rh.artlive.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.FollowBean;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.bean.TopicDetailsBean;
import com.example.rh.artlive.photo.DynamicGridAdapter;
import com.example.rh.artlive.photo.ImagePagerActivity;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;
import com.example.rh.artlive.view.CircleImageView;
import com.example.rh.artlive.view.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rh on 2017/12/23.
 */

public class TopicDetailsAdapter  extends BaseRecyclerAdapter<FollowBean> {

    private ArrayList<String> picarr1 =new ArrayList<>();


    public TopicDetailsAdapter(Context context, int layoutId, List<FollowBean> datas,ArrayList<String> list_det1) {
        super(context, layoutId, datas);
        mContext=context;
        this.picarr1=list_det1;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, FollowBean s) {

        TextView name=holder.getView(R.id.user_title);
        TextView mShareView=holder.getView(R.id.textView3);
        TextView mZanView=holder.getView(R.id.textView2);
        TextView time=holder.getView(R.id.time);
        TextView content=holder.getView(R.id.content);
        CircleImageView circleImageView=holder.getView(R.id.follow_user_icon);
        NoScrollGridView picarr=holder.getView(R.id.gridView_picarr);


        int size1 = picarr1.size();
        final String[] array1 = (String[]) picarr1.toArray(new String[size1]);
        if (array1.length > 0) {
            picarr.setVisibility(View.VISIBLE);
            picarr.setAdapter(new DynamicGridAdapter(array1, mContext));
            picarr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    imageBrower(position, array1);
                }
            });
        } else {
            picarr.setVisibility(View.GONE);
        }


        content.setText(s.getForum_content());
        time.setText(s.getTime());
        Glide.with(mContext).load(s.getUser_img()).into(circleImageView);
        name.setText(s.getUser_nickname());
        mShareView.setText(s.getZfcount());
        mZanView.setText(s.getZan());

    }

    /**
     * 点击图片放大浏览
     * @param position
     * @param urls
     */
    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        mContext.startActivity(intent);
    }
}
