package com.example.rh.artlive.articlechoice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rh.artlive.R;
import com.example.rh.artlive.activity.WenChangActivity;
import com.example.rh.artlive.bean.CorrectBean;
import com.example.rh.artlive.fragment.BaseFragment;
import com.example.rh.artlive.util.HttpUtil;
import com.example.rh.artlive.util.Log;
import com.example.rh.artlive.util.SharedPerfenceConstant;
import com.example.rh.artlive.util.UrlConstant;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * @version 1.0
 * @author hzc
 * @date 2015-6-24
 */

public class QuestionItemFragment extends BaseFragment implements View.OnClickListener{

	private View view;
	QuestionBean questionBean;
	int index ;
	private OptionsListAdapter adapter;
	private StringBuffer sb;
	private NoScrollListview lv;
	LocalBroadcastManager mLocalBroadcastManager;
	public static CorrectBean correctBean;

	ArrayList<String> list = new ArrayList<>();
	private String Error_Id;//全选按钮时购物车id数组
	private String True_Id;//全选按钮时购物车id数组
	private String mToken;



	private RelativeLayout mWenBottom;
	private Button mSetUp;
	private Button Up;
	private Button news;
	private TextView mTrue_View;
	private TextView mContentView;

	public QuestionItemFragment(int index,String mToken){
		this.index = index;
		this.mToken=mToken;
		questionBean = WenChangActivity.questionlist.get(index);
	}

	@Override
	public void onPause(){
		super.onPause();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
		view = inflater.inflate(R.layout.pager_item, container, false);
		init();
		setListener();
		return view;
	}

	private void init(){


		lv = (NoScrollListview) view.findViewById(R.id.lv_options);
		mWenBottom=(RelativeLayout)view.findViewById(R.id.bottom_wencahng);
		TextView tv_paper_name = (TextView) view.findViewById(R.id.tv_paper_name);
		TextView tv_sequence = (TextView) view.findViewById(R.id.tv_sequence);
		TextView tv_total_count = (TextView) view.findViewById(R.id.tv_total_count);
		TextView tv_description = (TextView) view.findViewById(R.id.tv_description);
		mSetUp=(Button)view.findViewById(R.id.down);
		Up=(Button)view.findViewById(R.id.up);
		news=(Button)view.findViewById(R.id.login_btn);
		mTrue_View=(TextView)view.findViewById(R.id.true_view);
		mContentView=(TextView)view.findViewById(R.id.wen_content);

		adapter = new OptionsListAdapter(getActivity(), questionBean.getQuestionOptions(),lv,index,questionBean.getKnowledgePointName());
		lv.setAdapter(adapter);
		//TODO 展开listvie所有子条目使用了自定义Listview，下面的方法有问题

		tv_paper_name.setText("专项智能练习(言语理解与表达)");
		tv_sequence.setText((index+1)+"");
		tv_total_count.setText("/"+WenChangActivity.questionlist.size());
        //题目
		tv_description.setText(questionBean.getDescription());
		//标准答案
		mTrue_View.setText(questionBean.getKnowledgePointName());
		//解析
		mContentView.setText(questionBean.getKnowledgePointId());

		//题目的id
		String questionType = questionBean.getExercises_id();
		sb = new StringBuffer();
//		if(questionType == 1){//单选

			lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
					adapter.notifyDataSetChanged();
					mWenBottom.setVisibility(View.VISIBLE);
					long[] ids = lv.getCheckedItemIds();
					for (int i = 0; i < ids.length; i++) {
						id = ids[i];
						sb.append(questionBean.getQuestionOptions().get((int)id).getName()).append("");
					}
					Toast.makeText(getActivity(), "选中的选项为"+sb.toString(), Toast.LENGTH_SHORT).show();
					String aa=sb.toString();
					//错题的id和正确的id传给服务端
					if (aa.equals(questionBean.getKnowledgePointName())) {
						WenChangActivity.truelist.append(questionBean.getExercises_id()+",");
					}else{
						WenChangActivity.errorlist.append(questionBean.getExercises_id()+",");
					}
					sb.setLength(0);
					Error_Id=new String(WenChangActivity.errorlist);
					True_Id=new String(WenChangActivity.truelist);
				}
			});
//		}
	}

	private void setListener(){
		news.setOnClickListener(this);
		Up.setOnClickListener(this);
		mSetUp.setOnClickListener(this);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.up:
				break;
			case R.id.down:
				Intent intent = new Intent("com.leyikao.jumptonext");
				mLocalBroadcastManager.sendBroadcast(intent);
				break;
			case R.id.login_btn:
	//				WenChangActivity.errorlist.size();
	//                Log.e("大小"+WenChangActivity.errorlist.size());
	//				int size=WenChangActivity.errorlist.size();
	//				String[] array=WenChangActivity.errorlist.toArray(new String[size]);
	//				for (int i=0;i<array.length;i++){
	//					Log.e("id"+array[i]);
	//				}
				setData();
				break;
		}
	}

	private void setData(){
		HashMap<String, String> map = new HashMap<>();
		map.put("access_token",mToken);
		map.put("errorid",Error_Id);
		map.put("yesid",True_Id);
		Log.e("错题"+Error_Id+"对题"+True_Id+"mToken"+mToken);
		HttpUtil.postHttpRequstProgess(getActivity(), "正在加载", UrlConstant.ERRORID, map, new StringCallback() {
			@Override
			public void onError(Call call, Exception e) {
			}
			@Override
			public void onResponse(String response) {
				Log.e("错题和正确题 的id"+response);
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);
					if ("1".equals(jsonObject.getString("state"))) {
						if (jsonObject.has("data")) {
							WenChangActivity.truelist.append("");
							WenChangActivity.errorlist.append("");
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, "wen", null);
	}
}