package com.example.rh.artlive.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.rh.artlive.R;
import com.example.rh.artlive.fragment.LiveAllFragment;
import com.example.rh.artlive.fragment.LiveArtFragment;
import com.example.rh.artlive.fragment.LiveBordCastFragment;
import com.example.rh.artlive.fragment.LiveDireFragment;
import com.example.rh.artlive.fragment.LivePerformFragment;
import com.example.rh.artlive.fragment.LivePhotoFragment;
import com.example.rh.artlive.view.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rh on 2017/11/20.
 * 虫窝直播
 */

public class LiveActivity extends BaseFragmentActivity{

    private View view;
    private TabLayout mTabLayout;
    private CustomViewPager customViewPager;
    private LiveAllFragment testframent;
    private LiveBordCastFragment testframent2;
    private LiveDireFragment testframent3;
    private LivePerformFragment testframent4;
    private LivePhotoFragment testframent5;
    private LiveArtFragment testframent6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actiivty_live);
        init();
        initView();
    }

    private void init(){
        mTabLayout = (TabLayout)findViewById(R.id.network_tabLayout);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    /**
     * ViewPager初始化、设置属性
     */
    private void initView(){
        customViewPager = (CustomViewPager)findViewById(R.id.main_viewpager);
        if (customViewPager != null) {
            setupViewPager(customViewPager);
            customViewPager.setOffscreenPageLimit(4);//实现首次加载
        }
        final TabLayout tabLayout = (TabLayout)findViewById(R.id.network_tabLayout);
        tabLayout.setupWithViewPager(customViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());

        testframent= new LiveAllFragment();
        adapter.addFragment(testframent, "全部");

        testframent2 = new LiveBordCastFragment();
        adapter.addFragment(testframent2,"播音");

        testframent3=new LiveDireFragment();
        adapter.addFragment(testframent3,"编导");

        testframent4=new LivePerformFragment();
        adapter.addFragment(testframent4,"表演");

        testframent5=new LivePhotoFragment();
        adapter.addFragment(testframent5,"摄影");

        testframent6=new LiveArtFragment();
        adapter.addFragment(testframent6,"美术");

        viewPager.setAdapter(adapter);

    }

    static class Adapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String view) {
            mFragments.add(fragment);
            mFragmentTitles.add(view);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
