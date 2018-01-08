package com.example.rh.artlive.fragment;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.rh.artlive.R;
import com.example.rh.artlive.activity.BookActivity;
import com.example.rh.artlive.activity.ExportActivity;
import com.example.rh.artlive.activity.FindPeopleActivity;
import com.example.rh.artlive.activity.LiveActivity;
import com.example.rh.artlive.activity.MatchActivity;
import com.example.rh.artlive.activity.OutFitActivity;
import com.example.rh.artlive.activity.RecordActivity;
import com.example.rh.artlive.activity.ShowActivity;
import com.example.rh.artlive.activity.TeacherActivity;
import com.example.rh.artlive.activity.TopicActivity;
import com.example.rh.artlive.activity.WenChangActivity;
import com.example.rh.artlive.activity.WishActivity;
import com.example.rh.artlive.activity.WormLiveActivity;
import com.example.rh.artlive.bean.FollowBean;
import com.example.rh.artlive.ottoEvent.AppBus;
import com.example.rh.artlive.ottoEvent.AppCityEvent;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.example.rh.artlive.view.BannerItem;
import com.example.rh.artlive.view.CustomViewPager;
import com.example.rh.artlive.view.PullToRefreshLayout;
import com.example.rh.artlive.view.SimpleImageBanner;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by rh on 2017/11/16.
 * 虫窝
 */

public class WormFragment extends BaseFragment implements View.OnClickListener{

    private View view;
    private TabLayout mTabLayout;
    private CustomViewPager customViewPager;
    private SimpleImageBanner mSimpleBanner;

    private HeadlinesFragment testfragment1;
    private RecommFragment testfragment2;
    private VoiceFragment testfragment3;
    private VideoFragment testfragment4;

    private TextView mView1;
    private TextView mView2;
    private TextView mView3;
    private TextView mView4;
    private TextView mView5;
    private TextView mView6;
    private TextView mView7;
    private TextView mView8;
    private TextView mView9;
    private TextView mView10;

    private ImageView mWormBack;

    //AppBarLayout
    private AppBarLayout mAppBarLayout;
    //顶部HeaderLayout
    private RelativeLayout headerLayout;
    private SmartTabLayout mTabs;
    //是否隐藏了头部
    private boolean isHideHeaderLayout = false;
    ArrayList<String> baner_list=new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savesInstanceState) {
        super.onCreateView(inflater, container, savesInstanceState);
        view = inflater.inflate(R.layout.fragment_order, null);

        init();
        setListener();
        initView();

        return view;
    }


    String[] urls = new String[]{//640*360 360/640=0.5625
            "http://47.92.53.249/Uploads/photo/2017-11-28/虫窝.jpg",//"
            "http://47.92.53.249/Uploads/photo/2017-11-28/虫窝.jpg",//
    };
    ArrayList<BannerItem> getList() {
        ArrayList<BannerItem> list = new ArrayList<>();
        for (int i = 0; i < urls.length; i++) {
            BannerItem item = new BannerItem();
            item.imgUrl = urls[i];
            list.add(item);
        }
        return list;
    }

    private void init(){

        mTabLayout = (TabLayout)view.findViewById(R.id.network_tabLayout);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mSimpleBanner=(SimpleImageBanner)view.findViewById(R.id.worm_banner);
//        Bundle bundle=getArguments();
//        baner_list=bundle.getStringArrayList("banner");
//        Log.e("轮播"+bundle.getStringArrayList("banr"));
        mSimpleBanner.setSource(getList()).startScroll();


        mView1=(TextView)view.findViewById(R.id.me_user_tv_cart);
        mView2=(TextView)view.findViewById(R.id.me_user_tv_favorite);
        mView3=(TextView)view.findViewById(R.id.me_user_tv_history);
        mView4=(TextView)view.findViewById(R.id.me_user_tv_favorite1);
        mView5=(TextView)view.findViewById(R.id.me_user_tv_history2);
        mView6=(TextView)view.findViewById(R.id.me_user_tv_cart3);
        mView7=(TextView)view.findViewById(R.id.me_user_tv_favorite5);
        mView8=(TextView)view.findViewById(R.id.me_user_tv_history5);
        mView9=(TextView)view.findViewById(R.id.me_user_tv_favorite3);
        mView10=(TextView)view.findViewById(R.id.me_user_tv_history3);

        headerLayout=(RelativeLayout)view.findViewById(R.id.bar_down);

        mWormBack=(ImageView)view.findViewById(R.id.image);
        initAppBarLayout();
    }


    // 初始化AppBarLayout
    private void initAppBarLayout(){
        LayoutTransition mTransition = new LayoutTransition();
        /**
         * 添加View时过渡动画效果
         */
        ObjectAnimator addAnimator = ObjectAnimator.ofFloat(null, "translationY", 0, 1.f).
                setDuration(mTransition.getDuration(LayoutTransition.APPEARING));
        mTransition.setAnimator(LayoutTransition.APPEARING, addAnimator);

        //header layout height
        final int headerHeight = getResources().getDimensionPixelOffset(R.dimen.header_height);
        mAppBarLayout = (AppBarLayout)view.findViewById(R.id.appbar);
        mAppBarLayout.setLayoutTransition(mTransition);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                verticalOffset = Math.abs(verticalOffset);
                if ( verticalOffset >= headerHeight ){
                    isHideHeaderLayout = true;
                    //当偏移量超过顶部layout的高度时，我们认为他已经完全移动出屏幕了
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AppBarLayout.LayoutParams mParams = (AppBarLayout.LayoutParams) headerLayout.getLayoutParams();
                            mParams.setScrollFlags(0);
                            headerLayout.setLayoutParams(mParams);
                            headerLayout.setVisibility(View.GONE);
                            mWormBack.setVisibility(View.VISIBLE);
                            AppBus.getInstance().post(new AppCityEvent("worm"));
                        }
                    },100);
                }
            }
        });
    }
    private void setListener(){
        mView1.setOnClickListener(this);
        mView2.setOnClickListener(this);
        mView3.setOnClickListener(this);
        mView4.setOnClickListener(this);
        mView5.setOnClickListener(this);
        mView6.setOnClickListener(this);
        mView7.setOnClickListener(this);
        mView8.setOnClickListener(this);
        mView9.setOnClickListener(this);
        mView10.setOnClickListener(this);
        mWormBack.setOnClickListener(this);
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
        HomeFragment.Adapter adapter = new HomeFragment.Adapter(getChildFragmentManager());

        testfragment1 = new HeadlinesFragment();
        adapter.addFragment(testfragment1,"头条");

        testfragment2= new RecommFragment();
        adapter.addFragment(testfragment2, "推荐");

        testfragment3 = new VoiceFragment();
        adapter.addFragment(testfragment3,"语音");

        testfragment4= new VideoFragment();
        adapter.addFragment(testfragment4, "视频");


        viewPager.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.me_user_tv_cart:
                Intent intent=new Intent(getActivity(), TopicActivity.class);
                startActivity(intent);
                break;
            case R.id.me_user_tv_favorite:
                Intent intent1=new Intent(getActivity(), FindPeopleActivity.class);
                startActivity(intent1);
                break;
            case R.id.me_user_tv_history:
                Intent intent3=new Intent(getActivity(), WormLiveActivity.class);
                startActivity(intent3);
                break;
            case R.id.me_user_tv_favorite1:
                Intent intent4=new Intent(getActivity(), ShowActivity.class);
                startActivity(intent4);
                break;
            case R.id.me_user_tv_history2:
                Intent intent5=new Intent(getActivity(), RecordActivity.class);
                startActivity(intent5);
                break;
            case R.id.me_user_tv_cart3:
                Intent intent6=new Intent(getActivity(), WenChangActivity.class);
                startActivity(intent6);
                break;
            case R.id.me_user_tv_favorite5:
                Intent intent7=new Intent(getActivity(), ExportActivity.class);
                startActivity(intent7);
                break;
            case R.id.me_user_tv_history5:
                Intent intent8=new Intent(getActivity(), TeacherActivity.class);
                startActivity(intent8);
                break;
            case R.id.me_user_tv_favorite3:
                Intent intent9=new Intent(getActivity(), OutFitActivity.class);
                startActivity(intent9);
                break;
            case R.id.me_user_tv_history3:
                Intent intent10=new Intent(getActivity(), WishActivity.class);
                startActivity(intent10);
                break;
            case R.id.image:
                //监听返回键
                if ( isHideHeaderLayout ){
                    isHideHeaderLayout = false;
//                ((FollowFragment)Adapter.getFragments().get(0)).getRvList().scrollToPosition(0);
                    headerLayout.setVisibility(View.VISIBLE);
                    mWormBack.setVisibility(View.GONE);
                    AppBus.getInstance().post(new AppCityEvent("worm_1"));

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AppBarLayout.LayoutParams mParams = (AppBarLayout.LayoutParams) headerLayout.getLayoutParams();
                            mParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL|
                                    AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
                            headerLayout.setLayoutParams(mParams);
                        }
                    },300);
                }else {
                    getActivity().finish();
                }
                break;

        }
    }


}
