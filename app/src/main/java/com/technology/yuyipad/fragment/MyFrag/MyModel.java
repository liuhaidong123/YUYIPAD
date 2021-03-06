package com.technology.yuyipad.fragment.MyFrag;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.technology.yuyipad.DbUtils.IDbUtlis;
import com.technology.yuyipad.Net.ErrorText;
import com.technology.yuyipad.Net.Ip;
import com.technology.yuyipad.Net.Iport;
import com.technology.yuyipad.Net.gson;
import com.technology.yuyipad.Net.ok;
import com.technology.yuyipad.ToastUtils.toast;
import com.technology.yuyipad.code.ServerCode;
import com.technology.yuyipad.lzhUtils.MyApplication;
import com.technology.yuyipad.user.User;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by wanyu on 2017/9/27.
 */

public class MyModel {
    IUser iUser;
    String resStr;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    iUser.onError(ErrorText.netError,Iport.IgetUserInfo);
                    break;
                case 1:
                    try{
                        UserBean bean= gson.gson.fromJson(resStr,UserBean.class);
                        if (bean!=null){
                            if (ServerCode.successCode.equals(bean.getCode())){
                                iUser.onSuccess(bean);
                                IDbUtlis.getInstance().saveOkhttpString(MyApplication.activityCurrent,Iport.IgetUserInfo,resStr);
                            }
                            else if (ServerCode.tokenErrorCode.equals(bean.getCode())){//登录状态失效
                                iUser.onErrorTokenOut();
                            }
                            else {
                                iUser.onError(bean.getMessage(),Iport.IgetUserInfo);
                            }
                        }
                        else {
                            iUser.onError(ErrorText.dateEmpty,Iport.IgetUserInfo);
                        }
                    }
                    catch (Exception e){
                        iUser.onError(ErrorText.gsonError,Iport.IgetUserInfo);
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    //获取用户信息
    public void getUserInfo(IUser iUser){
        this.iUser=iUser;
        if (iUser==null){
            return;
        }
        HashMap<String,String>mp=new HashMap<>();
        mp.put("token", User.token);
        ok.getCall(Ip.path+ Iport.IgetUserInfo,mp,ok.OK_GET).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                    handler.sendEmptyMessage(0);
            }
            @Override
            public void onResponse(Response response) throws IOException {
                    resStr=response.body().string();
                    Log.i("获取个人信息",resStr);
                    handler.sendEmptyMessage(1);
            }
        });
    }
}
