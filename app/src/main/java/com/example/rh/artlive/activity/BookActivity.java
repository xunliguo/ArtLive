package com.example.rh.artlive.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import com.example.rh.artlive.R;
import com.example.rh.artlive.fragment.BookAllFragment;
import com.example.rh.artlive.fragment.BookShelfFragment;
import com.example.rh.artlive.fragment.WormFollowFragment;
import com.example.rh.artlive.fragment.WormHotFragment;
import com.example.rh.artlive.fragment.WormMineFragment;

/**
 * Created by rh on 2017/11/20.
 * 书籍
 */

public class BookActivity extends BaseFragmentActivity implements View.OnClickListener{
    private ImageView mShowDraw;


    private BookAllFragment testFragment2;//订餐
    private BookShelfFragment testFragment3;//购物

    private TabLayout mTabLayout;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        fragmentManager = getSupportFragmentManager();

        testFragment2 = new BookAllFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content, testFragment2).commit();

        init();
        setListener();
        initView();
    }

    private void init(){
        mShowDraw=(ImageView)findViewById(R.id.showDraw);
    }

    private void setListener(){
        mShowDraw.setOnClickListener(this);
    }


    /***
     * 初始化控件
     */
    public void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.mTabLayout);

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                hideFragments(transaction);
                switch (tab.getPosition()) {
                    case 0:
                        if (testFragment2 == null) {
                            testFragment2 = new BookAllFragment();
                            transaction.add(R.id.content, testFragment2);
                        } else {
                            transaction.show(testFragment2);
                        }
                        break;
                    case 1:
                        if (testFragment3 == null) {
                            testFragment3 = new BookShelfFragment();
                            transaction.add(R.id.content, testFragment3);
                        } else {
                            transaction.show(testFragment3);
                        }
                        break;
                }
                transaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void hideFragments(FragmentTransaction transaction) {

        if (testFragment2 != null) {
            transaction.hide(testFragment2);
        }
        if (testFragment3 != null) {
            transaction.hide(testFragment3);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showDraw:
                finish();
                break;
        }
    }
}
