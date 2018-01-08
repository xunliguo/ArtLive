package com.example.rh.artlive.view;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by color on 16/5/31.
 */

public interface OnItemClickListener<T> {
    void onItemClick(ViewGroup parent, View view, T t, int position);

    boolean onItemLongClick(ViewGroup parent, View view, T t, int position);
}
