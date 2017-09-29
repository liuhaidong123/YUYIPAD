package com.technology.yuyipad.ToastUtils;

import android.content.Context;
import android.widget.Toast;

import com.technology.yuyipad.Net.ErrorText;

/**
 * Created by wanyu on 2017/9/27.
 */

public class toast {
    static toast tos;
    private  toast(){

    }
    public static toast getInstance(){
        if (tos==null){
            tos=new toast();
        }
        return tos;
    }
    public void text(Context con, String msg){
        Toast.makeText(con,msg,Toast.LENGTH_SHORT).show();
    }
    public void textGsonFaile(Context con){
        Toast.makeText(con,ToastText.gsonError,Toast.LENGTH_SHORT).show();
    }
    public void textNetWorkError(Context con){
        Toast.makeText(con,ToastText.netWorkError,Toast.LENGTH_SHORT).show();
    }
    public void textOnSuccess(Context con){
        Toast.makeText(con,ToastText.onSuccess,Toast.LENGTH_SHORT).show();
    }
    interface ToastText{
        String gsonError= ErrorText.gsonError;
        String netWorkError=ErrorText.netError;
        String onSuccess="成功";
    }
}
