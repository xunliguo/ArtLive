package com.example.rh.artlive.photo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.rh.artlive.R;
import com.example.rh.artlive.activity.MainActivity;
import com.example.rh.artlive.util.MeasureUtils;
import com.example.rh.artlive.util.UiUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class DynamicGridAdapter extends BaseAdapter {
	private String[] files;

	private LayoutInflater mLayoutInflater;

	public DynamicGridAdapter(String[] files, Context context) {
		this.files = files;
		mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return files == null ? 0 : files.length;
	}

	@Override
	public String getItem(int position) {
		return files[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyGridViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new MyGridViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.gridview_item,
					parent, false);
			
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.album_image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (MyGridViewHolder) convertView.getTag();
		}
		String url = getItem(position);
		if (getCount() == 1) {
			viewHolder.imageView.setLayoutParams(new android.widget.AbsListView.LayoutParams(300, 250));
		}
		
		if (getCount() == 2 ||getCount() == 4) {
			viewHolder.imageView.setLayoutParams(new android.widget.AbsListView.LayoutParams(200, 200));
		}


		// 根据屏幕宽度动态设置图片宽高
		int width = MeasureUtils.getWidth(UiUtils.getContext());
		int imageWidth = (width / 3 - 40);
		viewHolder.imageView.setLayoutParams(new ViewGroup.LayoutParams(imageWidth, imageWidth));
//		String url = getItem(position);
		ImageLoader.getInstance().displayImage(url, viewHolder.imageView);
		ImageLoader.getInstance().displayImage(url, viewHolder.imageView);

		return convertView;
	}

	private static class MyGridViewHolder {
		ImageView imageView;
	}
}
