package com.technology.yuyipad.activity.FamilyUser.Fragment.Models;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.technology.yuyipad.Net.Ip;
import com.technology.yuyipad.Net.Iport;
import com.technology.yuyipad.Net.ok;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Bean.bean_HomeUserTempAndPress;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Bean.bean_MedicalRecordList;
import com.technology.yuyipad.httptools.UrlTools;
import com.technology.yuyipad.user.User;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.technology.yuyipad.Net.gson.gson;

/**
 * Created by wanyu on 2017/8/17.
 */
//FamilyUserMessageActivity
public class FamilyUserMessageModel {
    IElectMsg iElectMsg;//
    String resStr;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    try {
                        bean_MedicalRecordList recordList =gson.fromJson(resStr,bean_MedicalRecordList.class);
                        if (recordList!=null){
                            iElectMsg.onGetElectMsg(recordList);
                        }
                        else {
                            Log.e("获取自己的电子病例失败","FamilyUserMessageModel:返回数据为null");
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Log.e("获取自己的电子病例异常",""+resStr);
                    }
                    break;
                case 2://获取家庭用户的所有病历
                    try {
                        bean_MedicalRecordList recordList  =gson.fromJson(resStr,bean_MedicalRecordList.class);
                        if (recordList!=null){
                            iElectMsg.onGetElectMsg(recordList);
                        }
                        else {
                            Log.e("获取自己的电子病例失败","FamilyUserMessageModel:返回数据为null");
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Log.e("获取家庭用户病例异常",""+resStr);
                    }
                    break;
                case 3:
                    try{
                        bean_HomeUserTempAndPress bean=gson.fromJson(resStr,bean_HomeUserTempAndPress.class);
                        if (bean!=null){
                           iElectMsg.onGetUserTempAndPress(bean);
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Log.e("获取用户的血压／温度数据异常",""+resStr);
                    }
                    break;
            }
        }
    };
    public FamilyUserMessageModel(IElectMsg iElectMsg){
        this.iElectMsg=iElectMsg;
    }
    //获取家人电子病历http://localhost:8080/yuyi/medical/homeuserMedicalTime.do?id=1
    public void getFamilyUserEleMsg(String ids) {
        Map<String, String> mp = new HashMap<>();
        mp.put("id", ids);
        ok.getCall(Ip.path + Iport.interface_famiUserEleList, mp, ok.OK_GET).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                resStr = response.body().string();
                Log.i("家人电子病历列表---", resStr);
                handler.sendEmptyMessage(2);
            }
        });
    }

    //获取我自己的电子病历
    public void getMyEleMsg() {
        Map<String, String> mp = new HashMap<>();
        mp.put("token", User.token);
        ok.getCall(Ip.path + Iport.interface_medicalRecordList, mp, ok.OK_GET).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                    iElectMsg.onNetWorkError();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                resStr = response.body().string();
                Log.i("我的电子病历列表---", resStr);
                handler.sendEmptyMessage(1);
            }
        });
    }

    //获取用户的血压与温度信息
    public void getTempAndPress(String homeUserId){
        String url= UrlTools.BASE + UrlTools.URL_CLICK_USER_HEAD_FIRST_PAGE;
        Map<String,String> mp=new HashMap<>();
        mp.put("token",User.token);
        mp.put("humeuserId",homeUserId);
        ok.getCall(url,mp,ok.OK_POST).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                iElectMsg.onNetWorkError();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                resStr=response.body().string();
                Log.i("获取用户的血压／温度信息",resStr);
                handler.sendEmptyMessage(3);
            }
        });
    }
    public interface IElectMsg{
        void onNetWorkError();//网络有问题
        void onGetElectMsg(bean_MedicalRecordList recordList);//获取电子病例列表（我的／家庭用户的）
        void onGetUserTempAndPress(bean_HomeUserTempAndPress bean);//获取用户的温度与血压
    }
}
