package com.technology.yuyipad.lhdUtils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.technology.yuyipad.R;

/**
 * Created by liuhaidong on 2017/10/10.
 */

public class MyDialog {
    private static PopupWindow mPopupwindow = new PopupWindow();

    //加载弹框
    public static void showPopuWindow(final Activity activity, View view) {
        View view2 = LayoutInflater.from(activity).inflate(R.layout.loading_box, null);
        //设置透明度
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 0.5f;
        activity.getWindow().setAttributes(params);
        mPopupwindow.setContentView(view2);
        //设置弹框的款，高
        mPopupwindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupwindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupwindow.setFocusable(true);//如果有交互需要设置焦点为true
        mPopupwindow.setOutsideTouchable(false);//设置内容外可以点击

        //相对于父控件的位置
        mPopupwindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        //当弹框销毁时，将透明度初始化，否则弹框销毁后，所依附的activity页面背景将会改变
        mPopupwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                params.alpha = 1f;
                activity.getWindow().setAttributes(params);
            }
        });

    }

    public static void stopDialog(){
        if (mPopupwindow!=null){
            mPopupwindow.dismiss();
        }
    }

}
