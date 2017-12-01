package com.technology.yuyipad.lzhUtils;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;

import com.technology.yuyipad.R;

/**
 * Created by wanyu on 2017/12/1.
 */

public class DialogUtils{
    static Dialog dialog;
    public   static void showDialog(){
        if (dialog!=null&&dialog.isShowing()){
            return;
        }
        dialog=new Dialog(MyApplication.activityCurrent,R.style.dialog);
        View contentView= LayoutInflater.from(MyApplication.activityCurrent).inflate(R.layout.dia,null);
        dialog.setContentView(contentView);
        dialog.show();
//        mDialog = new Dialog(getActivity(), R.style.IsDelDialog);//自定义的样式，没有贴出代码来
//        mDialog.setContentView(view);
//        Window dialogWindow = dialog.getWindow();
//        WindowManager m = con.getWindowManager();
//        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
//        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        p.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度设置为屏幕的0.6，根据实际情况调整
//        p.width =  WindowManager.LayoutParams.WRAP_CONTENT; // 宽度设置为屏幕的0.65，根据实际情况调整
//        dialogWindow.setAttributes(p);
    }
    public static void stopDialog(){
        if (dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
    }
}
