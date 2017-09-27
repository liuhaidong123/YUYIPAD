package com.technology.yuyipad.lzhUtils;

import android.text.TextUtils;

/**
 * Created by wanyu on 2017/9/27.
 */

public class Empty {
    static  Empty em;
    private Empty(){

    }
    public static Empty getInstance(){
        if (em==null){
            em=new Empty();
        }
        return em;
    }
    public boolean isEmptyOrNull(String text){
        return !"".equals(text)&&!TextUtils.isEmpty(text);
    }
}
