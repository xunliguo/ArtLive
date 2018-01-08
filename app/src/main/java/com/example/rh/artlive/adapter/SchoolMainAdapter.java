package com.example.rh.artlive.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.example.rh.artlive.R;

import java.util.List;

/**
 * Created by rh on 2017/12/25.
 */

public class SchoolMainAdapter extends RvAdapter<String> {


    private int checkedPosition;

    public void setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
        notifyDataSetChanged();
    }

    public SchoolMainAdapter(Context context, List<String> list, RvListener listener) {
        super(context, list, listener);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.item_school_list;
    }

    @Override
    protected RvHolder getHolder(View view, int viewType) {
        return new SchoolMainAdapter.SortHolder(view, viewType, listener);
    }

    private class SortHolder extends RvHolder<String> {

        private TextView tvName;
        private View mView;

        SortHolder(View itemView, int type, RvListener listener) {
            super(itemView, type, listener);
            this.mView = itemView;
            tvName = (TextView) itemView.findViewById(R.id.tv_sort);
        }

        @Override
        public void bindHolder(String string, int position) {
            tvName.setText(string);
            if (position == checkedPosition) {
//                mView.setBackgroundColor(Color.parseColor("#f3f3f3"));
//                tvName.setTextColor(Color.parseColor("#0068cf"));
            } else {
//                mView.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                tvName.setTextColor(Color.parseColor("#1e1d1d"));
            }
        }
    }
}
