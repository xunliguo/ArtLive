package com.example.rh.artlive.articlechoice;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.rh.artlive.activity.WHTrainActivity;
import com.example.rh.artlive.activity.WenChangActivity;
import com.example.rh.artlive.util.Log;

/**
 * @Description: ViewPager的数据适配器
 * @author hzc
 */
public class ItemAdapter extends FragmentStatePagerAdapter {
	Context context;
	private String mToken;

	public ItemAdapter(FragmentManager fm,String token) {
		super(fm);
		this.mToken=token;
	}
	@Override
	public Fragment getItem(int arg0) {
		if(arg0 == WenChangActivity.questionlist.size()){
			return new ScantronItemFragment();
		}
		return new QuestionItemFragment(arg0,mToken);
	}

	@Override
	public int getCount() {
		return WenChangActivity.questionlist.size()+1;
	}

}
