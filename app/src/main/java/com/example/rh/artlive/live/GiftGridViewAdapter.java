package com.example.rh.artlive.live;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rh.artlive.R;

import java.util.ArrayList;


/**
 * author：Administrator on 2016/12/26 15:03
 * description:文件说明
 * version:版本
 */
///定影GridView的Adapter
public class GiftGridViewAdapter extends BaseAdapter {
    private int page;
    private int count;
    private ArrayList<LiveGiftBean> gifts ;
    private Context context ;

    public void setGifts(ArrayList<LiveGiftBean> gifts) {
        this.gifts = gifts;
        notifyDataSetChanged();
    }
    public GiftGridViewAdapter(Context context, int page, int count) {
        this.page = page;
        this.count = count;
        this.context = context ;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 8;
    }

    @Override
    public LiveGiftBean getItem(int position) {
        // TODO Auto-generated method stub
        return gifts.get(page * count + position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        final LiveGiftBean catogary = gifts.get(page * count + position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gift, null);
            viewHolder.grid_fragment_home_item_img = (ImageView) convertView.findViewById(R.id.grid_fragment_home_item_img);
            viewHolder.grid_fragment_home_item_txt = (TextView) convertView.findViewById(R.id.grid_fragment_home_item_txt);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        viewHolder.grid_fragment_home_item_img.setImageResource(catogary.getImage_source());
//        int size=LiveCameraActivity.
//        int size=LiveCameraActivity.giftBeen.size();
//        String [] array=LiveCameraActivity.giftBeen.toArray(new String[size]);
//        for (int i=0;i<array.length;i++){
//            viewHolder.grid_fragment_home_item_txt.setText(LiveCameraActivity.giftBeen.get(i).getTitle());
//            Glide.with(context).load(LiveCameraActivity.giftBeen.get(i).getPoster()).into(viewHolder.grid_fragment_home_item_img);
//        }

        viewHolder.grid_fragment_home_item_txt.setText(catogary.getTitle());
        Glide.with(context).load(catogary.getPoster()).into(viewHolder.grid_fragment_home_item_img);

//        viewHolder.grid_fragment_home_item_txt.setText(LiveCameraActivity.giftBeen.ge);
//        viewHolder.grid_fragment_home_item_img.setImageResource(catogary.giftType);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onGridViewClickListener!=null){
                    onGridViewClickListener.click(catogary);
                }
            }
        });

        return convertView;
    }
    public class ViewHolder {
        public ImageView grid_fragment_home_item_img;
        public TextView grid_fragment_home_item_txt;
    }

    public  OnGridViewClickListener onGridViewClickListener ;

    public void setOnGridViewClickListener(OnGridViewClickListener onGridViewClickListener) {
        this.onGridViewClickListener = onGridViewClickListener;
    }

    public interface OnGridViewClickListener{
        void click(LiveGiftBean gift);
    }
}
