package com.example.rh.artlive.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.rh.artlive.activity.BookPayActivity;
import com.example.rh.artlive.activity.WenChangActivity;
import com.example.rh.artlive.articlechoice.QuestionItemFragment;
import com.example.rh.artlive.articlechoice.ScantronItemFragment;
import com.example.rh.artlive.fragment.BookItemFragment;

/**
 * Created by rh on 2017/12/20.
 */

public class BookItemAdapter extends FragmentStatePagerAdapter {

    Context context;
    private String mToken;

    public BookItemAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int arg0) {
        if(arg0 == BookPayActivity.bookBeanList.size()){
            return new ScantronItemFragment();
        }
        return new BookItemFragment(arg0);
    }

    @Override
    public int getCount() {
        return BookPayActivity.bookBeanList.size()+1;
    }
}
