package com.example.rh.artlive.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rh.artlive.R;
import com.example.rh.artlive.activity.BookActivity;
import com.example.rh.artlive.activity.CollegesActivity;
import com.example.rh.artlive.activity.MatchActivity;
import com.example.rh.artlive.activity.OutFitActivity;
import com.example.rh.artlive.activity.TeacherActivity;
import com.example.rh.artlive.view.CustomViewPager;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rh on 2017/11/16.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private View view;

    private TabLayout mTabLayout;
    private CustomViewPager customViewPager;
    private FollowFragment mFindFragment;
    private HotFragment mFollowTwoFragmnet;

    private TextView mView1;
    private TextView mView2;
    private TextView mView3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savesInstanceState) {
        super.onCreateView(inflater, container, savesInstanceState);
        view = inflater.inflate(R.layout.fragment_home, null);

        init();
        setListener();
        initView();

        return view;
    }

    private void init(){
        mTabLayout = (TabLayout)view.findViewById(R.id.network_tabLayout);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        mView1=(TextView)view.findViewById(R.id.me_user_tv_cart);
        mView2=(TextView)view.findViewById(R.id.me_user_tv_favorite);
        mView3=(TextView)view.findViewById(R.id.me_user_tv_history);
    }
    private void setListener(){
        mView1.setOnClickListener(this);
        mView2.setOnClickListener(this);
        mView3.setOnClickListener(this);
    }

    /**
     * ViewPager初始化、设置属性
     */
    private void initView(){
        customViewPager = (CustomViewPager)view.findViewById(R.id.main_viewpager);
        if (customViewPager != null) {
            setupViewPager(customViewPager);
            customViewPager.setOffscreenPageLimit(2);//实现首次加载
        }
        final TabLayout tabLayout = (TabLayout)view.findViewById(R.id.network_tabLayout);
        tabLayout.setupWithViewPager(customViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());

        mFindFragment = new FollowFragment();
        adapter.addFragment(mFindFragment,"关注");

        mFollowTwoFragmnet= new HotFragment();
        adapter.addFragment(mFollowTwoFragmnet, "热门");


        viewPager.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.me_user_tv_cart:
                Intent intent=new Intent(getActivity(), BookActivity.class);
                startActivity(intent);
                break;
            case R.id.me_user_tv_favorite:
                Intent intent1=new Intent(getActivity(), MatchActivity.class);
                startActivity(intent1);
                break;
            case R.id.me_user_tv_history:
                Intent intent2=new Intent(getActivity(), CollegesActivity.class);
                startActivity(intent2);
                break;
        }
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
