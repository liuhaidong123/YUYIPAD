package com.technology.yuyipad.activity.Medicinal;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.technology.yuyipad.DbUtils.IDbUtlis;
import com.technology.yuyipad.Net.Ip;
import com.technology.yuyipad.Net.Iport;
import com.technology.yuyipad.Net.gson;
import com.technology.yuyipad.Net.ok;
import com.technology.yuyipad.code.ServerCode;
import com.technology.yuyipad.lzhUtils.MyApplication;
import com.technology.yuyipad.user.User;

import net.tsz.afinal.db.sqlite.DbModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanyu on 2017/10/17.
 */

public class MyMedicalModel {
    IMedical iMedical;
    String resStr;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:

                    break;
                case 1:
                    try{
                        BeanDrugStates bean= gson.gson.fromJson(resStr,BeanDrugStates.class);
                        if (bean!=null){
                            if (ServerCode.successCode.equals(bean.getCode())){
                                if (bean.getResult()!=null&&bean.getResult().size()>0){
                                    iMedical.onGetMedicalListSuccess(bean);
                                    IDbUtlis.getInstance().saveOkhttpString(MyApplication.activityCurrent,Iport.interface_MyDrugStateList,resStr);
                                }
                                else {
                                    iMedical.onGetMedicalListError("没有查询到您的药品记录！",Iport.interface_MyDrugStateList);
                                }
                            }
                            else if (ServerCode.tokenErrorCode.equals(bean)){
                                iMedical.onTokenError();
                            }
                            else {
                                iMedical.onGetMedicalListError(bean.getMessage(),Iport.interface_MyDrugStateList);
                            }
                        }
                        else {
                            iMedical.onGetMedicalListError("没有查询到数据！",Iport.interface_MyDrugStateList);
                        }
                    }
                    catch (Exception e){
                        iMedical.onGetMedicalListError("数据异常！",Iport.interface_MyDrugStateList);
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    public void getMedicalList(IMedical iMedical){
        this.iMedical=iMedical;
        Map<String,String> map=new HashMap<>();
        map.put("token", User.token);
        ok.getCall(Ip.path+ Iport.interface_MyDrugStateList,map,ok.OK_POST).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
               handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                resStr=response.body().string();
                Log.i("获取我的所有药品状态",resStr);
                handler.sendEmptyMessage(1);
            }
        });
    }
}
