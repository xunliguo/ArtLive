package com.example.rh.artlive.articlechoice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rh.artlive.R;
import com.example.rh.artlive.activity.WHTrainActivity;
import com.example.rh.artlive.activity.WenChangActivity;


/**
 * @version 1.0
 * @author hzc
 * @date 2015-6-24
 */

public class ScantronItemFragment extends Fragment {

	LocalBroadcastManager mLocalBroadcastManager;
	public ScantronItemFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		 mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
		View rootView = inflater.inflate(R.layout.pager_item_scantron,
				container, false);

		Intent intent=new Intent(getActivity(),WenChangActivity.class);
		startActivity(intent);
//		TextView tv_submit_result = (TextView) rootView.findViewById(R.id.tv_submit_result);
//		tv_submit_result.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
////				Intent intent = new Intent(getActivity(),ResultReportActivity.class);
////				startActivity(intent);
//
//			}
//		});
//		MyAdapter adapter = new MyAdapter(getActivity());
//		gv.setAdapter(adapter);
//		gv.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//				// TODO跳转到相应的viewpager 页面
//				  Intent intent = new Intent("com.leyikao.jumptopage");
//                  intent.putExtra("index", position);
//                  mLocalBroadcastManager.sendBroadcast(intent);
//			}
//		});
		return rootView;

	}
}