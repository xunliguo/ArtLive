package com.example.rh.artlive.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rh.artlive.R;

/**
 * Created by rh on 2017/12/12.
 * 虫窝直播我的
 */

public class WormMineFragment extends BaseFragment {

    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savesInstanceState) {
        super.onCreateView(inflater, container, savesInstanceState);
        view = inflater.inflate(R.layout.fragment_worm_mine, null);

        return view;
    }
}
