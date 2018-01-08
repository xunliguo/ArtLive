package com.example.rh.artlive.util;

import android.app.Activity;

import java.util.Stack;

public class ActivityUtils {

    private static Stack<Activity> activityList;
    private static ActivityUtils mActivityUtils;

    public static ActivityUtils getInstance() {
        if (mActivityUtils == null) {
            mActivityUtils = new ActivityUtils();
            activityList = new Stack<Activity>();
        }
        return mActivityUtils;
    }

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (activityList.contains(activity)) {
            activityList.remove(activity);
        }
    }

    public void removeAllActivity() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    public int getSize() {
        return activityList.size();
    }
}
