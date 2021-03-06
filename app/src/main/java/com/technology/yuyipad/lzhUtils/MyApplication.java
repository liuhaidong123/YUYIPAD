package com.technology.yuyipad.lzhUtils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.sst.jkezt.health.utils.JkezAPIMain;
import com.technology.yuyipad.RongUtils.RongUserInfoProvider;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;

/**
 * Created by wanyu on 2017/9/27.
 */

public class MyApplication extends Application{
    public static Activity activityCurrent;
    private static List<Activity> list;
    @Override
    public void onCreate() {
        super.onCreate();
    //    JkezAPIMain.initSDK(this, "wanyult");//血压体温测量
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        RongIM.init(this);
        RongIM.setUserInfoProvider(RongUserInfoProvider.getInstance(),false);
        RongIM.getInstance().setMessageAttachedUserInfo(true);

        if (Build.VERSION.SDK_INT >= 14) {//4.0以上
            list = new ArrayList<>();
            if (Build.VERSION.SDK_INT >= 14) {//4.0以上
                registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                        list.add(activity);
                    }

                    @Override
                    public void onActivityStarted(Activity activity) {

                    }

                    @Override
                    public void onActivityResumed(Activity activity) {
                        activityCurrent = activity;
                    }

                    @Override
                    public void onActivityPaused(Activity activity) {

                    }

                    @Override
                    public void onActivityStopped(Activity activity) {

                    }

                    @Override
                    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                    }

                    @Override
                    public void onActivityDestroyed(Activity activity) {
                        list.remove(activity);
                    }
                });
            }
        }
    }
    //退出登录
    public static void removeActivity() {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Activity activity = list.get(i);
                Log.i("remove-名字--", activity.getClass().getSimpleName());
                activity.finish();
            }
            list.clear();
        }
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
