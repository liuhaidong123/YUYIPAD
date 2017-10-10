package com.technology.yuyipad.lzhUtils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import com.technology.yuyipad.R;

/**
 * Created by wanyu on 2017/10/9.
 */

public class PopupSettings {
    static PopupSettings settings;
    private PopupSettings(){

    }
    public static PopupSettings getInstance(){
        if (settings==null){
            settings=new PopupSettings();
        }
     return settings;
    }
//    定义所有中心缩放的效果(宽高度均为wrap——content)
    public void showWindowCenter(final Activity ac, PopupWindow pop, View contentView, View parentView){
        ac.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams params= ac.getWindow().getAttributes();
        params.alpha=0.6f;
        ac.getWindow().setAttributes(params);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pop.setContentView(contentView);
        pop.setBackgroundDrawable(new ColorDrawable(Color.argb(000, 255, 255, 255)));
        pop.setTouchable(true);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setAnimationStyle(R.style.popup3_anim);
        pop.showAtLocation(parentView, Gravity.CENTER, 0,0);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ac.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                WindowManager.LayoutParams params= ac.getWindow().getAttributes();
                params.alpha=1f;
                ac.getWindow().setAttributes(params);
            }
        });
    }
}
