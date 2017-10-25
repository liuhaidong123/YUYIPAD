package com.technology.yuyipad.activity.MedicalRecord;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.technology.yuyipad.Net.Ip;
import com.technology.yuyipad.Net.Iport;
import com.technology.yuyipad.Net.ok;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Bean.bean_MedicalRecordList;
import com.technology.yuyipad.user.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.technology.yuyipad.Net.gson.gson;

/**
 * Created by wanyu on 2017/10/23.
 */

public class EleMedicalRecordListModel {
    IMedicalRecord iMedicalRecord;
    String resStr;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    iMedicalRecord.onError("网络异常");
                    break;
                case 1:
                    try {
                        bean_MedicalRecordList recordList =gson.fromJson(resStr,bean_MedicalRecordList.class);
                        if (recordList!=null){
                            iMedicalRecord.onSuccess(recordList);
                        }
                        else {
                            iMedicalRecord.onError("没有查询到数据！");
                            Log.e("获取自己的电子病例失败","FamilyUserMessageModel:返回数据为null");
                        }
                    }
                    catch (Exception e){
                        iMedicalRecord.onError("数据异常！");
                        e.printStackTrace();
                        Log.e("获取自己的电子病例异常",""+resStr);
                    }
                    break;
            }
        }
    };
    //获取我自己的电子病历
    public void getMyEleMsg( IMedicalRecord iMedicalRecord) {
        this.iMedicalRecord=iMedicalRecord;
        Map<String, String> mp = new HashMap<>();
        mp.put("token", User.token);
        ok.getCall(Ip.path + Iport.interface_medicalRecordList, mp, ok.OK_GET).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                resStr = response.body().string();
                Log.i("我的电子病历列表---", resStr);
                handler.sendEmptyMessage(1);
            }
        });
    }
    public interface IMedicalRecord{
        void onError(String msg);
        void onTokenError();
        void onSuccess(bean_MedicalRecordList bList);
    }
}
