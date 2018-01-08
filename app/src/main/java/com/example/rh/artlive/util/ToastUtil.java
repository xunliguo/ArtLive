package com.example.rh.artlive.util;

import android.content.Context;
import android.widget.Toast;


public class ToastUtil {
    public static int Error = 0;
    public static int Success = 1;

    private static Context mContext;

    private ToastUtil() {
    }

    public static void showToast(Context mContext, String msg) {
        try {
            Toast.makeText(mContext.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(MyApplicationToast.getAppContext().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

}
