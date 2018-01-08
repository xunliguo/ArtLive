package com.example.rh.artlive.articlechoice;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rh.artlive.R;
import com.example.rh.artlive.util.Log;

import java.util.List;

public class OptionsListAdapter extends BaseAdapter {
	private Context mContext;
	ListView lv ;
	int index;
	public List<QuestionOptionBean> options ;
	private String mTrue;
	private ImageView mNo_View;
	private  LinearLayout back;

	
	public OptionsListAdapter(Context context, List<QuestionOptionBean> options, ListView lv, int index,String mTrueBean) {
		this.mContext = context;
		this.options = options;
		this.lv = lv;
		mTrue=mTrueBean;
	}

	public int getCount() {
		return options.size();
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
	 
			View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_option, null);
		    back=(LinearLayout)view.findViewById(R.id.selected_back);
			CheckedTextView ctv = (CheckedTextView) view.findViewById(R.id.ctv_name);
			TextView option = (TextView) view.findViewById(R.id.tv_option);
		    mNo_View=(ImageView)view.findViewById(R.id.no_View);
			
			ctv.setText(options.get(position).getName());
			option.setText(options.get(position).getDescription());
			updateBackground(position, back);
			return view;
	 
	}

	/**
	 * 选中切换颜色
	 * @param position
	 * @param view
	 */
	public void updateBackground(int position, View view) {
		String click_name=options.get(position).getName();
		if (lv.isItemChecked(position )) {
			view.setBackgroundColor(Color.parseColor("#ff1db9a7"));
			if (click_name.equals(mTrue)) {
				mNo_View.setImageResource(R.mipmap.wen_true);
			}
		} else {
			view.setBackgroundColor(Color.parseColor("#ffffff"));
		}
	}
}
