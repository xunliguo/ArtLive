package com.example.rh.artlive.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.rh.artlive.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rh on 2017/9/14.
 */

public class PurChaseFragment extends BaseFragment{
    private View view;
    private TabLayout mTabLayout;

    // 标志位，标志已经初始化完成。
    private boolean isPrepared;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);
        view=inflater.inflate(R.layout.fargment_purchase,null);
        isPrepared = true;
        lazyLoad();
        return view;

    }
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        isPrepared = false;
        init();
        setListener();
    }

    private void init(){
//        mTabLayout = (TabLayout)view.findViewById(R.id.network_tabLayout);
//        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }
    private void setListener(){

    }

}
