package com.technology.yuyipad.activity.Setting.Presenter;

import android.os.Handler;
import android.os.Message;

import com.technology.yuyipad.activity.Login.ILogin;
import com.technology.yuyipad.activity.Login.LoginModel;
import com.technology.yuyipad.activity.Setting.Bean.BeanChangePhone;
import com.technology.yuyipad.activity.Setting.Model.ChangePhoneModel;
import com.technology.yuyipad.lzhUtils.Empty;
import com.technology.yuyipad.lzhUtils.PhoneUtils;
import com.technology.yuyipad.user.User;

import static io.rong.imlib.statistics.UserData.phone;

/**
 * Created by wanyu on 2017/10/19.
 */

public class ChangePhonePresenter {
    IChangePhone iphone;
    ChangePhoneModel model;
    int time=60;
    Runnable runnable;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what>0){
                iphone.getTimeOut(msg.what);
            }
            else if (msg.what==0){
                iphone.getTimeOut(msg.what);
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
                iphone.getTimeOut(msg.what);
            }
        }
    };

    public void onChangePhone(String phone,String smsCode,IChangePhone iChangePhone){
        if (Empty.getInstance().notEmptyOrNull(phone)&&Empty.getInstance().notEmptyOrNull(smsCode)){
            if (PhoneUtils.isPhone(phone)){
                if (smsCode.length()==6){
                    if (model==null){
                        model=new ChangePhoneModel();
                    }
                    model.changePhone(phone,smsCode,iChangePhone);
                }
                else {
                    iChangePhone.onChangeError("验证码格式不正确！");
                }
            }
            else {
                iChangePhone.onChangeError("手机号不正确！");
            }
        }
        else {
            iChangePhone.onChangeError("手机号或验证码不正确！");
        }
    }

    public void getSmSCode(String phoneNum,IChangePhone iChangePhone){
        if (Empty.getInstance().notEmptyOrNull(phoneNum)){
            if (PhoneUtils.isPhone(phoneNum)){
                if (phoneNum.equals(User.tele)){
                    iChangePhone.onGetSmSError("新手机号不能与原手机号相同！");
                }
                else {
                    if (model==null){
                        model=new ChangePhoneModel();
                    }
                    this.iphone=iChangePhone;
                    model.getSMSCode(phoneNum,iChangePhone);
                }
            }
            else {
                iChangePhone.onGetSmSError("手机号不正确！");
            }
        }
        else {
            iChangePhone.onGetSmSError("手机号不正确！");
        }
    }
    //验证码成功后记时
    public void onTimer(){
        time=10;
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
        if (handler!=null&&runnable!=null){
            handler.removeCallbacks(runnable);
        }
    }

    public interface IChangePhone{
        void onGetSmSSuccess();
        void getTimeOut(int count);
        void onGetSmSError(String msg);
        void onChangeSuccess(String newPhone,BeanChangePhone bean);
        void onChangeError(String msg);
    }
}
