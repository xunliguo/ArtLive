/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.rh.artlive.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rh.artlive.R;
import com.example.rh.artlive.application.DemoApplication;
import com.example.rh.artlive.fragment.CodeFragment;
import com.example.rh.artlive.fragment.FollowFragment;
import com.example.rh.artlive.fragment.HomeFragment;
import com.example.rh.artlive.fragment.HotFragment;
import com.example.rh.artlive.fragment.PasswordFragment;
import com.example.rh.artlive.fragment.PhoneFragment;
import com.example.rh.artlive.ottoEvent.AppBus;
import com.example.rh.artlive.ottoEvent.AppCityEvent;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.view.CustomViewPager;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 注册页
 * 
 */
public class RegisterActivity extends BaseFragmentActivity implements View.OnClickListener {
//	private EditText userNameEditText;
//	private EditText passwordEditText;
//	private EditText confirmPwdEditText;

	private TabLayout mTabLayout;
	private CustomViewPager customViewPager;
	private PhoneFragment mFindFragment;
	private CodeFragment mCodeFragment;
	private PasswordFragment mFollowTwoFragmnet;
	private String mStep="0";


	@Override
	public void onStart() {
		super.onStart();
		//注册到bus事件总线中
		AppBus.getInstance().register(this);

	}

	@Override
	public void onStop() {
		super.onStop();
		AppBus.getInstance().unregister(this);

	}

	// 接受事件
	@Subscribe
	public void setContent(AppCityEvent data) {
		mStep=data.getName();
		initView(mStep);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		init();
		setListener();
		initView(mStep);
//		userNameEditText = (EditText) findViewById(R.id.username);
//		passwordEditText = (EditText) findViewById(R.id.password);
//		confirmPwdEditText = (EditText) findViewById(R.id.confirm_password);
	}


	private void init(){
		mTabLayout = (TabLayout)findViewById(R.id.network_tabLayout);
		mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

	}
	private void setListener(){
	}

	/**
	 * ViewPager初始化、设置属性
	 */
	private void initView(String mStep){
		customViewPager = (CustomViewPager)findViewById(R.id.main_viewpager);
		if (customViewPager != null) {
			setupViewPager(customViewPager);
			Log.e("点击"+mStep);
			if ("1".equals(mStep)){
				customViewPager.setCurrentItem(1);
			}else if ("2".equals(mStep)){
				customViewPager.setCurrentItem(3);
			}
			customViewPager.setOffscreenPageLimit(2);//实现首次加载
		}
		final TabLayout tabLayout = (TabLayout)findViewById(R.id.network_tabLayout);
		tabLayout.setupWithViewPager(customViewPager);
	}

	private void setupViewPager(ViewPager viewPager) {
		Adapter adapter = new Adapter(getSupportFragmentManager());

		mFindFragment = new PhoneFragment();
		adapter.addFragment(mFindFragment,"1请输入手机号      >");

		mCodeFragment = new CodeFragment();
		adapter.addFragment(mCodeFragment,"2请输入验证码      >");

		mFollowTwoFragmnet= new PasswordFragment();
		adapter.addFragment(mFollowTwoFragmnet, "3设置密码");


		viewPager.setAdapter(adapter);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
//			case R.id.me_user_tv_cart:
//				Intent intent=new Intent(getActivity(), TeacherActivity.class);
//				startActivity(intent);
//				break;
//			case R.id.me_user_tv_favorite:
//				Intent intent1=new Intent(getActivity(), OutFitActivity.class);
//				startActivity(intent1);
//				break;
//			case R.id.me_user_tv_history:
//				Intent intent2=new Intent(getActivity(), CollegesActivity.class);
//				startActivity(intent2);
//				break;
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
//
//	/**
//	 * 注册
//	 *
//	 * @param view
//	 */
//	public void register(View view) {
//		final String username = userNameEditText.getText().toString().trim();
//		final String pwd = passwordEditText.getText().toString().trim();
//		String confirm_pwd = confirmPwdEditText.getText().toString().trim();
//		if (TextUtils.isEmpty(username)) {
//			Toast.makeText(this, getResources().getString(R.string.User_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
//			userNameEditText.requestFocus();
//			return;
//		} else if (TextUtils.isEmpty(pwd)) {
//			Toast.makeText(this, getResources().getString(R.string.Password_cannot_be_empty), Toast.LENGTH_SHORT).show();
//			passwordEditText.requestFocus();
//			return;
//		} else if (TextUtils.isEmpty(confirm_pwd)) {
//			Toast.makeText(this, getResources().getString(R.string.Confirm_password_cannot_be_empty), Toast.LENGTH_SHORT).show();
//			confirmPwdEditText.requestFocus();
//			return;
//		} else if (!pwd.equals(confirm_pwd)) {
//			Toast.makeText(this, getResources().getString(R.string.Two_input_password), Toast.LENGTH_SHORT).show();
//			return;
//		}
//
//		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
//			final ProgressDialog pd = new ProgressDialog(this);
//			pd.setMessage(getResources().getString(R.string.Is_the_registered));
//			pd.show();
//
//			new Thread(new Runnable() {
//				public void run() {
//					try {
//						// 调用sdk注册方法
//						EMClient.getInstance().createAccount(username, pwd);
//						runOnUiThread(new Runnable() {
//							public void run() {
//								if (!RegisterActivity.this.isFinishing())
//									pd.dismiss();
//								// 保存用户名
//								DemoApplication.getInstance().setCurrentUserName(username);
//								Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_successfully), 0).show();
//								finish();
//							}
//						});
//					} catch (final HyphenateException e) {
//						runOnUiThread(new Runnable() {
//							public void run() {
//								if (!RegisterActivity.this.isFinishing())
//									pd.dismiss();
//								int errorCode=e.getErrorCode();
//								if(errorCode== EMError.NETWORK_ERROR){
//									Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
//								}else if(errorCode == EMError.USER_ALREADY_EXIST){
//									Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
//								}else if(errorCode == EMError.USER_AUTHENTICATION_FAILED){
//									Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
//								}else if(errorCode == EMError.USER_ILLEGAL_ARGUMENT){
//								    Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name), Toast.LENGTH_SHORT).show();
//								}else{
//									Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
//								}
//							}
//						});
//					}
//				}
//			}).start();
//		}
//	}
//
//	public void back(View view) {
//		finish();
//	}

}
