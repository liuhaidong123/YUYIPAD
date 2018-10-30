package com.technology.yuyipad.activity.Setting.Model;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.technology.yuyipad.Net.Ip;
import com.technology.yuyipad.Net.Iport;
import com.technology.yuyipad.Net.gson;
import com.technology.yuyipad.Net.ok;
import com.technology.yuyipad.activity.Setting.Bean.BeanChangePhone;
import com.technology.yuyipad.activity.Setting.Bean.bean_SMScode;
import com.technology.yuyipad.activity.Setting.Presenter.ChangePhonePresenter;
import com.technology.yuyipad.user.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanyu on 2017/10/19.
 */

public class ChangePhoneModel {
    ChangePhonePresenter.IChangePhone iChangePhone;
    String cooki="";
    String resStr;
    String phones;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case -1://获取验证码失败
                    iChangePhone.onGetSmSError("网络异常");
                    break;
                case 1://获取验证码
                    try{
                        bean_SMScode bean= gson.gson.fromJson(resStr,bean_SMScode.class);
                        if ("0".equals(bean.getCode())){
                            iChangePhone.onGetSmSSuccess();
                        }
                        else {
                            iChangePhone.onChangeError("此手机号已被注册");
                        }
                    }
                    catch (Exception e){
                        iChangePhone.onGetSmSError("数据异常");
                        e.printStackTrace();
                    }
                    break;
                case -2://切换手机号
                    iChangePhone.onChangeError("网络异常");
                    break;
                case 2://切换手机号
                    try{
                        BeanChangePhone bean=gson.gson.fromJson(resStr,BeanChangePhone.class);
                        if ("0".equals(bean.getCode())){
                            iChangePhone.onChangeSuccess(phones,bean);
                        }
                        else {
                            iChangePhone.onChangeError(bean.getMessage());
                        }
                    }
                    catch (Exception e){
                        iChangePhone.onChangeError("数据异常");
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };

    //获取验证码参数：电话号码，请确保号码是正确的
    public void getSMSCode(String ph, ChangePhonePresenter.IChangePhone iChangePhone) {
        this.iChangePhone=iChangePhone;
        cooki="";//初始化cooki
        Map<String, String> mp = new HashMap<>();
        mp.put("id", ph);
        ok.getCall(Ip.path + Iport.interface_GetSMSCode, mp, ok.OK_POST).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(-1);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                resStr = response.body().string();
                cooki=response.headers().get("Set-Cookie");
                Log.i("更改手机号验证码",resStr);
                handler.sendEmptyMessage(1);
            }
        });
    }
    //    http://192.168.1.168:8082/yuyi/personal/modifymobile.do?token=1213&newMobile=13717883009&vcode=123456
//    参数：
//    token=令牌
//            newMobile=新手机号
//    vcode=验证码
    public void changePhone(String phone, String sms, ChangePhonePresenter.IChangePhone iChangePhone){
        if (!"".equals(cooki)&&!TextUtils.isEmpty(cooki)){
            this.phones=phone;
            Map<String,String>mp=new HashMap<>();
            mp.put("token", User.token);
            mp.put("newMobile",phone);
            mp.put("vcode",sms);
            ok.getCallCookie(Ip.path+Iport.interface_ChangePhone,mp,cooki).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    handler.sendEmptyMessage(-2);
                }
                @Override
                public void onResponse(Response response) throws IOException {
                    resStr=response.body().string();
                    Log.i("更改绑定手机号",resStr);
                    handler.sendEmptyMessage(2);
                }
            });
        }
        else {
            iChangePhone.onChangeError("验证码失效");
        }
    }
}
