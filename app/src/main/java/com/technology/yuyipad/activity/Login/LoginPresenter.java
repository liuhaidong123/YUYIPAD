package com.technology.yuyipad.activity.Login;

import android.os.Handler;
import android.os.Message;

import com.technology.yuyipad.lzhUtils.Empty;
import com.technology.yuyipad.lzhUtils.PhoneUtils;

/**
 * Created by wanyu on 2017/10/17.
 */

public class LoginPresenter {
    LoginModel model;
    int time=60;
    ILogin iLogin;
    Runnable runnable;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what>0){
                iLogin.getTimeOut(msg.what);
            }
            else if (msg.what==0){
                iLogin.getTimeOut(msg.what);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            handler.sendEmptyMessage(-2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            else if(msg.what==-2){
                iLogin.getTimeOut(msg.what);
            }
        }
        };
    public void getSmsCode(String phone,ILogin iLogin){
        if (Empty.getInstance().notEmptyOrNull(phone)){
            if (PhoneUtils.isPhone(phone)){
                if (model==null){
                    model=new LoginModel();
                }
                this.iLogin=iLogin;
                model.getSMSCode(phone,iLogin);
            }
            else {
                iLogin.getSMSCodeError("手机号不正确！");
            }
        }
        else {
            iLogin.getSMSCodeError("手机号不正确！");
        }
    }
    public void onLogin(String phone,String smsCode,ILogin iLogin){
        if (Empty.getInstance().notEmptyOrNull(phone)&&Empty.getInstance().notEmptyOrNull(smsCode)){
            if (PhoneUtils.isPhone(phone)){
                if (smsCode.length()==6){
                    if (model==null){
                        model=new LoginModel();
                    }
                    model.onLogin(phone,smsCode,iLogin);
                }
                else {
                    iLogin.onLoginError("验证码格式不正确！");
                }
            }
            else {
                iLogin.onLoginError("手机号不正确！");
            }
        }
        else {
            iLogin.onLoginError("手机号或验证码不正确！");
        }
    }

    //验证码成功后记时
    public void onTimer(){
        time=60;
        runnable=new Runnable() {
            @Override
            public void run() {
                while (time>0) {
                    try {
                        time--;
                        handler.sendEmptyMessage(time);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(runnable).start();
    }

    public void stopTimer(){
        handler.removeCallbacks(runnable);
    }
}
