package com.technology.yuyipad.activity.UserInfo;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.technology.yuyipad.DbUtils.IDbUtlis;
import com.technology.yuyipad.Enum.UserSex;
import com.technology.yuyipad.Net.Ip;
import com.technology.yuyipad.Net.Iport;
import com.technology.yuyipad.Net.gson;
import com.technology.yuyipad.Net.ok;
import com.technology.yuyipad.code.ServerCode;
import com.technology.yuyipad.lzhUtils.DialogUtils;
import com.technology.yuyipad.lzhUtils.MyApplication;
import com.technology.yuyipad.user.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanyu on 2017/10/9.
 */

public class UserInfoModel {
    Iuser iuser;
    IuserChange iuserChange;
    String resStr;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case -1:
                    iuser.onError("网络异常",Iport.interface_UserMsg);
                    break;
                case 1:
                    try{
                        UserBean user= gson.gson.fromJson(resStr,UserBean.class);
                        if (user!=null){
                            if (ServerCode.successCode.equals(user.getCode())){
                                iuser.onSuccess(user);
                                IDbUtlis.getInstance().saveOkhttpString(MyApplication.activityCurrent,Iport.interface_UserMsg,resStr);
                            }
                            else if (ServerCode.tokenErrorCode.equals(user.getCode())){
                                iuser.onTokenError();
                            }
                            else {
                                iuser.onError(user.getMessage(),Iport.interface_UserMsg);
                                }
                        }
                        else {
                            iuser.onError("数据为空",Iport.interface_UserMsg);
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        iuser.onError("数据异常",Iport.interface_UserMsg);
                    }
                    break;
                //修改信息
                case -2:
                    DialogUtils.stopDialog();
                    iuserChange.onChangeError("网络异常");
                    break;
                case 2:
                    DialogUtils.stopDialog();
                    try{
                        UserChangeBean bea=gson.gson.fromJson(resStr,UserChangeBean.class);
                        if (bea!=null){
                            if (ServerCode.successCode.equals(bea.getCode())){
                                iuserChange.onChangeSuccess();
                            }
                            else {
                                iuserChange.onChangeError(bea.getMessage());
                            }
                        }
                        else {
                            iuserChange.onChangeError("返回数据为空");
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        iuserChange.onChangeError("数据异常");
                    }
                    break;
            }
        }
    };
    public void getUserData(Iuser iuser){
            this.iuser=iuser;
            Map<String,String> mp=new HashMap<>();
            mp.put("token", User.token);
            ok.getCall(Ip.path+ Iport.interface_UserMsg,mp,ok.OK_GET).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    handler.sendEmptyMessage(-1);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    resStr=response.body().string();
                    Log.i("获取用户个人信息--",resStr);
                    handler.sendEmptyMessage(1);
                }
            });
    }

    //保存用户信息
    public void saveUserDate(String bit64, String name, String age, UserSex sex, boolean isPhotoChange,IuserChange iuserChange){
        DialogUtils.showDialog();
        this.iuserChange=iuserChange;
        if (Integer.parseInt(age)<1|Integer.parseInt(age)>150){
            iuserChange.onChangeError("年龄不正确");
            return;
        }
        Map<String,String>mp=new HashMap<>();
        mp.put("token",User.token);
        if (isPhotoChange){
            mp.put("avatar",bit64);
        }
        else {
            mp.put("avatar","");
        }
        mp.put("trueName",name);//真实姓名
        mp.put("age",age);//年龄
        mp.put("gender",""+UserSex.UserSexToInt(sex));//性别
        ok.getCall(Ip.path+Iport.interface_UserMsgRevise,mp,ok.OK_POST).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(-2);
            }
            @Override
            public void onResponse(Response response) throws IOException {
                resStr=response.body().string();
                Log.i("修改个人信息----",resStr);
                handler.sendEmptyMessage(2);
            }
        });
    }
}
