package com.example.rh.artlive.adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.OutFitBean;
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
 * Created by rh on 2017/12/9.
 */

public class PostDetailsAdapter extends BaseRecyclerAdapter<PostDetailsBean> {


    private ArrayList<String> list_details =new ArrayList<>();

    private String mPost_Title;
    private String mPost_Content;
    private String mPost_image;

    private int commleght;




    public PostDetailsAdapter(Context context, int layoutId, List<PostDetailsBean> datas,ArrayList<String> list_det,String mPost_Title,String mPost_Content,String mPost_image,int commleght) {
        super(context, layoutId, datas);
        mContext=context;
        list_details=list_det;
        this.mPost_Title=mPost_Title;
        this.mPost_Content=mPost_Content;
        this.mPost_image=mPost_image;
        this.commleght=commleght;
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
        Log.e("适配器大小"+commleght);

        if (s.getI()==0||commleght==0){
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


        int size1=list_details.size();
        final String[] array1 = (String[])list_details.toArray(new String[size1]);
        Log.e("picarr"+array1.length);

        if (array1.length>0){
            picarr.setVisibility(View.VISIBLE);
            picarr.setAdapter(new DynamicGridAdapter(array1,mContext));
            picarr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    imageBrower(position,array1);
                }
            });
        }else{
            picarr.setVisibility(View.GONE);
        }

        /**
         * 通过设置控件高度，使GirdView最多显示九张图片
         */
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) picarr.getLayoutParams();
        params.height=950;//设置当前控件布局的高度
        picarr.setLayoutParams(params);//将设置好的布局参数应用到控件中
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
