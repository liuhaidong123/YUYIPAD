package com.technology.yuyipad.RongUtils;

import android.app.Activity;
import android.view.View;

/**
 * Created by liuhaidong on 2017/10/20.
 */

public class RongWindow {
   static RongWindow window;
    private RongWindow(){

    }
    public static RongWindow getInstance(){
        if (window==null){
            window=new RongWindow();
        }
        return window;
    }
    public void show(Activity ac, View parentView){

    }
}
