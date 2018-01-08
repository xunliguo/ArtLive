package com.example.rh.artlive.util;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.rh.artlive.R;


/**
 * Created by color on 16/4/26.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LoadingDialog extends DialogFragment {
    View view;
    TextView tx;
    ProgressWheel progressWheel;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.my_dialog, container, false);
        tx = (TextView) view.findViewById(R.id.load_txt);
        progressWheel = (ProgressWheel) view.findViewById(R.id.load_progress);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setDimAmount(0f);//背景黑暗度
        return view;
    }

    public void setTitle(String title) {
        if (tx == null) {
            tx = (TextView) view.findViewById(R.id.load_txt);
        }
        tx.setText(title);
    }

    public void setTitle(int title) {
        if (tx == null) {
            tx = (TextView) view.findViewById(R.id.load_txt);
        }
        tx.setText(getActivity().getResources().getText(title));
    }


    public void setTitleColor(int color) {
        tx.setTextColor(color);
    }

    public void setTitleResourceColor(int color) {
        tx.setTextColor(getActivity().getResources().getColor(color));
    }

    public void setProcessColor(int defaultBarColor) {
        progressWheel.setBarColor(defaultBarColor);
    }

    @Override
    public void show(FragmentManager transaction, String tag) {
        try{
            super.show(transaction,tag);
        }catch (IllegalStateException ignore){

        }
    }

    @Override
    public void dismiss() {
        try{
            super.dismiss();
        }catch (IllegalStateException ignore){

        }
    }

    @Override
    public void dismissAllowingStateLoss() {
        try {
            super.dismissAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
