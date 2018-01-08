package com.example.rh.artlive.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rh.artlive.R;

/**
 * Created by rh on 2017/11/25.
 */

public class Fragmentthird extends Fragment{

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savesInstanceState) {
        super.onCreateView(inflater, container, savesInstanceState);
        view = inflater.inflate(R.layout.third, null);
        return view;
    }
}
