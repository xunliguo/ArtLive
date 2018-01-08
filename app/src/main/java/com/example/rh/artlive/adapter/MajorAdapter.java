package com.example.rh.artlive.adapter;

import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.widget.TextView;

import com.example.rh.artlive.R;
import com.example.rh.artlive.bean.ExamBean;
import com.example.rh.artlive.bean.SchoolBean;
import com.example.rh.artlive.view.BaseRecyclerAdapter;
import com.example.rh.artlive.view.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by rh on 2017/12/23.
 */

public class MajorAdapter extends BaseRecyclerAdapter<SchoolBean> {

    String major_title;
    String major_content;


    public MajorAdapter(Context context, int layoutId, List<SchoolBean> datas,String major_title,String major_content) {
        super(context, layoutId, datas);
        mContext=context;
        this.major_title=major_title;
        this.major_content=major_content;
    }
    @Override
    public void convert(BaseRecyclerViewHolder holder, SchoolBean s) {

        TextView name=holder.getView(R.id.name);
        TextView name_content=holder.getView(R.id.school_true);
        name.setText(s.getMajortop());
        if (major_title.equals(s.getMajortop())){
            name_content.setText(major_content);
        }else{
            name_content.setText("");
        }

    }
}
