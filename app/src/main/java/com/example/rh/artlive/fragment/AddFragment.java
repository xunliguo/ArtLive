package com.example.rh.artlive.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rh.artlive.R;
import com.example.rh.artlive.activity.ArticleActivity;
import com.example.rh.artlive.activity.PostActivity;
import com.example.rh.artlive.util.DataString;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by rh on 2017/11/20.
 */

public class AddFragment extends BaseFragment implements View.OnClickListener{

    private View view;
    private TextView mDay;
    private TextView mMonthy;
    private TextView mWeek;
    private TextView mView;
    private TextView mView6;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savesInstanceState) {
        super.onCreateView(inflater, container, savesInstanceState);
        view = inflater.inflate(R.layout.fragment_add, null);
        init();
        setListener();
        return view;
    }
    private void init(){
        mDay=(TextView)view.findViewById(R.id.key_name);
        mMonthy=(TextView)view.findViewById(R.id.price);
        mWeek=(TextView)view.findViewById(R.id.key_url);
        mView=(TextView)view.findViewById(R.id.me_user_tv_cart);
        mView6=(TextView)view.findViewById(R.id.me_user_tv_favorite3);

        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mDay.setText(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
        mMonthy.setText(String.valueOf(c.get(Calendar.MONTH) + 1)+"/"+String.valueOf(c.get(Calendar.YEAR)));
        if("1".equals(String.valueOf(c.get(Calendar.DAY_OF_WEEK)))){
            mWeek.setText("星期天");
        }else if("2".equals(String.valueOf(c.get(Calendar.DAY_OF_WEEK)))){
            mWeek.setText("星期一");
        }else if("3".equals(String.valueOf(c.get(Calendar.DAY_OF_WEEK)))){
            mWeek.setText("星期二");
        }else if("4".equals(String.valueOf(c.get(Calendar.DAY_OF_WEEK)))){
            mWeek.setText("星期三");
        }else if("5".equals(String.valueOf(c.get(Calendar.DAY_OF_WEEK)))){
            mWeek.setText("星期四");
        }else if("6".equals(String.valueOf(c.get(Calendar.DAY_OF_WEEK)))){
            mWeek.setText("星期五");
        }else if("7".equals(String.valueOf(c.get(Calendar.DAY_OF_WEEK)))){
            mWeek.setText("星期六");
        }
    }
    private void setListener(){
        mView.setOnClickListener(this);
        mView6.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.me_user_tv_cart:
                Intent intent=new Intent(getActivity(), ArticleActivity.class);
                startActivity(intent);
                break;
            case R.id.me_user_tv_favorite3:
                Intent intent1=new Intent(getActivity(), PostActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
