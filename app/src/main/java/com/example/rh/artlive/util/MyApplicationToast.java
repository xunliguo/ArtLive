package com.example.rh.artlive.util;

import android.app.Application;
import android.content.Context;


/**
 * Created by PC on 2016/6/24.
 */
public class MyApplicationToast extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static Context getAppContext() {
        return mContext;
    }

}
