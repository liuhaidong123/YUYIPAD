package com.technology.yuyipad.lzhUtils;

import android.content.Context;

/**
 * Created by wanyu on 2017/9/26.
 */

public class StateBarUtils {
    //获取状态栏高度
    public static int getStatusBarHeight(Context con) {
        int result = 0;
        int resourceId = con.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = con.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
