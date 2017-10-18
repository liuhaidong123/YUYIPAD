package com.technology.yuyipad.activity.FamilyUser.Fragment.Models;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.technology.yuyipad.Enum.UserSex;
import com.technology.yuyipad.Net.Ip;
import com.technology.yuyipad.Net.Iport;
import com.technology.yuyipad.Net.gson;
import com.technology.yuyipad.Net.ok;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Bean.bean_AddFamilyUser;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Bean.bean_DeleteFamilyUser;
import com.technology.yuyipad.activity.FamilyUser.Fragment.Presenter.IFamilyUserAdd;
import com.technology.yuyipad.lzhUtils.Empty;
import com.technology.yuyipad.user.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanyu on 2017/10/16.
 */
//添加／修改／删除家庭用户
public class FamilyUserAddModel {
    IFamilyUserAdd iAdd;
    String resStr;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case -1:
                    iAdd.onError("网络异常");
                    break;
                case 1://添加／修改成功
                    try{
                        bean_AddFamilyUser fami = gson.gson.fromJson(resStr, bean_AddFamilyUser.class);
                        if ("0".equals(fami.getCode())){
                            iAdd.onSuccess();
                        }
                        else {
                            iAdd.onError(fami.getMessage());
                        }
                    }
                    catch (Exception e){
                        iAdd.onError("失败，数据异常");
                        e.printStackTrace();
                    }
                    break;
                case 2://删除成功
                    try {
                        bean_DeleteFamilyUser del = gson.gson.fromJson(resStr, bean_DeleteFamilyUser.class);
                        if ("0".equals(del.getCode())) {
                           iAdd.onSuccess();
                        } else {
                            iAdd.onError(del.getMessage());
                        }
                    } catch (Exception e) {
                        iAdd.onError("失败，数据异常");
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    //此处不检查数据的完整性（由presenter进行检查）
    public void addFamilyUser(String rela, String name, String age, String tele, UserSex sex, String bit64, boolean isBitChange,String id ,IFamilyUserAdd iAdd){
        this.iAdd=iAdd;
        Map<String, String> mp = new HashMap<>();
        mp.put("token", User.token);
        mp.put("nickName", rela);//家庭关系
        mp.put("trueName", name);
        mp.put("age", age);
        mp.put("gender",UserSex.UserSexToInt(sex)+"");
        mp.put("telephone", tele);

        if (Empty.getInstance().notEmptyOrNull(id)){//修改时多加一个id字段
            mp.put("id",id + "");
        }
        if (isBitChange){
            mp.put("avatar", bit64);
            Log.e("bit64----map---", bit64.toString());
        }
        ok.getCallCookie(Ip.path+ Iport.interface_addFamilyUser, mp,"").enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                resStr = response.body().string();
                handler.sendEmptyMessage(1);
                Log.i("添加家庭用户--", resStr);
            }
        });
    }

    //删除用户
    public void deleteUser(String id,IFamilyUserAdd iAdd) {
        this.iAdd=iAdd;
        Map<String, String> mp = new HashMap<>();
        mp.put("token",User.token);
        mp.put("id", id+ "");
        ok.getCall(Ip.path + Iport.interfce_DeleteFamilyUser, mp, ok.OK_GET).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(0);
            }
            @Override
            public void onResponse(Response response) throws IOException {
                resStr = response.body().string();
                handler.sendEmptyMessage(2);
                Log.i("删除家庭用户--", resStr);
            }
        });
    }
}
