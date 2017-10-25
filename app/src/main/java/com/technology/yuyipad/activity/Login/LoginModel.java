package com.technology.yuyipad.activity.Login;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.technology.yuyipad.Net.Ip;
import com.technology.yuyipad.Net.Iport;
import com.technology.yuyipad.Net.gson;
import com.technology.yuyipad.Net.ok;
import com.technology.yuyipad.bean.BeanCode;
import com.technology.yuyipad.code.ServerCode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.rong.imageloader.utils.L;

/**
 * Created by wanyu on 2017/10/17.
 */

public class LoginModel {
    ILogin iLogin;
    String resStr;
    String cookie;//验证码cookie
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case -1://验证码失败
                    iLogin.getSMSCodeError("网络异常！");
                    break;
                case 1://验证码成功
                    try{
                        BeanCode bCode= gson.gson.fromJson(resStr,BeanCode.class);
                        if (bCode!=null){
                            if (ServerCode.successCode.equals(bCode.getCode())){
                                iLogin.getSMSCodeSuccess();
                            }
                            else {
                                iLogin.getSMSCodeError(bCode.getResult());
                                }
                        }
                        else {
                            iLogin.getSMSCodeError("数据错误！");
                        }
                    }
                    catch (Exception e){
                        iLogin.getSMSCodeError("数据异常！");
                        e.printStackTrace();
                    }
                    break;
                case -2:
                    iLogin.onLoginError("网络异常！");
                    break;
                case 2:
                    try{
                        LoginBean bean=gson.gson.fromJson(resStr, LoginBean.class);
                        if (bean!=null){
                            if (ServerCode.successCode.equals(bean.getCode())){
                                iLogin.onLoginSuccess(bean);
                            }
                            else {
                                iLogin.onLoginError(bean.getMessage());
                            }
                        }
                        else {
                            iLogin.onLoginError("数据错误！");
                        }
                    }
                    catch (Exception e){
                        iLogin.onLoginError("数据异常！");
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    public void getSMSCode(String phone,ILogin iLogin){
        this.iLogin=iLogin;
        Map<String,String>mp=new HashMap<>();
        mp.put("id",phone);
        ok.getCall(Ip.path+ Iport.interface_LoginGetSMS,mp,ok.OK_POST).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(-1);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                resStr=response.body().string();
                cookie=response.headers().get("Set-Cookie");
                Log.e("获取验证码",resStr);
                handler.sendEmptyMessage(1);
            }
        });
    }

    public  void onLogin(String phone,String smsCode,ILogin iLogin){
        this.iLogin=iLogin;
        Map<String,String>mp=new HashMap<>();
        mp.put("id",phone); mp.put("vcode",smsCode);
        ok.getCallCookie(Ip.path+Iport.interface_Login,mp,cookie).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(-2);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                resStr=response.body().string();
                Log.i("登录",resStr);
                handler.sendEmptyMessage(2);

            }
        });
    }
}
