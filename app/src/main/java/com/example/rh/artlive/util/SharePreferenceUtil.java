package com.example.rh.artlive.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * sharePerference
 */
public class SharePreferenceUtil {
    private static SharedPreferences sp;
    private static SharePreferenceUtil spDao;
    private static SharedPreferences.Editor editor;
    private static String file = "share";

    public synchronized static SharePreferenceUtil getInstance(Context context) {// 单线程单例
        if (spDao == null) {
            spDao = new SharePreferenceUtil();
        }
        sp = context.getSharedPreferences(file, context.MODE_PRIVATE);
        editor = sp.edit();
        return spDao;
    }

    //清理数据
    public void cleanEditor() {
        editor.clear();
    }

    // 设置Stirng类型
    public void setString(String name, String value) {
        editor.putString(name, value);
        editor.commit();
    }
    // 设置Stirng类型
    public void setStringBuffer(String name, StringBuffer value) {
        editor.putString(name, String.valueOf(value));
        editor.commit();
    }

    // 设置Int类型
    public int setInt(String name, int value) {
        editor.putInt(name, value);
        editor.commit();
        return value;
    }

    // 设置Boolean类型
    public void setBoolean(String name, Boolean value) {
        editor.putBoolean(name, value);
        editor.commit();
    }

    // 设置Long类型
    public void setLong(String name, Long value) {
        editor.putLong(name, value);
        editor.commit();
    }

    // 获取Stirng类型
    public String getString(String name) {
        return sp.getString(name, "");
    }

    // 获取Int类型
    public int getInt(String name, int i) {
        return sp.getInt(name, 0);
    }

    // 获取Boolean类型
    public Boolean getBoolean(String name) {
        return sp.getBoolean(name, true);
    }

    // 获取Boolean类型
    public Long getLong(String name) {
        return sp.getLong(name,0L);
    }
}
