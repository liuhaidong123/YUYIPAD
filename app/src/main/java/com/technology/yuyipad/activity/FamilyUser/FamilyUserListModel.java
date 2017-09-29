package com.technology.yuyipad.activity.FamilyUser;

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
import com.technology.yuyipad.lzhUtils.MyApplication;
import com.technology.yuyipad.user.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanyu on 2017/9/29.
 */
//获取家庭用户列表
public class FamilyUserListModel {
    IFamilyUserList iUser;
    String resStr;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    iUser.onError(ErrorText.netError,Iport.IgetFamilyUserList);
                    break;
                case 1:
                    try{
                        FamilyUserListBean bean= gson.gson.fromJson(resStr,FamilyUserListBean.class);
                        if (bean!=null){
                            if ("0".equals(bean.getCode())){
                                iUser.onSuccess(bean);
                                IDbUtlis.getInstance().saveOkhttpString(MyApplication.activityCurrent,Iport.IgetFamilyUserList,resStr);
                            }
                            else {
                                iUser.onError(bean.getMessage(),Iport.IgetFamilyUserList);
                            }
                        }
                        else {
                            iUser.onError(ErrorText.dateEmpty,Iport.IgetFamilyUserList);
                        }
                    }
                    catch (Exception e){
                        iUser.onError(ErrorText.gsonError,Iport.IgetFamilyUserList);
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    public FamilyUserListModel(){

    }


    public void getUserList(IFamilyUserList iUser) {
        this.iUser=iUser;
        Map<String, String> mp = new HashMap<>();
        mp.put("token", User.token);
        ok.getCall(Ip.path + Iport.IgetFamilyUserList, mp, ok.OK_GET).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                resStr = response.body().string();
                handler.sendEmptyMessage(1);
                Log.i("获取家庭用户列表--", resStr);
            }
        });
    }
}
