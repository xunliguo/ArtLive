package com.example.rh.artlive.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;
import com.example.rh.artlive.activity.PhotoActivity;
import com.example.rh.artlive.bean.OutFitBean;
import com.example.rh.artlive.bean.PhotoBean;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rh on 2017/11/27.
 */

public class PhotoAdapter extends BaseRecyclerAdapter<PhotoBean> {

    private CheckInterface checkInterface;




    public PhotoAdapter(Context context, int layoutId, List<PhotoBean> datas) {
        super(context, layoutId, datas);
        mContext=context;
    }


    /**
     * 单选接口
     *
     * @param checkInterface
     */
    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, final PhotoBean s) {

        ImageView imageView=holder.getView(R.id.ivAvatar);
        CheckBox checkBox=holder.getView(R.id.cb);

        boolean choosed = s.isChoosed();
        if (choosed){
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s.setIsChecked(((CheckBox) v).isChecked());
                checkInterface.checkGroup(s.getI(),((CheckBox) v).isChecked());//向外暴露接口
            }
        });

        Glide.with(mContext).load(s.getPicarr_img_url()).into(imageView);


        if (PhotoActivity.flag==1){
            checkBox.setVisibility(View.VISIBLE);
        }else{
            checkBox.setVisibility(View.GONE);
        }
    }


    /**
     * 复选框接口
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *  @param position  元素位置
         * @param isChecked 元素选中与否
         */
        void checkGroup(int position, boolean isChecked);
    }
}
